package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by cookatrice on 2014. 10. 20..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ObsSosBleSta implements Serializable {
    private static final long serialVersionUID = -3916426865968672215L;

    //common
    private String stdDt; // 기준일자 varchar2(8 byte)
	private String pocIndCd; // poc 구분 코드 varchar2(2 byte)
	private String comCd; // 구분 varchar2(50 byte)
    private String comCdNm; //코드명
    private int sortSeq; //정렬순서

    //ObsSosBleSyrupSta - 컬럼명 앞에 Syrup
	private Long syrupBleCnt; // ble 모수 number(10,0)
	private Long syrupBtOnCnt; // bt on number(10,0)
	private Long syrupBleFlyrCnt; // ble 전단 number(10,0)
	private Long syrupSmtBcnCnt; // smart beacon number(10,0)

    //ObsSosBleOcbSta - 컬럼명 앞에 Ocb
	private Long ocbBleFlyrCnt; // ble전달 number(10,0)
	private Long ocbBleChkinCnt; // ble체크인 number(10,0)
	private Long ocbBtOnCnt; // bt on number(10,0)
	private Long ocbSmtBcnCnt; // smart beacon number(10,0)
	private Long ocbStorFlyrCnt; // 매장전단 number(10,0)
	private Long ocbTraraFlyrCnt; // 상권전단 number(10,0)

    //ObsSosBleSJoinOSta
	private Long bleFlyrChkinCnt; // (s)ble ∩ (o)ble 전단+체크인 number(10,0)
	private Long bleFlyrCnt; // (s)ble ∩ (o)ble 전단 number(10,0)
	private Long bleChkinCnt; // (s)ble ∩ (o)ble 체크인 number(10,0)
	private Long bleBtonCnt; // (s)bt on ∩ (o) bt on number(10,0)
}
