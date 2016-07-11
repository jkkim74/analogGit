package com.skplanet.bisportal.model.syrup;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author kyoungoh lee
 *
 * Created by lko on 2014-12-05.
 *
 * @Desc
 *
 */
@Data
public class SmwRcmdStatDtl implements Serializable {
    private static final long serialVersionUID = -7964953419777106357L;

    private String strdDt;//기준일자
    private String strdYm;//기준년월
    private String strdYr;//기준년도
    private String strdWk;//기준주차
    private String gndrCd;//성별코드
    private String ageRngCd;//연령범위코드
    private String osCd;//운영체제코드
    private String telcCd;//통신사코드
    private String recType;//쿠폰유형
    private String recId;//추천ID
    private String recNm;//추천명
    private BigDecimal tgtCnt = BigDecimal.ZERO;//노출건수
    private BigDecimal accumTgtCnt = BigDecimal.ZERO;//누적노출건수
    private BigDecimal issueCnt = BigDecimal.ZERO;//발급건수
    private BigDecimal accumIssueCnt = BigDecimal.ZERO;//누적발급건수
    private BigDecimal issuePageCnt = BigDecimal.ZERO;//발급조회건수
    private BigDecimal accumIssuePageCnt = BigDecimal.ZERO;//누적발급조회건수
    private BigDecimal operDtm = BigDecimal.ZERO;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
