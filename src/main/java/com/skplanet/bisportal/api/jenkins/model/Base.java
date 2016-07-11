package com.skplanet.bisportal.api.jenkins.model;

import lombok.Data;

import java.util.List;

/**
 * Jenkins의 사용자 권한 내에 있는 Job 기본 객체
 *
 * @author HO-JIN, HA (mimul@wiseeco.com)
 * @see <a href="http://172.19.103.102:8080/api/json"></a>
 */
@Data
public class Base {
	private String mode;
	private int numExecutors;
	private List<Job> jobs;
}
