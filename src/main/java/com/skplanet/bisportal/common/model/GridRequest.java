package com.skplanet.bisportal.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * The GridRequest class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GridRequest implements Serializable {
	private static final long serialVersionUID = 1407763125472740236L;
	private int page;
	private int rows;
	private int firstIndex;
	private int lastIndex;

	private String sidx;
	private String sord;
	private String searchField;
	private String searchString;
	private String searchOper;
	private ReportDateType dateType;
	private String startDate;
	private String endDate;
}
