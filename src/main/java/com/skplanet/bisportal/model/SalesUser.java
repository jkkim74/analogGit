package com.skplanet.bisportal.model;

import java.io.Serializable;

import lombok.Data;

/**
 * The SalesUser class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Data
public class SalesUser implements Serializable {
	private static final long serialVersionUID = -1781317851759966468L;
	private Long id;
	private String username;
	private String fullName;
	private int birthType;
	private String birthDate;
	private String sex;
	private String email;
	private String mobile;
	private String signupDate;
	private String lastUpdate;
	private int totalRecordCount;
}
