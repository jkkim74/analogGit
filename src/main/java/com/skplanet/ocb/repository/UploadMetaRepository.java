package com.skplanet.ocb.repository;

import com.skplanet.ocb.model.UploadProgress;
import com.skplanet.ocb.model.UploadStatus;

public interface UploadMetaRepository {

	UploadProgress selectUploadProgress(String pageId, String username);

	int upsertUploadProgress(String pageId, String username, String underScoredColumnName, String filename,
			UploadStatus running);

}
