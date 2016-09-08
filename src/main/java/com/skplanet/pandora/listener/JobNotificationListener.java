package com.skplanet.pandora.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.skplanet.pandora.common.Constant;
import com.skplanet.pandora.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobNotificationListener extends JobExecutionListenerSupport {

	@Autowired
	private UploadService uploadService;

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("Job Execution Status : {}", jobExecution.getStatus());

		switch (jobExecution.getStatus()) {
		case COMPLETED:
		case FAILED:
			JobParameters parameters = jobExecution.getJobParameters();
			uploadService.markFinish(parameters.getString(Constant.PAGE_ID), parameters.getString(Constant.USERNAME));
			uploadService.removeUploadedFile(parameters.getString(Constant.FILE_PATH));
			break;
		default:
			break;
		}
	}

}