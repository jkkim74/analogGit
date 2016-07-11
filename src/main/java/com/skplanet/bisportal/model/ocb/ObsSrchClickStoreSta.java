package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by cookatrice on 2014. 5. 19`..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc 검색결과클릭 매장
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsSrchClickStoreSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -1556810167979131194L;
	private String stdDt;// VARCHAR2 (8 BYTE),
	private String stdYm;// VARCHAR2 (6 BYTE),
	private String pocIndCd;// VARCHAR2 (2 BYTE),
	private String storeId;// VARCHAR2 (50 BYTE),
	private Long clickCnt;// NUMBER (15)


    private String storeNm; //매장명
    private String storeIdNm; //ID+매장명
    private String rank;    //순위
    private String clickCntTwo;   //clickCnt String parameter test
}
