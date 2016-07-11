package com.skplanet.bisportal.model.mstr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * The MstrRequest class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 * 
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MstrRequest implements Serializable {
	private static final long serialVersionUID = 5589447932679676766L;
	private String sessionId;
	private String projectId;
	private String objectId;
	private int objectType;
	private String menuCode;
	private String saveServerInfo;

	private String weekType;
	private String searchStartDate;
	private String searchEndDate;
}
