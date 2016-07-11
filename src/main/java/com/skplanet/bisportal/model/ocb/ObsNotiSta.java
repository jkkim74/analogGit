package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by cookatrice on 2014. 5. 19..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc Table
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsNotiSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -5940786528403789728L;
	private String stdDt;// 기준일자 varchar2 8
	private String stdym; // stdYm 기준일자 varchar2(6) n
	private String pocIndCd;// poc 구분 코드 varchar2 2
	private Long pushOnUserCnt;// push 알림 on 유저수 number 10
	private Long pushOffUserCnt;// push 알림 off 유저수 number 10
	private Long trnsOnUserCnt;// 거래정보 on 유저수 number 10
	private Long trnsOffUserCnt;// 거래정보 off 유저수 number 10
	private Long bnftOnUserCnt;// 혜택정보 on 유저수 number 10
	private Long bnftOffUserCnt;// 혜택정보 off 유저수 number 10
	private Long presntOnUserCnt;// 선물알림 on 유저수 number 10
	private Long presntOffUserCnt;// 선물알림 off 유저수 number 10
	private Long infoOnUserCnt;// 정보성알림 on 유저수 number 10
	private Long infoOffUserCnt;// 정보성알림 off 유저수 number 10
	private Long withBuddOnUserCnt;// 친구와같이쓰기 on 유저수 number 10
	private Long withBuddOffUserCnt;// 친구와같이쓰기 off 유저수 number 10
}
