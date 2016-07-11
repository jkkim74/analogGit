package com.skplanet.bisportal.common.utils;

import java.io.Serializable;
import java.util.List;

import com.skplanet.bisportal.common.model.Rule;

/**
 * The JqGridFilter class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Deprecated
public class JqGridFilter implements Serializable {
	private static final long serialVersionUID = 4940243205337054227L;
	private String source;
	private String groupOp;
	private List<Rule> rules;
}
