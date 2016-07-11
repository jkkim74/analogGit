package com.skplanet.bisportal.common.validator;

import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.model.bip.DashboardData;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isMonth;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * The DashboardValidator class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Component("dashboardValidator")
public class DashboardValidator implements Validator {
	public boolean supports(Class<?> clazz) {
		return clazz.equals(DashboardData.class);
	}

	public void validate(Object target, Errors errors) {
		DashboardData dashboardData = (DashboardData) target;
		ValidationUtils.rejectIfEmpty(errors, "strdDt", "strdDt is empty");
		ValidationUtils.rejectIfEmpty(errors, "dateType", "dateType is empty");
		ValidationUtils.rejectIfEmpty(errors, "svcId", "svcId is empty");
		validateDate(dashboardData, errors);
	}

	private void validateDate(DashboardData dashboardData, Errors errors) {
		if (CollectionUtils.isEmpty(dashboardData.getDataSet())) {
			errors.rejectValue("dataSet", "invalid.parameter", "invalid parameter");
		}
		if (isMonth(dashboardData.getDateType())) {
			if (!DateUtil.isValidDate(dashboardData.getStrdDt(), Constants.DATE_YM_FORMAT)) {
				errors.rejectValue("strdDt", "invalid.parameter", "invalid parameter");
			}
		} else if (isDay(dashboardData.getDateType()) || isWeek(dashboardData.getDateType())) {
			if (!DateUtil.isValidDate(dashboardData.getStrdDt(), Constants.DATE_YMD_FORMAT)) {
				errors.rejectValue("strdDt", "invalid.parameter", "invalid parameter");
			}
		} else {
			errors.rejectValue("dateType", "invalid.parameter", "invalid parameter");
		}
	}
}
