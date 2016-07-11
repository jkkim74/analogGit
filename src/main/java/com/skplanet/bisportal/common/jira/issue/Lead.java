package com.skplanet.bisportal.common.jira.issue;

import lombok.Data;

import com.skplanet.bisportal.common.jira.project.AvatarUrl;

@Data
public class Lead {
	private String self;
	private String name;
	private AvatarUrl avatarUrls;
	private String displayName;
	private Boolean active;
	private String key;
}
