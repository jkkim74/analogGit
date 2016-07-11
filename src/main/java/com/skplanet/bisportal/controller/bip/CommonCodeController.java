package com.skplanet.bisportal.controller.bip;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.common.model.SearchCodeSection;
import com.skplanet.bisportal.model.bip.ComIntgComCd;
import com.skplanet.bisportal.model.bip.SvcComCd;
import com.skplanet.bisportal.service.bip.CommonCodeService;

/**
 * Created by sjune on 2014-05-28.
 * 
 * @author sjune
 */
@Controller
@RequestMapping("/commonCode")
public class CommonCodeController {
	@Autowired
	private CommonCodeService commonCodeServiceImpl;

	/**
	 * POC코드를 조회한다.
	 * 
	 * @param comIntgComCd
	 * @return list of SearchCodeSection
	 */
	@RequestMapping(value = "/poc/{sysIndCd}/{comGrpCd}", method = RequestMethod.GET)
	@ResponseBody
	public List<SearchCodeSection> getPocCodes(ComIntgComCd comIntgComCd) {
		return commonCodeServiceImpl.getPocCodes(comIntgComCd);
	}

	/**
	 * UV,LV,PV 방문 코드 조회
	 *
	 * @return list of SearchCodeSection
	 */
	@RequestMapping(value = "/measure/visitMeasureCodes", method = RequestMethod.GET)
	@ResponseBody
	public List<SearchCodeSection> getVisitMeasureCodes() {
		return commonCodeServiceImpl.getVisitMeasureCodes();
	}

	/**
	 * OBS > 방문페이지 measure 코드를 조회한다.
	 * 
	 * @return list of SearchCodeSection
	 */
	@RequestMapping(value = "/measure/ObsVstPage", method = RequestMethod.GET)
	@ResponseBody
	public List<SearchCodeSection> getObsVstPageStaMeasureCodes() {
		return commonCodeServiceImpl.getObsVstPageStaMeasureCodes();
	}

	/**
	 * 컨텐츠상세분석 > 상권전단내 매장전단
	 *
	 * @return list of SearchCodeSection
	 */
	@RequestMapping(value = "/measure/ObsCntntFlyrDetlSta", method = RequestMethod.GET)
	@ResponseBody
	public List<SearchCodeSection> getObsCntntFlyrDetlStaMeasureCodes() {
		return commonCodeServiceImpl.getObsCntntFlyrDetlStaMeasureCodes();
	}

	/**
	 * 컨텐츠상세분석 > Feed
	 *
	 * @return list of SearchCodeSection
	 */
	@RequestMapping(value = "/FeedTypeEnum", method = RequestMethod.GET)
	@ResponseBody
	public List<SearchCodeSection> getFeedTypeEnumCodes() {
		return commonCodeServiceImpl.getFeedTypeEnumCodes();
	}

	/**
	 * T map > KPI 코드를 조회한다.
	 *
	 * @return list of SearchCodeSection
	 */
	@RequestMapping(value = "/tmapKpiCodes", method = RequestMethod.GET)
	@ResponseBody
	public List<SearchCodeSection> getTmapKpiCodes() {
		return commonCodeServiceImpl.getTmapKpiCodes();
	}

	/**
	 * Oneid 서비스의 검색조건 정보를 조회한다.
	 *
	 * @return list of SvcComCd
	 */
	@RequestMapping(value = "/searchBox/{svcId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, List<SvcComCd>> getSeachBoxCodes(SvcComCd svcComCd) {
		return commonCodeServiceImpl.getSvcComCds(svcComCd);
	}
}
