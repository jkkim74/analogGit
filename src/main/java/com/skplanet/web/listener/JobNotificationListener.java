package com.skplanet.web.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.skplanet.web.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobNotificationListener extends JobExecutionListenerSupport {

	@Autowired
	private UploadService uploadService;

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("Job Execution Status : {}", jobExecution.getStatus());

		if (!jobExecution.isRunning()) {
			JobParameters parameters = jobExecution.getJobParameters();
			uploadService.endImport(parameters);
		}
	}

}