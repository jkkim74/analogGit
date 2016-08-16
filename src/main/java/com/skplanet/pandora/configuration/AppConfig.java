package com.skplanet.pandora.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.skplanet.pandora")
@EnableTransactionManagement
@Import({ WebConfig.class, DataSourceConfig.class })
public class AppConfig {

}
