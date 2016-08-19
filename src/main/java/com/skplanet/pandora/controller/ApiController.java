package com.skplanet.pandora.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skplanet.pandora.exception.EmptyFileException;
import com.skplanet.pandora.model.ApiResponse;

@RestController
public class ApiController {

	public static final String UPLOADED_FILE_DIR = System.getProperty("user.home") + "/pandora-upload";

	// @Autowired
	// private ResourceLoader resourceLoader;
	// resourceLoader.getResource("file:" + Paths.get(ROOT,
	// filename).toString())

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public ApiResponse handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
		File directory = new File(UPLOADED_FILE_DIR);
		if (!directory.exists()) {
			directory.mkdir();
		}

		if (file.isEmpty()) {
			throw new EmptyFileException();
		}

		try (InputStream in = file.getInputStream()) {
			Files.copy(in, Paths.get(UPLOADED_FILE_DIR, UUID.randomUUID() + "-" + file.getOriginalFilename()));
		}

		return ApiResponse.builder().type(ApiResponse.DEFAULT_TYPE).code(ApiResponse.DEFAULT_CODE)
				.message("Upload Success").build();
	}

}
