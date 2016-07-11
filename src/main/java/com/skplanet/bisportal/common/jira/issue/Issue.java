package com.skplanet.bisportal.common.jira.issue;

import java.io.File;
import java.io.IOException;

import lombok.Data;

import com.skplanet.bisportal.common.jira.JsonPrettyString;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Issue extends JsonPrettyString{
	private String expand;
	private String id;
	private String self;
	private String key;
	
	private IssueFields fields = new IssueFields();

	public void addAttachment(String filePath) throws IOException {		
		addAttachment(new File(filePath));
	}
	
	public void addAttachment(File file) throws IOException {	
		fields.addAttachment(file);				
	}

	/**
	 * check attachment exist 
	 * @return
	 */
	public boolean hasAttachments() {
		if (fields.getFileList() != null && fields.getFileList().size() > 0)
			return true;
		
		return false;
	}
}
