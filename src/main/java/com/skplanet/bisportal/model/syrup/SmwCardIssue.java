package com.skplanet.bisportal.model.syrup;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * Created by cookatrice on 2015. 1. 9..
 */
@Data
public class SmwCardIssue implements Serializable {

	private static final long serialVersionUID = 7119608758692244857L;

	private String os;
	private String standardDate;
	private String membership;
	private String sex;
	private String ageRange;
	private String telecom;

	private BigDecimal notiCnt;

	private BigDecimal totIssueCnt;
	private BigDecimal totIssueUserCnt;
	private BigDecimal dayIssueCnt;
	private BigDecimal dayIssueUserCnt;

	@Override
	public String toString() {
		return standardDate + " " + notiCnt + " " + totIssueCnt + " " + totIssueUserCnt + " " + dayIssueCnt + " "
				+ dayIssueUserCnt;
	}
}
