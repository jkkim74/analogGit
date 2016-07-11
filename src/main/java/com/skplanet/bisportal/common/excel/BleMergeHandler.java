package com.skplanet.bisportal.common.excel;

import org.apache.poi.ss.usermodel.Sheet;

import com.skplanet.bisportal.common.model.MultiHeader;

/**
 * Created by pepsi on 15. 1. 8..
 */
public interface BleMergeHandler {
	void handleBleMerge(Sheet worksheet, MultiHeader headers, String statContents) throws Exception;
}
