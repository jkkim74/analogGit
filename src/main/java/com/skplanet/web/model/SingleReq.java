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

    private long sn;
    private String userName;
    private String memberId;
    private String extractTarget;
    private String extractCond;
    private String periodType;
    private String periodFrom;
    private String periodTo;
    private ProgressStatus status;  //PROCESSING, FINISHED, FAILED
    private Date reqStartTime;
    private Date reqFinishTime;
}
