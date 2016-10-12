package com.skplanet.pandora.repository.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.ocb.security.UserInfo;
import com.skplanet.ocb.util.AutoMappedMap;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.model.UploadStatus;

@Repository
public interface MysqlRepository {

	AutoMappedMap selectTest(Map<String, Object> params);

	UploadProgress selectUploadProgress(@Param("pageId") String pageId, @Param("username") String username);

	int upsertUploadProgress(@Param("pageId") String pageId, @Param("username") String username,
			@Param("columnName") String columnName, @Param("filename") String filename,
			@Param("uploadStatus") UploadStatus uploadStatus);

	List<UserInfo> selectUsers(Map<String, Object> params);

	int upsertUser(Map<String, Object> params);

	void insertAuthorities(@Param("username") String username, @Param("authorities") String authorities);

	void deleteAuthorities(@Param("username") String username, @Param("authorities") String authorities);

	void insertAccesses(@Param("username") String username, @Param("pageList") String pageList);

	void deleteAccesses(@Param("username") String username, @Param("pageList") String pageList);

}
