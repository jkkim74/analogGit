package com.skplanet.pandora.repository.mysql;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.pandora.model.Constant;
import com.skplanet.pandora.model.UploadStatus;

@Repository
public interface MysqlRepository {

	Map<String, Object> selectTest(Map<String, Object> params);

	UploadStatus selectUploadStatus(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username);

	void updateUploadStatus(@Param(Constant.PAGE_ID) String pageId, @Param(Constant.USERNAME) String username,
			@Param("uploadStatus") UploadStatus uploadStatus);

}
