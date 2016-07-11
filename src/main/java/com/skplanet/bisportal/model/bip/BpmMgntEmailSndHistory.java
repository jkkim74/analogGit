package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * The BpmMgntEmailSndCtt class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class BpmMgntEmailSndHistory implements Serializable {
	private static final long serialVersionUID = -7529899293269722701L;
	private Date auditDtm;     /* 변경일자 */
	private String sndYn;     /* 발송성공여부 */
	private String auditId;     /* 최종변경자/발송자ID */
	private String rcvEmailAddr;     /* 이메일 수신자 */
	private String rcvId;     /* 수신자 ID */
	private long sndObjId;     /* 이메일 발송 템플릿 정보 */
	private String sndDtm;     /* 발송일시 */
}
