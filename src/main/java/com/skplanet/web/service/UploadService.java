package com.skplanet.web.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.CaseFormat;
import com.skplanet.web.exception.BizException;
import com.skplanet.web.model.UploadProgress;
import com.skplanet.web.model.UploadStatus;
import com.skplanet.web.repository.mysql.UploadMetaRepository;
import com.skplanet.web.repository.oracle.UploadTempRepository;
import com.skplanet.web.util.Constant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadService {

	@Autowired
	private UploadMetaRepository metaRepository;

	@Autowired
	private UploadTempRepository tempRepository;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job importJob;

	@Value("${app.files.autoRemove}")
	private boolean autoRemove;

	@Transactional("mysqlTxManager")
	public JobParameters readyToImport(MultipartFile file, String pageId, String username, String columnName) {
		Path filePath = saveUploadFile(file);

		markRunning(pageId, username, columnName, filePath.getFileName().toString());

		prepareTemporaryTable(pageId, username);

		long numberOfColumns = getNumberOfColumnsAndValidate(pageId, filePath);

		JobParameters jobParameters = new JobParametersBuilder().addString("pageId", pageId)
				.addString("username", username).addString("filePath", filePath.toString())
				.addLong("numberOfColumns", numberOfColumns).toJobParameters();

		return jobParameters;
	}

	public JobExecution beginImport(JobParameters jobParameters) {
		try {
			JobExecution execution = jobLauncher.run(importJob, jobParameters);
			return execution;
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			throw new BizException("Import Job Error", e);
		}
	}

	public void endImport(JobParameters parameters) {
		markFinish(parameters.getString("pageId"), parameters.getString("username"));
		removeUploadedFile(Paths.get(parameters.getString("filePath")));
	}

	public void markRunning(String pageId, String username, String columnName, String filename) {
		UploadProgress uploadProgress = metaRepository.selectUploadProgress(pageId, username);

		if (uploadProgress != null && uploadProgress.getUploadStatus() == UploadStatus.RUNNING) {
			throw new BizException("업로드 중입니다");
		}

		String underScoredColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, columnName);

		metaRepository.upsertUploadProgress(pageId, username, underScoredColumnName, filename, UploadStatus.RUNNING);
	}

	public void markFinish(String pageId, String username) {
		metaRepository.upsertUploadProgress(pageId, username, null, null, UploadStatus.FINISH);
	}

	public void prepareTemporaryTable(String pageId, String username) {
		if (tempRepository.countTable(pageId, username) <= 0) {
			tempRepository.createTable(pageId, username);
		}
		tempRepository.truncateTable(pageId, username);
	}

	public long getNumberOfColumnsAndValidate(String pageId, Path uploadPath) {
		long numberOfColumns = 1;
		if ("PAN0103".equalsIgnoreCase(pageId)) {
			numberOfColumns = 6;
		}

		// 업로드에 필요한 열 수가 있는지 검증한다.
		try (BufferedReader reader = Files.newBufferedReader(uploadPath, Charset.forName("EUC-KR"))) {
			String firstLine = reader.readLine();
			if (numberOfColumns - 1 != StringUtils.countOccurrencesOf(firstLine, ",")) {
				throw new BizException("파일 양식을 확인해 주세요. " + numberOfColumns + "개의 열이 필요합니다");
			}
		} catch (IOException e) {
			throw new BizException("Failed to read a file while the uploaded one validating", e);
		}

		return numberOfColumns;
	}

	public Path saveUploadFile(MultipartFile file) {
		File uploadDirectory = new File(Constant.APP_FILE_DIR);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdir();
		}

		Path uploadPath = Paths.get(Constant.APP_FILE_DIR, UUID.randomUUID() + "-" + file.getOriginalFilename());

		try (InputStream in = file.getInputStream()) {
			Files.copy(in, uploadPath);
		} catch (IOException e) {
			throw new BizException("Failed to copy the upload file", e);
		}

		return uploadPath;
	}

	public void removeUploadedFile(Path filePath) {
		if (autoRemove) {
			try {
				if (!Files.deleteIfExists(filePath)) {
					log.warn("Failed to delete [{}] because it did not exist", filePath);
				}
			} catch (IOException e) {
				log.error("Failed to remove uploaded file", e);
			}
		}
	}

	public UploadProgress getFinishedUploadProgress(String pageId, String username) {
		UploadProgress uploadProgress = getUploadProgress(pageId, username);

		if (uploadProgress.getUploadStatus() == UploadStatus.RUNNING) {
			throw new BizException("업로드 중입니다");
		}

		return uploadProgress;
	}

	public UploadProgress getUploadProgress(String pageId, String username) {
		UploadProgress uploadProgress = metaRepository.selectUploadProgress(pageId, username);

		if (uploadProgress == null) {
			throw new BizException("업로드를 먼저 해주세요");
		}

		return uploadProgress;
	}

}
