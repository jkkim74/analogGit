package com.skplanet.pandora.repository.mysql;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.ocb.model.UploadProgress;
import com.skplanet.ocb.model.UploadStatus;
import com.skplanet.ocb.repository.UploadMetaRepository;

@Repository
public interface MysqlUploadMetaRepository extends UploadMetaRepository {

	UploadProgress selectUploadProgress(@Param("pageId") String pageId, @Param("username") String username);

	int upsertUploadProgress(@Param("pageId") String pageId, @Param("username") String username,
			@Param("columnName") String columnName, @Param("filename") String filename,
			@Param("uploadStatus") UploadStatus uploadStatus);

}
