package com.skplanet.bisportal.model.acl;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 *
 * @author kyoungoh lee
 *
 * @Desc 사용자보안서약 테이블
 */
@Data
public class UserSign implements Serializable {
    private static final long serialVersionUID = -3186229439833715106L;
    private String loginId;
    private String signYn;
    private String signDt;
    private String userId;
    private String userNm;
    private String orgNm;
    private String corgNm;
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
