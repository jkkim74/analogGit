package com.skplanet.pandora.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.pandora.exception.EmptyFileException;
import com.skplanet.pandora.model.ApiResponse;
import com.skplanet.pandora.model.AutoMappedMap;
import com.skplanet.pandora.repository.mysql.MysqlRepository;
import com.skplanet.pandora.repository.oracle.OracleRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api")
@Slf4j
public class ApiController {

	public static final String UPLOADED_FILE_DIR = System.getProperty("user.home") + "/pandora-upload";

	// @Autowired
	// private ResourceLoader resourceLoader;
	// resourceLoader.getResource("file:" + Paths.get(ROOT,
	// filename).toString())

	@Autowired
	private MysqlRepository mysqlRepository;

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job importUserJob;

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public ApiResponse handleUpload(@RequestParam("file") MultipartFile file, @RequestParam String username,
			@RequestParam String pageId, @RequestParam String dataType) throws IOException {

		log.info("Uploading file... pageId={}, username={}, , dataType={}", username, pageId, dataType);

		if (file.isEmpty()) {
			throw new EmptyFileException();
		}

		Path uploadPath = saveUploadFile(file);

		// mysqlRepository.updateUploadStatus(pageId, username,
		// UploadStatus.RUNNING);

		if (oracleRepository.countTable(pageId, username) > 0) {
		} else {
			oracleRepository.createTable(pageId, username);
		}
		oracleRepository.truncateTable(pageId, username);

		// List<String> list = FileUtils.readLines(uploadPath.toFile(),
		// StandardCharsets.UTF_8);
		// List<String> bulkList = IOUtils.readLines(file.getInputStream(),
		// StandardCharsets.UTF_8);
		// oracleRepository.insertBulk(pageId, username, bulkList);

		JobParameters jobParameters = new JobParametersBuilder().addString("pageId", pageId)
				.addString("username", username).addString("filePath", uploadPath.toString()).toJobParameters();

		try {
			jobLauncher.run(importUserJob, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {

			e.printStackTrace();
		}

		// mysqlRepository.updateUploadStatus(pageId, username,
		// UploadStatus.FINISH);

		// removeUploadFile(uploadPath);

		return ApiResponse.builder().type(ApiResponse.DEFAULT_TYPE).code(ApiResponse.DEFAULT_CODE)
				.message("Uploaded " + file.getOriginalFilename()).build();
	}

	private Path saveUploadFile(MultipartFile file) throws IOException {
		File directory = new File(UPLOADED_FILE_DIR);
		if (!directory.exists()) {
			directory.mkdir();
		}

		Path uploadPath = Paths.get(UPLOADED_FILE_DIR, UUID.randomUUID() + "-" + file.getOriginalFilename());

		try (InputStream in = file.getInputStream()) {
			Files.copy(in, uploadPath);
		}
		return uploadPath;
	}

	@Async
	private void removeUploadFile(Path uploadPath) throws IOException {
		if (!Files.deleteIfExists(uploadPath)) {
			log.warn("Failed to delete the uploaded file after all processing... {}", uploadPath);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/upload")
	public List<AutoMappedMap> getUploadedPreview(@RequestParam String username, @RequestParam String pageId) {
		return oracleRepository.selectPreview(pageId, username);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/memberInfo")
	public List<AutoMappedMap> getUploadedPreview(@RequestParam Map<String, Object> params) {
		return oracleRepository.selectMemberInfo(params);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/selectTmp")
	public void selectTmp() {
		List<Map<String, Object>> list = oracleRepository.selectTmp("pan0101", "jhon");

		for (Map<String, Object> map : list) {
			System.out.println(map.toString());
		}
	}

}
