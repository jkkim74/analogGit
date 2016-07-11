package com.skplanet.bisportal.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * The MultiHeader(Excel Header) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class MultiHeader implements Serializable {
	private static final long serialVersionUID = 4732753926818008652L;
	private String header1[];
	private String header2[];
	private String header3[];
}
