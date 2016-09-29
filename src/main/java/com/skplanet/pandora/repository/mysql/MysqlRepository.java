package com.skplanet.pandora.repository.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.model.UploadStatus;
import com.skplanet.pandora.model.UserInfo;

public interface MysqlRepository {

	AutoMappedMap selectTest(Map<String, Object> params);

	UploadProgress selectUploadProgress(@Param("pageId") String pageId, @Param("username") String username);

	int upsertUploadProgress(@Param("pageId") String pageId, @Param("username") String username,
			@Param("columnName") String columnName, @Param("filename") String filename,
			@Param("uploadStatus") UploadStatus uploadStatus);

	List<String> selectUsers();

	int upsertUserInfo(UserInfo user);

	List<AutoMappedMap> selectAccess(@Param("username") String username);

	void insertAccess(@Param("username") String username, @Param("pageList") String pageList);

	void deleteAccess(@Param("username") String username);

}
