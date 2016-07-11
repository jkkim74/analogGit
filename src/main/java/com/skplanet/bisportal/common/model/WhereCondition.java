package com.skplanet.bisportal.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by cookatrice on 2014. 6. 29..
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WhereCondition implements Serializable {
	private static final long serialVersionUID = -3775099340718305076L;
	private String startDate;
	private String endDate;
    private String searchDate;
    private Integer searchDiff;

	private Long svcId;
	private String idxClGrpCd;
	private String idxClCd;
	private String idxCttCd;

    private String kpiId;   //getOcbEwmaChartData 임시 condition

    private String startWeekNumber;
    private String endWeekNumber;

    /** 연동주기코드 */
    private String lnkgCyclCd;

    /** 엑셀 파일 이름 */
    private String xlsName;

    /** 검색 조건 날짜 타입 */
    private String dateType;

    /** 엑셀 출력 제목명 */
    private String titleName;

    private String pivotFlag;


    /** change the dashboard logic. 2015.07.06 - cookatrice */
    private String category;
    private String svcNm;
    private String idxClCdGrpNm;
    private long seq;
    private String chartType;   //chartType 2015.07.28
}
