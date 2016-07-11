package com.skplanet.bisportal.model.syrup;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lko on 2014-11-28.
 */
@Data
public class SmwCponLocStat implements Serializable{
    private static final long serialVersionUID = 3083156722834149392L;

    private String dispDt;//표시일자
    private String cldrDt;//기준일자
    private String cdNm;
    private String cdNm3;//구분
    private String strdDt;//기준일자
    private String menu;//메뉴
    private String recType;//쿠폰유형
    private String tgtOrder;//노출순서
    private String brandCd;//브랜드코드
    private String cpProdCd;//쿠폰상품코드
    private String brandNm;//브랜드명
    private String cpNm;//쿠폰명
    private BigDecimal pageCnt = BigDecimal.ZERO;//상세페이지 조회건수
    private BigDecimal pageUserCnt = BigDecimal.ZERO;//상세페이지 조회고객수
    private String operDtm;//작업일시

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
