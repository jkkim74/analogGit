package com.skplanet.bisportal.model.syrup;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * Created by cookatrice on 2015. 1. 9..
 */
@Data
public class SmwAppClickN implements Serializable {
	private static final long serialVersionUID = 6850111682927879625L;

	private String os;
	private String standardDate;
	private String sex;
	private String ageRange;
	private String telecom;

	private String menu;
	private String menuDesc;

	private BigDecimal totUv;
	private BigDecimal totPv;

	@Override
	public String toString() {
		return standardDate + " " + totUv + " " + totPv;
	}
}
