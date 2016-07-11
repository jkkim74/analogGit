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
 * @Desc 알림설정
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsPushCnfgSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 8116000704461347486L;
	private String stdDt;// VARCHAR2 (8 BYTE),
    private String stdYm;// VARCHAR2 (6 BYTE),
    private String pocIndCd;// VARCHAR2 (2 BYTE),
    private Long bnftOnUserCnt;// NUMBER (10),
    private Long bnftOffUserCnt;// NUMBER (10),
    private Long trOnUserCnt;// NUMBER (10),
    private Long trOffUserCnt;// NUMBER (10),
    private Long arndStoreOnUserCnt;// NUMBER (10),
    private Long arndStoreOffUserCnt;// NUMBER (10)
    private Long prsntOnUserCnt;// NUMBER (10)
    private Long prsntOffUserCnt;// NUMBER (10)
    private Long infoOnUserCnt;// NUMBER (10)
    private Long infoOffUserCnt;// NUMBER (10)
    private Long p2pOnUserCnt;// NUMBER (10)
    private Long p2pOffUserCnt;// NUMBER (10)
    private Long ocbBnftOnUserCnt;// NUMBER (10)
    private Long ocbBnftOffUserCnt;// NUMBER (10)

    private Long bnftSum;       //혜택푸쉬수신 on-off 합계
    private Long trSum;         //TR푸쉬수신 on-off 합계
    private Long arndStoreSum;  //주면매장푸쉬수신 on-off 합계
    private Long prsntSum;      //선물푸쉬수신 on-off 합계
    private Long infoSum;       //정보성푸쉬수신 on-off 합계
    private Long p2pSum;        //p2p푸쉬수신 on-off 합계
    private Long ocbBnftSum;    //ocb혜택푸쉬수신 on-off 합계

}
