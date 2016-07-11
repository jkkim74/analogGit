package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * The BpmMgntEmailSndObj class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class BpmMgntEmailSndObj implements Serializable {
	private static final long serialVersionUID = 8534835181533285992L;
	private String sndEmailAddr;     /* 메일 발송자 */
	private String emailSndTitleNm;  /* 메일 제목 */
	private String emailTemplate;    /* 이메일 템플릿 정보 */
	private String emailType;     /* 이메일 종류 */
	private Date auditDtm;     /* 최종변경일시 */
	private String auditId;     /* 최종변경자ID */
	private long sndObjId;     /* EMAIL발송 정보 */
}
