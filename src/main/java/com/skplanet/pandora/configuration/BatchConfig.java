package com.skplanet.pandora.configuration;

import java.util.ArrayList;

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
	public FlatFileItemReader<UploadedPreview> itemReader(@Value("#{jobParameters[filePath]}") String filePath,
			@Value("#{jobParameters[numberOfColumns]}") final Long numberOfColumns) {

		FlatFileItemReader<UploadedPreview> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(filePath));

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		ArrayList<String> names = new ArrayList<>();
		for (int i = 0; i < numberOfColumns; i++) {
			names.add("column" + (i + 1));
		}
		lineTokenizer.setNames(names.toArray(new String[0]));

		BeanWrapperFieldSetMapper<UploadedPreview> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(UploadedPreview.class);

		DefaultLineMapper<UploadedPreview> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);

		reader.setLineMapper(lineMapper);

		return reader;
	}

	@Bean
	@JobScope
	public JdbcBatchItemWriter<UploadedPreview> itemWriter(@Value("#{jobParameters[pageId]}") String pageId,
			@Value("#{jobParameters[username]}") String username,
			@Value("#{jobParameters[numberOfColumns]}") Long numberOfColumns) {

		JdbcBatchItemWriter<UploadedPreview> writer = new JdbcBatchItemWriter<UploadedPreview>() {
		};
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<UploadedPreview>());

		StringBuilder sql = new StringBuilder("INSERT INTO TMP_");
		sql.append(pageId.toUpperCase()).append('_').append(username.toUpperCase());
		sql.append('(');

		for (int i = 0; i < numberOfColumns; i++) {
			if (i != 0) {
				sql.append(',');
			}
			sql.append("column").append(i + 1);
		}

		sql.append(") VALUES (");

		for (int i = 0; i < numberOfColumns; i++) {
			if (i != 0) {
				sql.append(',');
			}
			sql.append(":column").append(i + 1);
		}

		sql.append(')');

		writer.setSql(sql.toString());
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
		return stepBuilderFactory.get("step1").<UploadedPreview, UploadedPreview> chunk(1000)
				.reader(itemReader(null, null)).writer(itemWriter(null, null, null)).build();
	}

}