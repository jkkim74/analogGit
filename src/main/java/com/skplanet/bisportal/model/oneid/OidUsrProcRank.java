package com.skplanet.bisportal.model.oneid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 인입/이탈 상위 목록 관리.
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper=false)
public class OidUsrProcRank implements Serializable {
	private static final long serialVersionUID = -6998524290616643980L;
	private String orgDt;     /* 등록 */
	private BigDecimal cnt = BigDecimal.ZERO;     /* 입입/이탈 페이지 ID 별 개수 */
	private int rank;
	private String inoutFlg;     /* 인입/이탈 구분 */
	private String poc;     /* POC 분류 */
	private String pageId;     /* 페이지ID */
	private String dispOrd;     /* 순서 */
	private String stepCd;     /* 스텝코드 */
	private String procCd;     /* 프로세스코드 */
	private String reqSstCd;     /* 서비스사이트코드 */
	private String svcId;     /* 서비스ID */
	private String strdDt;     /* 기준일 */
}
