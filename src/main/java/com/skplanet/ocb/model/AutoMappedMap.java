package com.skplanet.ocb.model;

import org.apache.commons.collections.map.ListOrderedMap;

import com.google.common.base.CaseFormat;

/*
 * DB변경에 대한 빈번한 Anemic model 생성을 피하기 위해 underscore(_) 컬럼명을 camelCase로 자동변경하여 뷰에서 활용. 아래 소스 참고.
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
