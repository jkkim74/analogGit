package com.skplanet.bisportal.common.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skplanet.bisportal.model.bip.HandInput;
import com.skplanet.bisportal.model.bip.OrgUser;

/**
 * The JqGridRequest class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JqGridRequest implements Serializable {
	private static final long serialVersionUID = -7297149042579742251L;
	private int page;
	private int rows;
	private String sidx;
	private String sord;
	private String searchField;
	private String searchString;
	private String searchOper;
	private ReportDateType dateType;
	private String startDate;
	private String endDate;
	private String previousEndDate;
	private String startDatePreMonth;
	private String endDatePreMonth;
	private String previousEndDatePreMonth;
	private String lastMonth;
	private String monthBeforeLast;
	private String pocCode;
	private String htmlData;
	private String xlsName;
	private String titleName;
	private String svcId;
	private String basicDate;
	private String whereConditions;
	private String viewName;
	private String username;
	private String dimensions;
	private String measureCode;
	private int itemCode = 1; // searchCode
	private int serviceTypeCode = 1; // searchServiceType
	private String statContents; // searchStatContent

	private int firstIndex;
	private int lastIndex;
	private int recordCountPerPage;
	private List<HandInput> handInputs;
	private List<String> loginIds;
	private OrgUser orgUser;

	private String strdStYear;
	private String strdEdYear;
	private String strdPrevEdYear;

	private String strdWkFrStSeq;
	private String strdWkFrEdSeq;
	private String strdWkToStSeq;
	private String strdWkToEdSeq;

	// tcloud
	private String commonGroupCode;

    //ocb feed
    private String feedType;

	private String m1Day;
	private String m2Day;
	private String m3Day;
	private String d30StartDay;

	// tmap kpi
	private String kpiCode;
}
