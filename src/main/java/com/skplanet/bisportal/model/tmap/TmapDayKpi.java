package com.skplanet.bisportal.model.tmap;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by ophelisis on 2015. 6. 11..
 * 
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 * 
 * @Desc Table : TMAP_CLDR, TMAP_CODE, TMAP_DAY_ACCUM_KPI_ESTI, TMAP_DAY_ACCUM_KPI_STC, TMAP_DAY_ACCUM_KPI_TRGT, TMAP_MON_KPI_STC
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TmapDayKpi extends BasePivot implements Serializable {
	private static final long serialVersionUID = 8933569353590024479L;
	private String strdDt; //기준일자
	private String cdNm; //요일
	private String hdayNm; //명절
	private BigDecimal mntTrgtUv; //월목표
	private BigDecimal rsltUv; //실적
	private BigDecimal uvAchivRt; //달성율(%)
	private BigDecimal dayIncre; //일증가량
	private BigDecimal trgtUv; //목표치
	private BigDecimal difTrgtRslt; //목표치대비실적
	private BigDecimal difTrgtRsltRt; //목표치대비실적비율
	private BigDecimal estiUv; //예측치
	private BigDecimal uv; //전월동일수치
	private BigDecimal difUv; //전월동일수치대비실적
	private BigDecimal difUvRt; //전월동일대비
	private BigDecimal dayUv; //일UV
}
