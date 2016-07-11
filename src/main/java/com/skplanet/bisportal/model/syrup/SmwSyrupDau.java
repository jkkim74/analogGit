package com.skplanet.bisportal.model.syrup;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * The RtdDau class(OCB, Syrup, MobileShopper 정보).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SmwSyrupDau extends BasePivot implements Serializable {
	private static final long serialVersionUID = -9019316790800326744L;
	private String strdDt; //기준일
	private String mbrCnt; //회원수
	private String authMbrCnt; //인증회원수
	private String ciMbrCnt; //CI회원수
	private String operDtm; //작업일시
}
