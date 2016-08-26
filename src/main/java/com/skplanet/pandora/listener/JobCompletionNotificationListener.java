package com.skplanet.pandora.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("Job Execution Status : {}", jobExecution.getStatus());

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

		}
	}

}