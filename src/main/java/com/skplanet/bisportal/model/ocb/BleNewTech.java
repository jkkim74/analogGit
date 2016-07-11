package com.skplanet.bisportal.model.ocb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The BleNewTech class(Ble NewTech 통계 정보).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BleNewTech implements Serializable {
	private static final long serialVersionUID = 6744056400734053628L;
	private int totalRecordCount;
	private String stdDt; // 기준일자 varchar2(8 byte)
	private String name1;
	private String name2;
	private String mid;
	private String campaignId;
	private String bid;
	private String svcIndCd;

	private BigDecimal ocbSrpPv;
	private BigDecimal ocbCoinPv;
	private BigDecimal ocbLeafletPv;
	private BigDecimal srpPushPopPv;
	private BigDecimal srpPushNotiPv;
	private BigDecimal srpIconBtnPv;
	private BigDecimal ocbSrpUv;
	private BigDecimal ocbCoinUv;
	private BigDecimal ocbLeafletUv;
	private BigDecimal srpPushPopUv;
	private BigDecimal srpPushNotiUv;
	private BigDecimal srpIconBtnUv;

	private BigDecimal ocbMlfClkPv;
	private BigDecimal ocbChkinClkPv;
	private BigDecimal srpOkClkPv;
	private BigDecimal srpCnclClkPv;
	private BigDecimal srpIconBtnClkPv;
	private BigDecimal srpIconPv;
	private BigDecimal ocbMlfClkUv;
	private BigDecimal ocbChkinClkUv;
	private BigDecimal srpOkClkUv;
	private BigDecimal srpCnclClkUv;
	private BigDecimal srpIconBtnClkUv;
	private BigDecimal srpIconUv;

	private BigDecimal ocbMlfReadPv;
	private BigDecimal ocbChkinReadPv;
	private BigDecimal srpOkReadPv;
	private BigDecimal evntPgPv;
	private BigDecimal copnDlPv;
	private BigDecimal mbrTownPv;
	private BigDecimal grpCopnDlPv;
	private BigDecimal ocbMlfReadUv;
	private BigDecimal ocbChkinReadUv;
	private BigDecimal srpOkReadUv;
	private BigDecimal evntPgUv;
	private BigDecimal copnDlUv;
	private BigDecimal mbrTownUv;
	private BigDecimal grpCopnDlUv;

	private BigDecimal ssCopnDlPv;
	private BigDecimal rdmCopnDlPv;
	private BigDecimal srpCopnDlPv;
	private BigDecimal bleCopnDlPv;
	private BigDecimal mbrIssPv;
	private BigDecimal ssCopnDlUv;
	private BigDecimal rdmCopnDlUv;
	private BigDecimal srpCopnDlUv;
	private BigDecimal bleCopnDlUv;
	private BigDecimal mbrIssUv;
}
