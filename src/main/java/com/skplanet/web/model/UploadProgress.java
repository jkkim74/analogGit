package com.skplanet.web.model;

import lombok.Data;

@Data
public class UploadProgress {

	private String username;
	private String pageId;
	private String param;
	private String filename;
	private ProgressStatus status;

}
