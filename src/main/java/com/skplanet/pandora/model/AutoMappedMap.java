package com.skplanet.pandora.model;

import org.apache.commons.collections.map.ListOrderedMap;

import com.google.common.base.CaseFormat;

/*
 * 참고.
 * https://github.com/eGovFrame/egovframework.rte.root/blob/master/Persistence/egovframework.rte.psl.dataaccess/src/main/java/egovframework/rte/psl/dataaccess/util/EgovMap.java
 */
public class AutoMappedMap extends ListOrderedMap {

	private static final long serialVersionUID = 1022894531804522485L;

	@Override
	public Object put(Object key, Object value) {
		Object camelCaseKey = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.toString());
		return super.put(camelCaseKey, value);
	}

}
