package com.skplanet.pandora.configuration;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:/config/common.properties")
@EnableTransactionManagement(proxyTargetClass = true)
public class DataSourceConfig implements EnvironmentAware, ApplicationContextAware {

	ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	Environment env;

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

	@Bean
	public PlatformTransactionManager mysqlTxManager() {
		return new DataSourceTransactionManager(mysqlDataSource());
	}

	@Bean
	public PlatformTransactionManager oracleTxManager() {
		return new DataSourceTransactionManager(oracleDataSource());
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
		sessionFactory.setConfigLocation(applicationContext.getResource("classpath:config/pandora/mybatis-config.xml"));
		sessionFactory.setMapperLocations(applicationContext.getResources("classpath*:sql/mysql/**/*.xml"));
		return sessionFactory.getObject();
	}

	@Bean
	public MapperScannerConfigurer mysqlMapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("com.skplanet.pandora.repository.mysql");
		configurer.setSqlSessionFactoryBeanName("mysqlSqlSessionFactory");
		return configurer;
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
		sessionFactory.setConfigLocation(applicationContext.getResource("classpath:config/pandora/mybatis-config.xml"));
		sessionFactory.setMapperLocations(applicationContext.getResources("classpath*:sql/oracle/**/*.xml"));
		return sessionFactory.getObject();
	}

	@Bean
	public MapperScannerConfigurer oracleMapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("com.skplanet.pandora.repository.oracle");
		configurer.setSqlSessionFactoryBeanName("oracleSqlSessionFactory");
		return configurer;
	}

	@Bean(destroyMethod = "close")
	public BasicDataSource qcDataSource() {
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
	public SqlSessionFactory qcSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(qcDataSource());
		sessionFactory.setConfigLocation(applicationContext.getResource("classpath:config/pandora/mybatis-config.xml"));
		sessionFactory.setMapperLocations(applicationContext.getResources("classpath*:sql/querycache/**/*.xml"));
		return sessionFactory.getObject();
	}

	@Bean
	public MapperScannerConfigurer qcMapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("com.skplanet.pandora.repository.querycache");
		configurer.setSqlSessionFactoryBeanName("qcSqlSessionFactory");
		return configurer;
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
		// .addScript("sql/insert-data.sql")
	}

	@Bean
	@Override
	public DataSource oracleDataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.addScript("sql/init-db-oracle.sql").build();
		// .addScript("sql/insert-data.sql")
	}

}