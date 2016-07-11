package com.skplanet.bisportal.service.bip;

import static ch.lambdaj.collection.LambdaCollections.with;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.lambdaj.function.matcher.Predicate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.BasicDateWeekNumber;
import com.skplanet.bisportal.common.model.SearchCodeSection;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.model.bip.BpmWkStrdInfo;
import com.skplanet.bisportal.model.bip.ComIntgComCd;
import com.skplanet.bisportal.model.bip.CommonCode;
import com.skplanet.bisportal.model.bip.SvcComCd;
import com.skplanet.bisportal.repository.bip.BpmWkStcPrstRepository;
import com.skplanet.bisportal.repository.bip.ComIntgComCdRepository;

/**
 * Created by sjune on 2014-05-28.
 * 
 * @author sjune
 */
@Service
public class CommonCodeServiceImpl implements CommonCodeService {
	@Autowired
	private ComIntgComCdRepository comIntgComCdRepository;
	@Autowired
	private BpmWkStcPrstRepository bpmWkStcPrstRepository;

	@Override
	public List<SearchCodeSection> getPocCodes(ComIntgComCd comIntgComCd) {
		List<SearchCodeSection> searchCodeSections = Lists.newArrayList();
		List<ComIntgComCd> comIntgComCds = comIntgComCdRepository.getComIntgComCds(comIntgComCd);
		for (ComIntgComCd commonCode : comIntgComCds) {
			searchCodeSections.add(new SearchCodeSection(commonCode.getComCd(), commonCode.getComCdNm()));
		}
		return searchCodeSections;
	}

	@Override
	public List<SearchCodeSection> getVisitMeasureCodes() {
		List<SearchCodeSection> searchCodeSections = Lists.newArrayList();
		CommonCode.MesurVst[] obsVstPageStaMeasures = CommonCode.MesurVst.values();
		for (CommonCode.MesurVst measure : obsVstPageStaMeasures) {
			searchCodeSections.add(new SearchCodeSection(measure.name(), measure.label()));
		}
		return searchCodeSections;

	}

	@Override
	public List<SearchCodeSection> getObsVstPageStaMeasureCodes() {
		List<SearchCodeSection> searchCodeSections = Lists.newArrayList();
		CommonCode.MesurObsVstPageSta[] obsVstPageStaMeasures = CommonCode.MesurObsVstPageSta.values();
		for (CommonCode.MesurObsVstPageSta measure : obsVstPageStaMeasures) {
			searchCodeSections.add(new SearchCodeSection(measure.name(), measure.label()));
		}
		return searchCodeSections;
	}

	@Override
	public List<SearchCodeSection> getObsCntntFlyrDetlStaMeasureCodes() {
		List<SearchCodeSection> searchCodeSections = Lists.newArrayList();
		CommonCode.MesurObsCntntFlyrDetlSta[] obsCntntFlyrDetlStaMeasures = CommonCode.MesurObsCntntFlyrDetlSta
				.values();
		for (CommonCode.MesurObsCntntFlyrDetlSta measure : obsCntntFlyrDetlStaMeasures) {
			searchCodeSections.add(new SearchCodeSection(measure.name(), measure.label()));
		}
		return searchCodeSections;
	}

	@Override
	public List<SearchCodeSection> getFeedTypeEnumCodes() {
		List<SearchCodeSection> searchCodeSections = Lists.newArrayList();
		CommonCode.FeedTypeEnum[] feedTypeEnum = CommonCode.FeedTypeEnum.values();
		for (CommonCode.FeedTypeEnum measure : feedTypeEnum) {
			searchCodeSections.add(new SearchCodeSection(measure.name(), measure.label()));
		}
		return searchCodeSections;
	}

	@Override
	public List<SearchCodeSection> getTmapKpiCodes() {
		List<SearchCodeSection> searchCodeSections = Lists.newArrayList();
		CommonCode.KpiCodeEnum[] kpiCodeEnum = CommonCode.KpiCodeEnum.values();
		for (CommonCode.KpiCodeEnum measure : kpiCodeEnum) {
			searchCodeSections.add(new SearchCodeSection(measure.label1(), measure.label2()));
		}
		return searchCodeSections;
	}

