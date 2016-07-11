package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by cookatrice on 2014. 5. 14..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc feed
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsFeedSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 5691982714123423221L;
	private String stdDt;// VARCHAR2 (8 BYTE),
	private String stdYm;// VARCHAR2 (6 BYTE),
	private String pocIndCd;// VARCHAR2 (2 BYTE),
	private String feedId;// VARCHAR2 (4 BYTE),
	private String dispOrder;// VARCHAR2 (10 BYTE),
	private Long expsrCnt;// NUMBER (15),
	private Long expsrCntOnUv;// NUMBER (15), voyop-220 issue
	private Long expsrCntOnLv;// NUMBER (15),
	private Long clickCnt;// NUMBER (15),
	private Long clickCntOnUv;// NUMBER (15),
	private Long clickCntOnLv;// NUMBER (15),
	private Double timeSptFVst;// FLOAT (126),
	private Long prchsCnt;// NUMBER (10),
	private Long presntCnt;// NUMBER (10),
	private Long dnldCnt;// NUMBER (10),
	private Long storeViewCnt;// NUMBER (10),
	private Long msnActCnt;// NUMBER (10),
	private Long callReqCnt;// NUMBER (10),
	private Long hmpageCnt;// NUMBER (10),
	private Long prodViewCnt;// NUMBER (10),
	private Long detlViewCnt;// NUMBER (10),
	private Long scutCnt;// NUMBER (10),
	private Long callCnt;// NUMBER (10)
	private String isYn; // varchar2
	private String allianceYn; // varchar2
	private Long appExpsrCnt;// feed 신규추가
	private Long appExpsrCntOnUv;// feed 신규추가
	private Long appExpsrCntOnLv;// feed 신규추가

	private String feedNm; // feed 이름
	private String feedStartDate; // feed 시작일
	private String feedEndDate; // feed 종료일
}
