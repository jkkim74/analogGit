package com.skplanet.pandora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skplanet.pandora.model.Member;
import com.skplanet.pandora.model.UploadProgress;
import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.pandora.service.UploadService;

@RestController
@RequestMapping("api")
public class ApiController {

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private UploadService uploadService;

	@RequestMapping(method = RequestMethod.GET, value = "/mergedMember")
	public List<Member> getMergedMember(@RequestParam String pageId, @RequestParam String username,
			@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit) {
		UploadProgress uploadProgress = uploadService.getFinishedUploadProgress(pageId, username);

		return oracleRepository.selectMergedMember(uploadProgress, offset, limit);
	}

}
