package com.skplanet.bisportal.model.mstr;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsFactory;

/**
 * The MstrResponse class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 * 
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MstrResponse implements Serializable {
	private static final long serialVersionUID = -4393388374947513267L;
	private String baseUrl;
	private String serverName;
	private int serverPort;
	private String projectName;
	private String usrSmgr;
	private String hiddenSections;
	private String currentViewMedia;
	private String visMode;
	private String evt;
	private String src;
	private String reportId;
	private String documentId;
	private String sessionId;
	private String prompts;
	private String dateTypes;
	private String searchStartDate;
	private String searchEndDate;
	private transient WebObjectsFactory objFactory;
	private transient WebIServerSession serverSession;
}
