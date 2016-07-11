package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The ObsVstFstUvSta class.
 * 
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 * 
 * @Desc Table : OBS_D_VST_FST_UV_STA
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsVstFstUvSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -4882870186096311063L;
	private String strdDt; //일자
	private BigDecimal allVstCnt;
	private BigDecimal newVstCnt;
	private BigDecimal preMVstCnt;
	private BigDecimal reVstCnt;
	private String operDt;
}
