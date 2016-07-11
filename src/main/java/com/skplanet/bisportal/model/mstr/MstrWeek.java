package com.skplanet.bisportal.model.mstr;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The MstrWeek class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MstrWeek implements Serializable {
	private static final long serialVersionUID = -7774612074693061584L;
	private String wkPerdCd;
	private String wkPerdNm;
	private String wkStDt;
	private String wkEdDt;
	private String wkDt;
}
