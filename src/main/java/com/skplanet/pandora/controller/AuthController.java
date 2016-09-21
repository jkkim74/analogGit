package com.skplanet.pandora.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	static UserDetails getUserInfo() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@GetMapping(path = "/auth/me")
	public UserDetails me() {
		return getUserInfo();
	}

}
