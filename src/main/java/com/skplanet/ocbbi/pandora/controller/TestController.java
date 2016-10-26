package com.skplanet.ocbbi.pandora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.ocb.service.IdmsService;

@RestController
@RequestMapping("test")
public class TestController {

	@Autowired
	private IdmsService idmsService;

	@GetMapping("triggerIdms")
	public void idms() {
		idmsService.send();
	}

}
