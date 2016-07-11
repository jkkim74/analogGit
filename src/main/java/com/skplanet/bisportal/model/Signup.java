package com.skplanet.bisportal.model;

import lombok.Data;

import java.io.Serializable;

/**
 * The Signup class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class Signup implements Serializable {
	private static final long serialVersionUID = 2307289951543776728L;
	private String signupDate;
	private String sex;
	private String ageGroup;
	private int age;
	private int signupCnt;
}
