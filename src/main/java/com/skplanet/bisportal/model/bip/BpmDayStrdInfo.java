package com.skplanet.bisportal.model.bip;

import java.io.Serializable;

import lombok.Data;

/**
 * The BpmDayStrdInfo class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class BpmDayStrdInfo implements Serializable {
	private static final long serialVersionUID = 1191773646854053249L;
	private String dowNm; /* 요일명 */
	private long cdSeq; /* 코드순번 */
	private String dayStrdVal; /* 일기준값 */
	private String auditDtm; /* 최종변경일시 */
	private String auditId; /* 최종변경자ID */
	private String dlyStrdDt; /* 일통계기준일자 */
}
