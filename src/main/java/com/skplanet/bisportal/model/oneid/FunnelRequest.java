package com.skplanet.bisportal.model.oneid;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skplanet.bisportal.common.model.ReportDateType;

/**
 * Funnel 차트용 요청 파라미터 매핑 클래스.
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FunnelRequest implements Serializable {
	private static final long serialVersionUID = 8327424430378263038L;
	private ReportDateType dateType;
	private String startDate; // 검색 시작일자
	private String endDate; // 검색 종료일자
	private String dispOrd;	// 디스플레이 순서
	private String inoutFlg; // 인입/이탈 구분
	private String poc; /* PC/Mobile 분 */
	private String procCd; /* 프로새스 코드 */
	private String reqSstCd; /* 서비스 사이트 코드 */
	private String stepCd; /* 스텝 코드 */
	private String svcId; /* 서비스 ID */
}
