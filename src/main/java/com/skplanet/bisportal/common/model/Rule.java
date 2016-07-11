package com.skplanet.bisportal.common.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The Rule class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Rule implements Serializable {
	private static final long serialVersionUID = -1247744558755048754L;
	private String junction;
	private String field;
	private String op;
	private String data;
}
