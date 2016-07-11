package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * COMUSER.COM_INTG_COM_CD model.
 * 
 * @author sjune
 */
@Data
public class ComIntgComCd implements Serializable {
    private static final long serialVersionUID = 1969571940773706511L;
    private String sysIndCd;
	private String comGrpCd;
	private String comCd;
	private String comGrpCdNm;
	private String comCdNm;
	private Long sortSeq;
	private String useYn;
	private String comCdDesc;
	private Timestamp operDtm;
}
