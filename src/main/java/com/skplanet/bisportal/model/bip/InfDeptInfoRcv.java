package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * The InfDeptInfoRcv(조직정보 변경사항 수신 테이블) class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class InfDeptInfoRcv implements Serializable {
	private static final long serialVersionUID = -7718423563521603992L;
	private String groupType;     
	private String senddt;     
	private String tFlag;     
	private String deptfullnm;     
	private String etsosok;     
	private String esosok;     
	private String mprs;     
	private String wrkplc;     
	private String dirempno;     
	private String lowyn;     
	private String highpartdeptnm;     
	private String highpartdept;     
	private String deptreducnm;     
	private String levelcd;     
	private String tsosok;     
	private String sosok;     
	private String deptnm;     
	private String outdept;     
	private String destructdd;     
	private String crtyymmdd;     
	private String indept;     
}
