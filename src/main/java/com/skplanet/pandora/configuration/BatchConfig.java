package com.skplanet.pandora.configuration;

import javax.sql.DataSource;

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

import com.skplanet.pandora.listener.JobNotificationListener;
import com.skplanet.pandora.model.UploadedPreview;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

	@Autowired
	protected JobBuilderFactory jobBuilderFactory;

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;

	@Autowired
	protected JobExecutionListener jobExecutionListener;

	@Autowired
	protected DataSource oracleDataSource;

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
	public FlatFileItemReader<UploadedPreview> itemReader(@Value("#{jobParameters[filePath]}") String filePath) {
		FlatFileItemReader<UploadedPreview> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(filePath));
		reader.setLineMapper(new DefaultLineMapper<UploadedPreview>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "column1" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<UploadedPreview>() {
					{
						setTargetType(UploadedPreview.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	@JobScope
	public JdbcBatchItemWriter<UploadedPreview> itemWriter(@Value("#{jobParameters[pageId]}") String pageId,
			@Value("#{jobParameters[username]}") String username) {

		JdbcBatchItemWriter<UploadedPreview> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<UploadedPreview>());
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
		return stepBuilderFactory.get("step1").<UploadedPreview, UploadedPreview> chunk(1000).reader(itemReader(null))
				.writer(itemWriter(null, null)).build();
	}

}