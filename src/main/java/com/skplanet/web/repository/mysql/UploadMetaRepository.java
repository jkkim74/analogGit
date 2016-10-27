package com.skplanet.web.repository.mysql;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.web.model.UploadProgress;
import com.skplanet.web.model.UploadStatus;

@Repository
public interface UploadMetaRepository {

	UploadProgress selectUploadProgress(@Param("pageId") String pageId, @Param("username") String username);

	int upsertUploadProgress(@Param("pageId") String pageId, @Param("username") String username,
			@Param("columnName") String columnName, @Param("filename") String filename,
			@Param("uploadStatus") UploadStatus uploadStatus);

}
