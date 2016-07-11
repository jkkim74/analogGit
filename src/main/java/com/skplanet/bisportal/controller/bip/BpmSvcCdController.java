package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmSvcCd;
import com.skplanet.bisportal.service.bip.BpmSvcCdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <pre>
 * The BpmSvcCdController class.
 * 경영실적 코드조회 컨트롤러
 * </pre>
 * 
 * @author sjune
 */
@Controller
@RequestMapping("/bpmSvcCd")
public class BpmSvcCdController {
	@Autowired
	private BpmSvcCdService bpmSvcCdServiceImpl;

	/**
	 * 서비스 코드 목록(조회대상 대분류)을 조회한다.
	 *
	 * @return BpmSvcCd
	 */
	@RequestMapping(value = "/svcs", method = RequestMethod.GET)
	@ResponseBody
	public List<BpmSvcCd> getBpmSvcs() {
		return bpmSvcCdServiceImpl.getBpmSvcs();
	}

	/**
	 * 지표구분 그룹 코드(조회대상 중분류) 목록을 조회한다.
	 *
	 * @param whereCondition
	 * @return BpmSvcCd
	 */
	@RequestMapping(value = "/cycleToGrps", method = RequestMethod.GET)
	@ResponseBody
	public List<BpmSvcCd> getBpmCycleToGrps(WhereCondition whereCondition) {
		return bpmSvcCdServiceImpl.getBpmCycleToGrps(whereCondition);
	}

	/**
	 * 지표구분 코드(조회대상 소분류) 목록을 조회한다.
	 *
	 * @param whereCondition
	 * @return BpmSvcCd
	 */
	@RequestMapping(value = "/grpToCls", method = RequestMethod.GET)
	@ResponseBody
	public List<BpmSvcCd> getBpmGrpToCls(WhereCondition whereCondition) {
		return bpmSvcCdServiceImpl.getBpmGrpToCls(whereCondition);
	}

	/**
	 * 주차 코드를 조회한다.
	 *
	 * @param wkStcStrdYmw
	 *            주차 기준날짜
	 * @return BpmSvcCd
	 */
	@RequestMapping(value = "/wkStrds/{wkStcStrdYmw}", method = RequestMethod.GET)
	@ResponseBody
	public List<BpmSvcCd> getBpmWkStrds(@PathVariable String wkStcStrdYmw) {
		return bpmSvcCdServiceImpl.getBpmWkStrds(wkStcStrdYmw);
	}
}
