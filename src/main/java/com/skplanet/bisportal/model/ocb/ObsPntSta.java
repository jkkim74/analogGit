package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * 포인트 이용통계 model.
 * 
 * @author sjune
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsPntSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -41442389997844764L;
	private String stdDt;
	private String stdYm;
	private String pocIndCd;
	private Long grpPntUserCnt; // 같이쓰기 유저
	private Long grpPntTotSum; // 같이쓰기 총 합산 포인트
	private Long grpPntSumPGrpAvg; // 같이쓰기 그룹 당 합산 포인트 평균
	private Long grpPntMbrAvg; // 같이쓰기 그룹원 수 평균

}
