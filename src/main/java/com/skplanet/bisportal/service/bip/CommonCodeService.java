package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.common.model.BasicDateWeekNumber;
import com.skplanet.bisportal.common.model.SearchCodeSection;
import com.skplanet.bisportal.model.bip.ComIntgComCd;
import com.skplanet.bisportal.model.bip.SvcComCd;

import java.util.List;
import java.util.Map;

/**
 * Created by sjune on 2014-05-28.
 * 
 * @author sjune
 */
public interface CommonCodeService {
	List<SearchCodeSection> getPocCodes(ComIntgComCd comIntgComCd);

    List<SearchCodeSection> getVisitMeasureCodes();

    List<SearchCodeSection> getObsVstPageStaMeasureCodes();

    List<SearchCodeSection> getObsCntntFlyrDetlStaMeasureCodes();

    /**
     * ocb > feed
     * @return
     */
    List<SearchCodeSection> getFeedTypeEnumCodes();

    List<SearchCodeSection> getTmapKpiCodes();

    Map<String, List<SvcComCd>> getSvcComCds(SvcComCd svcComCd);

    /**
     * 오늘 날짜에 해당하는 주차를 구한다.
     *
     * @return String
     *
     */
    String getTodayWeekNumber();

    /**
     * basicDate를 기준으로 요약리포트에 필요한 주차를 구한다.
     *
     * @param basicDate
     *            기준날짜(yyyyMMdd)
     * @return SummaryReportWeekNumber
     *
     */
    BasicDateWeekNumber getBasicDateWeekNumber(String basicDate);
}
