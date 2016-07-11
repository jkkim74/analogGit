package com.skplanet.bisportal.common.jira.issue;

import java.util.Map;

import lombok.Data;

import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;

import com.skplanet.bisportal.common.jira.JsonPrettyString;

@Data
@EqualsAndHashCode(callSuper = false)
public class Worklog extends JsonPrettyString{
	
	private String self;
	private String id;
	private Reporter author;
	private Reporter updateAuthor;
	private String comment;
	private Map<String, String> visibility;
	private DateTime started;
	private String timeSpent;
	private Integer timeSpentSeconds;
	
	private Integer startAt;
	private Integer maxResults;
	private Integer total;
	private String[] worklogs;
}
