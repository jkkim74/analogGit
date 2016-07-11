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
public class ObsCntntStoreFlyrSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -499535255389166523L;
	private String stdDt;// 기준일자 varchar2(8 byte)
	private String stdYm; // 기준일자 varchar2(6 byte)
	private String pocIndCd;// poc 구분 코드 varchar2(2 byte)
	private String flyrId;// 전단 id varchar2(50 byte)
	private String flyrTitle;// 전단 제목 varchar2(255 byte)
	private Long rcvCnt;// 수신 number(10,0)
	private Long clickCnt;// 클릭 number(10,0)
	private String storeNm;// 상점명 varchar2(255 byte)
	private String sndDt;// 발송요청일자 varchar2(8 byte)

}
