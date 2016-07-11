package com.skplanet.bisportal.model.tmap;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by cookatrice on 15. 6. 12..
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class TmapMonKpiStc extends BasePivot implements Serializable {
	private static final long serialVersionUID = 5125251192608676478L;
	private String strdYm;
	private String kpi;
	private BigDecimal uvActiveUv = BigDecimal.ZERO;
	private BigDecimal uvTmap = BigDecimal.ZERO;
	private BigDecimal uvTmapPublic = BigDecimal.ZERO;
	private BigDecimal avgActiveUv = BigDecimal.ZERO;
	private BigDecimal avgTmap = BigDecimal.ZERO;
	private BigDecimal avgTmapPublic = BigDecimal.ZERO;
}
