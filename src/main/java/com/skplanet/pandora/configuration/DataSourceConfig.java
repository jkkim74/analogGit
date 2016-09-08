package com.skplanet.pandora.configuration;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.skplanet.pandora.model.AutoMappedMap;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class DataSourceConfig implements EnvironmentAware, ApplicationContextAware {

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	protected Environment env;

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

	@Bean
	public PlatformTransactionManager mysqlTxManager() {
		return new DataSourceTransactionManager(mysqlDataSource());
	}

	@Bean(destroyMethod = "close")
	public DataSource mysqlDataSource() {
		BasicDataSource datasource = getCommonBasicDataSource();
		datasource.setDriverClassName(env.getProperty("jdbc.pandora.mysql.driverClass"));
		datasource.setUrl(env.getProperty("jdbc.pandora.mysql.url"));
		datasource.setUsername(env.getProperty("jdbc.pandora.mysql.username"));
		datasource.setPassword(env.getProperty("jdbc.pandora.mysql.password"));
		datasource.setInitialSize(env.getProperty("jdbc.pandora.mysql.initialSize", Integer.class));
		datasource.setMinIdle(env.getProperty("jdbc.pandora.mysql.initialSize", Integer.class));
		return datasource;
	}

	@Bean
	public SqlSessionFactory mysqlSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(mysqlDataSource());
		sessionFactory.setConfiguration(mybatisConfiguration());
		sessionFactory.setDatabaseIdProvider(mybatisDatabaseIdProvider());
		sessionFactory.setMapperLocations(applicationContext.getResources("classpath*:/sql/mysql/**/*.xml"));
		return sessionFactory.getObject();
	}

	@Bean
	public MapperScannerConfigurer mysqlMapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("com.skplanet.pandora.repository.mysql");
		configurer.setSqlSessionFactoryBeanName("mysqlSqlSessionFactory");
		return configurer;
	}

	@Bean
	public PlatformTransactionManager oracleTxManager() {
		return new DataSourceTransactionManager(oracleDataSource());
	}

	@Bean(destroyMethod = "close")
	public DataSource oracleDataSource() {
		BasicDataSource datasource = getCommonBasicDataSource();
		datasource.setDriverClassName(env.getProperty("jdbc.pandora.oracle.driverClass"));
		datasource.setUrl(env.getProperty("jdbc.pandora.oracle.url"));
		datasource.setUsername(env.getProperty("jdbc.pandora.oracle.username"));
		datasource.setPassword(env.getProperty("jdbc.pandora.oracle.password"));
		datasource.setInitialSize(env.getProperty("jdbc.pandora.oracle.initialSize", Integer.class));
		datasource.setMinIdle(env.getProperty("jdbc.pandora.oracle.initialSize", Integer.class));
		return datasource;
	}

	@Bean
	public SqlSessionFactory oracleSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(oracleDataSource());
		sessionFactory.setConfiguration(mybatisConfiguration());
		sessionFactory.setDatabaseIdProvider(mybatisDatabaseIdProvider());
		sessionFactory.setMapperLocations(applicationContext.getResources("classpath*:/sql/oracle/**/*.xml"));
		return sessionFactory.getObject();
	}

	@Bean
	public MapperScannerConfigurer oracleMapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("com.skplanet.pandora.repository.oracle");
		configurer.setSqlSessionFactoryBeanName("oracleSqlSessionFactory");
		return configurer;
	}

	@Bean
	public PlatformTransactionManager querycacheTxManager() {
		return new DataSourceTransactionManager(querycacheDataSource());
	}

	@Bean(destroyMethod = "close")
	public DataSource querycacheDataSource() {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName(env.getProperty("jdbc.pandora.querycache.driverClass"));
		datasource.setUrl(env.getProperty("jdbc.pandora.querycache.url"));
		datasource.setUsername(env.getProperty("jdbc.pandora.querycache.username"));
		datasource.setPassword(env.getProperty("jdbc.pandora.querycache.password"));
		datasource.setInitialSize(env.getProperty("jdbc.pandora.querycache.initialSize", Integer.class));
		datasource.setMaxActive(50);
		datasource.setMaxIdle(15);
		datasource.setMinIdle(env.getProperty("jdbc.pandora.querycache.initialSize", Integer.class));
		return datasource;
	}

	@Bean
	public SqlSessionFactory querycacheSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(querycacheDataSource());
		sessionFactory.setConfiguration(mybatisConfiguration());
		sessionFactory.setMapperLocations(applicationContext.getResources("classpath*:/sql/querycache/**/*.xml"));
		return sessionFactory.getObject();
	}

	@Bean
	public MapperScannerConfigurer querycacheMapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("com.skplanet.pandora.repository.querycache");
		configurer.setSqlSessionFactoryBeanName("querycacheSqlSessionFactory");
		return configurer;
	}

	@Bean
	public org.apache.ibatis.session.Configuration mybatisConfiguration() {
		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		config.setMapUnderscoreToCamelCase(true);
		config.getTypeAliasRegistry().registerAlias("AutoMappedMap", AutoMappedMap.class);
		return config;
	}

	@Bean
	public DatabaseIdProvider mybatisDatabaseIdProvider() throws IOException {
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

	private BasicDataSource getCommonBasicDataSource() {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setMaxActive(50);
		datasource.setMaxIdle(15);
		datasource.setTestOnBorrow(false);
		datasource.setValidationQuery("select 1 from dual");
		datasource.setTimeBetweenEvictionRunsMillis(10000);
		datasource.setTestWhileIdle(true);
		datasource.setNumTestsPerEvictionRun(3);
		datasource.setMinEvictableIdleTimeMillis(-1);
		return datasource;
	}

}

@Configuration
@Profile("local")
class LocalDataSourceConfig extends DataSourceConfig {

	@Bean
	@Override
	public DataSource mysqlDataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.addScript("sql/init-db-mysql.sql").build();
	}

	@Bean
	@Override
	public DataSource oracleDataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.addScripts("sql/init-db-oracle.sql", "sql/data-oracle.sql").build();
	}

	@Bean
	@Override
	public DataSource querycacheDataSource() {
		return new EmbeddedDatabaseBuilder().build();
	}

}