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
public class BpmMgntEmailSndCtt implements Serializable {
	private static final long serialVersionUID = 2403235536478382686L;
	private String sndDtm;     /* 발송주기/발송일시 */
	private String emailCtxtCtt;     /* EMAIL본문내용 */
	private String sndEmailAddr;     /* 발송EMAIL주소 */
	private String emailSndTitleNm;     /* EMAIL발송제목명 */
	private String auditId;     /* 최종변경자/발송자ID */
	private Date auditDtm;     /* 최종변경일시 */
	private long sndObjId;     /* 이메일 발송 템플릿 정보 */
}
