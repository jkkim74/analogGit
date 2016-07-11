package com.skplanet.bisportal.common.model;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by pepsi on 2014. 5. 12..
 */
@Data
public class ChartRequest implements Serializable {
	private static final long serialVersionUID = 6048839195835349057L;
	private ReportDateType dateType;
	private String startDate;
	private String endDate;
	private String pocCode;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
