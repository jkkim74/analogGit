package com.skplanet.bisportal.model.syrup;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by lko on 2014-11-21.
 */
@Data
public class SmwCldrWk implements Serializable{

    private static final long serialVersionUID = 6452663781038962143L;

    private String strdYear;
    private String strdWkSeq;
    private String wkStDt;
    private String wkEdDt;
    private String dispWk;
    private String operDtm;
    private String searchDate;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
