package com.skplanet.bisportal.common.excel;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.bip.SummaryHeader;

/**
 * The HeaderHandler class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public interface HeaderHandler {
	@SuppressWarnings({ "rawtypes" })
	String[][] handleHeader(JqGridRequest jqGridRequest, SummaryHeader summaryHeader) throws Exception;
}
