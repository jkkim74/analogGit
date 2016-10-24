package com.skplanet.ocb.repository;

import java.util.List;

import com.skplanet.ocb.model.AutoMappedMap;

public interface UploadTempRepository {

	int countTable(String pageId, String username);

	void createTable(String pageId, String username);

	void truncateTable(String pageId, String username);

	int countUploadedPreview(String pageId, String username);

	List<AutoMappedMap> selectUploadedPreview(String pageId, String username);

}