	@Override
	public Map<String, List<SvcComCd>> getSvcComCds(SvcComCd svcComCd) {
		List<SvcComCd> whereConditions = Arrays.asList(new SvcComCd("23", "PROCESS"), new SvcComCd("23", "POC"),
				new SvcComCd("23", "SST"));
		Map<String, List<SvcComCd>> svcComCdMaps = Maps.newLinkedHashMap();
		List<SvcComCd> svcComCds = comIntgComCdRepository.getSvcComCds(whereConditions);
		svcComCdMaps.put("process", with(svcComCds).clone().retain(new Predicate<SvcComCd>() {
			@Override
			public boolean apply(SvcComCd svcComCd) {
				return StringUtils.equals("PROCESS", svcComCd.getCodeType());
			}
		}));
		svcComCdMaps.put("poc", with(svcComCds).clone().retain(new Predicate<SvcComCd>() {
			@Override
			public boolean apply(SvcComCd svcComCd) {
				return StringUtils.equals("POC", svcComCd.getCodeType());
			}
		}));
		svcComCdMaps.put("sst", with(svcComCds).clone().retain(new Predicate<SvcComCd>() {
			@Override
			public boolean apply(SvcComCd svcComCd) {
				return StringUtils.equals("SST", svcComCd.getCodeType());
			}
		}));
		return svcComCdMaps;
	}

	@Override
	public String getTodayWeekNumber() {
		// 오늘날짜에 해당하는 주차
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setSearchDate(DateUtil.getCurrentDate(Constants.DATE_YMD_FORMAT));
		BpmWkStrdInfo currentBpmWkStrdInfo = bpmWkStcPrstRepository.getBpmWkStrdInfo(whereCondition);
		return currentBpmWkStrdInfo.getWkStcStrdYmw();
	}

	@Override
	public BasicDateWeekNumber getBasicDateWeekNumber(String basicDate) {
		BasicDateWeekNumber weekNumber = new BasicDateWeekNumber();
		WhereCondition whereCondition = new WhereCondition();
		// 기준주차
		whereCondition.setSearchDate(basicDate);
		BpmWkStrdInfo basicBpmWkStrdInfo = bpmWkStcPrstRepository.getBpmWkStrdInfo(whereCondition);
		weekNumber.setBasic(basicBpmWkStrdInfo.getWkStcStrdYmw());
		weekNumber.setBasicStartDate(basicBpmWkStrdInfo.getWkStaDt());
		weekNumber.setBasicEndDate(basicBpmWkStrdInfo.getWkEndDt());
		// 기준 1주전 주차
		whereCondition.setSearchDiff(1);
		BpmWkStrdInfo oneWeekAgoBpmWkStrdInfo = bpmWkStcPrstRepository.getWeekAgoBpmWkStrdInfo(whereCondition);
		weekNumber.setOneWeekAgo(oneWeekAgoBpmWkStrdInfo.getWkStcStrdYmw());
		weekNumber.setOneWeekAgoStartDate(oneWeekAgoBpmWkStrdInfo.getWkStaDt());
		weekNumber.setOneWeekAgoEndDate(oneWeekAgoBpmWkStrdInfo.getWkEndDt());
		// 기준 14주전 주차
		whereCondition.setSearchDiff(14);
		BpmWkStrdInfo fourteenWeeAgoBpmWkStrdInfo = bpmWkStcPrstRepository.getWeekAgoBpmWkStrdInfo(whereCondition);
		weekNumber.setFourteenWeekAgo(fourteenWeeAgoBpmWkStrdInfo.getWkStcStrdYmw());
		// 기준 1년전 주차
		BpmWkStrdInfo oneYearAgoBpmWkStrdInfo = bpmWkStcPrstRepository.getOneYearAgoBpmWkStrdInfo(whereCondition);
		weekNumber.setOneYearAgo(oneYearAgoBpmWkStrdInfo.getWkStcStrdYmw());
		weekNumber.setOneYearAgoStartDate(oneYearAgoBpmWkStrdInfo.getWkStaDt());
		weekNumber.setOneYearAgoEndDate(oneYearAgoBpmWkStrdInfo.getWkEndDt());
		return weekNumber;
	}
}
