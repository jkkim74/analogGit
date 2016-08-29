package com.skplanet.pandora.repository.mysql;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.pandora.common.Constant;
import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.model.UploadStatus;

@Repository
public interface MysqlRepository {

	AutoMappedMap selectTest(Map<String, Object> params);

	UploadProgress selectUploadProgress(@Param(Constant.PAGE_ID) String pageId,
			@Param(Constant.USERNAME) String username);

	int upsertUploadProgress(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username,
			@Param(Constant.COLUMN_NAME) String columnName, @Param(Constant.UPLOAD_STATUS) UploadStatus uploadStatus);

}
