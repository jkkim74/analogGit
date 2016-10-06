package com.skplanet.pandora.model;

import lombok.Data;

@Data
public class UploadProgress {

	private String pageId;
	private String username;
	private String columnName;
	private String filename;
	private UploadStatus uploadStatus;
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	
	

}
