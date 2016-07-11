package com.skplanet.bisportal.model.bip;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The HandInput(수기입력(Tmap, Syrup 출력 테이블) class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HandInput implements Serializable {
  private static final long serialVersionUID = 2393641321370113933L;
  /** MAPP_STRD_DT */
  private String mappStrdDt;

  /** lnkgCyclCd */
  private String lnkgCyclCd;

  /** Month RSLT_VAL */
  private BigDecimal rsltVal;

  /** AUDIT_ID */
  private String auditId;

  /** DISP_DT */
  private String dispDt;

  /** Day RSLT_VAL1 */
  private BigDecimal rsltVal1;

  /** Day RSLT_VAL2 */
  private BigDecimal rsltVal2;

  /** Day RSLT_VAL3 */
  private BigDecimal rsltVal3;

  /** Day RSLT_VAL4 */
  private BigDecimal rsltVal4;

  /** Day RSLT_VAL5 */
  private BigDecimal rsltVal5;
}
