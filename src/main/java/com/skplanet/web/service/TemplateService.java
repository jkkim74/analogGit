package com.skplanet.web.service;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@SuppressWarnings("deprecation")
@Service
public class TemplateService {

	@Autowired
	private VelocityEngine velocityEngine;

	public String mergeTemplate(String template, Map<String, Object> map) {
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, "UTF-8", map);
	}

}
