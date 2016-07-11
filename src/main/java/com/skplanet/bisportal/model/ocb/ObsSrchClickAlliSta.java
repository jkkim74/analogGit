package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by cookatrice on 2014. 5. 21..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc 검색결과클릭 제휴사
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsSrchClickAlliSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 5259344013330444811L;
	private String stdYm; // VARCHAR2 (6 BYTE),
	private String stdDt;// VARCHAR2 (8 BYTE),
	private String pocIndCd;// VARCHAR2 (2 BYTE),
	private String alliId;// VARCHAR2 (50 BYTE),
	private Long clickCnt;// NUMBER (15)

    private String alliNm; //제휴사명
    private String alliIdNm; //ID+제휴사명
    private String rank;    //순위
}
