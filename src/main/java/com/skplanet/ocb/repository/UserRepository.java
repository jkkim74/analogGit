package com.skplanet.ocb.repository;

import java.util.List;
import java.util.Map;

import com.skplanet.ocb.security.UserInfo;

public interface UserRepository {

	List<UserInfo> selectUsers(Map<String, Object> map);

	int upsertUser(Map<String, Object> map);

	void insertAuthorities(String username, String authorities);

	void deleteAuthorities(String username, String authorities);

	void deleteAccesses(String username, String pageList);

	void insertAccesses(String username, String pageList);

}
