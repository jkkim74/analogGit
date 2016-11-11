package com.skplanet.web.repository.mysql;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.web.model.UploadProgress;
import com.skplanet.web.model.ProgressStatus;

@Repository
public interface UploadMetaRepository {

	UploadProgress selectUploadProgress(@Param("pageId") String pageId, @Param("username") String username);

	int upsertUploadProgress(@Param("pageId") String pageId, @Param("username") String username,
			@Param("param") String param, @Param("filename") String filename, @Param("status") ProgressStatus status);

}
