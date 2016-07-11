package com.skplanet.bisportal.model.mstr;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The MstrRequest class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 * 
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MstrSession implements Serializable {
	private static final long serialVersionUID = -1420549949662725070L;
	private String username;
	private String ip;
	private String projectId;
	private String sessionId;
}
