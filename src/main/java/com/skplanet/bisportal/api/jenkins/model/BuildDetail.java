package com.skplanet.bisportal.api.jenkins.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HO-JIN, HA (mimul@wiseeco.com)
 * @see <a href="http://172.19.103.102:8080/job/VoyagerPortal_DEV/98/api/json"></a>
 */
@Data
@EqualsAndHashCode(callSuper=false)
@SuppressWarnings("rawtypes")
public class BuildDetail extends Build implements Serializable {
	private static final long serialVersionUID = -9211839248717403494L;
	private List actions;
	private boolean building;
	private int duration;
	private String fullDisplayName;
	private String id;
	private long timestamp;
	private BuildResult result;
}
