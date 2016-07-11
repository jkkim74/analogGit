package com.skplanet.bisportal.model.sankey;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Sankey Chart request Created by cookatrice on 2015. 8. 10..
 */

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SankeyRequest implements Serializable {
	private static final long serialVersionUID = 1266294572608918820L;

	private String startDate;
	private String endDate;
	private String site;
	private String service;
	private String type;	//step0 type.(ex,방문페이지, 국가)

	private long maxDepth;
	private long minValue;
}
