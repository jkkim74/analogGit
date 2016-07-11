package com.skplanet.bisportal.common.validator;

import com.skplanet.bisportal.common.model.ReportDateType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;

public class BleValidatorTest extends AbstractContextLoadingTest {
	@Autowired
	private BleValidator validator;

	@Test
	public void testBleValidator() {
		JqGridRequest jqGridRequest = new JqGridRequest();
		jqGridRequest.setDateType(ReportDateType.DAY);
		jqGridRequest.setStartDate("20150102");
		jqGridRequest.setEndDate("20150108");
		BindException errors = new BindException(jqGridRequest, JqGridRequest.class.getName());
		ValidationUtils.invokeValidator(validator, jqGridRequest, errors);
		Assert.assertTrue(errors.hasErrors());
	}
}
