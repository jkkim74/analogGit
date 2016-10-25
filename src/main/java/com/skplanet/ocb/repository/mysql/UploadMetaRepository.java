package com.skplanet.ocb.repository.mysql;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.ocb.model.UploadProgress;
import com.skplanet.ocb.model.UploadStatus;

@Repository
public interface UploadMetaRepository {

	UploadProgress selectUploadProgress(@Param("pageId") String pageId, @Param("username") String username);

	int upsertUploadProgress(@Param("pageId") String pageId, @Param("username") String username,
			@Param("columnName") String columnName, @Param("filename") String filename,
			@Param("uploadStatus") UploadStatus uploadStatus);

}
