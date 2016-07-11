package com.skplanet.bisportal.model.oneid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 프로세스 단계별 인입/이탈 정보 관리.
 *  @author HoJin-Ha (mimul@wiseeco.com)
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OidUsrProc implements Serializable {
	private static final long serialVersionUID = 8255646203340441015L;
	private String orgDt;     /* 등록 */
	private BigDecimal outCnt = BigDecimal.ZERO;     /* 이탈 */
	private BigDecimal inCnt = BigDecimal.ZERO;     /* 인입 */
	private BigDecimal exInCnt = BigDecimal.ZERO;     /* 외부 인입 */
	private BigDecimal stepInCnt = BigDecimal.ZERO; /* 단계 전체 인입수 (exInCnt + inCnt) */
	private BigDecimal nextStepCnt = BigDecimal.ZERO; /* 다은 단계 인입수(stepInCnt - outCnt) */
	private String nextStepRate; /* 다음 단계 인입율 */
	private String dispOrd;     /* 순서 */
	private String poc;     /* PC/Mobile 분 */
	private String pageId;     /* 페이지 ID */
	private String stepCd;     /* 스텝 코드 */
	private String stepNm;    /* 스텝 이름 */
	private String procCd;     /* 프로새스 코드 */
	private String reqSstCd;     /* 서비스 사이트 코드 */
	private String svcId;     /* 서비스 ID */
	private String strdDt;     /* 기준일자 */
}
