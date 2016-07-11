package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;
import org.apache.commons.lang.StringUtils;

/**
 * The ObsCntntMbilFlyrCopnSta class(모바일전단 쿠폰다운).
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * @see
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsCntntMbilFlyrCopnSta extends BasePivot implements Serializable {
  private static final long serialVersionUID = 8945522722453310786L;
  private long dnldCntOnPv; /*  */
  private long dnldCntOnLv; /* 다운로드 PV */
  private long dnldCntOnUv; /* 다우론드 UV */
  private String copnNm = StringUtils.EMPTY; /* 크폰 이름 */
  private String copnId = StringUtils.EMPTY; /* 쿠폰아이디 */
  private String sndStartDt = StringUtils.EMPTY; /* 발송시작일 */
  private String storeNm = StringUtils.EMPTY; /* 가맹점명 */
  private String flyrTitle = StringUtils.EMPTY; /* 전단 제목 */
  private String flyrId = StringUtils.EMPTY; /* 전단 ID */
  private String pocIndCd = StringUtils.EMPTY; /* POC 구분 코드 */
  private String stdDt = StringUtils.EMPTY; /*  */
}
