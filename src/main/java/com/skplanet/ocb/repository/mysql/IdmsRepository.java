package com.skplanet.ocb.repository.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.ocb.model.AutoMappedMap;

@Repository
public interface IdmsRepository {

	void insertLogin(@Param("username") String username, @Param("userIp") String userIp,
			@Param("loginDttm") String loginDttm);

	void updateLogout(@Param("username") String username, @Param("userIp") String userIp,
			@Param("logoutDttm") String logoutDttm);

	List<AutoMappedMap> selectYesterdayLoginout();

}
