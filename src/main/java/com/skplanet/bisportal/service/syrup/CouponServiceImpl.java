package com.skplanet.bisportal.service.syrup;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwCponLocStat;
import com.skplanet.bisportal.model.syrup.SmwCponStat;
import com.skplanet.bisportal.model.syrup.SmwCponStatDtl;
import com.skplanet.bisportal.repository.syrup.SmwCponLocStatRepository;
import com.skplanet.bisportal.repository.syrup.SmwCponStatDtlRepository;
import com.skplanet.bisportal.repository.syrup.SmwCponStatRepository;

/**
 * Created by lko on 2014-11-21.
 */
@Service
public class CouponServiceImpl implements CouponService {
	@Autowired
	private SmwCponStatDtlRepository smwCponStatDtlRepository;

	@Autowired
	private SmwCponLocStatRepository smwCponLocStatRepository;

	@Autowired
	private SmwCponStatRepository smwCponStatRepository;

	@Override
	public List<SmwCponStatDtl> getCponStatForGrid(JqGridRequest jqGRidRequest) {
		if (isDay(jqGRidRequest.getDateType())) {
			return smwCponStatDtlRepository.getCponStatDtlPerDay(jqGRidRequest);
		} else if (isWeek(jqGRidRequest.getDateType())) {
			return smwCponStatDtlRepository.getCponStatDtlPerWeek(jqGRidRequest);
		} else {
			return smwCponStatDtlRepository.getCponStatDtlPerMonth(jqGRidRequest);
		}
	}

	@Override
	public List<SmwCponStatDtl> getCponStatForGridForExcel(JqGridRequest jqGRidRequest) {
		if (isDay(jqGRidRequest.getDateType())) {
			return smwCponStatDtlRepository.getCponStatDtlPerDayForExcel(jqGRidRequest);
		} else if (isWeek(jqGRidRequest.getDateType())) {
			return smwCponStatDtlRepository.getCponStatDtlPerWeekForExcel(jqGRidRequest);
		} else {
			return smwCponStatDtlRepository.getCponStatDtlPerMonthForExcel(jqGRidRequest);
		}
	}

	@Override
	public List<SmwCponLocStat> getCponLocStatForGrid(JqGridRequest jqGRidRequest) {
		if (isDay(jqGRidRequest.getDateType())) {
			return smwCponLocStatRepository.getCponLocStatPerDay(jqGRidRequest);
		} else if (isWeek(jqGRidRequest.getDateType())) {
			return smwCponLocStatRepository.getCponLocStatPerWeek(jqGRidRequest);
		} else {
			return smwCponLocStatRepository.getCponLocStatPerMonth(jqGRidRequest);
		}
	}

	@Override
	public List<SmwCponStat> getCouponAchieve(OlapDimensionRequest olapDimensionRequest) {
		String dimensions = olapDimensionRequest.getDimensions();
		// check dimension
		if (dimensions.indexOf("os") > -1) {
			olapDimensionRequest.setUseOs("Y");
		}
		if (dimensions.indexOf("standardDate") > -1) {
			olapDimensionRequest.setUseStandardDate("Y");
		}
		if (dimensions.indexOf("sex") > -1) {
			olapDimensionRequest.setUseSex("Y");
		}
		if (dimensions.indexOf("ageRange") > -1) {
			olapDimensionRequest.setUseAgeRange("Y");
		}
		if (dimensions.indexOf("inputChannel") > -1) {
			olapDimensionRequest.setUseInputChannel("Y");
		}
		if (dimensions.indexOf("couponType") > -1) {
			olapDimensionRequest.setUseCouponType("Y");
		}
		if (dimensions.indexOf("telecom") > -1) {
			olapDimensionRequest.setUseTelecom("Y");
		}

		if (isDay(olapDimensionRequest.getDateType())) {
			return smwCponStatRepository.getCouponAchievePerDay(olapDimensionRequest);
		} else if (isWeek(olapDimensionRequest.getDateType())) {
			return smwCponStatRepository.getCouponAchievePerWeek(olapDimensionRequest);
		} else {
			return smwCponStatRepository.getCouponAchievePerMonth(olapDimensionRequest);
		}
	}
}
