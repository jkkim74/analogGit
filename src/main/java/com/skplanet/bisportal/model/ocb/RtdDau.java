package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The RtdDau class(OCB, Syrup, MobileShopper 정보).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RtdDau extends BasePivot implements Serializable {
	private static final long serialVersionUID = 2050564264331022482L;
	private String strdDt; //조회 당일(OCB).
	private String syrupStrdDt;//조회일자 전날.
	private String preStrdDt;//조회일자 전날(23시).
	private String syrupPreStrdDt;//조회일자 전날(23시).
	private String oneHourBeforeStrdDt;//조회일자 전날.
	private String syrupOneHourBeforeStrdDt;//조회일자 전날.
	private String twoHourBeforeStrdDt;//조회일자 전날.
	private String syrupTwoHourBeforeStrdDt;//조회일자 전날.
	private int flag = 0;
	private int syrupFlag = 0;
	private String hour;
	private String syrupHour;
	private String oneHourBefore;// 한시간전
	private String syrupOneHourBefore;// 한시간전
	private String twoHourBefore;// 두시간전
	private String syrupTwoHourBefore;// 두시간전
	private BigDecimal dau;
	private BigDecimal dauAcm;
	private BigDecimal hau;
	private BigDecimal hauAcm;
	private BigDecimal gap;
	private BigDecimal fctGap;
	private BigDecimal fct;
	private BigDecimal hauMthAvg;
	/* OCB/Syrup Report */
	private BigDecimal mobileTotal;// NUMBER
	private BigDecimal app;// NUMBER
	private BigDecimal mOcb;// NUMBER
	private BigDecimal offlineCnt;// NUMBER
	private String operDtm;// DATE
	/* Stickness Report */
	private BigDecimal dauStickness;// NUMBER
	/* Syrup WAU/MAU */
	private String mbrCnt; //회원수
	private String authMbrCnt; //인증회원수
	private String ciMbrCnt; //CI회원수
}
