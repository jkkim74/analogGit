package com.skplanet.ocb.repository.oracle;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.skplanet.ocb.model.AutoMappedMap;

@Repository
public interface UploadTempRepository {

	int countTable(@Param("pageId") String pageId, @Param("username") String username);

	void createTable(@Param("pageId") String pageId, @Param("username") String username);

	void truncateTable(@Param("pageId") String pageId, @Param("username") String username);

	List<AutoMappedMap> selectUploadedPreview(@Param("pageId") String pageId, @Param("username") String username);

	int countUploadedPreview(@Param("pageId") String pageId, @Param("username") String username);

}