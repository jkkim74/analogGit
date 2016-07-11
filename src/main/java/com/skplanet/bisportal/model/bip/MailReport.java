package com.skplanet.bisportal.model.bip;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The MailReport class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class MailReport implements Serializable {
	private static final long serialVersionUID = -454020852313439963L;
	private String dimension;
	private String period;  // 일누적, 월누적
	private String dimensionUnit; // 천명, 천건, 천원
	private int svcId;
	private String idxClGrpCd;
	private BigDecimal basicValue = BigDecimal.ZERO;//기준값
	private BigDecimal milliBasicValue = BigDecimal.ZERO; // 기준값의 1/1000
	private String increaseValue = "0.0%";//지난 30일 편균대비 증감
	private BigDecimal oneMonthAgoMonthAccu = BigDecimal.ZERO;//당월누적
	private BigDecimal thisMonthAccu = BigDecimal.ZERO;//당월누적
	private String oneMonthAgoIncreaseAccu = "0.0%";//전월동기대비 증감
	private String brandImageUrl = "check_img04.jpg";
	private String dimensionImageUrl = StringUtils.EMPTY;
	private String increaseColor = "#808080";
	private String oneMonthAgoIncreaseColor = "#808080";
}
