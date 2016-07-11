package com.skplanet.bisportal.model.syrup;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * Created by cookatrice on 2015. 1. 8..
 */
@Data
public class SmwAppExec implements Serializable {

	private static final long serialVersionUID = 7788911628157752855L;

	private String standardDate;

	private String os;
	private String sex;
	private String appVersion;
	private String ageRange;
	private String telecom;

	private BigDecimal totUv;
	private BigDecimal totPv;
	private BigDecimal mbrUv;
	private BigDecimal mbrPv;
	private BigDecimal wletExecUv;
	private BigDecimal wletExecPv;
	private BigDecimal mbrshTwnUv;
	private BigDecimal mbrshTwnPv;
	private BigDecimal wdgExecUv;
	private BigDecimal wdgExecPv;

	@Override
	public String toString() {
		return standardDate + " " + totUv + " " + totPv + " " + mbrUv + " " + mbrPv + " " + wletExecUv + " "
				+ wletExecPv + " " + mbrshTwnUv + " " + mbrshTwnPv + " " + wdgExecUv + " " + wdgExecPv;
	}
}
