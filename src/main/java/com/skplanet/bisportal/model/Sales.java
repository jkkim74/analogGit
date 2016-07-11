package com.skplanet.bisportal.model;

import lombok.Data;

/**
 * The Sales class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Data
public class Sales {
	private Long productId;
	private Long userId;
	private Double amt;
	private String payDate;
}
