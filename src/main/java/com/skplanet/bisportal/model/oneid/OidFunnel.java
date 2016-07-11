package com.skplanet.bisportal.model.oneid;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Funnel 단계별 화면 디스플레이용 데이터 관리.
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Data
public class OidFunnel implements Serializable {
	private static final long serialVersionUID = -950304315464413699L;
	private List<OidUsrProcRank> entrances; // 인입 정보
	private OidUsrProc oidUsrProc; // step 정보
	private List<OidUsrProcRank> exits; // 이탈 정보
	private String stepNm;
	private String pageId;
	private String conversionRate; // 전환율
	private boolean multiLayer = false; // 멀티 레이어 여부
}
