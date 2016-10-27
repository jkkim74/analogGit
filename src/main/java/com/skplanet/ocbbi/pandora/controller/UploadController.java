package com.skplanet.ocbbi.pandora.controller;

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
import com.skplanet.ocb.model.ApiResponse;
import com.skplanet.ocb.model.AutoMappedMap;
import com.skplanet.ocb.model.UploadProgress;
import com.skplanet.ocb.model.UploadStatus;
import com.skplanet.ocb.repository.mysql.UploadMetaRepository;
import com.skplanet.ocb.repository.oracle.UploadTempRepository;
import com.skplanet.ocb.service.UploadService;
import com.skplanet.ocb.util.Helper;
import com.skplanet.ocbbi.pandora.service.ForwardService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/files")
@Slf4j
public class UploadController {

	@Autowired
	private UploadMetaRepository metaRepository;

	@Autowired
	private UploadTempRepository tempRepository;

	@Autowired
	private UploadService uploadService;

	@Autowired
	private ForwardService forwardService;

	@PostMapping
	public ApiResponse handleUpload(@RequestParam("file") MultipartFile file, @RequestParam String pageId,
			@RequestParam String columnName) throws IOException {

		String username = Helper.currentUser().getUsername();

		log.info("Uploading file... pageId={}, username={}, columnName={}", pageId, username, columnName);

		if (file.isEmpty()) {
			throw new BizException("빈 파일입니다");
		}

		if ("PAN0105".equalsIgnoreCase(pageId)) {
			forwardService.forwardToFtpServer(file, pageId, username, columnName);
		} else {
			JobParameters jobParameters = uploadService.readyToImport(file, pageId, username, columnName);

			uploadService.beginImport(jobParameters);
		}

		return ApiResponse.builder().message("업로드 성공").build();
	}

	@GetMapping
	public ApiResponse getUploadedPreview(@RequestParam String pageId,
			@RequestParam(defaultValue = "false") boolean countOnly) {

		String username = Helper.currentUser().getUsername();

		if (countOnly) {
			// 업로드 진행상태 체크 용도
			UploadProgress uploadProgress = metaRepository.selectUploadProgress(pageId, username);

			if (uploadProgress == null) {
				return ApiResponse.builder().code(910).message("업로드를 먼저 해주세요").build();
			}

			int count = tempRepository.countUploadedPreview(pageId, username);

			if (uploadProgress.getUploadStatus() == UploadStatus.FINISH) {
				return ApiResponse.builder().code(910).message(UploadStatus.FINISH.toString()).totalItems(count)
						.build();
			}
			return ApiResponse.builder().totalItems(count).build();
		} else {
			List<AutoMappedMap> list = tempRepository.selectUploadedPreview(pageId, username);
			return ApiResponse.builder().value(list).build();
		}
	}

}