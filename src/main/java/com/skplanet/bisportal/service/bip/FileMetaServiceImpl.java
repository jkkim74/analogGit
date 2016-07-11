package com.skplanet.bisportal.service.bip;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.model.bip.FileMeta;
import com.skplanet.bisportal.repository.bip.FileMetaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cookatrice
 */
@Service
public class FileMetaServiceImpl implements FileMetaService {
	@Autowired
	private FileMetaRepository fileMetaRepository;

	@Override
	public List<FileMeta> getFileMetaLists(FileMeta params) {
		return fileMetaRepository.getFileMetaLists(params);
	}

	@Override
	@Transactional
	public int deleteFile(List<Long> fileMetaIdList, String updateId) {
		return fileMetaRepository.deleteFile(fileMetaIdList, updateId);
	}
}
