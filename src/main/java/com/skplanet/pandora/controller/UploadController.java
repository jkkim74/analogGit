package com.skplanet.pandora.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.pandora.common.BizException;
import com.skplanet.pandora.model.ApiResponse;
import com.skplanet.pandora.model.Preview;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/upload")
@Slf4j
public class UploadController {

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private UploadService uploadService;

	@RequestMapping(method = RequestMethod.POST)
	public ApiResponse handleUpload(@RequestParam("file") MultipartFile file, @RequestParam String pageId,
			@RequestParam String username, @RequestParam String columnName) throws IOException {

		log.info("Uploading file... pageId={}, username={}, columnName={}", pageId, username, columnName);

		if (file.isEmpty()) {
			throw new BizException("Empty file");
		}

		uploadService.markRunning(pageId, username, columnName);

		Path filePath = uploadService.saveUploadFile(file);

		uploadService.prepareTemporaryTable(pageId, username);

		uploadService.bulkInsert(pageId, username, filePath);

		return ApiResponse.builder().code(0).message("Uploaded " + file.getOriginalFilename()).build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Preview> getUploadedPreview(@RequestParam String pageId, @RequestParam String username) {
		return oracleRepository.selectPreview(pageId, username);
	}

}
