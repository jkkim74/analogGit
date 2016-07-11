package com.skplanet.bisportal.model.syrup;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * Created by cookatrice on 2015. 1. 9..
 */
@Data
public class SmwCponStat implements Serializable {

	private static final long serialVersionUID = -1378786080395075284L;

	private String os;
	private String standardDate;
	private String sex;
	private String ageRange;
	private String inputChannel;
	private String couponType;
	private String telecom;

	private BigDecimal issueCnt;
	private BigDecimal accumIssueCnt;
	private BigDecimal issueUserCnt;
	private BigDecimal accumIssueUserCnt;
	private BigDecimal useCnt;
	private BigDecimal accumUseCnt;
	private BigDecimal showCnt;
	private BigDecimal accumShowCnt;
	private BigDecimal issuePageCnt;
	private BigDecimal accumIssuePageCnt;
	private BigDecimal issuePageDtlCnt;
	private BigDecimal accumIssuePageDtlCnt;

	@Override
	public String toString() {
		return standardDate + " " + issueCnt + " " + accumIssueCnt + " " + issueUserCnt + " " + accumIssueUserCnt + " "
				+ useCnt + " " + accumUseCnt + " " + showCnt + " " + accumShowCnt + " " + issuePageCnt + " "
				+ accumIssuePageCnt + " " + issuePageDtlCnt + " " + accumIssuePageDtlCnt;
	}
}
