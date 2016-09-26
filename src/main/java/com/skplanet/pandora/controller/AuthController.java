package com.skplanet.pandora.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.pandora.model.ApiResponse;
import com.skplanet.pandora.model.UserInfo;
import com.skplanet.pandora.service.UserService;

@RestController
public class AuthController {

	@Autowired
	private UserService userService;

	static UserDetails getUserInfo() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@GetMapping("/auth/me")
	public UserDetails me() {
		return getUserInfo();
	}

	@PostMapping("/api/users")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse createUser(@RequestParam Map<String, Object> params) {
		userService.createUser((String) params.get("username"));
		return ApiResponse.builder().message("사용자 등록 성공").build();
	}

	@GetMapping("/api/users")
	public List<UserInfo> getUsers() {
		return userService.getUsers();
	}

}
