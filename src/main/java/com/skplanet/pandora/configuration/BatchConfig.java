package com.skplanet.pandora.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.skplanet.pandora.controller.listener.JobNotificationListener;
import com.skplanet.pandora.model.Preview;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	JobExecutionListener jobExecutionListener;

	@Autowired
	DataSource oracleDataSource;

	@Autowired
	SqlSessionFactory oracleSqlSessionFactory;

	@Override
	public void setDataSource(DataSource dataSource) {
		// avoid No qualifying bean of type. use memory job repository.
	}

	@Override
	protected JobLauncher createJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(getJobRepository());
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

	@Bean
	@JobScope
	public FlatFileItemReader<Preview> itemReader(@Value("#{jobParameters[filePath]}") String filePath) {
		FlatFileItemReader<Preview> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(filePath));
		reader.setLineMapper(new DefaultLineMapper<Preview>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "column1" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Preview>() {
					{
						setTargetType(Preview.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	@JobScope
	public JdbcBatchItemWriter<Preview> itemWriter(@Value("#{jobParameters[pageId]}") String pageId,
			@Value("#{jobParameters[username]}") String username) {

		// MyBatisBatchItemWriter<Object> writer = new
		// MyBatisBatchItemWriter<>();
		// writer.setSqlSessionFactory(oracleSqlSessionFactory);
		// writer.setStatementId("com.skplanet.pandora.repository.oracle.OracleRepository.insertUploadedData");

		JdbcBatchItemWriter<Preview> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Preview>());
		writer.setSql("INSERT INTO TMP_" + pageId.toUpperCase() + "_" + username.toUpperCase()
				+ "(column1) VALUES (:column1)");
		writer.setDataSource(oracleDataSource);

		return writer;
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobNotificationListener();
	}

	@Bean
	public Job importJob() {
		return jobBuilderFactory.get("importJob").incrementer(new RunIdIncrementer()).listener(listener()).flow(step1())
				.end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Preview, Preview> chunk(1000).reader(itemReader(null))
				.writer(itemWriter(null, null)).build();
	}

}