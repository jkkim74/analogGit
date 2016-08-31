package com.skplanet.pandora.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.CaseFormat;
import com.skplanet.pandora.common.BizException;
import com.skplanet.pandora.common.Constant;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.model.UploadStatus;
import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.repository.oracle.OracleRepository;

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

	@Transactional("mysqlTxManager")
	public void markRunning(String pageId, String username, String columnName) {
		UploadProgress uploadProgress = mysqlRepository.selectUploadProgress(pageId, username);

		if (uploadProgress != null && uploadProgress.getUploadStatus() == UploadStatus.RUNNING) {
			throw new BizException("Not finished upload");
		}

		String underScoredColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, columnName);

		mysqlRepository.upsertUploadProgress(pageId, username, underScoredColumnName, UploadStatus.RUNNING);
	}

	@Transactional("mysqlTxManager")
	public void markFinish(String pageId, String username) {
		mysqlRepository.upsertUploadProgress(pageId, username, null, UploadStatus.FINISH);
	}

	public UploadProgress getFinishedUploadProgress(String pageId, String username) {
		UploadProgress uploadProgress = mysqlRepository.selectUploadProgress(pageId, username);

		if (uploadProgress == null) {
			throw new BizException("Not uploaded yet");
		}

		if (uploadProgress.getUploadStatus() == UploadStatus.RUNNING) {
			throw new BizException("Not finished upload");
		}

		return uploadProgress;
	}

	@Transactional("oracleTxManager")
	public void prepareTemporaryTable(String pageId, String username) {
		if (oracleRepository.countTable(pageId, username) <= 0) {
			oracleRepository.createTable(pageId, username);
		}
		oracleRepository.truncateTable(pageId, username);
	}

	public void bulkInsert(String pageId, String username, Path uploadPath) {
		JobParameters jobParameters = new JobParametersBuilder().addString(Constant.PAGE_ID, pageId)
				.addString(Constant.USERNAME, username).addString(Constant.FILE_PATH, uploadPath.toString())
				.toJobParameters();

		try {
			jobLauncher.run(importJob, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			log.error("{}", e);
		}
	}

	public Path saveUploadFile(MultipartFile file) {
		File uploadDirectory = new File(Constant.UPLOADED_FILE_DIR);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdir();
		}

		Path uploadPath = Paths.get(Constant.UPLOADED_FILE_DIR, UUID.randomUUID() + "-" + file.getOriginalFilename());

		try (InputStream in = file.getInputStream()) {
			Files.copy(in, uploadPath);
		} catch (IOException e) {
			throw new BizException("Failed to copy the upload file");
		}

		return uploadPath;
	}

	public void removeUploadedFile(String filePath) {
		try {
			if (!Files.deleteIfExists(new File(filePath).toPath())) {
				log.warn("Failed to delete [{}] because it did not exist", filePath);
			}
		} catch (IOException e) {
			log.error("{} : {}", e.toString(), filePath);
		}
	}

}
