package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by cookatrice
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ObsCntntTraraFlyrSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -5972579035252967967L;
	public String stdDt;// 기준일자 VARCHAR2(8 BYTE)
	public String stdYm;// 기준일자 VARCHAR2(6 BYTE)
	public String pocIndCd;// POC 구분 코드 VARCHAR2(2 BYTE)
	public String pushGrpId;// 그룹핑 아이디 VARCHAR2(50 BYTE)
	public String grpId;// 권역 VARCHAR2(50 BYTE)
	public Long rcvCntOnUv;// 수신 UV NUMBER(10,0)
	public Long rcvCntOnLv;// 수신 LV NUMBER(10,0)
	public Long rcvCntOnPv;// 수신 PV NUMBER(10,0)
	public Long clickCntOnUv;// 클릭 UV NUMBER(10,0)
	public Long clickCntOnLv;// 클릭 LV NUMBER(10,0)
	public Long clickCntOnPv;// 클릭 PV NUMBER(10,0)
	public String sndStartDt;// 발송시작일 VARCHAR2(8 BYTE)
	public String sndEndDt;// 발송종료일 VARCHAR2(8 BYTE)

}
