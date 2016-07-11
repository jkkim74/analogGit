package com.skplanet.bisportal.model.syrup;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author kyoungoh lee
 *
 * Created by lko on 2014-11-19.
 *
 * @Desc 일/주/월별 쿠폰 발급 테이블
 */
@Data
public class SmwCponStatDtl implements Serializable{
    private static final long serialVersionUID = 5023062165159980906L;
	private int totalRecordCount;
    private String strdDt;//기준일자
    private String strdYm;//기준년월
    private String strdYr;//기준년도
    private String strdWk;//기준주차
    private String cpProdCd;//쿠폰상품코드(쿠폰ID)
    private String cponNm;//쿠폰명
    private String ptNm;//유형
    private String regPocNm; //브랜드명(제휴사명)
    private BigDecimal issueCnt = BigDecimal.ZERO; //발급건수
    private BigDecimal accumIssueCnt = BigDecimal.ZERO; //누적발급건수
    private BigDecimal useCnt = BigDecimal.ZERO; //사용건수
    private BigDecimal accumUseCnt = BigDecimal.ZERO; //누적사용건수
    private BigDecimal showCnt = BigDecimal.ZERO; //노출건수
    private BigDecimal accumShowCnt = BigDecimal.ZERO; //누적노출건수
    private BigDecimal issuePageCnt = BigDecimal.ZERO; //발급페이지조회수
    private BigDecimal accumIssuePageCnt = BigDecimal.ZERO; //누적발급페이지조회수
    private BigDecimal issuePageDtlCnt = BigDecimal.ZERO; //상세발급페이지조회수
    private BigDecimal accumIssuePageDtlCnt = BigDecimal.ZERO; //누적상세발급페이지조회수
    private String operDtm;//작업일시

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
