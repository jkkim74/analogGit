package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * The BpmMgntRsltScrnCmnt class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class BpmMgntRsltScrnCmnt implements Serializable {
	private static final long serialVersionUID = 6956891883478407778L;
	private String stcStrdDt;
	private String mgntRsltScrnClCd;
	private String auditId;
	private Date auditDtm;
	private String cmntCtt;
	private String ocbComment;
	private String syrupComment;
	private String tstoreComment;
	private String hoppinComment;
	private String tmapComment;
	private String sk11Comment;
	private String tcloudComment;
}
