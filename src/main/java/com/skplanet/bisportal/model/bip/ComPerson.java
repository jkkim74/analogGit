package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * The ComPerson class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class ComPerson implements Serializable {
	private static final long serialVersionUID = 7396875971567860374L;
	private String flag;
	private String author;
	private String holdoffidivi;
	private String userType;
	private String auditDtm;     
	private String auditId;     
	private String mblPhonNum;     
	private String offcPhonNum;     
	private String emailAddr;     
	private String sex;     
	private String psnTypCd;     
	private String aplyEndDt;     
	private String aplyStaDt;     
	private String userStCd;     
	private String jbchargenm;     
	private String jbcharge;     
	private String jbranknm;     
	private String jbrank;     
	private String jbgrpnm;     
	private String jbgrp;     
	private String tsosok;     
	private String sosok;     
	private String orgCd;     
	private String actvnYn;     
	private String userNm;     
	private String loginId;     
}
