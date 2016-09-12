package com.skplanet.pandora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	// @Override
	// public void onStartup(ServletContext servletContext) throws
	// ServletException {
	// AnnotationConfigWebApplicationContext context = new
	// AnnotationConfigWebApplicationContext();
	// context.setConfigLocation("com.skplanet.pandora.configuration");
	//
	// servletContext.addListener(new ContextLoaderListener(context));
	//
	// ServletRegistration.Dynamic dispatcher =
	// servletContext.addServlet("dispatcher",
	// new DispatcherServlet(context));
	// dispatcher.setLoadOnStartup(1);
	// dispatcher.addMapping("/");
	// dispatcher.setMultipartConfig(new
	// MultipartConfigElement(System.getProperty("java.io.tmpdir")));
	//
	// FilterRegistration.Dynamic encodingFilter =
	// servletContext.addFilter("encodingFilter",
	// new CharacterEncodingFilter("UTF-8", true));
	// encodingFilter.addMappingForUrlPatterns(null, false, "/*");
	// }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}