package com.skplanet.bisportal.service.ocb;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.skplanet.bisportal.api.sms.SmsHelper;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.AjaxResult;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.ocb.*;
import com.skplanet.bisportal.repository.ocb.ObsActnStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsSosBleStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsVstFstUvStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsVstInflowUvStaRepository;
import com.skplanet.bisportal.repository.ocb.RtdDauRepository;
import com.skplanet.bisportal.repository.syrup.SmwSyrupDauRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skp.bss.msg.rms.front.vo.MultiRequestVo;

import java.math.BigDecimal;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isMonth;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * The CustomerServiceImpl class(고객 상세 분석).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
	private static final Injector injector = Guice.createInjector();

	@Autowired
	private ObsActnStaRepository obsActnStaRepository;

	@Autowired
	private ObsSosBleStaRepository obsSosBleStaRepository;

	@Autowired
	private RtdDauRepository rtdDauRepository;

	@Autowired
	private ObsVstInflowUvStaRepository obsVstInflowUvStaRepository;

	@Autowired
	private ObsVstFstUvStaRepository obsVstFstUvStaRepository;

	@Autowired
	private SmwSyrupDauRepository smwSyrupDauRepository;

	@Override
	public List<ObsActnSta> getCustomersActionForGrid(JqGridRequest jqGridRequest) {
		if (isMonth(jqGridRequest.getDateType())) {
			return obsActnStaRepository.getCustomersActionPerMonth(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return obsActnStaRepository.getCustomersActionPerWeek(jqGridRequest);
		}
		return obsActnStaRepository.getCustomersActionPerDay(jqGridRequest);
	}

	@Override
	public List<ObsSosBleSta> getBleDiffData(JqGridRequest jqGridRequest) {
		return obsSosBleStaRepository.getBleDiffData(jqGridRequest);
	}

	@Override
	public DauResponse getDau() throws Exception {
		RtdDau requestQuery = getRequestRtdDau();
		List<RtdDau> rdtDaus = rtdDauRepository.getRtdDauByStdDt(requestQuery);
		DauResponse dauResponse = new DauResponse();
		if (CollectionUtils.isEmpty(rdtDaus)) {
			log.error("hour data not found.");
			return dauResponse;
		}
		Dau dau = new Dau();
		dau.setStdDt(requestQuery.getStrdDt() + requestQuery.getHour());
		if (requestQuery.getFlag() == 1) {
			dau.setOcbActiveUserCnt(rdtDaus.get(1).getFct()); // 실적값 못 가져옴.
			// OCB GAP 평균 계산
			BigDecimal ocbGapAvg = BigDecimal.ZERO;
			List<RtdDau> ocbRdtDaus = rtdDauRepository.getOcbRtdDauByStdDt(requestQuery);
			if (CollectionUtils.isNotEmpty(ocbRdtDaus)) {
				double totalOcbGap = sum(ocbRdtDaus, on(RtdDau.class).getFctGap().doubleValue());
				ocbGapAvg = BigDecimal.valueOf(totalOcbGap / ocbRdtDaus.size());
			}
			dau.setExpectedOcbActiveUserCnt(rdtDaus.get(1).getFct().add(ocbGapAvg));
		} else {
			dau.setOcbActiveUserCnt(rdtDaus.get(0).getFct());
			dau.setExpectedOcbActiveUserCnt(rdtDaus.get(1).getFct());
		}

		if (requestQuery.getSyrupFlag() == 1) {
			dau.setSyrupActiveUserCnt(rdtDaus.get(3).getFct());
			// Syrup GAP 평균 계산
			List<RtdDau> syrupRdtDaus = rtdDauRepository.getSyrupRtdDauByStdDt(requestQuery);
			double totalSyrupGap = sum(syrupRdtDaus, on(RtdDau.class).getGap().doubleValue());
			double syrupGapAvg = totalSyrupGap / syrupRdtDaus.size();
			dau.setExpectedSyrupActiveUserCnt(rdtDaus.get(3).getFct().add(BigDecimal.valueOf(syrupGapAvg)));
		} else {
			dau.setSyrupActiveUserCnt(rdtDaus.get(2).getHauAcm());
			dau.setExpectedSyrupActiveUserCnt(rdtDaus.get(3).getFct());
		}
		if (StringUtils.equals(DateUtil.getCurrentDate("yyyyMMddHH").substring(8), "00")) {
			dau.setOcbActiveUserCnt(new BigDecimal("0"));
			dau.setSyrupActiveUserCnt(new BigDecimal("0"));
		}
		dauResponse.setDau(dau);
		// 전일 DAU_MTH_AVG 없을 경우 처리.
		List<RtdDau> ocbSyrupRdtDaus = rtdDauRepository.getOcbSyrupRtdDauCountByStdDt(requestQuery);
		if (ocbSyrupRdtDaus.get(0).getFlag() < 1 || ocbSyrupRdtDaus.get(1).getFlag() < 1) {
			requestQuery.setPreStrdDt(DateUtil.addDays(requestQuery.getPreStrdDt(), -1));
			requestQuery.setSyrupPreStrdDt(DateUtil.addDays(requestQuery.getSyrupPreStrdDt(), -1));
		}
		dauResponse.setOcbs(rtdDauRepository.getOcbRtdDauByPreStrdDt(requestQuery));
		dauResponse.setSyrups(rtdDauRepository.getSyrupRtdDauByPreStrdDt(requestQuery));

		return dauResponse;
	}

	@Override
	public List<RtdDau> getOcbDauForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return rtdDauRepository.getOcbDauForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return rtdDauRepository.getOcbDauForPivotPerWeek(jqGridRequest);
		} else {
			return rtdDauRepository.getOcbDauForPivotPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<ObsVstInflowUvSta> getVisitInflRtForPivot(JqGridRequest jqGridRequest) {
		return obsVstInflowUvStaRepository.getVisitInflRtForPivotPerDay(jqGridRequest);
	}

	@Override
	public List<ObsVstFstUvSta> getVisitFstForPivot(JqGridRequest jqGridRequest) {
		return obsVstFstUvStaRepository.getVisitFstForPivotPerDay(jqGridRequest);
	}

	@Override
	public List<RtdDau> getSyrupDauForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return rtdDauRepository.getSyrupDauForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return smwSyrupDauRepository.getSyrupDauForPivotPerWeek(jqGridRequest);
		} else {
			return smwSyrupDauRepository.getSyrupDauForPivotPerMonth(jqGridRequest);
		}
	}

	@Override
	public List<RtdDau> getAppSticknessForPivot(JqGridRequest jqGridRequest) {
		if (isDay(jqGridRequest.getDateType())) {
			return rtdDauRepository.getAppSticknessForPivotPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			return rtdDauRepository.getAppSticknessForPivotPerWeek(jqGridRequest);
		} else {
			return rtdDauRepository.getAppSticknessForPivotPerMonth(jqGridRequest);
		}
	}

	private RtdDau getRequestRtdDau() throws Exception {
		String currentMinute = DateUtil.getCurrentDate("yyyyMMddHHmm");
		String syrupCurrentHour = DateUtil.getCurrentDate("yyyyMMddHH");
		String currentHour = DateUtil.getCurrentDate("yyyyMMddHH");
		if (Integer.parseInt(currentMinute.substring(10)) < 30) {
			currentHour = DateUtil.addHours(currentHour, -1);
			syrupCurrentHour = DateUtil.addHours(syrupCurrentHour, -1);
		}
		String oneHourBefore = DateUtil.addHours(currentHour, -1);
		RtdDau requestQuery = new RtdDau();
		requestQuery.setStrdDt(currentHour.substring(0, 8));
		requestQuery.setOneHourBeforeStrdDt(oneHourBefore.substring(0, 8));
		requestQuery.setHour(currentHour.substring(8));
		requestQuery.setOneHourBefore(oneHourBefore.substring(8));

		List<RtdDau> rdtDauCounts = rtdDauRepository.getRtdDauCountByStdDt(requestQuery);
		if ((rdtDauCounts.get(0).getFlag() < 1 && rdtDauCounts.get(1).getFlag() < 1)) {
			// 하루전 데이터 시럽/OCB분리
			currentHour = DateUtil.addHours(currentHour, -24);
			syrupCurrentHour = currentHour;
			requestQuery.setFlag(2);
			requestQuery.setSyrupFlag(2);
			// OCB 2시간 이상 데이터 저장안되어서 SMS발송
			// this.sendMMS("OCB");
		}
		if (rdtDauCounts.get(2).getFlag() < 1 && rdtDauCounts.get(3).getFlag() < 1) {
			if (requestQuery.getFlag() != 2) {
				syrupCurrentHour = DateUtil.addHours(syrupCurrentHour, -24);
				currentHour = syrupCurrentHour;
				requestQuery.setFlag(2);
				requestQuery.setSyrupFlag(2);
			}
			// Syrup 2시간 이상 데이터 저장안되어서 SMS발송
			// this.sendMMS("Syrup");
		}

		if (requestQuery.getFlag() == 2 || (rdtDauCounts.get(0).getFlag() < 1 && rdtDauCounts.get(1).getFlag() < 1)) {// OCB 실적치, 예측치 모두 미존재함
			oneHourBefore = DateUtil.addHours(currentHour, -1);
			requestQuery.setStrdDt(currentHour.substring(0, 8));
			requestQuery.setOneHourBeforeStrdDt(oneHourBefore.substring(0, 8));
			requestQuery.setHour(currentHour.substring(8));
			requestQuery.setOneHourBefore(oneHourBefore.substring(8));
		} else if (rdtDauCounts.get(0).getFlag() > 0 && rdtDauCounts.get(1).getFlag() < 1) {// OCB 실적치 있고 예측값 없는 경우
			currentHour = DateUtil.addHours(currentHour, -1);
			oneHourBefore = DateUtil.addHours(currentHour, -1);
			requestQuery.setStrdDt(currentHour.substring(0, 8));
			requestQuery.setOneHourBeforeStrdDt(oneHourBefore.substring(0, 8));
			requestQuery.setHour(currentHour.substring(8));
			requestQuery.setOneHourBefore(oneHourBefore.substring(8));
			requestQuery.setFlag(1);
		}

		if (requestQuery.getFlag() == 2 || (rdtDauCounts.get(2).getFlag() > 0 && rdtDauCounts.get(3).getFlag() > 0)
				|| (rdtDauCounts.get(2).getFlag() < 1 && rdtDauCounts.get(3).getFlag() < 1)) {// Syrup 실적치, 예측치 모두 존재함
			oneHourBefore = DateUtil.addHours(syrupCurrentHour, -1);
			requestQuery.setSyrupStrdDt(syrupCurrentHour.substring(0, 8));
			requestQuery.setSyrupOneHourBeforeStrdDt(oneHourBefore.substring(0, 8));
			requestQuery.setSyrupHour(syrupCurrentHour.substring(8));
			requestQuery.setSyrupOneHourBefore(oneHourBefore.substring(8));
		} else if (rdtDauCounts.get(2).getFlag() > 0 && rdtDauCounts.get(3).getFlag() < 1) {// Syrup 실적치 있고 예측값 없는 경우
			syrupCurrentHour = DateUtil.addHours(syrupCurrentHour, -1);
			oneHourBefore = DateUtil.addHours(syrupCurrentHour, -1);
			requestQuery.setSyrupStrdDt(syrupCurrentHour.substring(0, 8));
			requestQuery.setSyrupOneHourBeforeStrdDt(oneHourBefore.substring(0, 8));
			requestQuery.setSyrupHour(syrupCurrentHour.substring(8));
			requestQuery.setSyrupOneHourBefore(oneHourBefore.substring(8));
			requestQuery.setSyrupFlag(1);
		}

		requestQuery.setPreStrdDt(DateUtil.addDays(requestQuery.getStrdDt(), -1));
		requestQuery.setSyrupPreStrdDt(DateUtil.addDays(requestQuery.getSyrupStrdDt(), -1));
		return requestQuery;
	}

	@Override
	public AjaxResult sendMMS(String serviceName) {
		AjaxResult ajaxResult = null;
		try {
			ajaxResult = new AjaxResult();
			String hostName = Utils.getHostName();
			if (StringUtils.isEmpty(hostName)
					|| (!StringUtils.equals(hostName, Constants.HOST_WAS01)
					&& !StringUtils.equals(hostName, Constants.HOST_WAS02)
					&& !StringUtils.equals(hostName, Constants.HOST_DEV_WAS))) {
				log.info("운영서버가 아니기에 SMS 발송을 할 수 없습니다.");
				ajaxResult.setCode(400);
				ajaxResult.setMessage("bad_request");
				return ajaxResult;
			}

			String[] rcvNums = {"01025430571"};
			if (ArrayUtils.isEmpty(rcvNums)) {
				log.info("수신 리스트에 핸드폰 정보가 없습니다.");
				ajaxResult.setCode(404);
				ajaxResult.setMessage("receive data_not_dound");
				return ajaxResult;
			}

			SmsHelper smsHelper = injector.getInstance(SmsHelper.class);
			MultiRequestVo multiRequest = smsHelper.sendMMS(rcvNums,
					"6시 17분에 받으신 OCB 데이터 장애 문자는 Voyager 장애전파 SMS 테스트중 잘못 나간 문자로 실제상황이 아닙니다. 번거롭게 해드려 죄송합니다. 양해부탁드립니다.");
			if (multiRequest != null && StringUtils.equals("0", multiRequest.getResultCode())) {
				ajaxResult.setCode(200);
				ajaxResult.setMessage("success");
			} else {
				ajaxResult.setCode(500);
				ajaxResult.setMessage("biz_fail");
			}
		} catch (Exception e) {
			log.error("sendMMS {}", e);
			if (ajaxResult == null)
				ajaxResult = new AjaxResult();
			ajaxResult.setCode(998);
			ajaxResult.setMessage("mms_fail");
		}
		return ajaxResult;
	}
}
