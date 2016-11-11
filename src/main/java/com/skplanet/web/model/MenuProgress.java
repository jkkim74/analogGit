package com.skplanet.web.model;

import lombok.Data;

@Data
public class MenuProgress {

	private String menuId;
	private String username;
	private String param;
	private String filename;
	private ProgressStatus status;

}
