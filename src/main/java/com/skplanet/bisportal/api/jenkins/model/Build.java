package com.skplanet.bisportal.api.jenkins.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HO-JIN, HA (mimul@wiseeco.com)
 * @see <a href="http://172.19.103.102:8080/job/VoyagerPortal_DEV/api/json"></a>
 */
@Data
public class Build implements Serializable {
	private static final long serialVersionUID = 1151746206209823371L;
	private int number;
	private String url;
}
