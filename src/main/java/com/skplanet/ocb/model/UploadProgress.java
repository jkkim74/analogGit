package com.skplanet.ocb.model;

import lombok.Data;

@Data
public class UploadProgress {

	private String pageId;
	private String username;
	private String columnName;
	private String filename;
	private UploadStatus uploadStatus;

}