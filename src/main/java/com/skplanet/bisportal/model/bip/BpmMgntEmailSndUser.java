package com.skplanet.bisportal.model.bip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * The BpmMgntRsltScrnCmnt class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BpmMgntEmailSndUser implements Serializable {
	private static final long serialVersionUID = 790902384541890156L;
	private String auditDtm;     /* 최종변경일시 */
	private String auditId;     /* 최종변경자ID */
	private long sndObjId;     /* 경영실적 EMAIL 발송 내용 정보 */
	private String loginId;     /* 대상자ID(사번) */
}
