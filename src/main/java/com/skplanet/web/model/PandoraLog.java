package com.skplanet.web.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by cookatrice on 2017. 5. 24..
 */
@Data
public class PandoraLog implements Serializable {
    private static final long serialVersionUID = -1437234290739148885L;

    private int sn;
    private String userId;
    private String menuId;
    private String actionId;
    private String searchCond;
    private String ptsMask;
    private String ptsPrefix;
    private Date actionTime;

    private String menuName;
    private String actionName;
}
