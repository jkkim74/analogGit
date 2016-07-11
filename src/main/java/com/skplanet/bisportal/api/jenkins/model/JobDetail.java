package com.skplanet.bisportal.api.jenkins.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HO-JIN, HA (mimul@wiseeco.com)
 * @see <a href="http://172.19.103.102:8080/job/VoyagerPortal_DEV/api/json"></a>
 */
@Data
@EqualsAndHashCode(callSuper = false, doNotUseGetters = false)
public class JobDetail extends Job implements Serializable {
	private static final long serialVersionUID = -1823134095857662787L;
	private String displayName;
	private boolean buildable;
	private String color;
	private List<Build> builds;
	private Build firstBuild;
	private Build lastBuild;
	private Build lastCompletedBuild;
	private Build lastFailedBuild;
	private Build lastStableBuild;
	private Build lastSuccessfulBuild;
	private Build lastUnstableBuild;
	private Build lastUnsuccessfulBuild;
	private int nextBuildNumber;
	private List<Job> downstreamProjects;
	private List<Job> upstreamProjects;
}
