package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by cookatrice on 2014. 8. 11..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc Table
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsCntntMbilFlyrPageSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -8386662358948132874L;
	private String stdDt;// 기준일자 varchar2 8
	private String stdym; // stdYm 기준일자 varchar2(6) n
	private String pocIndCd; // POC 구분 코드 VARCHAR2(2 BYTE)
	private String pageId; // 페이지 ID VARCHAR2(150 BYTE)
	private Long id; // PUSH_GRP_ID OR PUSH_ID NUMBER(10,0)
	private String title; // 전단 제목 OR 지역 이름 VARCHAR2(255 BYTE)
	private Long inqCntOnUv; // 조회 UV NUMBER(10,0)
	private Long inqCntOnLv; // 조회 LV NUMBER(10,0)
	private Long inqCntOnPv; // 조회 PV NUMBER(10,0)
}
