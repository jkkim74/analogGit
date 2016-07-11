package com.skplanet.bisportal.common.excel;

import com.skplanet.bisportal.common.model.MultiHeader;

/**
 * Created by pepsi on 15. 1. 8..
 */
public interface BleHeaderHandler {
	MultiHeader buildHeader(String statContents) throws Exception;
}
