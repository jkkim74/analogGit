package com.skplanet.bisportal.model.acl;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 *
 * @author kyoungoh lee
 *
 * Created by lko on 2014-11-18.
 *
 * @Desc 사용자서약관리 테이블
 *
 */
@Data
public class UserSignMngmt implements Serializable {

    private static final long serialVersionUID = -452622759580739689L;

    private String signYr;

    private String docKndIndCd;

    private String signStartDt;

    private String signEndDt;

    private String auditId;

    private String auditDtm;

    private String currentDate;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
