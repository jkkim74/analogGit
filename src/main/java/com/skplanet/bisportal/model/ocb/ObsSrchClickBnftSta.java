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
 * @Desc 검색결과클릭 혜택
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsSrchClickBnftSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 5160293707322190763L;
	private String stdDt;// VARCHAR2 (8 BYTE),
    private String stdYm; // VARCHAR2 (6 BYTE),
	private int rowNumber;
    private String pocIndCd;// VARCHAR2 (2 BYTE),
	private String bnftTyp;// VARCHAR2 (20 BYTE),
	private String bnftId;// VARCHAR2 (20 BYTE),
	private String bnftName;// VARCHAR2 (100 BYTE),
	private Long clickCnt;// NUMBER (15)

}
