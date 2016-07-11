package com.skplanet.bisportal.model.bip;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skplanet.bisportal.common.model.ReportDateType;

/**
 * Created by cookatrice on 2015. 7. 6..
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DashboardData implements Serializable {
	private static final long serialVersionUID = -5470709661092060208L;
	// 경영실적 API용(dateType, svcId, strdDt)
	private ReportDateType dateType;
	private BigDecimal svcId;
	private String strdDt;

	private String id; // make id for chart with codes(svcId, idxClGrpCd,idxClCd,idxCttCd)
	private String chartType; // sparkline or ewma
	private String bm; // svcNm
	private String keyIndex;// idxClCdGrpNm
	private String currentData;
	private List<BpmDlyPrst> dataSet; // list of matched
	private List<BpmDlyPrst> past7DayDataSet; // 직전7일
	private List<BpmDlyPrst> oldDataSet; // list of matched
	private List<DaaEwmaStatDaily> ewmaSet; // ewma data
	private long seq;// seq
}
