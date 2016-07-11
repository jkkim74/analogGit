package com.skplanet.bisportal.model.ocb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Dau class(DAU 정보).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Dau implements Serializable {
	private static final long serialVersionUID = 6807309738718737711L;
	private String stdDt;
	private BigDecimal ocbActiveUserCnt;
	private BigDecimal expectedOcbActiveUserCnt;
	private BigDecimal syrupActiveUserCnt;
	private BigDecimal expectedSyrupActiveUserCnt;
	private BigDecimal mobileShopperActiveUserCnt;
	private BigDecimal expectedMobileShopperActiveUserCnt;
}
