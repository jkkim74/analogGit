package com.skplanet.bisportal.model.bip;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

/**
 * Created by seoseungho on 2014. 10. 15..
 */
public class DssValidator {
    public void validate(Dss dss, Errors errors) {
        // required value validation
        if (dss.getBmIdList() == null || ArrayUtils.isEmpty(dss.getBmIdList().toArray())) {
            errors.rejectValue("bmIdList", "required", "Bm 타입 데이터를 입력해주세요.");
        } else if (StringUtils.isEmpty(dss.getSubject())) {
            errors.rejectValue("subject", "required", "분석 주제를 입력해주세요.");
        } else if (StringUtils.isEmpty(dss.getContent())) {
            errors.rejectValue("content", "required", "분석 내용을 입력해주세요.");
        } else if (StringUtils.isEmpty(dss.getAnalysisStart())) {
            errors.rejectValue("analysisStart", "required", "분석 시작일을 입력해주세요.");
        } else if (StringUtils.isEmpty(dss.getAnalysisEnd())) {
            errors.rejectValue("analysisEnd", "required", "분석 종료일을 입력해주세요.");
        } else if (StringUtils.isEmpty(dss.getDataSource())) {
            errors.rejectValue("dataSource", "required", "데이터소스를 입력해주세요.");
        } else if (StringUtils.isEmpty(dss.getDataStart())) {
            errors.rejectValue("dataStart", "required", "데이터 수집시작일을 입력해주세요.");
        } else if (StringUtils.isEmpty(dss.getDataEnd())) {
            errors.rejectValue("dataEnd", "required", "데이터 수집종료일을 입력해주세요.");
        }
    }
}
