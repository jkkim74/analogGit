package com.skplanet.ocb.model;

import java.util.LinkedHashMap;

import com.google.common.base.CaseFormat;

/**
 * <p>
 * 빈번한 DB변경 및 다수의 조회 컬럼에 대한 모델 변경/생성을 피하기 위해 뷰에서 활용.
 * </p>
 * <p>
 * - Guava 유틸 활용하여 컬럼명을 자동으로 camelCase 키로 변환.<br>
 * - LinkedHashMap 상속받아서 삽입순서대로 값 반환. (조회한 내용 그대로 csv 생성 시 유용)
 * </p>
 */
public class AutoMap extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1022894531804522485L;

	@Override
	public Object put(String key, Object value) {
		String camelCaseKey = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.toString());
		return super.put(camelCaseKey, value);
	}

}
