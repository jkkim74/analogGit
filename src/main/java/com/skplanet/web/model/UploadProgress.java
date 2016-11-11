package com.skplanet.web.model;

import lombok.Data;

@Data
public class UploadProgress {

	private String pageId;
	private String username;
	private String columnName;
	private String filename;
	private ProgressStatus status;

}
