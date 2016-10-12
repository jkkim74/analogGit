package com.skplanet.pandora.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.ocb.exception.BizException;
import com.skplanet.ocb.util.ApiResponse;
import com.skplanet.ocb.util.AutoMappedMap;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.model.UploadStatus;
import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.service.UploadService;
import com.skplanet.pandora.util.Constant;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/upload")
@Slf4j
public class UploadController {

	@Autowired
	private MysqlRepository mysqlRepository;

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private UploadService uploadService;

	@PostMapping
	public ApiResponse handleUpload(@RequestParam("file") MultipartFile file, @RequestParam String pageId,
			@RequestParam String columnName) throws IOException {

		String username = AuthController.getUserInfo().getUsername();

		log.info("Uploading file... pageId={}, username={}, columnName={}", pageId, username, columnName);

		if (file.isEmpty()) {
			throw new BizException("빈 파일입니다");
		}

		if (Constant.PAN0105.equalsIgnoreCase(pageId)) {
			uploadService.forwardToFtpServer(file, pageId, username, columnName);
		} else {
			JobParameters jobParameters = uploadService.readyToImport(file, pageId, username, columnName);

			uploadService.beginImport(jobParameters);
		}

		return ApiResponse.builder().message("업로드 성공").build();
	}

	@GetMapping
	public ApiResponse getUploadedPreview(@RequestParam String pageId,
			@RequestParam(defaultValue = "false") boolean countOnly) {

		String username = AuthController.getUserInfo().getUsername();

		if (countOnly) {
			// 업로드 진행상태 체크 용도
			UploadProgress uploadProgress = mysqlRepository.selectUploadProgress(pageId, username);

			if (uploadProgress == null) {
				return ApiResponse.builder().code(910).message("업로드를 먼저 해주세요").build();
			}

			int count = oracleRepository.countUploadedPreview(pageId, username);

			if (uploadProgress.getUploadStatus() == UploadStatus.FINISH) {
				return ApiResponse.builder().code(910).message(UploadStatus.FINISH.toString()).totalRecords(count)
						.build();
			}
			return ApiResponse.builder().totalRecords(count).build();
		} else {
			List<AutoMappedMap> list = oracleRepository.selectUploadedPreview(pageId, username);
			return ApiResponse.builder().value(list).build();
		}
	}

}
