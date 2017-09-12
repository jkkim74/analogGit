package com.skplanet.web.repository.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.web.security.UserInfo;

@Repository
public interface UserRepository {

	List<UserInfo> selectUsers(Map<String, Object> params);

	int upsertUser(Map<String, Object> params);

	void insertAuthorities(@Param("username") String username, @Param("authorities") String authorities);

	void deleteAuthorities(@Param("username") String username, @Param("authorities") String authorities);

	void insertAccesses(@Param("username") String username, @Param("menuList") String menuList);

	void deleteAccesses(@Param("username") String username, @Param("menuList") String menuList);

	void updateMasking(@Param("username") String username, @Param("maskingYn") String maskingYn);

	Integer selectPassCount(@Param("username") String username);

	void updatePassCount(@Param("username") String username);

	void insertPassCount(@Param("username") String username);

    void deletePassCount(@Param("username") String username);

	void updateUserEnabled(@Param("username") String username);

	void deleteAccessToken(@Param("username") String username);

	void deleteRefreshToken(@Param("username") String username);

	void insertUserAuthHis(@Param("userId") String userId,@Param("targetUserId") String targetUserId,
						   @Param("actionId") String actionId,@Param("isAdmin") String isAdmin,@Param("ip") String ip);
	
}
