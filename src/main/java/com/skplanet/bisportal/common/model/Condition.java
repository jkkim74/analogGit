package com.skplanet.bisportal.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sjune on 2014-07-03.
 *
 * @author sjune
 */
@Data
public class Condition implements Serializable {
    private static final long serialVersionUID = 6496966476798056720L;
    private String searchDate;
    private String startDate;
    private String endDate;
    private String startWeekNumber;
    private String endWeekNumber;
    private String oneYearAgoWeekNumber;
    private List<WhereCondition> whereConditions;
    private String dateType;  //day or week or month
    private String chartType;  //sparkline or ewma
    private boolean oneYearBefore;
}
