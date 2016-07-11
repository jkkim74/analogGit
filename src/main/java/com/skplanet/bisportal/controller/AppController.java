package com.skplanet.bisportal.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * The AppController class.
 * 
 * @author sjune
 */
@Controller
@Slf4j
public class AppController {

	@Value("#{common['voyager.baseUrl.api']}")
	private String apiBaseUrl;

	/**
	 * application index page
	 * 
	 * @param mav
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView home(ModelAndView mav) {
		mav.addObject("apiBaseUrl", apiBaseUrl);
		mav.setViewName("app");
		return mav;
	}
}
