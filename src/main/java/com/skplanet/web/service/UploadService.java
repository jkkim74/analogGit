package com.skplanet.web.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.CaseFormat;
import com.skplanet.web.exception.BizException;
import com.skplanet.web.model.MenuProgress;
import com.skplanet.web.model.ProgressStatus;
import com.skplanet.web.repository.mysql.UploadMetaRepository;
import com.skplanet.web.repository.oracle.UploadTempRepository;
import com.skplanet.web.util.Constant;
import com.skplanet.web.util.Helper;

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

	@Value("${app.files.encoding.upload}")
	private String encoding;

	@Transactional("mysqlTxManager")
	public JobParameters readyToImport(MultipartFile file, String menuId, String username, String param) {
		Path filePath = saveUploadFile(file);

		markStatus(ProgressStatus.UPLOADING, menuId, username, param, filePath.getFileName().toString());

		prepareTemporaryTable(menuId, username);

		long numberOfColumns = getNumberOfColumnsAndValidate(menuId, filePath);

		JobParameters jobParameters = new JobParametersBuilder().addString("menuId", menuId)
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

	@Transactional("mysqlTxManager")
	public void markStatus(ProgressStatus progressStatus, String menuId, String username, String param,
			String filename) {
		if (progressStatus == ProgressStatus.UPLOADING) {
			MenuProgress progress = metaRepository.selectMenuProgress(menuId, username);

			if (progress != null && progress.getStatus() == ProgressStatus.UPLOADING) {
				throw new BizException("업로드 중입니다");
			}
		}

		String underscoredParam = param == null ? null
				: CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, param);

		metaRepository.upsertMenuProgress(menuId, username, underscoredParam, filename, progressStatus);
	}

	private void prepareTemporaryTable(String menuId, String username) {
		if (tempRepository.countTable(menuId, username) <= 0) {
			tempRepository.createTable(menuId, username);
		}
		tempRepository.truncateTable(menuId, username);
	}

	private long getNumberOfColumnsAndValidate(String menuId, Path uploadPath) {
		long numberOfColumns = 1;
		if ("PAN0103".equalsIgnoreCase(menuId)) {
			numberOfColumns = 6;
		}

		// 업로드에 필요한 열 수가 있는지 검증한다.
		try (BufferedReader reader = Files.newBufferedReader(uploadPath, Charset.forName(encoding))) {
			String firstLine = reader.readLine();
			if (numberOfColumns - 1 != StringUtils.countOccurrencesOf(firstLine, ",")) {
				throw new BizException("파일 양식을 확인해 주세요. " + numberOfColumns + "개의 열이 필요합니다");
			}
		} catch (IOException e) {
			throw new BizException("Failed to read a file while the uploaded one validating", e);
		}

		return numberOfColumns;
	}

	private Path saveUploadFile(MultipartFile file) {
		File uploadDirectory = new File(Constant.APP_FILE_DIR);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdir();
		}

		Path uploadPath = Paths.get(Constant.APP_FILE_DIR, Helper.uniqueCsvFilename(null));

		try (InputStream in = file.getInputStream()) {
			Files.copy(in, uploadPath);
		} catch (IOException e) {
			throw new BizException("Failed to copy the upload file", e);
		}

		return uploadPath;
	}

	@Scheduled(fixedDelay = 86400000L) // day by day after startup
	public void clearFileStore() {
		if (!autoRemove) {
			return;
		}

		Path appFileDir = Paths.get(Constant.APP_FILE_DIR);

		for (File f : appFileDir.toFile().listFiles()) {
			// 30days = 2592000000msec = 60 * 60 * 24 * 30 * 1000
			if (f.isFile() && f.lastModified() < System.currentTimeMillis() - 2592000000L) {
				boolean result = f.delete();
				if (!result) {
					log.warn("Failed to remove file: {}", f.getName());
				}

			}
		}
	}

	public MenuProgress getFinishedMenuProgress(String menuId, String username) {
		MenuProgress progress = getMenuProgress(menuId, username);

		switch (progress.getStatus()) {
		case UPLOADING:
			throw new BizException("업로드 중입니다");
		case PROCESSING:
			throw new BizException("처리 중인 작업이 있습니다");
		default:
			return progress;
		}
	}

	public MenuProgress getMenuProgress(String menuId, String username) {
		MenuProgress progress = metaRepository.selectMenuProgress(menuId, username);

		if (progress == null) {
			throw new BizException("업로드를 먼저 해주세요");
		}

		return progress;
	}

}
