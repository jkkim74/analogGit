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

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UploadController {

	public static final String UPLOADED_FILE_DIR = System.getProperty("user.home") + "/pandora-upload";

	// @Autowired
	// private ResourceLoader resourceLoader;
	// resourceLoader.getResource("file:" + Paths.get(ROOT,
	// filename).toString())

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public void handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
		File directory = new File(UPLOADED_FILE_DIR);
		if (!directory.exists()) {
			directory.mkdir();
		}

		if (file.isEmpty()) {
			log.info("File is empty");
			return;
		}

		try (InputStream in = file.getInputStream()) {
			Files.copy(in, Paths.get(UPLOADED_FILE_DIR, UUID.randomUUID() + "-" + file.getOriginalFilename()));
		}
	}

}
