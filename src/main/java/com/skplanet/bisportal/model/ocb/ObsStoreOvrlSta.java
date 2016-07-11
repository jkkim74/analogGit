package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by cookatrice on 2014. 5. 16..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsStoreOvrlSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -1043408163450825168L;
	private String stdDt;// VARCHAR2 (8 BYTE),
	private String stdYm; // STDYM 기준일자 VARCHAR2(6) N
	private String pocIndCd;// VARCHAR2 (2 BYTE),
	private String ocbYn;// VARCHAR2 (1 BYTE),
	private Long totStoreCnt;// NUMBER (10),
	private Long newOcbStoreCnt;// NUMBER (10),
	private Long chkinCnt;// NUMBER (10),
	private Double chkinAvgCnt;// FLOAT (126),
	private Long callCnt;// NUMBER (10),
	private Double callAvgCnt;// FLOAT (126),
	private Long photoRegCnt;// NUMBER (10),
	private Double photoRegAvgCnt;// FLOAT (126),
	private Long rviewRegCnt;// NUMBER (10),
	private Double rviewRegAvgCnt;// FLOAT (126),
	private Long shrClickCnt;// NUMBER (10),
	private Double shrClickAvgCnt;// FLOAT (126),
	private Long bnftOfferCnt;// NUMBER (10),
	private Double bnftOfferAvgCnt;// FLOAT (126),
	private Long ptrnCnt;// NUMBER (10),
	private Double ptrnAvgCnt;// FLOAT (126),
	private Long starCnt;// NUMBER (10)

}
