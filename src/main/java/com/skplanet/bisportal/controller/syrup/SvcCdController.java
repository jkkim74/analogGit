package com.skplanet.bisportal.controller.syrup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.model.syrup.SmwCldrWk;
import com.skplanet.bisportal.service.syrup.SvcCdService;

/**
 * Created by lko on 2014-11-21.
 */
@Controller
@RequestMapping("/svcCd")
public class SvcCdController {
	@Autowired
	private SvcCdService svcCdServiceImpl;

	/**
	 * 주차 코드를 조회한다.
	 *
	 * @param smwCldrWk
	 *            주차 기준날짜
	 * @return SmwCldrWk
	 */
	@RequestMapping(value = "/wkStrd", method = RequestMethod.GET)
	@ResponseBody
	public List<SmwCldrWk> getWkStrd(SmwCldrWk smwCldrWk) {
		return svcCdServiceImpl.getWkStrd(smwCldrWk);
	}

	/**
	 * 주차 코드를 조회한다.
	 *
	 * @param smwCldrWk
	 *            년도
	 * @return SmwCldrWk
	 */
	@RequestMapping(value = "/wkStrds", method = RequestMethod.GET)
	@ResponseBody
	public List<SmwCldrWk> getWkStrds(SmwCldrWk smwCldrWk) {
		return svcCdServiceImpl.getWkStrds(smwCldrWk);
	}
}
