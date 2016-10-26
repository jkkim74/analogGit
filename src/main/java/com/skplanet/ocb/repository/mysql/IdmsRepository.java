package com.skplanet.ocb.repository.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IdmsRepository {

	void insertLogin(@Param("username") String username, @Param("userIp") String userIp,
			@Param("loginDttm") String loginDttm);

	void updateLogout(@Param("username") String username, @Param("userIp") String userIp,
			@Param("logoutDttm") String logoutDttm);

	List<Map<String, Object>> selectAccessLogAtYesterday();

	void insertMemberSearch(@Param("selDttm") String selDttm, @Param("wasIp") String wasIp,
			@Param("username") String username, @Param("userIp") String userIp, @Param("mbrId") String mbrId,
			@Param("mbrKorNm") String mbrKorNm, @Param("pageId") String pageId, @Param("funcCd") String funcCd,
			@Param("mbrCnt") int mbrCnt);

	List<Map<String, Object>> selectMemberSearchLogAtYesterday();

}
