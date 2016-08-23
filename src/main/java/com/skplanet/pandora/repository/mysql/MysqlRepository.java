package com.skplanet.pandora.repository.mysql;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.pandora.model.UploadStatus;

@Repository
public interface MysqlRepository {

	Map<String, Object> selectTest(Map<String, Object> params);

	void updateUploadStatus(@Param("pageId") String pageId, @Param("username") String username, @Param("uploadStatus") UploadStatus uploadStatus);

}
