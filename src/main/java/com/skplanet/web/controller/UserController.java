package com.skplanet.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.web.model.ApiResponse;
import com.skplanet.web.security.UserInfo;
import com.skplanet.web.service.IdmsLogService;
import com.skplanet.web.service.UserService;
import com.skplanet.web.util.Helper;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private IdmsLogService idmsLogService;

	@PostMapping
	@RolesAllowed("ROLE_ADMIN")
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional("mysqlTxManager")
	public ApiResponse createUser(@RequestParam Map<String, Object> params) {
		userService.createUser(params);
		return ApiResponse.builder().message("사용자 등록 성공").build();
	}

	@GetMapping
	@RolesAllowed("ROLE_ADMIN")
	public List<UserInfo> getUsers(@RequestParam Map<String, Object> params) {
		return userService.getUsers(params);
	}

	@GetMapping("/me")
	public UserInfo me(@RequestParam(defaultValue = "false") boolean login) {
		UserInfo user = Helper.currentUser();
		if (login) {
			idmsLogService.login(user.getUsername(), Helper.currentClientIp(), Helper.nowDateTimeString());
		}
		return user;
	}

	@GetMapping("/logout")
	public void logout() {
		UserInfo user = Helper.currentUser();
		idmsLogService.logout(user.getUsername(), Helper.currentClientIp(), Helper.nowDateTimeString());
	}

	@PostMapping("/info")
	@RolesAllowed("ROLE_ADMIN")
	@Transactional("mysqlTxManager")
	public ApiResponse updateUser(@RequestParam Map<String, Object> params) {
		UserInfo adminUser = Helper.currentUser();
		UserInfo user = userService.updateUser(params);
		boolean isAdmin = true;
		if(params.get("enabled").equals(false)){
			isAdmin = false;
		}
		userService.insertUserAuthInfo(adminUser.getUsername().toString(),params.get("username").toString(),"info", isAdmin, Helper.currentClientIp());
		return ApiResponse.builder().message("사용자 정보 수정 완료").value(user).build();
	}

	@PostMapping("/access")
	@RolesAllowed("ROLE_ADMIN")
	@Transactional("mysqlTxManager")
	public ApiResponse saveAccess(@RequestParam String username, @RequestParam String menuList) {
		userService.updateAccesses(username, menuList);
		return ApiResponse.builder().message("화면 권한 수정 완료").build();
	}

	@PostMapping("/admin")
	@RolesAllowed("ROLE_ADMIN")
	@Transactional("mysqlTxManager")
	public ApiResponse saveAdmin(@RequestParam String username, @RequestParam boolean isAdmin) {
		UserInfo adminUser = Helper.currentUser();
		userService.insertUserAuthInfo(adminUser.getUsername().toString(),username,"admin", isAdmin, Helper.currentClientIp());
		userService.updateAdmin(username, isAdmin);
		return ApiResponse.builder().message("관리자 정보 수정 완료").build();
	}

	@PostMapping("/masking")
	@RolesAllowed("ROLE_ADMIN")
	@Transactional("mysqlTxManager")
    public ApiResponse updateMasking(@RequestParam String username, @RequestParam String maskingYn){
		UserInfo adminUser = Helper.currentUser();
		boolean isAdmin = true;
		if(maskingYn.equals("false")){
			isAdmin = false;
		}
		userService.insertUserAuthInfo(adminUser.getUsername().toString(),username,"admin", isAdmin, Helper.currentClientIp());
		userService.updateMasking(username, maskingYn);
		return ApiResponse.builder().message("마스킹 권한 수정 완료").build();
	}

}
