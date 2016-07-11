package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * EISUSER.BPM_WK_STRD_INFO 주차 정보 model.
 * 
 * @author sjune
 */
@Data
public class BpmWkStrdInfo implements Serializable {
	private static final long serialVersionUID = -4760186735573351657L;
	private String wkStcStrdYmw;
	private String auditId;
	private String auditDtm;
	private String wkStrdVal;
	private Long cdSeq;
	private String wkStaDt;
	private String wkEndDt;
}
