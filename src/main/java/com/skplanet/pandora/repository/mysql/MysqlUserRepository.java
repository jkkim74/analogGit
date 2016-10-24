package com.skplanet.pandora.repository.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.ocb.repository.UserRepository;
import com.skplanet.ocb.security.UserInfo;

@Repository
public interface MysqlUserRepository extends UserRepository {

	List<UserInfo> selectUsers(Map<String, Object> params);

	int upsertUser(Map<String, Object> params);

	void insertAuthorities(@Param("username") String username, @Param("authorities") String authorities);

	void deleteAuthorities(@Param("username") String username, @Param("authorities") String authorities);

	void insertAccesses(@Param("username") String username, @Param("pageList") String pageList);

	void deleteAccesses(@Param("username") String username, @Param("pageList") String pageList);

}
