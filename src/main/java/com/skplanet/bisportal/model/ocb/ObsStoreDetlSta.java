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
public class ObsStoreDetlSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 7383197523248656783L;
	private String stdDt;// VARCHAR2 (8 BYTE),
	private String stdYm; // STDYM 기준일자 VARCHAR2(6) N
	private String pocIndCd;// VARCHAR2 (2 BYTE),
	private String storeId;// VARCHAR2 (50 BYTE),
	private Long chkinCnt;// NUMBER (10),
	private Long chkinUserCnt;// NUMBER (10),
	private Long callCnt;// NUMBER (10),
	private Long callUserCnt;// NUMBER (10),
	private Long photoRegCnt;// NUMBER (10),
	private Long photoRegUserCnt;// NUMBER (10),
	private Long rviewRegCnt;// NUMBER (10),
	private Long rviewRegUserCnt;// NUMBER (10),
	private Long shrClickCnt;// NUMBER (10),
	private Long shrClickUserCnt;// NUMBER (10),
	private Long bnftOfferCnt;// NUMBER (10),
	private Long ptrnCnt;// NUMBER (10),
	private String starYn;// VARCHAR2 (1 BYTE)

	private String storeNm; // store name
	private int totalRecordCount;

}
