package com.skplanet.bisportal.model.syrup;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by lko on 2014-11-20.
 */
@Data
public class SmwMbrJoin implements Serializable{
    private static final long serialVersionUID = 7621184689198732742L;

    private String dispDT;//표시일자

    private String grpNM;//구분

    private String nm;//상세

    private String data;//합계데이터

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
