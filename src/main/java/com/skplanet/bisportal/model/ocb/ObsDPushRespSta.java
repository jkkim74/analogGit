package com.skplanet.bisportal.model.ocb;

/**
 * Created by lko on 2014-10-15.
 */

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 *
 * @author kyoungoh lee
 *
 * @Desc OCB 마케팅 push 발송 결과
 */
@Data
public class ObsDPushRespSta implements Serializable {
    private static final long serialVersionUID = -661985496494820240L;
    private String stdDt;
    private String pocIndCd;
    private String pushTyp;
    private String sndMsg;
    private String appVer;
    private int sndCustCnt;
    private int rcvCustCnt;
    private int lchCustCnt;
    private float rcvRate;
    private float lchRate;
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
