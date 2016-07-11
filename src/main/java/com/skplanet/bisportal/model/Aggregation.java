package com.skplanet.bisportal.model;

import java.io.Serializable;

import lombok.Data;

/**
 * The JqGridRequest class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Data
public class Aggregation implements Serializable {
	private static final long serialVersionUID = 7337313426775499583L;
	private int totalRecourdCount;
	private String key; // yyyyMMdd, yyyy, yyyyMM, 남자/여자 etc.
	private Double value; // 합계금액
}
