package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ophelisis on 2015. 8. 12..
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 * @Desc Table : OBS_D_MKT_PUSH_SEND
 */
@Data
public class ObsMktPushSend implements Serializable {
    private static final long serialVersionUID = -8102129513837768263L;
    private String stdDt;
    private String sndTime;
    private String title;
    private String msg;
    private String pushType;
    private String displayType;
    private String eventId;
    private String bigPicture;
    private String custCnt;
    private String pushRcvCustCnt;
    private String pushClkCustCnt;
    private String notiClkCustCnt;
    private String dirtClkCnt;
    private String clkSum;
    private String rcvPer;
    private String ratPer;
}
