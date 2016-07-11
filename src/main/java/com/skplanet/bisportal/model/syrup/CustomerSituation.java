package com.skplanet.bisportal.model.syrup;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by seoseungho on 2014. 11. 21..
 */
@Data
public class CustomerSituation implements Serializable {
    private static final long serialVersionUID = 4795478641169528416L;
    private String standardDate;

    private String os;
    private String sex;
    private String appVersion;
    private String ageRange;
    private String telecom;

    private BigDecimal appDownCount;
    private BigDecimal appDownTotalCount;
    private BigDecimal walletJoinCount;
    private BigDecimal walletJoinTotalCount;
    private BigDecimal cardIssueCount;
    private BigDecimal cardIssueTotalCount;
    private BigDecimal ciExistCount;
    private BigDecimal ciExistTotalCount;

    @Override
    public String toString() {
        return standardDate + " " + appDownCount + " " + appDownTotalCount + " " + walletJoinCount + " " + walletJoinTotalCount + " "
                + cardIssueCount + " " + cardIssueTotalCount + " " + ciExistCount + " " + ciExistTotalCount;
    }
}
