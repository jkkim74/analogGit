package com.skplanet.bisportal.model.sankey;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by cookatrice on 2015. 8. 11..
 */
@Data
public class SankeyResponse implements Serializable {
	private static final long serialVersionUID = 222561080475498554L;

	private String site;
	private String service;
	private String depth;
	private String source;
	private String target;
	private String value;

}
