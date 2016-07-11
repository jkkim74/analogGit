package com.skplanet.bisportal.common.validator;

import com.skplanet.bisportal.common.consts.Constants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.DateUtil;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isSum;

/**
 * The BleValidator class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Component("bleValidator")
public class BleValidator implements Validator {
	public boolean supports(Class<?> clazz) {
		return clazz.equals(JqGridRequest.class);
	}

	public void validate(Object target, Errors errors) {
		JqGridRequest jqGridRequest = (JqGridRequest) target;
		ValidationUtils.rejectIfEmpty(errors, "startDate", "startdate.empty");
		ValidationUtils.rejectIfEmpty(errors, "endDate", "enddate.empty");
		validateDate(jqGridRequest, errors);
	}

	private void validateDate(JqGridRequest jqGridRequest, Errors errors) {
		if (isSum(jqGridRequest.getDateType()) || isDay(jqGridRequest.getDateType())) {
			if (StringUtils.isEmpty(jqGridRequest.getSearchString())) {
				if (DateUtil.getDays(jqGridRequest.getStartDate(), jqGridRequest.getEndDate(), Constants.DATE_YMD_FORMAT) > 3) {
					errors.rejectValue("startDate", "invalid.parameter", "invalid parameter");
				}
			} else {
				if (jqGridRequest.getItemCode() == 1) {
					if (DateUtil.getDays(jqGridRequest.getStartDate(), jqGridRequest.getEndDate(), Constants.DATE_YMD_FORMAT) > 29) {
						errors.rejectValue("startDate", "invalid.parameter", "invalid parameter");
					}
				} else {
					if (DateUtil.getDays(jqGridRequest.getStartDate(), jqGridRequest.getEndDate(), Constants.DATE_YMD_FORMAT) > 89) {
						errors.rejectValue("startDate", "invalid.parameter", "invalid parameter");
					}
				}
			}
		}
	}
}
