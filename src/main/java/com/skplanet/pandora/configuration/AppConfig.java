package com.skplanet.pandora.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;

@Configuration
@ComponentScan(basePackages = "com.skplanet.pandora")
public class AppConfig extends AsyncConfigurerSupport {

}
