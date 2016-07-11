package com.skplanet.bisportal.service.oneid;

import ch.lambdaj.function.matcher.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.skplanet.bisportal.common.utils.NumberUtil;
import com.skplanet.bisportal.model.oneid.FunnelRequest;
import com.skplanet.bisportal.model.oneid.OidFunnel;
import com.skplanet.bisportal.model.oneid.OidUsrProc;
import com.skplanet.bisportal.repository.oneid.OidUsrProcRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.collection.LambdaCollections.with;

/**
 * Created by mimul on 2015. 6. 11..
 */
@Slf4j
@Service
public class FunnelServiceImpl implements FunnelService {
	@Autowired
	private OidUsrProcRepository oidUsrProcRepository;

	@Override
	public Map<String, List<OidFunnel>> getFunnelDatas(FunnelRequest funnelRequest) {
		return getOidFunnels(funnelRequest, getStepCds(funnelRequest.getProcCd()));
	}

	@Override
	public List<String> getStepCds(String procCd) {
		return oidUsrProcRepository.getStepCds(procCd);
	}

	private Map<String, List<OidFunnel>> getOidFunnels(FunnelRequest funnelRequest, List<String> steps) {
		Map<String, List<OidFunnel>> dataMaps = Maps.newLinkedHashMap();
		List<OidUsrProc> oidUsrProcs = oidUsrProcRepository.getOidUsrProcs(funnelRequest);
		if (CollectionUtils.isEmpty(oidUsrProcs)) {
			log.info("data not found!");
			return dataMaps;
		}
		int stepSize = steps.size();
		BigDecimal firstStepInCnt = BigDecimal.ZERO;
		for (final String stepCd : steps) {
			List<OidUsrProc> stepOidUsrProcs = with(oidUsrProcs).clone().retain(new Predicate<OidUsrProc>() {
				@Override
				public boolean apply(OidUsrProc oidUsrProc) {
					try {
						if (oidUsrProc.getStepInCnt().doubleValue() > 0) {
							oidUsrProc.setNextStepRate(NumberUtil.formatNumberByPoint(
									(oidUsrProc.getNextStepCnt().doubleValue() / oidUsrProc.getStepInCnt().doubleValue())
											* 100, 2) + "%");
						} else {
							oidUsrProc.setNextStepRate("0%");
						}
					} catch (Exception e) {
						oidUsrProc.setNextStepRate("0%");
					}
					return (StringUtils.equals(stepCd, oidUsrProc.getStepCd()));
				}
			});
			int size = CollectionUtils.size(stepOidUsrProcs);
			int stepNumber = Integer.parseInt(stepCd.substring(1));
			funnelRequest.setStepCd(stepCd);
			if (size == 0) {
				dataMaps.put((stepNumber == stepSize) ? "zlast" : stepCd, Arrays.asList(new OidFunnel()));
			} else if (size == 1) {
				OidFunnel oidFunnel = new OidFunnel();
				funnelRequest.setInoutFlg("0");
				funnelRequest.setDispOrd(stepOidUsrProcs.get(0).getDispOrd());
				if (StringUtils.isNotEmpty(funnelRequest.getDispOrd()))
					oidFunnel.setEntrances(oidUsrProcRepository.getOidUsrProcRanks(funnelRequest));
				funnelRequest.setInoutFlg("1");
				if (StringUtils.isNotEmpty(funnelRequest.getDispOrd()))
					oidFunnel.setExits(oidUsrProcRepository.getOidUsrProcRanks(funnelRequest));
				oidFunnel.setOidUsrProc(stepOidUsrProcs.get(0));
				firstStepInCnt = firstStepInCnt.add(stepOidUsrProcs.get(0).getExInCnt());
				if (stepNumber == stepSize) {// 마지막 단계전환율 계산
					try {
						if (firstStepInCnt.doubleValue() > 0) {
							oidFunnel.setConversionRate(NumberUtil.formatNumberByPoint((stepOidUsrProcs.get(0).getStepInCnt().doubleValue() / firstStepInCnt.doubleValue())
											* 100, 2));
						} else {
							oidFunnel.setConversionRate("0");
						}
					} catch (Exception e) {
						oidFunnel.setConversionRate("0");
						log.error("calculate ConversionRate, {}", e);
					}
				}
				dataMaps.put((stepNumber == stepSize) ? "zlast" : stepCd, Arrays.asList(oidFunnel));
			} else {
				List<OidFunnel> oidFunnels = Lists.newArrayList();
				for (OidUsrProc oidUsrProc : stepOidUsrProcs) {
					OidFunnel oidFunnel = new OidFunnel();
					oidFunnel.setMultiLayer(true);
					funnelRequest.setInoutFlg("0");
					funnelRequest.setDispOrd(oidUsrProc.getDispOrd());
					if (StringUtils.isNotEmpty(oidUsrProc.getDispOrd()))
						oidFunnel.setEntrances(oidUsrProcRepository.getOidUsrProcRanks(funnelRequest));
					funnelRequest.setInoutFlg("1");
					if (StringUtils.isNotEmpty(oidUsrProc.getDispOrd()))
						oidFunnel.setExits(oidUsrProcRepository.getOidUsrProcRanks(funnelRequest));
					oidFunnel.setOidUsrProc(oidUsrProc);
					oidFunnels.add(oidFunnel);
					firstStepInCnt = firstStepInCnt.add(oidUsrProc.getExInCnt());
				}
				dataMaps.put((Integer.parseInt(stepCd.substring(1)) == stepSize) ? "zlast" : stepCd, oidFunnels);
			}
		}

		return dataMaps;
	}
}
