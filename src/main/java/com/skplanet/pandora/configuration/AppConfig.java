package com.skplanet.pandora.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = "com.skplanet.pandora")
@Import({ WebConfig.class, DataSourceConfig.class })
@EnableScheduling
public class AppConfig {

}
