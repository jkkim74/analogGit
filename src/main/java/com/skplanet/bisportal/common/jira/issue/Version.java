package com.skplanet.bisportal.common.jira.issue;

import lombok.Data;

import org.joda.time.DateTime;

@Data
public class Version {
	private String self;
	private String id;
	private String description;
	private String name;
	private Boolean archived;
	private Boolean released;
	private DateTime releaseDate;
	private Boolean overdue;
	private String userReleaseDate;
	private String projectId;
	
	private DateTime startDate;
	private String userStartDate;
}
