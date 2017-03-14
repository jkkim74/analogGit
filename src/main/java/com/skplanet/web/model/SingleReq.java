package com.skplanet.web.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1003899 on 2017. 3. 12..
 */

@Data
public class SingleReq implements Serializable {

    private static final long serialVersionUID = 10374769796287439L;

    private int sn;
    private String username;
    private String memberId;
    private String extractTarget;   //tr, tr_mbrKorNm, mbrId
    private String extractCond;
    private String periodType;
    private String periodFrom;
    private String periodTo;
    private ProgressStatus status;  //PROCESSING, FINISHED, FAILED
    private Date reqStartTime;
    private Date reqFinishTime;

    private String ptsMasking;
    private String ptsPrefix;
    private String menuId;
    private String memberKorNM;

}
