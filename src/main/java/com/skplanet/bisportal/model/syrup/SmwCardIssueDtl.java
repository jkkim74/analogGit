package com.skplanet.bisportal.model.syrup;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author kyoungoh lee
 *
 * Created by lko on 2014-11-27.
 *
 * @Desc 일/주/월별 멤버십별 발급 상세 테이블
 *
 */
@Data
public class SmwCardIssueDtl implements Serializable{
    private static final long serialVersionUID = -6726730583814019932L;
    private String strdDt;                                 //기준일자
    private String strdYm;                                 //기준년월
    private String strdYr;                                 //기준년도
    private String strdWk;                                 //기준주차
    private String memCardClCd;                            //멤버십카드구분코드
    private String gndrCd;                                 //성별코드
    private String ageRngCd;                               //연령범위코드
    private String osCd;                                   //운영체제코드
    private String telcCd;                                 //통신사코드
    private String cardId;                                 //카드ID
    private String cardName;                               //카드명
    private String openDt;                                 //오픈일
    private BigDecimal totIssueCnt = BigDecimal.ZERO;      //총발급건수
    private BigDecimal dayIssueCnt = BigDecimal.ZERO;      //당일발급건수
    private BigDecimal cardExecCnt = BigDecimal.ZERO;      //카드실행건수
    private BigDecimal cardExecUserCnt = BigDecimal.ZERO;  //카드실행자수
    private BigDecimal pntQryExecCnt = BigDecimal.ZERO;    //포인트조회실행건수
    private BigDecimal pntQryExecUserCnt = BigDecimal.ZERO;//포인트조회실행자수
    private BigDecimal pageQryCnt = BigDecimal.ZERO;       //발급페이지조회건수
    private BigDecimal pageQryUserCnt = BigDecimal.ZERO;   //발급페이지조회자수
    private String operDtm;                                //작업일시

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
