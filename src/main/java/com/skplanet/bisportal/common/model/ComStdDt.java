package com.skplanet.bisportal.common.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The ComStdDt class(주차 정보).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ComStdDt implements Serializable {
	private static final long serialVersionUID = 6781408834702910912L;
	private String stdDt;
	private String stdYy;
	private String stdYm;
	private String wkCd;
	private String stdYmWkCd;
	private String dayIndCt;
}
