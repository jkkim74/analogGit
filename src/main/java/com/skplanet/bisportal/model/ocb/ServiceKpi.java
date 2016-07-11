package com.skplanet.bisportal.model.ocb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The ServiceKpi class(서비스별 KPI 정보).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceKpi implements Serializable {
	private static final long serialVersionUID = 8953095529846251209L;
	private BigDecimal ocbActiveUserCnt;
	private BigDecimal syrupActiveUserCnt;
	private BigDecimal mobileShopperActiveUserCnt;
}
