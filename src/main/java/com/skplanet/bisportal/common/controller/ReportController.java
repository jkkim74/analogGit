package com.skplanet.bisportal.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.common.support.ReportDateTypeEditor;

/**
 * The ReportController class.
 * <pre>
 *     레포트에서 공통적으로 사용되는 기능을 정의하는 controller
 * </pre>
 * @author sjune
 */
@Controller
public abstract class ReportController {

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		binder.registerCustomEditor(ReportDateType.class, new ReportDateTypeEditor());
	}
}
