package com.skplanet.pandora.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.CaseFormat;
import com.skplanet.ocb.exception.BizException;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.model.UploadStatus;
import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.util.Constant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadService {

	@Autowired
	private MysqlRepository mysqlRepository;

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job importJob;

	@Autowired
	private FtpService ftpService;

	@Transactional("mysqlTxManager")
	public JobParameters readyToImport(MultipartFile file, String pageId, String username, String columnName) {
		Path filePath = saveUploadFile(file);

		prepareTemporaryTable(pageId, username);

		long numberOfColumns = getNumberOfColumnsAndValidate(pageId, filePath);

		JobParameters jobParameters = new JobParametersBuilder().addString(Constant.PAGE_ID, pageId)
				.addString(Constant.USERNAME, username).addString(Constant.FILE_PATH, filePath.toString())
				.addLong(Constant.NUMBER_OF_COLUMNS, numberOfColumns).toJobParameters();

		markRunning(pageId, username, columnName, filePath.getFileName().toString());

		return jobParameters;
	}

	public void beginImport(JobParameters jobParameters) {
		try {
			jobLauncher.run(importJob, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			throw new BizException("Import Job Error", e);
		}
	}

	public void endImport(JobParameters parameters) {
		markFinish(parameters.getString(Constant.PAGE_ID), parameters.getString(Constant.USERNAME));
		removeUploadedFile(parameters.getString(Constant.FILE_PATH));
	}

	private void markRunning(String pageId, String username, String columnName, String filename) {
		UploadProgress uploadProgress = mysqlRepository.selectUploadProgress(pageId, username);

		if (uploadProgress != null && uploadProgress.getUploadStatus() == UploadStatus.RUNNING) {
			throw new BizException("업로드 중입니다");
		}

		String underScoredColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, columnName);

		mysqlRepository.upsertUploadProgress(pageId, username, underScoredColumnName, filename, UploadStatus.RUNNING);
	}

	private void markFinish(String pageId, String username) {
		mysqlRepository.upsertUploadProgress(pageId, username, null, null, UploadStatus.FINISH);
	}

	private void prepareTemporaryTable(String pageId, String username) {
		if (oracleRepository.countTable(pageId, username) <= 0) {
			oracleRepository.createTable(pageId, username);
		}
		oracleRepository.truncateTable(pageId, username);
	}

	private long getNumberOfColumnsAndValidate(String pageId, Path uploadPath) {
		long numberOfColumns = 1;
		if (Constant.PAN0103.equalsIgnoreCase(pageId)) {
			numberOfColumns = 6;
		}

		// 업로드에 필요한 열 수가 있는지 검증한다.
		try (BufferedReader reader = Files.newBufferedReader(uploadPath, StandardCharsets.UTF_8)) {
			String firstLine = reader.readLine();
			if (numberOfColumns - 1 != StringUtils.countOccurrencesOf(firstLine, ",")) {
				throw new BizException("파일 양식을 확인해 주세요. " + numberOfColumns + "개의 열이 필요합니다");
			}

			// 혹시 모르니 2번째 줄도 확인.
			String secondLine = reader.readLine();
			if (numberOfColumns - 1 != StringUtils.countOccurrencesOf(secondLine, ",")) {
				throw new BizException("파일 양식을 확인해 주세요. " + numberOfColumns + "개의 열이 필요합니다");
			}
		} catch (IOException e) {
			throw new BizException("Failed to read a file while the uploaded one validating", e);
		}

		return numberOfColumns;
	}

	private Path saveUploadFile(MultipartFile file) {
		File uploadDirectory = new File(Constant.UPLOADED_FILE_DIR);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdir();
		}

		Path uploadPath = Paths.get(Constant.UPLOADED_FILE_DIR, UUID.randomUUID() + "-" + file.getOriginalFilename());

		try (InputStream in = file.getInputStream()) {
			Files.copy(in, uploadPath);
		} catch (IOException e) {
			throw new BizException("Failed to copy the upload file", e);
		}

		return uploadPath;
	}

	private void removeUploadedFile(String filePath) {
		removeUploadedFile(Paths.get(filePath));
	}

	private void removeUploadedFile(Path filePath) {
		try {
			if (!Files.deleteIfExists(filePath)) {
				log.warn("Failed to delete [{}] because it did not exist", filePath);
			}
		} catch (IOException e) {
			log.error("Failed to remove uploaded file", e);
		}
	}

	public UploadProgress getFinishedUploadProgress(String pageId, String username) {
		UploadProgress uploadProgress = mysqlRepository.selectUploadProgress(pageId, username);

		if (uploadProgress == null) {
			throw new BizException("업로드를 먼저 해주세요");
		}

		if (uploadProgress.getUploadStatus() == UploadStatus.RUNNING) {
			throw new BizException("업로드 중입니다");
		}

		return uploadProgress;
	}

	public void forwardToFtpServer(MultipartFile file, String pageId, String username, String columnName) {
		Path filePath = saveUploadFile(file);

		prepareTemporaryTable(pageId, username);

		getNumberOfColumnsAndValidate(pageId, filePath);

		markRunning(pageId, username, columnName, filePath.getFileName().toString());

		String remotePath = "web/" + filePath.getFileName().toString();
		ftpService.sendForExtraction(filePath, remotePath);

		markFinish(pageId, username);

		removeUploadedFile(filePath);
	}

}
