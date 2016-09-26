package com.skplanet.pandora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.pandora.model.ApiResponse;
import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.UserInfo;
import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.service.UserService;

@RestController
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private MysqlRepository mysqlRepository;

	static UserDetails getUserInfo() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@GetMapping("/auth/me")
	public UserDetails me() {
		UserDetails user = getUserInfo();
		UserInfo userInfo = UserInfo.builder().username(user.getUsername()).authorities(user.getAuthorities()).build();
		userInfo.setPageList((String) mysqlRepository.selectAccess(user.getUsername()).get(0).get("pageList"));
		if (user instanceof UserInfo) {
			userInfo.setFullname(((UserInfo) user).getFullname());
			userInfo.setEmailAddr(((UserInfo) user).getEmailAddr());
			userInfo.setEnabled(user.isEnabled());
		}
		return userInfo;
	}

	@PostMapping("/api/users")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse createUser(@RequestParam String username) {
		userService.createUser(username);
		return ApiResponse.builder().message("사용자 등록 성공").build();
	}

	@GetMapping("/api/users")
	public List<UserInfo> getUsers() {
		return userService.getUsers();
	}

	@GetMapping("/api/usersAccess")
	public List<AutoMappedMap> getAccess() {
		return mysqlRepository.selectAccess(null);
	}

	@PostMapping("/api/saveAccess")
	@Transactional("mysqlTxManager")
	public ApiResponse saveAccess(@RequestParam String username, @RequestParam String pageList) {
		mysqlRepository.deleteAccess(username);

		if (!StringUtils.isEmpty(pageList)) {
			mysqlRepository.insertAccess(username, pageList);
		}
		return ApiResponse.builder().message("화면 권한 수정 완료").build();
	}

}
