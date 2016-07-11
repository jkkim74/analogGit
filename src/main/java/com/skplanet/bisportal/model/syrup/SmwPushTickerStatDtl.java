package com.skplanet.bisportal.model.syrup;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lko on 2014-12-05.
 */
@Data
public class SmwPushTickerStatDtl implements Serializable{

    private static final long serialVersionUID = -8894436477896418811L;

    private String strdDt;//기준일자
    private String statType;//유형
    private String pushId;//푸시ID
    private String pushMsg;//푸쉬메시지
    private String ptCd;//제휴사코드
    private String ptNm;//제휴사명
    private BigDecimal tgtCnt = BigDecimal.ZERO;//대상건수
    private BigDecimal sendCnt = BigDecimal.ZERO;//발송완료건수
    private BigDecimal sendSucCnt = BigDecimal.ZERO;//발송성공률
    private BigDecimal uv = BigDecimal.ZERO;//UV
    private String operDtm;//작업일시

    private String cdNm;//코드명
    private String cldrDt;//기준일자
    private String dispDt;//표시일자

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
