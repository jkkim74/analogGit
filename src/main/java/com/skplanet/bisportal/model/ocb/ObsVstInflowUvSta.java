package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The ObsVstInflowUvSta class.
 * 
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 * 
 * @Desc Table : OBS_D_VST_INFLOW_UV_STA
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsVstInflowUvSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 3051851910304157806L;
	private String strdDt; //일자
	private BigDecimal dau;
	private BigDecimal dau5Ver;
	private BigDecimal appclickTyp;
	private BigDecimal interactTyp;
	private BigDecimal trTyp;
	private BigDecimal bleTyp;
	private BigDecimal mleafletTyp;
	private BigDecimal zoneTyp;
	private BigDecimal mktgpushTyp;
	private BigDecimal lockTyp;
	private BigDecimal etcTyp;
	private String operDt;
}
