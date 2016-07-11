package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by cookatrice
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ObsCntntFlyrDetlSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 3973727397252467334L;
	public String stdDt;// 기준일자 VARCHAR2(8 BYTE)
	public String stdYm;// 기준일자 VARCHAR2(6 BYTE)
	public String pocIndCd;// POC 구분 코드 VARCHAR2(2 BYTE)
	public String pushGrpId;// 그룹핑 아이디 VARCHAR2(50 BYTE)
	public String flyrId;// 매장전단아이디 VARCHAR2(50 BYTE)
	public String grpId;// 권역 VARCHAR2(50 BYTE)
	public String storeNm;// 상점명 VARCHAR2(255 BYTE)
	public Long clickCntOnUv;// 클릭 UV NUMBER(10,0)
	public Long clickCntOnLv;// 클릭 LV NUMBER(10,0)
	public Long clickCntOnPv;// 클릭 PV NUMBER(10,0)
	public String sndStartDt;// 발송시작일 VARCHAR2(8 BYTE)
	public String sndEndDt;// 발송종료일 VARCHAR2(8 BYTE)
}
