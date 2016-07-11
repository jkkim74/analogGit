package com.skplanet.bisportal.model.bip;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lko on 2014-09-30.
 */

/**
 *
 * @author kyoungoh lee
 *
 * @Desc 화면 접근 로그 테이블
 */
@Data
public class DayVisitor implements Serializable{
    private static final long serialVersionUID = -3696575352742912757L;
    private String visitDate;
    private String serviceCode;
    private String categoryCode;
    private String categoryNm;
    private String menuCode;
    private String menuNm;
    private BigDecimal visitCount;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
