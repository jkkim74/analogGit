package com.skplanet.ocb.repository.mysql;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IdmsRepository {

	void login(@Param("username") String username, @Param("userIp") String userIp,
			@Param("loginDttm") String loginDttm);

	void logout(@Param("username") String username, @Param("userIp") String userIp,
			@Param("logoutDttm") String logoutDttm);

}
