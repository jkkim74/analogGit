package com.skplanet.bisportal.api.jenkins.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @author HO-JIN, HA (mimul@wiseeco.com)
 * @see <a href="http://172.19.103.102:8080/api/json"></a>
 */
@Data
public class Job implements Serializable {
	private static final long serialVersionUID = 8377904969250352914L;
	private String name;
	private String url;
}
