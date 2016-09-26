package com.skplanet.pandora.repository.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.SubmissionType;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.model.UploadStatus;
import com.skplanet.pandora.model.UserInfo;

public interface MysqlRepository {

	AutoMappedMap selectTest(Map<String, Object> params);

	UploadProgress selectUploadProgress(@Param("pageId") String pageId, @Param("username") String username);

	int upsertUploadProgress(@Param("pageId") String pageId, @Param("username") String username,
			@Param("columnName") String columnName, @Param("uploadStatus") UploadStatus uploadStatus);

	int upsertSubmissionResult(@Param("submissionType") SubmissionType submissionType);

	List<String> selectUsers();

	int upsertUserInfo(UserInfo user);

}
