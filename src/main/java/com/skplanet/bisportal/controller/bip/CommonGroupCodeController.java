package com.skplanet.bisportal.controller.bip;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.model.bip.CommonGroupCode;
import com.skplanet.bisportal.service.bip.CommonGroupCodeService;

/**
 * Created by cookatrice on 2014. 8. 29..
 *
 */

@Controller
@RequestMapping("/commonGroupCode")
public class CommonGroupCodeController {
	@Autowired
	private CommonGroupCodeService commonGroupCodeServiceImpl;

	/**
	 * code 조회
	 *
	 * @param groupCodeId
	 * @return list of SearchCodeSection
	 */
	@RequestMapping(value = "/getCodes", method = RequestMethod.GET)
	@ResponseBody
	public List<CommonGroupCode> getCodesByGroupCodeId(String groupCodeId) {
		return commonGroupCodeServiceImpl.getCodesByGroupCodeId(groupCodeId);
	}

}
