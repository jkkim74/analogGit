package com.skplanet.pandora.configuration;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.mysql")
	public DataSource mysqlDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.oracle")
	public DataSource oracleDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.querycache")
	public DataSource querycacheDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public PlatformTransactionManager mysqlTxManager() {
		return new DataSourceTransactionManager(mysqlDataSource());
	}

	@Bean
	public PlatformTransactionManager oracleTxManager() {
		return new DataSourceTransactionManager(oracleDataSource());
	}

	@Bean
	public PlatformTransactionManager querycacheManager() {
		return new DataSourceTransactionManager(querycacheDataSource());
	}

}

@Configuration
@Profile("local")
class LocalDataSourceConfig extends DataSourceConfig {

	@Bean
	@Primary
	public DataSource mysqlDataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.addScript("sql/init-db-mysql.sql").build();
	}

	@Bean
	public DataSource oracleDataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.addScripts("sql/init-db-oracle.sql", "sql/data-oracle.sql").build();
	}

	@Bean
	public DataSource querycacheDataSource() {
		return new EmbeddedDatabaseBuilder().build();
	}

}