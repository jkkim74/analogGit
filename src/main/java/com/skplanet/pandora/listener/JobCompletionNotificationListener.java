package com.skplanet.pandora.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.skplanet.pandora.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	@Autowired
	private UploadService uploadService;

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("Job Execution Status : {}", jobExecution.getStatus());

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			JobParameters parameters = jobExecution.getJobParameters();
			uploadService.markFinish(parameters.getString("pageId"), parameters.getString("username"));
			uploadService.removeUploadedFile(parameters.getString("filePath"));
		}
	}

}