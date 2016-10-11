package com.skplanet.pandora.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
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

	public static UserDetails getUserInfo() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@GetMapping("/auth/me")
	public UserDetails me() {
		return getUserInfo();
	}

	@PostMapping("/api/users")
	@RolesAllowed("ROLE_ADMIN")
	@Transactional("mysqlTxManager")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse createUser(@RequestParam Map<String, Object> params) {
		userService.createUser(params);
		return ApiResponse.builder().message("사용자 등록 성공").build();
	}

	@GetMapping("/api/users")
	@RolesAllowed("ROLE_ADMIN")
	public List<UserInfo> getUsers(@RequestParam Map<String, Object> params) {
		return userService.getUsers(params);
	}

	@PostMapping("/api/userInfo")
	@RolesAllowed("ROLE_ADMIN")
	@Transactional("mysqlTxManager")
	public ApiResponse updateUser(@RequestParam Map<String, Object> params) {
		UserInfo user = userService.updateUser(params);
		return ApiResponse.builder().message("사용자 정보 수정 완료").value(user).build();
	}

	@PostMapping("/api/saveAccess")
	@RolesAllowed("ROLE_ADMIN")
	@Transactional("mysqlTxManager")
	public ApiResponse saveAccess(@RequestParam String username, @RequestParam String pageList) {
		userService.updateAccesses(username, pageList);
		return ApiResponse.builder().message("화면 권한 수정 완료").build();
	}

	@PostMapping("/api/admin")
	@RolesAllowed("ROLE_ADMIN")
	@Transactional("mysqlTxManager")
	public ApiResponse saveAdmin(@RequestParam String username, @RequestParam boolean isAdmin) {
		userService.updateAdmin(username, isAdmin);
		return ApiResponse.builder().message("관리자 정보 수정 완료").build();
	}

}
