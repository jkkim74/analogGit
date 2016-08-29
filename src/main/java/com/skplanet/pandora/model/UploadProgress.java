package com.skplanet.pandora.model;

import lombok.Data;

@Data
public class UploadProgress {

	private String pageId;
	private String username;
	private String columnName;
	private UploadStatus uploadStatus;

}
