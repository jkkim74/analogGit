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
public class ObsCntntDscvSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -394736986632577041L;
	private String stdDt;// 기준일자 varchar2(8 byte)
	private String stdYm; // 기준일자 varchar2(6 byte)
	private Long pocIndCd;// poc 구분 코드 varchar2(2 byte)
	private Long expsrCntOnUv;// uv 노출 디스커버 number(10,0)
	private Long clickCntOnUv;// uv 클릭 디스커버 number(10,0)
	private Long clickCopnCntOnUv;// uv 클릭 내쿠폰 number(10,0)
	private Long clickPtrnCntOnUv;// uv 클릭 단골매장 number(10,0)
	private Long clickStarCntOnUv;// uv 클릭 스타 number(10,0)
	private Long clickMbilFlyrCntOnUv;// uv 클릭 모바일 전단지 number(10,0)
	private Long expsrCntOnLv;// lv 노출 디스커버 number(10,0)
	private Long clickCntOnLv;// lv 클릭 디스커버 number(10,0)
	private Long clickCopnCntOnLv;// lv 클릭 내쿠폰 number(10,0)
	private Long clickPtrnCntOnLv;// lv 클릭 단골매장 number(10,0)
	private Long clickStarCntOnLv;// lv 클릭 스타 number(10,0)
	private Long clickMbilFlyrCntOnLv;// lv 모바일전단지 number(10,0)
	private Long expsrCnt;// 횟수 노출 디스커버 number(10,0)
	private Long clickCnt;// 횟수 클릭 디스커버 number(10,0)
	private Long clickCopnCnt;// 횟수 내쿠폰 number(10,0)
	private Long clickPtrnCnt;// 횟수 단골매장 number(10,0)
	private Long clickStarCnt;// 횟수 스타 number(10,0)
	private Long clickMbilFlyrCnt;// 횟수 클릭 모바일 전단지 number(10,0)
}
