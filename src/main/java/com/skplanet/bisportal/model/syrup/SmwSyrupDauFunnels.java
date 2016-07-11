package com.skplanet.bisportal.model.syrup;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by ophelisis on 2015. 7. 27..
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 * @Desc Table : SMW_D_SYRUP_DAU_FUNNELS
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SmwSyrupDauFunnels extends BasePivot implements Serializable {
	private static final long serialVersionUID = -1030890852583828499L;
	private String strdDt;
	private String category;
	private String pageId;
	private BigDecimal mbrCnt;
	private String operDt;
}
