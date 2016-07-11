package com.skplanet.bisportal.model.ocb;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by pepsi on 15. 2. 25..
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SankeyAccessLog implements Serializable {
	private static final long serialVersionUID = 2736992926587341016L;
	private int sid;
	private String path;
	private String referer;
	private String time;
	private int seq;
}
