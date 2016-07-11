package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.model.bip.FileMeta;

import java.util.List;

/**
 * @author cookatrice
 */
public interface FileMetaService {
	List<FileMeta> getFileMetaLists(FileMeta params);

	int deleteFile(List<Long> fileMetaIdList, String deleteId);

}
