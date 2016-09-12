package com.skplanet.pandora.configuration;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class MyBatisConfig {

	@Bean
	public org.apache.ibatis.session.Configuration configuration() {
		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		config.setMapUnderscoreToCamelCase(true);
		config.getTypeAliasRegistry().registerAliases("com.skplanet.pandora.model");
		return config;
	}

	@Bean
	public DatabaseIdProvider databaseIdProvider() throws IOException {
		DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();

		Properties p = new Properties();
		p.setProperty("SQL Server", "sqlserver");
		p.setProperty("DB2", "db2");
		p.setProperty("Oracle", "oracle");
		p.setProperty("MySQL", "mysql");
		p.setProperty("PostgreSQL", "postgresql");
		p.setProperty("HSQL", "hsql");
		p.setProperty("H2", "h2");

		databaseIdProvider.setProperties(p);
		return databaseIdProvider;
	}

	private SqlSessionFactory sqlSessionFactory(DataSource dataSource, String mapperLocations)
			throws IOException, Exception {
		PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setConfiguration(configuration());
		sessionFactory.setDatabaseIdProvider(databaseIdProvider());
		sessionFactory.setMapperLocations(pathResolver.getResources(mapperLocations));
		return sessionFactory.getObject();
	}

	@Bean
	@Autowired
	public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource mysqlDataSource)
			throws Exception {
		return sqlSessionFactory(mysqlDataSource, "classpath*:/sql/mysql/**/*.xml");
	}

	@Bean
	public MapperScannerConfigurer mysqlMapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("com.skplanet.pandora.repository.mysql");
		configurer.setSqlSessionFactoryBeanName("mysqlSqlSessionFactory");
		return configurer;
	}

	@Bean
	@Autowired
	public SqlSessionFactory oracleSqlSessionFactory(@Qualifier("oracleDataSource") DataSource oracleDataSource)
			throws Exception {
		return sqlSessionFactory(oracleDataSource, "classpath*:/sql/oracle/**/*.xml");
	}

	@Bean
	public MapperScannerConfigurer oracleMapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("com.skplanet.pandora.repository.oracle");
		configurer.setSqlSessionFactoryBeanName("oracleSqlSessionFactory");
		return configurer;
	}

	@Bean
	@Autowired
	public SqlSessionFactory querycacheSqlSessionFactory(
			@Qualifier("querycacheDataSource") DataSource querycacheDataSource) throws Exception {
		return sqlSessionFactory(querycacheDataSource, "classpath*:/sql/querycache/**/*.xml");
	}

	@Bean
	public MapperScannerConfigurer querycacheMapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("com.skplanet.pandora.repository.querycache");
		configurer.setSqlSessionFactoryBeanName("querycacheSqlSessionFactory");
		return configurer;
	}

}
