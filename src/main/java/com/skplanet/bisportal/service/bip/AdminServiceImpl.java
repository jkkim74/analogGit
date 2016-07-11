package com.skplanet.bisportal.service.bip;

import ch.lambdaj.function.matcher.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.AjaxResult;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.common.utils.NumberUtil;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.repository.bip.BpmMgntRsltBatInfoRgstRepository;
import com.skplanet.bisportal.repository.bip.BpmMgntRsltBatOpPrstRepository;
import com.skplanet.bisportal.repository.bip.DaaEwmaStatDailyRepository;
import com.skplanet.bisportal.repository.bip.HandInputRepository;
import com.skplanet.bisportal.repository.bip.OrgUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import static ch.lambdaj.collection.LambdaCollections.with;
import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * 경영 실적 Admin 서비스 구현 클래스.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private BpmMgntRsltBatInfoRgstRepository bpmMgntRsltBatInfoRgstRepository;
	@Autowired
	private BpmMgntRsltBatOpPrstRepository bpmMgntRsltBatOpPrstRepository;
	@Autowired
	private HandInputRepository handInputRepository;
	@Autowired
	private OrgUserRepository orgUserRepository;
	@Autowired
	private VelocityEngine velocityEngine;
	@Autowired
	private DaaEwmaStatDailyRepository daaEwmaStatDailyRepository;
	@Autowired
	private MailService mailServiceImpl;

	/**
	 * 배치 정보 조회.
	 *
	 * @return collection of BpmMgntRsltBatInfoRgst
	 * @throws Exception
	 */
	@Override
	public List<BpmMgntRsltBatInfoRgst> getAllBpmMgntRsltBatInfoRgst() throws Exception {
		return bpmMgntRsltBatInfoRgstRepository.getAllBpmMgntRsltBatInfoRgst();
	}

	/**
	 * 배치 모니터링 정보 조회.
	 *
	 * @param basicDate
	 * @return collection of BpmMgntRsltBatOpPrst
	 * @throws Exception
	 */
	@Override
	public List<BpmMgntRsltBatOpPrst> getBpmMgntRsltBatOpPrst(String basicDate) throws Exception {
		return bpmMgntRsltBatOpPrstRepository.getBpmMgntRsltBatOpPrst(basicDate);
	}

	/**
	 * Tmap 수신 정보 조회.
	 *
	 * @param jqGridRequest
	 * @return collection of HandInput
	 * @throws Exception
	 */
	@Override
	public List<HandInput> getTMapCttMappInfo(JqGridRequest jqGridRequest) throws Exception {
		if (isDay(jqGridRequest.getDateType())) {
			jqGridRequest.setBasicDate(DateUtil.changeFormatDate(jqGridRequest.getBasicDate(),
					Constants.DATE_YMD_FORMAT, Constants.DATE_YM_FORMAT));
			return handInputRepository.getTMapCttMappInfoPerDay(jqGridRequest);
		} else if (isWeek(jqGridRequest.getDateType())) {
			jqGridRequest.setBasicDate(DateUtil.changeFormatDate(jqGridRequest.getBasicDate(),
					Constants.DATE_YMD_FORMAT, Constants.DATE_YM_FORMAT));
			return handInputRepository.getTMapCttMappInfoPerWeek(jqGridRequest);
		} else {
			jqGridRequest.setBasicDate(DateUtil.changeFormatDate(jqGridRequest.getBasicDate(),
					Constants.DATE_YMD_FORMAT, Constants.DATE_Y_FORMAT));
			return handInputRepository.getTMapCttMappInfoPerMonth(jqGridRequest);
		}
	}

	/**
	 * Syrup 수신 정보 조회.
	 *
	 * @param jqGridRequest
	 * @return collection of HandInput
	 * @throws Exception
	 */
	@Override
	public List<HandInput> getSyrupCttMappInfo(JqGridRequest jqGridRequest) throws Exception {
		if (isDay(jqGridRequest.getDateType())) {
			jqGridRequest.setBasicDate(DateUtil.changeFormatDate(jqGridRequest.getBasicDate(),
					Constants.DATE_YMD_FORMAT, Constants.DATE_YM_FORMAT));
			return handInputRepository.getSyrupCttMappInfoPerDay(jqGridRequest);
		} else {
			jqGridRequest.setBasicDate(DateUtil.changeFormatDate(jqGridRequest.getBasicDate(),
					Constants.DATE_YMD_FORMAT, Constants.DATE_Y_FORMAT));
			return handInputRepository.getSyrupCttMappInfoPerMonth(jqGridRequest);
		}
	}

	/**
	 * 배치 구동 여부 정보 조회.
	 *
	 * @param svcId
	 * @return Integer
	 * @throws Exception
	 */
	@Override
	public Integer getBatchJobCheck(Integer svcId) throws Exception {
		return handInputRepository.getBatchJobCheck(svcId);
	}

	/**
	 * Syrup, Tmap 수시 입력 데이터 등록.
	 *
	 * @param jqGridRequest
	 * @return String
	 * @throws Exception
	 */
	@Override
	@Transactional
	public String createSyrupCttMappInfoPerDay(JqGridRequest jqGridRequest) throws Exception {
		String trmsFlNm = "SKP_MGT_DAY_8_" + DateUtil.getCurrentDate() + ".dat";
		BigDecimal svcId = new BigDecimal(8);
		BpmIdxCttMappInfo cttMappInfo;
		BpmIdxCttMappInfoTmp tmpVo = new BpmIdxCttMappInfoTmp();
		tmpVo.setTrmsFlNm(trmsFlNm);
		tmpVo.setSvcId(svcId);
		// 기존 데이터 delete
		handInputRepository.deleteBpmIdxCttMappInfoTmp(tmpVo);
		// 데이터 주입
		for (HandInput handInput : jqGridRequest.getHandInputs()) {
			if (handInput.getRsltVal1() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L001");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal1());
				cttMappInfo.setLnkgCyclCd("D");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L001");
				tmpVo.setIdxClCd("M001");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("총계");
				tmpVo.setRsltVal(handInput.getRsltVal1());
				tmpVo.setLnkgCyclCd("DAY");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}

			if (handInput.getRsltVal2() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L003");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal2());
				cttMappInfo.setLnkgCyclCd("D");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L003");
				tmpVo.setIdxClCd("M001");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("총계");
				tmpVo.setRsltVal(handInput.getRsltVal2());
				tmpVo.setLnkgCyclCd("DAY");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}

			if (handInput.getRsltVal3() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L017");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal3());
				cttMappInfo.setLnkgCyclCd("D");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L017");
				tmpVo.setIdxClCd("M001");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("총계");
				tmpVo.setRsltVal(handInput.getRsltVal3());
				tmpVo.setLnkgCyclCd("DAY");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}
			if (handInput.getRsltVal4() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L080");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal4());
				cttMappInfo.setLnkgCyclCd("D");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L080");
				tmpVo.setIdxClCd("M001");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("총계");
				tmpVo.setRsltVal(handInput.getRsltVal4());
				tmpVo.setLnkgCyclCd("DAY");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}
			if (handInput.getRsltVal5() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L081");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal5());
				cttMappInfo.setLnkgCyclCd("D");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L081");
				tmpVo.setIdxClCd("M001");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("총계");
				tmpVo.setRsltVal(handInput.getRsltVal5());
				tmpVo.setLnkgCyclCd("DAY");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}
		}
		return trmsFlNm;
	}

	/**
	 * Syrup, Tmap 수시 입력 데이터 등록(월).
	 *
	 * @param jqGridRequest
	 * @return String
	 * @throws Exception
	 */
	@Override
	@Transactional
	public String createSyrupCttMappInfoPerMonth(JqGridRequest jqGridRequest) throws Exception {
		String trmsFlNm = "SKP_MGT_MTH_8_" + DateUtil.getCurrentDate() + ".dat";
		BigDecimal svcId = new BigDecimal(8);
		BpmIdxCttMappInfo cttMappInfo;
		BpmIdxCttMappInfoTmp tmpVo = new BpmIdxCttMappInfoTmp();
		tmpVo.setTrmsFlNm(trmsFlNm);
		tmpVo.setSvcId(svcId);
		// 기존 데이터 delete
		handInputRepository.deleteBpmIdxCttMappInfoTmp(tmpVo);
		// 데이터 주입
		for (HandInput handInput : jqGridRequest.getHandInputs()) {
			if (handInput.getRsltVal() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L018");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal());
				cttMappInfo.setLnkgCyclCd("M");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);
				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L018");
				tmpVo.setIdxClCd("M001");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("총계");
				tmpVo.setRsltVal(handInput.getRsltVal());
				tmpVo.setLnkgCyclCd("MTH");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}
		}
		return trmsFlNm;
	}

	/**
	 * @개요 : Tmap 일 실적 정보를 저장.
	 * 
	 * @param jqGridRequest
	 *            - 저장할 정보가 담긴 객체.
	 * @return void
	 * 
	 */
	@Override
	@Transactional
	public String createTmapCttMappInfoPerDay(JqGridRequest jqGridRequest) throws Exception {
		String trmsFlNm = "SKP_MGT_DAY_4_" + DateUtil.getCurrentDate("yyyyMMddHHmmss") + "_TM";
		BigDecimal svcId = new BigDecimal(4);
		BigDecimal zero = new BigDecimal(0);
		BpmIdxCttMappInfo cttMappInfo;
		BpmIdxCttMappInfo prevL003; // 전일 총회원수
		BpmIdxCttMappInfo nextL003; // 다음날 총회원수
		BigDecimal todayL001; // 기준일 신규가입자수
		BigDecimal nextL001; // 다음날 신규가입자수

		BpmIdxCttMappInfoTmp cttMappInfoTmp = new BpmIdxCttMappInfoTmp();
		cttMappInfoTmp.setTrmsFlNm(trmsFlNm);
		cttMappInfoTmp.setSvcId(svcId);
		// 기존 데이터 delete
		handInputRepository.deleteBpmIdxCttMappInfoTmp(cttMappInfoTmp);
		// 데이터 주입
		for (HandInput handInput : jqGridRequest.getHandInputs()) {
			handInput.setAuditId(jqGridRequest.getUsername());
			if (handInput.getRsltVal1() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L017");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal1());
				cttMappInfo.setLnkgCyclCd("D");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				cttMappInfoTmp = new BpmIdxCttMappInfoTmp();
				cttMappInfoTmp.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfoTmp.setSvcId(svcId);
				cttMappInfoTmp.setIdxClGrpCd("L017");
				cttMappInfoTmp.setIdxClCd("M001");
				cttMappInfoTmp.setIdxCttCd("S001");
				cttMappInfoTmp.setIdxCttCdDesc("총계");
				cttMappInfoTmp.setRsltVal(handInput.getRsltVal1());
				cttMappInfoTmp.setLnkgCyclCd("DAY");
				cttMappInfoTmp.setAuditId(jqGridRequest.getUsername());
				cttMappInfoTmp.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(cttMappInfoTmp);
			}
			if (handInput.getRsltVal3() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L003");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal3());
				cttMappInfo.setLnkgCyclCd("D");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				cttMappInfoTmp = new BpmIdxCttMappInfoTmp();
				cttMappInfoTmp.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfoTmp.setSvcId(svcId);
				cttMappInfoTmp.setIdxClGrpCd("L003");
				cttMappInfoTmp.setIdxClCd("M001");
				cttMappInfoTmp.setIdxCttCd("S001");
				cttMappInfoTmp.setIdxCttCdDesc("총계");
				cttMappInfoTmp.setRsltVal(handInput.getRsltVal3());
				cttMappInfoTmp.setLnkgCyclCd("DAY");
				cttMappInfoTmp.setAuditId(jqGridRequest.getUsername());
				cttMappInfoTmp.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(cttMappInfoTmp);

				prevL003 = handInputRepository.selectBpmIdxCttMappInfo(DateUtil.addDays(handInput.getMappStrdDt(), -1));
				if (prevL003 != null && prevL003.getRsltVal() != null) {
					todayL001 = handInput.getRsltVal3().add(prevL003.getRsltVal().negate()); // [D-1 총회원수] - [D-2 총회원수 ] = [D-1 신규가입자수]

					cttMappInfo = new BpmIdxCttMappInfo();
					cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
					cttMappInfo.setSvcId(svcId);
					cttMappInfo.setIdxClGrpCd("L001");
					cttMappInfo.setIdxClCd("M001");
					cttMappInfo.setIdxCttCd("S001");

					if (todayL001.compareTo(zero) == -1) {
						cttMappInfo.setRsltVal(zero);
					} else {
						cttMappInfo.setRsltVal(todayL001);
					}

					// cttMappInfo.setRsltVal(todayResult);
					cttMappInfo.setLnkgCyclCd("D");
					cttMappInfo.setAuditId(jqGridRequest.getUsername());
					handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

					cttMappInfoTmp = new BpmIdxCttMappInfoTmp();
					cttMappInfoTmp.setMappStrdDt(handInput.getMappStrdDt());
					cttMappInfoTmp.setSvcId(svcId);
					cttMappInfoTmp.setIdxClGrpCd("L001");
					cttMappInfoTmp.setIdxClCd("M001");
					cttMappInfoTmp.setIdxCttCd("S001");
					cttMappInfoTmp.setIdxCttCdDesc("총계");

					if (todayL001.compareTo(zero) == -1) {
						cttMappInfoTmp.setRsltVal(zero);
					} else {
						cttMappInfoTmp.setRsltVal(todayL001);
					}

					// tmpVo.setRsltVal(todayResult);
					cttMappInfoTmp.setLnkgCyclCd("DAY");
					cttMappInfoTmp.setAuditId(jqGridRequest.getUsername());
					cttMappInfoTmp.setTrmsFlNm(trmsFlNm);
					handInputRepository.insertBpmIdxCttMappInfoTmp(cttMappInfoTmp);
				}
				nextL003 = handInputRepository.selectBpmIdxCttMappInfo(DateUtil.addDays(handInput.getMappStrdDt(), 1));
				if (nextL003 != null && nextL003.getRsltVal() != null) {
					nextL001 = nextL003.getRsltVal().add(handInput.getRsltVal3().negate()); // [D 총회원수] - [D-1 총회원수 ] = [D 신규가입자수]

					cttMappInfo = new BpmIdxCttMappInfo();
					cttMappInfo.setMappStrdDt(nextL003.getMappStrdDt());
					cttMappInfo.setSvcId(svcId);
					cttMappInfo.setIdxClGrpCd("L001");
					cttMappInfo.setIdxClCd("M001");
					cttMappInfo.setIdxCttCd("S001");
					if (nextL001.compareTo(zero) == -1) {
						cttMappInfo.setRsltVal(zero);
					} else {
						cttMappInfo.setRsltVal(nextL001);
					}

					cttMappInfo.setLnkgCyclCd("D");
					cttMappInfo.setAuditId(jqGridRequest.getUsername());
					handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

					cttMappInfoTmp = new BpmIdxCttMappInfoTmp();
					cttMappInfoTmp.setMappStrdDt(nextL003.getMappStrdDt());
					cttMappInfoTmp.setSvcId(svcId);
					cttMappInfoTmp.setIdxClGrpCd("L001");
					cttMappInfoTmp.setIdxClCd("M001");
					cttMappInfoTmp.setIdxCttCd("S001");
					cttMappInfoTmp.setIdxCttCdDesc("총계");
					if (nextL001.compareTo(zero) == -1) {
						cttMappInfoTmp.setRsltVal(zero);
					} else {
						cttMappInfoTmp.setRsltVal(nextL001);
					}
					// tmpVo.setRsltVal(nextResult);
					cttMappInfoTmp.setLnkgCyclCd("DAY");
					cttMappInfoTmp.setAuditId(jqGridRequest.getUsername());
					cttMappInfoTmp.setTrmsFlNm(trmsFlNm);
					handInputRepository.insertBpmIdxCttMappInfoTmp(cttMappInfoTmp);
				}
			}
		}
		return trmsFlNm;
	}

	/**
	 * @개요 : Tmap 주 실적 정보를 저장.
	 * 
	 * @param jqGridRequest
	 *            - 저장할 정보가 담긴 객체.
	 * @return void
	 * 
	 */
	@Override
	@Transactional
	public String createTmapCttMappInfoPerWeek(JqGridRequest jqGridRequest) throws Exception {
		String trmsFlNm = "SKP_MGT_WEK_4_" + DateUtil.getCurrentDate("yyyyMMddHHmmss") + "_TM";
		BpmIdxCttMappInfo cttMappInfo;
		BpmIdxCttMappInfoTmp tmpVo = new BpmIdxCttMappInfoTmp();
		BigDecimal svcId = new BigDecimal(4);
		// TMP 데이터 삭제.
		tmpVo.setTrmsFlNm(trmsFlNm);
		tmpVo.setSvcId(svcId);
		handInputRepository.deleteBpmIdxCttMappInfoTmp(tmpVo);

		for (HandInput handInput : jqGridRequest.getHandInputs()) {
			if (handInput.getRsltVal1() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L040");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal1());
				cttMappInfo.setLnkgCyclCd("W");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L040");
				tmpVo.setIdxClCd("M001");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("총계");
				tmpVo.setRsltVal(handInput.getRsltVal1());
				tmpVo.setLnkgCyclCd("WEK");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}

			if (handInput.getRsltVal2() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L046");
				cttMappInfo.setIdxClCd("M026");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal2());
				cttMappInfo.setLnkgCyclCd("W");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L046");
				tmpVo.setIdxClCd("M026");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("Tstore");
				tmpVo.setRsltVal(handInput.getRsltVal2());
				tmpVo.setLnkgCyclCd("WEK");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}

			if (handInput.getRsltVal3() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L046");
				cttMappInfo.setIdxClCd("M026");
				cttMappInfo.setIdxCttCd("S002");
				cttMappInfo.setRsltVal(handInput.getRsltVal3());
				cttMappInfo.setLnkgCyclCd("W");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L046");
				tmpVo.setIdxClCd("M026");
				tmpVo.setIdxCttCd("S002");
				tmpVo.setIdxCttCdDesc("AppStore");
				tmpVo.setRsltVal(handInput.getRsltVal3());
				tmpVo.setLnkgCyclCd("WEK");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}

			if (handInput.getRsltVal4() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L046");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal2().add(handInput.getRsltVal3()));
				cttMappInfo.setLnkgCyclCd("W");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L046");
				tmpVo.setIdxClCd("M001");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("총계");
				tmpVo.setRsltVal(handInput.getRsltVal2().add(handInput.getRsltVal3()));
				tmpVo.setLnkgCyclCd("WEK");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}
		}
		return trmsFlNm;
	}

	/**
	 * @개요 : Tmap 월 실적 정보를 저장.
	 * 
	 * @param jqGridRequest
	 *            - 저장할 정보가 담긴 객체.
	 * @return void
	 * 
	 */
	@Override
	@Transactional
	public String createTmapCttMappInfoPerMonth(JqGridRequest jqGridRequest) throws Exception {
		String trmsFlNm = "SKP_MGT_MTH_4_" + DateUtil.getCurrentDate("yyyyMMddHHmmss") + "_TM";
		BpmIdxCttMappInfo cttMappInfo;
		BpmIdxCttMappInfoTmp tmpVo = new BpmIdxCttMappInfoTmp();
		BigDecimal svcId = new BigDecimal(4);
		// TMP 데이터 삭제.
		tmpVo.setTrmsFlNm(trmsFlNm);
		tmpVo.setSvcId(svcId);
		handInputRepository.deleteBpmIdxCttMappInfoTmp(tmpVo);

		for (HandInput handInput : jqGridRequest.getHandInputs()) {
			if (handInput.getRsltVal1() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L018");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal1());
				cttMappInfo.setLnkgCyclCd("M");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L018");
				tmpVo.setIdxClCd("M001");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("총계");
				tmpVo.setRsltVal(handInput.getRsltVal1());
				tmpVo.setLnkgCyclCd("MTH");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}

			if (handInput.getRsltVal2() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L047");
				cttMappInfo.setIdxClCd("M026");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal2());
				cttMappInfo.setLnkgCyclCd("M");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L047");
				tmpVo.setIdxClCd("M026");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("Tstore");
				tmpVo.setRsltVal(handInput.getRsltVal2());
				tmpVo.setLnkgCyclCd("MTH");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}

			if (handInput.getRsltVal3() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L047");
				cttMappInfo.setIdxClCd("M026");
				cttMappInfo.setIdxCttCd("S002");
				cttMappInfo.setRsltVal(handInput.getRsltVal3());
				cttMappInfo.setLnkgCyclCd("M");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L047");
				tmpVo.setIdxClCd("M026");
				tmpVo.setIdxCttCd("S002");
				tmpVo.setIdxCttCdDesc("AppStore");
				tmpVo.setRsltVal(handInput.getRsltVal3());
				tmpVo.setLnkgCyclCd("MTH");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}

			if (handInput.getRsltVal4() != null) {
				cttMappInfo = new BpmIdxCttMappInfo();
				cttMappInfo.setMappStrdDt(handInput.getMappStrdDt());
				cttMappInfo.setSvcId(svcId);
				cttMappInfo.setIdxClGrpCd("L047");
				cttMappInfo.setIdxClCd("M001");
				cttMappInfo.setIdxCttCd("S001");
				cttMappInfo.setRsltVal(handInput.getRsltVal2().add(handInput.getRsltVal3()));
				cttMappInfo.setLnkgCyclCd("M");
				cttMappInfo.setAuditId(jqGridRequest.getUsername());
				handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

				tmpVo = new BpmIdxCttMappInfoTmp();
				tmpVo.setMappStrdDt(handInput.getMappStrdDt());
				tmpVo.setSvcId(svcId);
				tmpVo.setIdxClGrpCd("L047");
				tmpVo.setIdxClCd("M001");
				tmpVo.setIdxCttCd("S001");
				tmpVo.setIdxCttCdDesc("총계");
				tmpVo.setRsltVal(handInput.getRsltVal2().add(handInput.getRsltVal3()));
				tmpVo.setLnkgCyclCd("MTH");
				tmpVo.setAuditId(jqGridRequest.getUsername());
				tmpVo.setTrmsFlNm(trmsFlNm);
				handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
			}
		}
		return trmsFlNm;
	}

	/**
	 * @개요 : 경영실적 처리 배치 실행.
	 * 
	 * @param trmsFlNm
	 *            - 배치 실행 파일 이름.
	 * @return void
	 * 
	 */
	@Override
	@Async
	public void runBatch(String trmsFlNm) throws Exception {
		String result = Utils.runBatch("BPM000015", trmsFlNm);
		log.info("runBatch : {}", result);
	}

	/**
	 * @개요 : 조직도 정보 조회.
	 * 
	 * @param
	 * @return List<OrgUser> 조직도 정보.
	 * 
	 */
	@Override
	public List<OrgUser> getOrgTree() throws Exception {
		List<OrgUser> orgUsers = orgUserRepository.getOrgTree();
		String key, title;
		OrgUser orgUserVo;
		OrgUser tree;
		boolean expand = true;
		List<OrgUser> trees = Lists.newArrayList();
		for (OrgUser orgUser : orgUsers) {
			if (StringUtils.isEmpty(orgUser.getLoginId())) {
				key = orgUser.getOrgCd();
				title = orgUser.getOrgNm();
			} else {
				key = orgUser.getLoginId();
				title = orgUser.getUserNm();
			}
			orgUserVo = new OrgUser();
			orgUserVo.setFolder(true);
			orgUserVo.setTitle(title);
			orgUserVo.setKey(key);
			orgUserVo.setExpand(expand);
			orgUserVo.setOrgCd(orgUser.getOrgCd());
			orgUserVo.setOrgNm(orgUser.getOrgNm());
			orgUserVo.setLoginId(StringUtils.EMPTY);
			orgUserVo.setUserNm(StringUtils.EMPTY);

			if (StringUtils.equals("1", orgUser.getLvl())) {
				trees.add(orgUserVo);
				expand = false;
			} else {
				int treeSize = trees.size();
				for (int j = 0; j < treeSize; j++) {
					tree = trees.get(j);
					if (StringUtils.equals(orgUser.getSupOrgCd(), tree.getKey())) {
						tree.getChildren().add(orgUserVo);
						trees.add(orgUserVo);
					}
				}
			}
		}
		int treesSize = trees.size();
		for (int i = treesSize - 1; i > 0; i--) {
			trees.remove(i);
		}

		return trees;
	}

	/**
	 * @개요 : 해당 조직에 소속된 직원 정보 조회.
	 * 
	 * @param orgUser
	 *            조직 정보.
	 * @return List<OrgUser> 직원 정보.
	 * 
	 */
	@Override
	public List<OrgUser> getOrgUserTree(OrgUser orgUser) throws Exception {
		return orgUserRepository.getOrgUserTree(orgUser);
	}

	/**
	 * @개요 : 사용자 이름으로 조직 및 사용자 정보 조회.
	 *
	 * @param orgUser
	 *            사용자 이름.
	 * @return List<OrgUser> 직원 정보.
	 *
	 */
	@Override
	public List<OrgUser> getOrgUsers(OrgUser orgUser) throws Exception {
		return orgUserRepository.getOrgUsers(orgUser);
	}

	/**
	 * @개요 : 경영실적 이메일 수신자 정보 조회.
	 * 
	 * @param sndObjId
	 *            메일 발송 종류(경영실적).
	 * @return List<OrgUser> 수신자 리스트.
	 * 
	 */
	@Override
	public List<OrgUser> getEmailOrgUser(Long sndObjId) throws Exception {
		return orgUserRepository.getEmailOrgUser(sndObjId);
	}

	/**
	 * @개요 : 발송 메일에 들어갈 코멘트 정보 저장.
	 * 
	 * @param bpmMgntRsltScrnCmnt
	 *            서비스별 코멘트 정보.
	 * @return void.
	 * 
	 */
	@Override
	@Transactional
	public void createBpmMgntRsltScrnCmnt(BpmMgntRsltScrnCmnt bpmMgntRsltScrnCmnt) throws Exception {
		// OCB Comment insert
		if (StringUtils.isNotEmpty(bpmMgntRsltScrnCmnt.getOcbComment())) {
			bpmMgntRsltScrnCmnt.setCmntCtt(bpmMgntRsltScrnCmnt.getOcbComment());
			bpmMgntRsltScrnCmnt.setMgntRsltScrnClCd(Constants.SVC_OCB);
			orgUserRepository.createBpmMgntRsltScrnCmnt(bpmMgntRsltScrnCmnt);
		}
		// Syrup Comment insert
		if (StringUtils.isNotEmpty(bpmMgntRsltScrnCmnt.getSyrupComment())) {
			bpmMgntRsltScrnCmnt.setCmntCtt(bpmMgntRsltScrnCmnt.getSyrupComment());
			bpmMgntRsltScrnCmnt.setMgntRsltScrnClCd(Constants.SVC_SYRUP);
			orgUserRepository.createBpmMgntRsltScrnCmnt(bpmMgntRsltScrnCmnt);
		}
		// Tstore
		if (StringUtils.isNotEmpty(bpmMgntRsltScrnCmnt.getTstoreComment())) {
			bpmMgntRsltScrnCmnt.setCmntCtt(bpmMgntRsltScrnCmnt.getTstoreComment());
			bpmMgntRsltScrnCmnt.setMgntRsltScrnClCd(Constants.SVC_TSTORE);
			orgUserRepository.createBpmMgntRsltScrnCmnt(bpmMgntRsltScrnCmnt);
		}
		// Hoppin
		if (StringUtils.isNotEmpty(bpmMgntRsltScrnCmnt.getHoppinComment())) {
			bpmMgntRsltScrnCmnt.setCmntCtt(bpmMgntRsltScrnCmnt.getHoppinComment());
			bpmMgntRsltScrnCmnt.setMgntRsltScrnClCd(Constants.SVC_HOPPIN);
			orgUserRepository.createBpmMgntRsltScrnCmnt(bpmMgntRsltScrnCmnt);
		}
		// Tmap
		if (StringUtils.isNotEmpty(bpmMgntRsltScrnCmnt.getTmapComment())) {
			bpmMgntRsltScrnCmnt.setCmntCtt(bpmMgntRsltScrnCmnt.getTmapComment());
			bpmMgntRsltScrnCmnt.setMgntRsltScrnClCd(Constants.SVC_TMAP);
			orgUserRepository.createBpmMgntRsltScrnCmnt(bpmMgntRsltScrnCmnt);
		}
		// Sk11
		if (StringUtils.isNotEmpty(bpmMgntRsltScrnCmnt.getSk11Comment())) {
			bpmMgntRsltScrnCmnt.setCmntCtt(bpmMgntRsltScrnCmnt.getSk11Comment());
			bpmMgntRsltScrnCmnt.setMgntRsltScrnClCd(Constants.SVC_SK11);
			orgUserRepository.createBpmMgntRsltScrnCmnt(bpmMgntRsltScrnCmnt);
		}
		// TCloud
		if (StringUtils.isNotEmpty(bpmMgntRsltScrnCmnt.getTcloudComment())) {
			bpmMgntRsltScrnCmnt.setCmntCtt(bpmMgntRsltScrnCmnt.getTcloudComment());
			bpmMgntRsltScrnCmnt.setMgntRsltScrnClCd(Constants.SVC_TCLOUD);
			orgUserRepository.createBpmMgntRsltScrnCmnt(bpmMgntRsltScrnCmnt);
		}
	}

	/**
	 * KID용 경영실적 이메일 지표 추출.
	 *
	 * @param jqGridRequest
	 * @return MailReports
	 * @throws Exception
	 */
	@Override
	public MailReports getBusinessDatasForKid(JqGridRequest jqGridRequest) throws Exception {
		MailReports mailReports = new MailReports();
		Map<String, List<MailReport>> mailReportMaps = Maps.newHashMap();
		jqGridRequest.setM1Day(DateUtil.addMonths(jqGridRequest.getBasicDate(), -1).substring(0, 6) + jqGridRequest.getBasicDate().substring(6));
		jqGridRequest.setM2Day(DateUtil.addMonths(jqGridRequest.getBasicDate(), -2).substring(0, 6) + jqGridRequest.getBasicDate()
						.substring(6));
		jqGridRequest.setM3Day(DateUtil.addMonths(jqGridRequest.getBasicDate(), -3).substring(0, 6) + jqGridRequest.getBasicDate()
						.substring(6));
		jqGridRequest.setD30StartDay(DateUtil.addDays(jqGridRequest.getBasicDate(), -30));// 30일 시작일자.
		jqGridRequest.setStartDate(jqGridRequest.getBasicDate().substring(0, 6) + "01");// 당월 초일자.
		MailEwma mailEwma = this.getIncreaseValueForKid(jqGridRequest);
		// OCB, Syrup 경영실적지표.
		getOcbSyrupReport(jqGridRequest, mailEwma, mailReportMaps);
		// OCB, Syrup 외 경영실적지표.
		List<BpmDlyPrst> etcs = orgUserRepository.getBusinessDatas(jqGridRequest);
		// 11번가 경영실적지표.
		getSk11Report(jqGridRequest, etcs, mailEwma, mailReportMaps);
		// Tstore
		getTstoreReport(jqGridRequest, etcs, mailEwma, mailReportMaps);
		// Tmap
		getTmapReport(jqGridRequest, etcs, mailEwma, mailReportMaps);
		// Tcloud
		getTcloudReport(jqGridRequest, etcs, mailEwma, mailReportMaps);
		// Hoppin
		getHoppinReport(jqGridRequest, etcs, mailEwma, mailReportMaps);
		mailReports.setReports(mailReportMaps);

		List<BpmMgntRsltScrnCmnt> bpmMgntRsltScrnCmnts = orgUserRepository.getBpmMgntRsltScrnCmnts(jqGridRequest);
		for (BpmMgntRsltScrnCmnt bpmMgntRsltScrnCmnt : bpmMgntRsltScrnCmnts) {
			if (StringUtils.equals(Constants.SVC_HOPPIN, bpmMgntRsltScrnCmnt.getMgntRsltScrnClCd()))
				mailReports.setHoppinComment(bpmMgntRsltScrnCmnt.getCmntCtt());
			else if (StringUtils.equals(Constants.SVC_OCB, bpmMgntRsltScrnCmnt.getMgntRsltScrnClCd()))
				mailReports.setOcbComment(bpmMgntRsltScrnCmnt.getCmntCtt());
			else if (StringUtils.equals(Constants.SVC_SK11, bpmMgntRsltScrnCmnt.getMgntRsltScrnClCd()))
				mailReports.setSk11Comment(bpmMgntRsltScrnCmnt.getCmntCtt());
			else if (StringUtils.equals(Constants.SVC_SYRUP, bpmMgntRsltScrnCmnt.getMgntRsltScrnClCd()))
				mailReports.setSyrupComment(bpmMgntRsltScrnCmnt.getCmntCtt());
			else if (StringUtils.equals(Constants.SVC_TMAP, bpmMgntRsltScrnCmnt.getMgntRsltScrnClCd()))
				mailReports.setTmapComment(bpmMgntRsltScrnCmnt.getCmntCtt());
			else if (StringUtils.equals(Constants.SVC_TSTORE, bpmMgntRsltScrnCmnt.getMgntRsltScrnClCd()))
				mailReports.setTstoreComment(bpmMgntRsltScrnCmnt.getCmntCtt());
			else if (StringUtils.equals(Constants.SVC_TCLOUD, bpmMgntRsltScrnCmnt.getMgntRsltScrnClCd()))
				mailReports.setTcloudComment(bpmMgntRsltScrnCmnt.getCmntCtt());
		}

		return mailReports;
	}

	/**
	 * @개요 : 발송 메일에 들어갈 증감률 정보 조회(KID).
	 *
	 * @param jqGridRequest 발송일자 정보.
	 * @return MailEwma EWMA 정보.
	 *
	 */
	private MailEwma getIncreaseValueForKid(JqGridRequest jqGridRequest) throws Exception {
		MailEwma mailEwma = new MailEwma();
		List<DaaEwmaStatDaily> daaEwmaStatDailys = daaEwmaStatDailyRepository.getBossEwmaDataForKid(jqGridRequest);
		for (DaaEwmaStatDaily daaEwmaStatDaily : daaEwmaStatDailys) {
			if (NumberUtil.isEmpty(daaEwmaStatDaily.getLclValue()))
				continue;
			if (StringUtils.equals(daaEwmaStatDaily.getBossSvcCd(), "25")) {// OCB
				if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10020")) {// DAU
					mailEwma.setOcbDauEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setOcbDauLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setOcbDauUcl(daaEwmaStatDaily.getUclValue());
				} else if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10021")) { // 월누적 DAU
					mailEwma.setOcbMauEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setOcbMauLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setOcbMauUcl(daaEwmaStatDaily.getUclValue());
				} else if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10022")) { // TR
					mailEwma.setOcbTrEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setOcbTrLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setOcbTrUcl(daaEwmaStatDaily.getUclValue());
				} else { // 월누적 TR
					mailEwma.setOcbMtrEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setOcbMtrLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setOcbMtrUcl(daaEwmaStatDaily.getUclValue());
				}
			} else if (StringUtils.equals(daaEwmaStatDaily.getBossSvcCd(), "8")) {// Syrup
				if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10020")) {// DAU
					mailEwma.setSyrupDauEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setSyrupDauLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setSyrupDauUcl(daaEwmaStatDaily.getUclValue());
				} else if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10021")) { // 월누적 DAU
					mailEwma.setSyrupMauEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setSyrupMauLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setSyrupMauUcl(daaEwmaStatDaily.getUclValue());
				} else if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10022")) { // TR
					mailEwma.setSyrupTrEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setSyrupTrLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setSyrupTrUcl(daaEwmaStatDaily.getUclValue());
				} else { // 월누적 TR
					mailEwma.setSyrupMtrEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setSyrupMtrLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setSyrupMtrUcl(daaEwmaStatDaily.getUclValue());
				}
			} else if (StringUtils.equals(daaEwmaStatDaily.getBossSvcCd(), "1")) { // 11번
				if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10024")) {// 구매확정액
					mailEwma.setSk11GmvEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setSk11GmvLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setSk11GmvUcl(daaEwmaStatDaily.getUclValue());
				} else if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10025")) { // 월누적 구매확정액
					mailEwma.setSk11MgmvEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setSk11MgmvLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setSk11MgmvUcl(daaEwmaStatDaily.getUclValue());
				} else if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10026")) { // Mobile 구매확정액
					mailEwma.setSk11MobileGmvEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setSk11MobileGmvLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setSk11MobileGmvUcl(daaEwmaStatDaily.getUclValue());
				} else if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10027")) { // 월누적 Mobile 구매확정액
					mailEwma.setSk11MobileMgmvEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setSk11MobileMgmvLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setSk11MobileMgmvUcl(daaEwmaStatDaily.getUclValue());
				} else if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10028")) { // Mobile Traffic
					mailEwma.setSk11MobileLvEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setSk11MobileLvLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setSk11MobileLvUcl(daaEwmaStatDaily.getUclValue());
				} else {// 월누적 Mobile Traffic
					mailEwma.setSk11MobileMlvEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setSk11MobileMlvLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setSk11MobileMlvUcl(daaEwmaStatDaily.getUclValue());
				}
			} else if (StringUtils.equals(daaEwmaStatDaily.getBossSvcCd(), "1")) {// Tstore
				if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10030")) {// 유료아용자
					mailEwma.setTstorePayUserEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setTstorePayUserLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setTstorePayUserUcl(daaEwmaStatDaily.getUclValue());
				} else if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10031")) { // 월누적 유료아용자
					mailEwma.setTstoreMpayUserEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setTstoreMpayUserLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setTstoreMpayUserUcl(daaEwmaStatDaily.getUclValue());
				} else if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10032")) { // 거래액
					mailEwma.setTstoreTransactAmountEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setTstoreTransactAmountLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setTstoreTransactAmountUcl(daaEwmaStatDaily.getUclValue());
				} else {// 월누적 거래액
					mailEwma.setTstoreMtransactAmountEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setTstoreMtransactAmountLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setTstoreMtransactAmountUcl(daaEwmaStatDaily.getUclValue());
				}
			} else if (StringUtils.equals(daaEwmaStatDaily.getBossSvcCd(), "4")) {// Tmap
				if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10020")) {// DAU
					mailEwma.setTmapDauEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setTmapDauLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setTmapDauUcl(daaEwmaStatDaily.getUclValue());
				} else { // 월누적 DAU
					mailEwma.setTmapMauEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setTmapMauLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setTmapMauUcl(daaEwmaStatDaily.getUclValue());
				}
			} else if (StringUtils.equals(daaEwmaStatDaily.getBossSvcCd(), "7")) {// Tcloud
				if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10008")) {// UV
					mailEwma.setTcloudUvEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setTcloudUvLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setTcloudUvUcl(daaEwmaStatDaily.getUclValue());
				} else {// 월누적 UV
					mailEwma.setTcloudMuvEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setTcloudMuvLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setTcloudMuvUcl(daaEwmaStatDaily.getUclValue());
				}
			} else if (StringUtils.equals(daaEwmaStatDaily.getBossSvcCd(), "6")) {// Hoppin
				if (StringUtils.equals(daaEwmaStatDaily.getKpiId(), "10035")) {// 콘텐츠이용건수
					mailEwma.setHoppinContentsUserEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setHoppinContentsUserLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setHoppinContentsUserUcl(daaEwmaStatDaily.getUclValue());
				} else { // 월누적 콘텐츠이용건수
					mailEwma.setHoppinMcontentsUserEwma(daaEwmaStatDaily.getEwmaValue());
					mailEwma.setHoppinMcontentsUserLcl(daaEwmaStatDaily.getLclValue());
					mailEwma.setHoppinMcontentsUserUcl(daaEwmaStatDaily.getUclValue());
				}
			}
		}
		return mailEwma;
	}

	/**
	 * OCB/Syrup 경영실적 메일 지표 추출.
	 *
	 * @param jqGridRequest
	 * @return
	 * @throws Exception
	 */
	private void getOcbSyrupReport(final JqGridRequest jqGridRequest, MailEwma mailEwma,
			Map<String, List<MailReport>> mailReportMaps) throws Exception {
		List<BpmDlyPrst> ocbSyrups = orgUserRepository.getBusinessOcbSyrupDatas(jqGridRequest);
		if (CollectionUtils.isNotEmpty(ocbSyrups)) {
			MailReport ocbDau = new MailReport();
			boolean ocbDauBasicFlag = (!NumberUtil.isEmpty(ocbSyrups.get(0).getDlyRsltVal()) && ocbSyrups.get(0).getSvcId() == 25
					&& StringUtils.equals("L091", ocbSyrups.get(0).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), ocbSyrups.get(0).getDlyStrdDt()));
			if (ocbDauBasicFlag) {
				ocbDau.setBasicValue(ocbSyrups.get(0).getDlyRsltVal());
				ocbDau.setMilliBasicValue(ocbDau.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> ocbDaus = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return ((Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) >= Integer.parseInt(jqGridRequest
								.getD30StartDay())) && (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate())) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 25);
					}
				});
				double ocbDauSum = sum(ocbDaus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal ocbDauAverage = BigDecimal.valueOf(ocbDauSum).divide(
						new BigDecimal(Integer.toString(ocbDaus.size())), Constants.ROUNDING_MODE);
				ocbDau.setIncreaseValue(NumberUtil.calculateGrowth(ocbDau.getBasicValue().doubleValue(),
						ocbDauAverage.doubleValue()));
				//
				Map<String, String> ocbDauEwmaUrl = Utils.getMailEwmaImage(mailEwma.getOcbDauEwma(),
						mailEwma.getOcbDauLcl(), mailEwma.getOcbDauUcl(), ocbDau.getIncreaseValue());
				ocbDau.setBrandImageUrl(ocbDauEwmaUrl.get(Constants.BRAND_URL));
				ocbDau.setDimensionImageUrl(ocbDauEwmaUrl.get(Constants.DIMENSION_URL));
				ocbDau.setIncreaseColor(Utils.getIncreaseFontColor(ocbDau.getIncreaseValue()));
			}
			ocbDau.setSvcId(25);
			ocbDau.setIdxClGrpCd("L091");
			ocbDau.setDimension("DAU");
			ocbDau.setPeriod("일누적");
			ocbDau.setDimensionUnit("천명");

			final int m0StartDay =  Integer.parseInt(jqGridRequest.getBasicDate().substring(0, 6) + "01");
			final int m1StartDay =  Integer.parseInt(jqGridRequest.getM1Day().substring(0, 6) + "01");
			final int m2StartDay =  Integer.parseInt(jqGridRequest.getM2Day().substring(0, 6) + "01");
			final int m3StartDay =  Integer.parseInt(jqGridRequest.getM3Day().substring(0, 6) + "01");
			MailReport ocbMau = new MailReport();
			if (ocbDauBasicFlag) {
				List<BpmDlyPrst> ocbM1Maus = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return ((Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) >= m1StartDay)
								&& (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) <= Integer.parseInt(jqGridRequest
								.getM1Day())) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 25);
					}
				});
				double ocbM1MauSum = sum(ocbM1Maus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				List<BpmDlyPrst> ocbM2Maus = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return ((Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) >= m2StartDay)
								&& (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) <= Integer.parseInt(jqGridRequest
								.getM2Day())) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 25);
					}
				});
				double ocbM2MauSum = sum(ocbM2Maus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				List<BpmDlyPrst> ocbM3Maus = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return ((Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) >= m3StartDay)
								&& (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) <= Integer.parseInt(jqGridRequest
								.getM3Day())) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 25);
					}
				});
				double ocbM3MauSum = sum(ocbM3Maus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				List<BpmDlyPrst> ocbM0Maus = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return ((Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) >= m0StartDay)
								&& (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) <= Integer.parseInt(jqGridRequest
								.getBasicDate())) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd())
								&& bpmDlyPrst
								.getSvcId() == 25);
					}
				});
				if (CollectionUtils.isNotEmpty(ocbM0Maus)) {
					double ocbM0MauSum = sum(ocbM0Maus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
					BigDecimal ocbM0MauAverage = BigDecimal.valueOf(ocbM0MauSum).divide(
							new BigDecimal(Integer.toString(ocbM0Maus.size())), Constants.ROUNDING_MODE);
					ocbMau.setBasicValue(ocbM0MauAverage);
					ocbMau.setMilliBasicValue(ocbMau.getBasicValue().divide(new BigDecimal("1000"), 0,
							RoundingMode.HALF_UP));
					BigDecimal ocbM1MauAverage = BigDecimal.valueOf(ocbM1MauSum).divide(
							new BigDecimal(Integer.toString(ocbM1Maus.size())), Constants.ROUNDING_MODE);
					BigDecimal ocbM2MauAverage = BigDecimal.valueOf(ocbM2MauSum).divide(
							new BigDecimal(Integer.toString(ocbM2Maus.size())), Constants.ROUNDING_MODE);
					BigDecimal ocbM3MauAverage = BigDecimal.valueOf(ocbM3MauSum).divide(
							new BigDecimal(Integer.toString(ocbM3Maus.size())), Constants.ROUNDING_MODE);
					BigDecimal ocbMauAverageSum = ocbM1MauAverage.add(ocbM2MauAverage);
					ocbMauAverageSum = ocbMauAverageSum.add(ocbM3MauAverage);
					//log.info("ocb mAverage {}, {}, {}, {}", ocbM0MauAverage, ocbM1MauAverage, ocbM2MauAverage, ocbM3MauAverage);
					BigDecimal ocb123MauAverage = ocbMauAverageSum.divide(new BigDecimal(3), Constants.ROUNDING_MODE);
					ocbMau.setIncreaseValue(NumberUtil.calculateGrowth(ocbMau.getBasicValue().doubleValue(),
							ocb123MauAverage.doubleValue()));

					Map<String, String> ocbMauEwmaUrl = Utils.getMailEwmaImage(mailEwma.getOcbMauEwma(),
							mailEwma.getOcbMauLcl(), mailEwma.getOcbMauUcl(), ocbMau.getIncreaseValue());
					ocbMau.setBrandImageUrl(ocbMauEwmaUrl.get(Constants.BRAND_URL));
					ocbMau.setDimensionImageUrl(ocbMauEwmaUrl.get(Constants.DIMENSION_URL));
					ocbMau.setIncreaseColor(Utils.getIncreaseFontColor(ocbMau.getIncreaseValue()));
				}
			}
			ocbMau.setSvcId(25);
			ocbMau.setIdxClGrpCd("L110");
			ocbMau.setDimension("DAU");
			ocbMau.setPeriod("월평균");
			ocbMau.setDimensionUnit("천명");
			//
			MailReport ocbTr = new MailReport();
			if (!NumberUtil.isEmpty(ocbSyrups.get(1).getDlyRsltVal()) && ocbSyrups.get(1).getSvcId() == 25
					&& StringUtils.equals("L092", ocbSyrups.get(1).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), ocbSyrups.get(1).getDlyStrdDt())) {
				ocbTr.setBasicValue(ocbSyrups.get(1).getDlyRsltVal());
				ocbTr.setMilliBasicValue(ocbTr.getBasicValue()
						.divide(new BigDecimal("1000"), 0, RoundingMode.HALF_UP));
				List<BpmDlyPrst> ocbTrs = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L092", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 25);
					}
				});
				double ocbTrSum = sum(ocbTrs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal ocbTrAverage = BigDecimal.valueOf(ocbTrSum).divide(
						new BigDecimal(Integer.toString(ocbTrs.size())), Constants.ROUNDING_MODE);
				ocbTr.setIncreaseValue(NumberUtil.calculateGrowth(ocbTr.getBasicValue().doubleValue(),
						ocbTrAverage.doubleValue()));
				//
				Map<String, String> ocbTrEwmaUrl = Utils.getMailEwmaImage(mailEwma.getOcbTrEwma(),
						mailEwma.getOcbTrLcl(), mailEwma.getOcbTrUcl(), ocbTr.getIncreaseValue());
				ocbTr.setBrandImageUrl(ocbTrEwmaUrl.get(Constants.BRAND_URL));
				ocbTr.setDimensionImageUrl(ocbTrEwmaUrl.get(Constants.DIMENSION_URL));
				ocbTr.setIncreaseColor(Utils.getIncreaseFontColor(ocbTr.getIncreaseValue()));
			}
			ocbTr.setSvcId(25);
			ocbTr.setIdxClGrpCd("L092");
			ocbTr.setDimension("TR");
			ocbTr.setPeriod("일누적");
			ocbTr.setDimensionUnit("천건");

			MailReport ocbMtr = new MailReport();
			if (!NumberUtil.isEmpty(ocbSyrups.get(2).getDlyRsltVal()) && ocbSyrups.get(2).getSvcId() == 25
					&& StringUtils.equals("L093", ocbSyrups.get(2).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), ocbSyrups.get(2).getDlyStrdDt())) {
				ocbMtr.setBasicValue(ocbSyrups.get(2).getDlyRsltVal());
				ocbMtr.setMilliBasicValue(ocbMtr.getBasicValue()
						.divide(new BigDecimal("1000"), 0, RoundingMode.HALF_UP));
				List<BpmDlyPrst> ocbMtrs = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L093", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 25);
					}
				});
				double ocbMtrSum = sum(ocbMtrs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal ocbMtrAverage = BigDecimal.valueOf(ocbMtrSum).divide(
						new BigDecimal(Integer.toString(ocbMtrs.size())), Constants.ROUNDING_MODE);
				ocbMtr.setIncreaseValue(NumberUtil.calculateGrowth(ocbMtr.getBasicValue().doubleValue(),
						ocbMtrAverage.doubleValue()));
				//
				Map<String, String> ocbMtrEwmaUrl = Utils.getMailEwmaImage(mailEwma.getOcbMtrEwma(),
						mailEwma.getOcbMtrLcl(), mailEwma.getOcbMtrUcl(), ocbMtr.getIncreaseValue());
				ocbMtr.setBrandImageUrl(ocbMtrEwmaUrl.get(Constants.BRAND_URL));
				ocbMtr.setDimensionImageUrl(ocbMtrEwmaUrl.get(Constants.DIMENSION_URL));
				ocbMtr.setIncreaseColor(Utils.getIncreaseFontColor(ocbMtr.getIncreaseValue()));
			}
			ocbMtr.setSvcId(25);
			ocbMtr.setIdxClGrpCd("L093");
			ocbMtr.setDimension("TR");
			ocbMtr.setPeriod("당월누적");
			ocbMtr.setDimensionUnit("천건");

			List<MailReport> ocbs = Arrays.asList(ocbDau, ocbMau, ocbTr, ocbMtr);
			mailReportMaps.put(Constants.SVC_OCB, ocbs);

			MailReport syrupDau = new MailReport();
			boolean syrupDauFlag = (!NumberUtil.isEmpty(ocbSyrups.get(3).getDlyRsltVal()) && ocbSyrups.get(3).getSvcId() == 8
					&& StringUtils.equals("L091", ocbSyrups.get(3).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), ocbSyrups.get(3).getDlyStrdDt()));
			if (syrupDauFlag) {
				syrupDau.setBasicValue(ocbSyrups.get(3).getDlyRsltVal());
				syrupDau.setMilliBasicValue(syrupDau.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> syrupDaus = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return ((Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) >= Integer.parseInt(jqGridRequest
								.getD30StartDay())) && (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate())) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 8);
					}
				});
				double syrupDauSum = sum(syrupDaus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal syrupDauAverage = BigDecimal.valueOf(syrupDauSum).divide(
						new BigDecimal(Integer.toString(syrupDaus.size())), Constants.ROUNDING_MODE);
				syrupDau.setIncreaseValue(NumberUtil.calculateGrowth(syrupDau.getBasicValue().doubleValue(),
						syrupDauAverage.doubleValue()));
				//
				Map<String, String> syrupDauEwmaUrl = Utils.getMailEwmaImage(mailEwma.getSyrupDauEwma(),
						mailEwma.getSyrupDauLcl(), mailEwma.getSyrupDauUcl(),
						syrupDau.getIncreaseValue());
				syrupDau.setBrandImageUrl(syrupDauEwmaUrl.get(Constants.BRAND_URL));
				syrupDau.setDimensionImageUrl(syrupDauEwmaUrl.get(Constants.DIMENSION_URL));
				syrupDau.setIncreaseColor(Utils.getIncreaseFontColor(syrupDau.getIncreaseValue()));
			}
			syrupDau.setSvcId(8);
			syrupDau.setIdxClGrpCd("L091");
			syrupDau.setDimension("DAU");
			syrupDau.setPeriod("일누적");
			syrupDau.setDimensionUnit("천명");

			MailReport syrupMau = new MailReport();
			if (syrupDauFlag) {
				List<BpmDlyPrst> syrupM1Maus = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return ((Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) >= m1StartDay)
								&& (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) <= Integer.parseInt(jqGridRequest
								.getM1Day())) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 8);
					}
				});
				double syrupM1MauSum = sum(syrupM1Maus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				List<BpmDlyPrst> syrupM2Maus = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return ((Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) >= m2StartDay)
								&& (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) <= Integer.parseInt(jqGridRequest
								.getM2Day())) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 8);
					}
				});
				double syrupM2MauSum = sum(syrupM2Maus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				List<BpmDlyPrst> syrupM3Maus = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return ((Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) >= m3StartDay)
								&& (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) <= Integer.parseInt(jqGridRequest
								.getM3Day())) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 8);
					}
				});
				double syrupM3MauSum = sum(syrupM3Maus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				List<BpmDlyPrst> syrupM0Maus = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return ((Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) >= m0StartDay)
								&& (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) <= Integer.parseInt(jqGridRequest
								.getBasicDate())) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd())
								&& bpmDlyPrst
								.getSvcId() == 8);
					}
				});
				if (CollectionUtils.isNotEmpty(syrupM0Maus)) {
					double syrupM0MauSum = sum(syrupM0Maus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
					BigDecimal syrupM0MauAverage = BigDecimal.valueOf(syrupM0MauSum).divide(
							new BigDecimal(Integer.toString(syrupM0Maus.size())), Constants.ROUNDING_MODE);
					syrupMau.setBasicValue(syrupM0MauAverage);
					syrupMau.setMilliBasicValue(syrupMau.getBasicValue().divide(new BigDecimal("1000"), 0,
							RoundingMode.HALF_UP));
					BigDecimal syrupM1MauAverage = BigDecimal.valueOf(syrupM1MauSum).divide(
							new BigDecimal(Integer.toString(syrupM1Maus.size())), Constants.ROUNDING_MODE);
					BigDecimal syrupM2MauAverage = BigDecimal.valueOf(syrupM2MauSum).divide(
							new BigDecimal(Integer.toString(syrupM2Maus.size())), Constants.ROUNDING_MODE);
					BigDecimal syrupM3MauAverage = BigDecimal.valueOf(syrupM3MauSum).divide(
							new BigDecimal(Integer.toString(syrupM3Maus.size())), Constants.ROUNDING_MODE);
					//log.info("syrup mAverage {}, {}, {}, {}", syrupM0MauAverage, syrupM1MauAverage, syrupM2MauAverage, syrupM3MauAverage);
					BigDecimal syrupMauAverageSum = syrupM1MauAverage.add(syrupM2MauAverage);
					syrupMauAverageSum = syrupMauAverageSum.add(syrupM3MauAverage);
					BigDecimal syrup123MauAverage = syrupMauAverageSum
							.divide(new BigDecimal(3), Constants.ROUNDING_MODE);
					syrupMau.setIncreaseValue(NumberUtil.calculateGrowth(syrupMau.getBasicValue().doubleValue(),
							syrup123MauAverage.doubleValue()));
					Map<String, String> syrupMauEwmaUrl = Utils.getMailEwmaImage(mailEwma.getSyrupMauEwma(),
							mailEwma.getSyrupMauLcl(), mailEwma.getSyrupMauUcl(), syrupMau.getIncreaseValue());
					syrupMau.setBrandImageUrl(syrupMauEwmaUrl.get(Constants.BRAND_URL));
					syrupMau.setDimensionImageUrl(syrupMauEwmaUrl.get(Constants.DIMENSION_URL));
					syrupMau.setIncreaseColor(Utils.getIncreaseFontColor(syrupMau.getIncreaseValue()));
				}
			}
			syrupMau.setSvcId(8);
			syrupMau.setIdxClGrpCd("L110");
			syrupMau.setDimension("DAU");
			syrupMau.setPeriod("월평균");
			syrupMau.setDimensionUnit("천명");

			MailReport syrupTr = new MailReport();
			if (!NumberUtil.isEmpty(ocbSyrups.get(4).getDlyRsltVal()) && ocbSyrups.get(4).getSvcId() == 8
					&& StringUtils.equals("L092", ocbSyrups.get(4).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), ocbSyrups.get(4).getDlyStrdDt())) {
				syrupTr.setBasicValue(ocbSyrups.get(4).getDlyRsltVal());
				syrupTr.setMilliBasicValue(syrupTr.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> syrupTrs = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L092", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 8);
					}
				});
				double syrupTrSum = sum(syrupTrs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal syrupTrAverage = BigDecimal.valueOf(syrupTrSum).divide(
						new BigDecimal(Integer.toString(syrupTrs.size())), Constants.ROUNDING_MODE);
				syrupTr.setIncreaseValue(NumberUtil.calculateGrowth(syrupTr.getBasicValue().doubleValue(),
						syrupTrAverage.doubleValue()));
				//
				Map<String, String> syrupTrEwmaUrl = Utils.getMailEwmaImage(mailEwma.getSyrupTrEwma(),
						mailEwma.getSyrupTrLcl(), mailEwma.getSyrupTrUcl(),
						syrupTr.getIncreaseValue());
				syrupTr.setBrandImageUrl(syrupTrEwmaUrl.get(Constants.BRAND_URL));
				syrupTr.setDimensionImageUrl(syrupTrEwmaUrl.get(Constants.DIMENSION_URL));
				syrupTr.setIncreaseColor(Utils.getIncreaseFontColor(syrupTr.getIncreaseValue()));
			}
			syrupTr.setSvcId(8);
			syrupTr.setIdxClGrpCd("L092");
			syrupTr.setDimension("TR");
			syrupTr.setPeriod("일누적");
			syrupTr.setDimensionUnit("천건");

			MailReport syrupMtr = new MailReport();
			if (!NumberUtil.isEmpty(ocbSyrups.get(5).getDlyRsltVal()) && ocbSyrups.get(5).getSvcId() == 8
					&& StringUtils.equals("L093", ocbSyrups.get(5).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), ocbSyrups.get(5).getDlyStrdDt())) {
				syrupMtr.setBasicValue(ocbSyrups.get(5).getDlyRsltVal());
				syrupMtr.setMilliBasicValue(syrupMtr.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> syrupMtrs = with(ocbSyrups).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L093", bpmDlyPrst.getIdxClGrpCd()) && bpmDlyPrst
								.getSvcId() == 8);
					}
				});
				double syrupMtrSum = sum(syrupMtrs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal syrupMtrAverage = BigDecimal.valueOf(syrupMtrSum).divide(
						new BigDecimal(Integer.toString(syrupMtrs.size())), Constants.ROUNDING_MODE);
				syrupMtr.setIncreaseValue(NumberUtil.calculateGrowth(syrupMtr.getBasicValue().doubleValue(),
						syrupMtrAverage.doubleValue()));
				//
				Map<String, String> syrupMtrEwmaUrl = Utils.getMailEwmaImage(mailEwma.getSyrupMtrEwma(),
						mailEwma.getSyrupMtrLcl(), mailEwma.getSyrupMtrUcl(),
						syrupMtr.getIncreaseValue());
				syrupMtr.setBrandImageUrl(syrupMtrEwmaUrl.get(Constants.BRAND_URL));
				syrupMtr.setDimensionImageUrl(syrupMtrEwmaUrl.get(Constants.DIMENSION_URL));
				syrupMtr.setIncreaseColor(Utils.getIncreaseFontColor(syrupMtr.getIncreaseValue()));
			}
			syrupMtr.setSvcId(8);
			syrupMtr.setIdxClGrpCd("L093");
			syrupMtr.setDimension("TR");
			syrupMtr.setPeriod("당월누적");
			syrupMtr.setDimensionUnit("천건");
			List<MailReport> syrups = Arrays.asList(syrupDau, syrupMau, syrupTr, syrupMtr);
			mailReportMaps.put(Constants.SVC_SYRUP, syrups);
		}
	}

	/**
	 * Hoppin 경영실적 메일 지표 추출.
	 *
	 * @param jqGridRequest
	 * @return
	 * @throws Exception
	 */
	private void getHoppinReport(final JqGridRequest jqGridRequest, List<BpmDlyPrst> etcs, MailEwma mailEwma,
			Map<String, List<MailReport>> mailReportMaps) throws Exception {
		if (CollectionUtils.isNotEmpty(etcs)) {
			List<BpmDlyPrst> hoppins = with(etcs).clone().retain(new Predicate<BpmDlyPrst>() {
				@Override
				public boolean apply(BpmDlyPrst bpmDlyPrst) {
					return bpmDlyPrst.getSvcId() == 6;
				}
			});
			MailReport hoppinContentsUser = new MailReport();
			if (!NumberUtil.isEmpty(hoppins.get(0).getDlyRsltVal())
					&& StringUtils.equals("L108", hoppins.get(0).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), hoppins.get(0).getDlyStrdDt())) {
				hoppinContentsUser.setBasicValue(hoppins.get(0).getDlyRsltVal());
				hoppinContentsUser.setMilliBasicValue(hoppinContentsUser.getBasicValue().divide(new BigDecimal("1000"),
						0, RoundingMode.HALF_UP));
				List<BpmDlyPrst> hoppinContentsUsers = with(hoppins).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L108", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double hoppinContentsUserSum = sum(hoppinContentsUsers, on(BpmDlyPrst.class).getDlyRsltVal()
						.doubleValue());
				BigDecimal hoppinContentsUserAverage = BigDecimal.valueOf(hoppinContentsUserSum).divide(
						new BigDecimal(Integer.toString(hoppinContentsUsers.size())), Constants.ROUNDING_MODE);
				hoppinContentsUser.setIncreaseValue(NumberUtil.calculateGrowth(hoppinContentsUser.getBasicValue()
						.doubleValue(), hoppinContentsUserAverage.doubleValue()));
				//
				Map<String, String> hoppinContentsUserEwmaUrl = Utils.getMailEwmaImage(mailEwma.getHoppinContentsUserEwma(),
						mailEwma.getHoppinContentsUserLcl(), mailEwma.getHoppinContentsUserUcl(),
						hoppinContentsUser.getIncreaseValue());
				hoppinContentsUser.setBrandImageUrl(hoppinContentsUserEwmaUrl.get(Constants.BRAND_URL));
				hoppinContentsUser.setDimensionImageUrl(hoppinContentsUserEwmaUrl.get(Constants.DIMENSION_URL));
				hoppinContentsUser.setIncreaseColor(Utils.getIncreaseFontColor(hoppinContentsUser.getIncreaseValue()));
			}
			hoppinContentsUser.setSvcId(6);
			hoppinContentsUser.setIdxClGrpCd("L108");
			hoppinContentsUser.setDimension("컨텐츠 이용건수");
			hoppinContentsUser.setPeriod("일누적");
			hoppinContentsUser.setDimensionUnit("천건");

			MailReport hoppinMcontentsUser = new MailReport();
			if (!NumberUtil.isEmpty(hoppins.get(1).getDlyRsltVal())
					&& StringUtils.equals("L109", hoppins.get(1).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), hoppins.get(1).getDlyStrdDt())) {
				hoppinMcontentsUser.setBasicValue(hoppins.get(1).getDlyRsltVal());
				hoppinMcontentsUser.setMilliBasicValue(hoppinMcontentsUser.getBasicValue().divide(new BigDecimal("1000"),
						0, RoundingMode.HALF_UP));
			}

			if (!NumberUtil.isEmpty(hoppinMcontentsUser.getBasicValue())) {
				List<BpmDlyPrst> hoppinMcontentsUsers = with(hoppins).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L109", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double hoppinMcontentsUserSum = sum(hoppinMcontentsUsers, on(BpmDlyPrst.class).getDlyRsltVal()
						.doubleValue());
				BigDecimal hoppinMcontentsUserAverage = BigDecimal.valueOf(hoppinMcontentsUserSum).divide(
						new BigDecimal(Integer.toString(hoppinMcontentsUsers.size())), Constants.ROUNDING_MODE);
				hoppinMcontentsUser.setIncreaseValue(NumberUtil.calculateGrowth(hoppinMcontentsUser.getBasicValue()
						.doubleValue(), hoppinMcontentsUserAverage.doubleValue()));
				//
				Map<String, String> hoppinMcontentsUserEwmaUrl = Utils.getMailEwmaImage(mailEwma.getHoppinMcontentsUserEwma(),
						mailEwma.getHoppinMcontentsUserLcl(), mailEwma.getHoppinMcontentsUserUcl(),
						hoppinMcontentsUser.getIncreaseValue());
				hoppinMcontentsUser.setBrandImageUrl(hoppinMcontentsUserEwmaUrl.get(Constants.BRAND_URL));
				hoppinMcontentsUser.setDimensionImageUrl(hoppinMcontentsUserEwmaUrl.get(Constants.DIMENSION_URL));
				hoppinMcontentsUser.setIncreaseColor(Utils.getIncreaseFontColor(hoppinMcontentsUser.getIncreaseValue()));
			}
			hoppinMcontentsUser.setSvcId(6);
			hoppinMcontentsUser.setIdxClGrpCd("L109");
			hoppinMcontentsUser.setDimension("컨텐츠 이용건수");
			hoppinMcontentsUser.setPeriod("당월누적");
			hoppinMcontentsUser.setDimensionUnit("천건");

			List<MailReport> hoppinReports = Arrays.asList(hoppinContentsUser, hoppinMcontentsUser);
			mailReportMaps.put(Constants.SVC_HOPPIN, hoppinReports);
		}
	}

	/**
	 * Tcloud 경영실적 메일 지표 추출.
	 *
	 * @param jqGridRequest
	 * @return
	 * @throws Exception
	 */
	private void getTcloudReport(final JqGridRequest jqGridRequest, List<BpmDlyPrst> etcs, MailEwma mailEwma,
			Map<String, List<MailReport>> mailReportMaps) throws Exception {
		if (CollectionUtils.isNotEmpty(etcs)) {
			List<BpmDlyPrst> tclouds = with(etcs).clone().retain(new Predicate<BpmDlyPrst>() {
				@Override
				public boolean apply(BpmDlyPrst bpmDlyPrst) {
					return bpmDlyPrst.getSvcId() == 7;
				}
			});
			MailReport tcloudUv = new MailReport();
			if (!NumberUtil.isEmpty(tclouds.get(0).getDlyRsltVal())
					&& StringUtils.equals("L017", tclouds.get(0).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), tclouds.get(0).getDlyStrdDt())) {
				tcloudUv.setBasicValue(tclouds.get(0).getDlyRsltVal());
				tcloudUv.setMilliBasicValue(tcloudUv.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> tcloudUvs = with(tclouds).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L017", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double tcloudUvSum = sum(tcloudUvs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal tcloudUvAverage = BigDecimal.valueOf(tcloudUvSum).divide(
						new BigDecimal(Integer.toString(tcloudUvs.size())), Constants.ROUNDING_MODE);
				tcloudUv.setIncreaseValue(NumberUtil.calculateGrowth(tcloudUv.getBasicValue().doubleValue(),
						tcloudUvAverage.doubleValue()));
				//
				Map<String, String> tcloudUvEwmaUrl = Utils.getMailEwmaImage(mailEwma.getTcloudUvEwma(),
						mailEwma.getTcloudUvLcl(), mailEwma.getTcloudUvUcl(),
						tcloudUv.getIncreaseValue());
				tcloudUv.setBrandImageUrl(tcloudUvEwmaUrl.get(Constants.BRAND_URL));
				tcloudUv.setDimensionImageUrl(tcloudUvEwmaUrl.get(Constants.DIMENSION_URL));
				tcloudUv.setIncreaseColor(Utils.getIncreaseFontColor(tcloudUv.getIncreaseValue()));
			}
			tcloudUv.setSvcId(7);
			tcloudUv.setIdxClGrpCd("L017");
			tcloudUv.setDimension("UV");
			tcloudUv.setPeriod("일누적");
			tcloudUv.setDimensionUnit("천명");

			MailReport tcloudMuv = new MailReport();
			if (!NumberUtil.isEmpty(tclouds.get(1).getDlyRsltVal())
					&& StringUtils.equals("L081", tclouds.get(1).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), tclouds.get(1).getDlyStrdDt())) {
				tcloudMuv.setBasicValue(tclouds.get(1).getDlyRsltVal());
				tcloudMuv.setMilliBasicValue(tcloudMuv.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> tcloudMuvs = with(tclouds).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L081", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double tcloudMuvSum = sum(tcloudMuvs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal tcloudMuvAverage = BigDecimal.valueOf(tcloudMuvSum).divide(
						new BigDecimal(Integer.toString(tcloudMuvs.size())), Constants.ROUNDING_MODE);
				tcloudMuv.setIncreaseValue(NumberUtil.calculateGrowth(tcloudMuv.getBasicValue().doubleValue(),
						tcloudMuvAverage.doubleValue()));
				//
				Map<String, String> tcloudMuvEwmaUrl = Utils.getMailEwmaImage(mailEwma.getTcloudMuvEwma(),
						mailEwma.getTcloudMuvLcl(), mailEwma.getTcloudMuvUcl(),
						tcloudMuv.getIncreaseValue());
				tcloudMuv.setBrandImageUrl(tcloudMuvEwmaUrl.get(Constants.BRAND_URL));
				tcloudMuv.setDimensionImageUrl(tcloudMuvEwmaUrl.get(Constants.DIMENSION_URL));
				tcloudMuv.setIncreaseColor(Utils.getIncreaseFontColor(tcloudMuv.getIncreaseValue()));
			}
			tcloudMuv.setSvcId(7);
			tcloudMuv.setIdxClGrpCd("L081");
			tcloudMuv.setDimension("UV");
			tcloudMuv.setPeriod("당월누적");
			tcloudMuv.setDimensionUnit("천명");

			List<MailReport> tcloudReports = Arrays.asList(tcloudUv, tcloudMuv);
			mailReportMaps.put(Constants.SVC_TCLOUD, tcloudReports);
		}
	}

	/**
	 * Tmap 경영실적 메일 지표 추출.
	 *
	 * @param jqGridRequest
	 * @return
	 * @throws Exception
	 */
	private void getTmapReport(final JqGridRequest jqGridRequest, List<BpmDlyPrst> etcs, MailEwma mailEwma,
			Map<String, List<MailReport>> mailReportMaps) throws Exception {
		if (CollectionUtils.isNotEmpty(etcs)) {
			List<BpmDlyPrst> tmaps = with(etcs).clone().retain(new Predicate<BpmDlyPrst>() {
				@Override
				public boolean apply(BpmDlyPrst bpmDlyPrst) {
					return bpmDlyPrst.getSvcId() == 4;
				}
			});
			MailReport tmapDau = new MailReport();
			if (!NumberUtil.isEmpty(tmaps.get(0).getDlyRsltVal())
					&& StringUtils.equals("L091", tmaps.get(0).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), tmaps.get(0).getDlyStrdDt())) {
				tmapDau.setBasicValue(tmaps.get(0).getDlyRsltVal());
				tmapDau.setMilliBasicValue(tmapDau.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> maus = with(tmaps).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L091", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double mauSum = sum(maus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal mauAverage = BigDecimal.valueOf(mauSum).divide(
						new BigDecimal(Integer.toString(maus.size())), Constants.ROUNDING_MODE);
				tmapDau.setIncreaseValue(NumberUtil.calculateGrowth(tmapDau.getBasicValue().doubleValue(),
						mauAverage.doubleValue()));
				Map<String, String> mauEwmaUrl = Utils.getMailEwmaImage(mailEwma.getTmapDauEwma(),
						mailEwma.getTmapDauLcl(), mailEwma.getTmapDauUcl(), tmapDau.getIncreaseValue());
				tmapDau.setBrandImageUrl(mauEwmaUrl.get(Constants.BRAND_URL));
				tmapDau.setDimensionImageUrl(mauEwmaUrl.get(Constants.DIMENSION_URL));
				tmapDau.setIncreaseColor(Utils.getIncreaseFontColor(tmapDau.getIncreaseValue()));
			}
			tmapDau.setSvcId(4);
			tmapDau.setIdxClGrpCd("L091");
			tmapDau.setDimension("DAU");
			tmapDau.setPeriod("일누적");
			tmapDau.setDimensionUnit("천명");

			MailReport tmapMau = new MailReport();
			if (!NumberUtil.isEmpty(tmaps.get(1).getDlyRsltVal())
					&& StringUtils.equals("L110", tmaps.get(1).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), tmaps.get(1).getDlyStrdDt())) {
				tmapMau.setBasicValue(tmaps.get(1).getDlyRsltVal());
				tmapMau.setMilliBasicValue(tmapMau.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> maus = with(tmaps).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L110", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double mauSum = sum(maus, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal mauAverage = BigDecimal.valueOf(mauSum).divide(
						new BigDecimal(Integer.toString(maus.size())), Constants.ROUNDING_MODE);
				tmapMau.setIncreaseValue(NumberUtil.calculateGrowth(tmapMau.getBasicValue().doubleValue(),
						mauAverage.doubleValue()));
				Map<String, String> mauEwmaUrl = Utils.getMailEwmaImage(mailEwma.getTmapMauEwma(),
						mailEwma.getTmapMauLcl(), mailEwma.getTmapMauUcl(), tmapMau.getIncreaseValue());
				tmapMau.setBrandImageUrl(mauEwmaUrl.get(Constants.BRAND_URL));
				tmapMau.setDimensionImageUrl(mauEwmaUrl.get(Constants.DIMENSION_URL));
				tmapMau.setIncreaseColor(Utils.getIncreaseFontColor(tmapMau.getIncreaseValue()));
			}
			tmapMau.setSvcId(4);
			tmapMau.setIdxClGrpCd("L110");
			tmapMau.setDimension("DAU");
			tmapMau.setPeriod("당월누적");
			tmapMau.setDimensionUnit("천명");
			List<MailReport> tmapReports = Arrays.asList(tmapDau, tmapMau);
			mailReportMaps.put(Constants.SVC_TMAP, tmapReports);

		}
	}

	/**
	 * 11번가 경영실적 메일 지표 추출.
	 *
	 * @param jqGridRequest
	 * @return
	 * @throws Exception
	 */
	private void getSk11Report(final JqGridRequest jqGridRequest, List<BpmDlyPrst> etcs, MailEwma mailEwma,
			Map<String, List<MailReport>> mailReportMaps) throws Exception {
		if (CollectionUtils.isNotEmpty(etcs)) {
			List<BpmDlyPrst> sk11s = with(etcs).clone().retain(new Predicate<BpmDlyPrst>() {
				@Override
				public boolean apply(BpmDlyPrst bpmDlyPrst) {
					return bpmDlyPrst.getSvcId() == 11;
				}
			});
			// GMV
			MailReport gmv = new MailReport();
			if (!NumberUtil.isEmpty(sk11s.get(0).getDlyRsltVal())
					&& StringUtils.equals("L096", sk11s.get(0).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), sk11s.get(0).getDlyStrdDt())) {
				gmv.setBasicValue(sk11s.get(0).getDlyRsltVal());
				gmv.setMilliBasicValue(gmv.getBasicValue().divide(new BigDecimal("1000"), 0, RoundingMode.HALF_UP));
				List<BpmDlyPrst> gmvs = with(sk11s).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L096", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double gmvSum = sum(gmvs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal gmvAverage = BigDecimal.valueOf(gmvSum).divide(
						new BigDecimal(Integer.toString(gmvs.size())), Constants.ROUNDING_MODE);
				gmv.setIncreaseValue(NumberUtil.calculateGrowth(gmv.getBasicValue().doubleValue(),
						gmvAverage.doubleValue()));
				//
				Map<String, String> gmvEwmaUrl = Utils.getMailEwmaImage(mailEwma.getSk11GmvEwma(),
						mailEwma.getSk11GmvLcl(), mailEwma.getSk11GmvUcl(), gmv.getIncreaseValue());
				gmv.setBrandImageUrl(gmvEwmaUrl.get(Constants.BRAND_URL));
				gmv.setDimensionImageUrl(gmvEwmaUrl.get(Constants.DIMENSION_URL));
				gmv.setIncreaseColor(Utils.getIncreaseFontColor(gmv.getIncreaseValue()));
			}
			gmv.setSvcId(11);
			gmv.setIdxClGrpCd("L096");
			gmv.setDimension("구매확정액");
			gmv.setPeriod("일누적");
			gmv.setDimensionUnit("천원");

			MailReport mgmv = new MailReport();
			if (!NumberUtil.isEmpty(sk11s.get(1).getDlyRsltVal())
					&& StringUtils.equals("L097", sk11s.get(1).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), sk11s.get(1).getDlyStrdDt())) {
				mgmv.setBasicValue(sk11s.get(1).getDlyRsltVal());
				mgmv.setMilliBasicValue(mgmv.getBasicValue().divide(new BigDecimal("1000"), 0, RoundingMode.HALF_UP));
				List<BpmDlyPrst> mgmvs = with(sk11s).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L097", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double mgmvSum = sum(mgmvs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal mgmvAverage = BigDecimal.valueOf(mgmvSum).divide(
						new BigDecimal(Integer.toString(mgmvs.size())), Constants.ROUNDING_MODE);
				mgmv.setIncreaseValue(NumberUtil.calculateGrowth(mgmv.getBasicValue().doubleValue(),
						mgmvAverage.doubleValue()));
				//
				Map<String, String> mgmvEwmaUrl = Utils.getMailEwmaImage(mailEwma.getSk11MgmvEwma(),
						mailEwma.getSk11MgmvLcl(), mailEwma.getSk11MgmvUcl(), mgmv.getIncreaseValue());
				mgmv.setBrandImageUrl(mgmvEwmaUrl.get(Constants.BRAND_URL));
				mgmv.setDimensionImageUrl(mgmvEwmaUrl.get(Constants.DIMENSION_URL));
				mgmv.setIncreaseColor(Utils.getIncreaseFontColor(mgmv.getIncreaseValue()));
			}
			mgmv.setSvcId(11);
			mgmv.setIdxClGrpCd("L097");
			mgmv.setDimension("구매확정액");
			mgmv.setPeriod("당월누적");
			mgmv.setDimensionUnit("천원");

			// Mobile GMV
			MailReport mobileGmv = new MailReport();
			if (!NumberUtil.isEmpty(sk11s.get(2).getDlyRsltVal())
					&& StringUtils.equals("L098", sk11s.get(2).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), sk11s.get(2).getDlyStrdDt())) {
				mobileGmv.setBasicValue(sk11s.get(2).getDlyRsltVal());
				mobileGmv.setMilliBasicValue(mobileGmv.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> mobileGmvs = with(sk11s).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L098", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double mobileGmvSum = sum(mobileGmvs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal mobileGmvAverage = BigDecimal.valueOf(mobileGmvSum).divide(
						new BigDecimal(Integer.toString(mobileGmvs.size())), Constants.ROUNDING_MODE);
				mobileGmv.setIncreaseValue(NumberUtil.calculateGrowth(mobileGmv.getBasicValue().doubleValue(),
						mobileGmvAverage.doubleValue()));
				//
				Map<String, String> mobileGmvEwmaUrl = Utils.getMailEwmaImage(mailEwma.getSk11MobileGmvEwma(),
						mailEwma.getSk11MobileGmvLcl(), mailEwma.getSk11MobileGmvUcl(), mobileGmv.getIncreaseValue());
				mobileGmv.setBrandImageUrl(mobileGmvEwmaUrl.get(Constants.BRAND_URL));
				mobileGmv.setDimensionImageUrl(mobileGmvEwmaUrl.get(Constants.DIMENSION_URL));
				mobileGmv.setIncreaseColor(Utils.getIncreaseFontColor(mobileGmv.getIncreaseValue()));
			}
			mobileGmv.setSvcId(11);
			mobileGmv.setIdxClGrpCd("L098");
			mobileGmv.setDimension("Mobile 구매확정액");
			mobileGmv.setPeriod("일누적");
			mobileGmv.setDimensionUnit("천원");

			MailReport mobileMgmv = new MailReport();
			if (!NumberUtil.isEmpty(sk11s.get(3).getDlyRsltVal())
					&& StringUtils.equals("L099", sk11s.get(3).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), sk11s.get(3).getDlyStrdDt())) {
				mobileMgmv.setBasicValue(sk11s.get(3).getDlyRsltVal());
				mobileMgmv.setMilliBasicValue(mobileMgmv.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> mobileMgmvs = with(sk11s).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L099", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double mobileMgmvSum = sum(mobileMgmvs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal mobileMgmvAverage = BigDecimal.valueOf(mobileMgmvSum).divide(
						new BigDecimal(Integer.toString(mobileMgmvs.size())), Constants.ROUNDING_MODE);
				mobileMgmv.setIncreaseValue(NumberUtil.calculateGrowth(mobileMgmv.getBasicValue().doubleValue(),
						mobileMgmvAverage.doubleValue()));
				//
				Map<String, String> mobileMgmvEwmaUrl = Utils.getMailEwmaImage(mailEwma.getSk11MobileMgmvEwma(),
						mailEwma.getSk11MobileMgmvLcl(), mailEwma.getSk11MobileMgmvUcl(), mobileMgmv.getIncreaseValue());
				mobileMgmv.setBrandImageUrl(mobileMgmvEwmaUrl.get(Constants.BRAND_URL));
				mobileMgmv.setDimensionImageUrl(mobileMgmvEwmaUrl.get(Constants.DIMENSION_URL));
				mobileMgmv.setIncreaseColor(Utils.getIncreaseFontColor(mobileMgmv.getIncreaseValue()));
			}
			mobileMgmv.setSvcId(11);
			mobileMgmv.setIdxClGrpCd("L099");
			mobileMgmv.setDimension("Mobile 구매확정액");
			mobileMgmv.setPeriod("당월누적");
			mobileMgmv.setDimensionUnit("천원");

			// Mobile Trafic
			MailReport mobileLv = new MailReport();
			if (!NumberUtil.isEmpty(sk11s.get(4).getDlyRsltVal())
					&& StringUtils.equals("L100", sk11s.get(4).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), sk11s.get(4).getDlyStrdDt())) {
				mobileLv.setBasicValue(sk11s.get(4).getDlyRsltVal());
				mobileLv.setMilliBasicValue(mobileLv.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> mobileLvs = with(sk11s).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L100", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double mobileLvSum = sum(mobileLvs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal mobileLvAverage = BigDecimal.valueOf(mobileLvSum).divide(
						new BigDecimal(Integer.toString(mobileLvs.size())), Constants.ROUNDING_MODE);
				mobileLv.setIncreaseValue(NumberUtil.calculateGrowth(mobileLv.getBasicValue().doubleValue(),
						mobileLvAverage.doubleValue()));
				//
				Map<String, String> mobileLvEwmaUrl = Utils.getMailEwmaImage(mailEwma.getSk11MobileLvEwma(),
						mailEwma.getSk11MobileLvLcl(), mailEwma.getSk11MobileLvUcl(), mobileLv.getIncreaseValue());
				mobileLv.setBrandImageUrl(mobileLvEwmaUrl.get(Constants.BRAND_URL));
				mobileLv.setDimensionImageUrl(mobileLvEwmaUrl.get(Constants.DIMENSION_URL));
				mobileLv.setIncreaseColor(Utils.getIncreaseFontColor(mobileLv.getIncreaseValue()));
			}
			mobileLv.setSvcId(11);
			mobileLv.setIdxClGrpCd("L100");
			mobileLv.setDimension("Mobile Traffic(LV)");
			mobileLv.setPeriod("일누적");
			mobileLv.setDimensionUnit("천건");

			MailReport mobileMlv = new MailReport();
			if (!NumberUtil.isEmpty(sk11s.get(5).getDlyRsltVal())
					&& StringUtils.equals("L101", sk11s.get(5).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), sk11s.get(5).getDlyStrdDt())) {
				mobileMlv.setBasicValue(sk11s.get(5).getDlyRsltVal());
				mobileMlv.setMilliBasicValue(mobileMlv.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> mobileMlvs = with(sk11s).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L101", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double mobileMlvSum = sum(mobileMlvs, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal mobileMlvAverage = BigDecimal.valueOf(mobileMlvSum).divide(
						new BigDecimal(Integer.toString(mobileMlvs.size())), Constants.ROUNDING_MODE);
				mobileMlv.setIncreaseValue(NumberUtil.calculateGrowth(mobileMlv.getBasicValue().doubleValue(),
						mobileMlvAverage.doubleValue()));
				//
				Map<String, String> mobileMlvEwmaUrl = Utils.getMailEwmaImage(mailEwma.getSk11MobileMlvEwma(),
						mailEwma.getSk11MobileMlvLcl(), mailEwma.getSk11MobileMlvUcl(), mobileMlv.getIncreaseValue());
				mobileMlv.setBrandImageUrl(mobileMlvEwmaUrl.get(Constants.BRAND_URL));
				mobileMlv.setDimensionImageUrl(mobileMlvEwmaUrl.get(Constants.DIMENSION_URL));
				mobileMlv.setIncreaseColor(Utils.getIncreaseFontColor(mobileMlv.getIncreaseValue()));
			}
			mobileMlv.setSvcId(11);
			mobileMlv.setIdxClGrpCd("L101");
			mobileMlv.setDimension("Mobile Traffic(LV)");
			mobileMlv.setPeriod("당월누적");
			mobileMlv.setDimensionUnit("천건");

			List<MailReport> sk11Reports = Arrays.asList(gmv, mgmv, mobileGmv, mobileMgmv, mobileLv, mobileMlv);
			mailReportMaps.put(Constants.SVC_SK11, sk11Reports);
		}
	}

	/**
	 * Tstore 경영실적 메일 지표 추출.
	 *
	 * @param jqGridRequest
	 * @return
	 * @throws Exception
	 */
	private void getTstoreReport(final JqGridRequest jqGridRequest, List<BpmDlyPrst> etcs, MailEwma mailEwma,
			Map<String, List<MailReport>> mailReportMaps) throws Exception {
		if (CollectionUtils.isNotEmpty(etcs)) {
			List<BpmDlyPrst> tstores = with(etcs).clone().retain(new Predicate<BpmDlyPrst>() {
				@Override
				public boolean apply(BpmDlyPrst bpmDlyPrst) {
					return bpmDlyPrst.getSvcId() == 1;
				}
			});

			// 유료이용자
			MailReport payUser = new MailReport();
			if (!NumberUtil.isEmpty(tstores.get(0).getDlyRsltVal())
					&& StringUtils.equals("L102", tstores.get(0).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), tstores.get(0).getDlyStrdDt())) {
				payUser.setBasicValue(tstores.get(0).getDlyRsltVal());
				payUser.setMilliBasicValue(payUser.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> payUsers = with(tstores).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L102", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double payUserSum = sum(payUsers, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal payUserAverage = BigDecimal.valueOf(payUserSum).divide(
						new BigDecimal(Integer.toString(payUsers.size())), Constants.ROUNDING_MODE);
				payUser.setIncreaseValue(NumberUtil.calculateGrowth(payUser.getBasicValue().doubleValue(),
						payUserAverage.doubleValue()));
				//
				Map<String, String> payUserEwmaUrl = Utils.getMailEwmaImage(mailEwma.getTstorePayUserEwma(),
						mailEwma.getTstorePayUserLcl(), mailEwma.getTstorePayUserUcl(), payUser.getIncreaseValue());
				payUser.setBrandImageUrl(payUserEwmaUrl.get(Constants.BRAND_URL));
				payUser.setDimensionImageUrl(payUserEwmaUrl.get(Constants.DIMENSION_URL));
				payUser.setIncreaseColor(Utils.getIncreaseFontColor(payUser.getIncreaseValue()));
			}
			payUser.setSvcId(1);
			payUser.setIdxClGrpCd("L102");
			payUser.setDimension("유료 이용자수");
			payUser.setPeriod("일누적");
			payUser.setDimensionUnit("천명");

			MailReport mpayUser = new MailReport();
			if (!NumberUtil.isEmpty(tstores.get(1).getDlyRsltVal())
					&& StringUtils.equals("L103", tstores.get(1).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), tstores.get(1).getDlyStrdDt())) {
				mpayUser.setBasicValue(tstores.get(1).getDlyRsltVal());
				mpayUser.setMilliBasicValue(mpayUser.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> mpayUsers = with(tstores).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L103", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double mpayUserSum = sum(mpayUsers, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal mpayUserAverage = BigDecimal.valueOf(mpayUserSum).divide(
						new BigDecimal(Integer.toString(mpayUsers.size())), Constants.ROUNDING_MODE);
				mpayUser.setIncreaseValue(NumberUtil.calculateGrowth(mpayUser.getBasicValue().doubleValue(),
						mpayUserAverage.doubleValue()));
				//
				Map<String, String> mpayUserEwmaUrl = Utils.getMailEwmaImage(mailEwma.getTstoreMpayUserEwma(),
						mailEwma.getTstoreMpayUserLcl(), mailEwma.getTstoreMpayUserUcl(), mpayUser.getIncreaseValue());
				mpayUser.setBrandImageUrl(mpayUserEwmaUrl.get(Constants.BRAND_URL));
				mpayUser.setDimensionImageUrl(mpayUserEwmaUrl.get(Constants.DIMENSION_URL));
				mpayUser.setIncreaseColor(Utils.getIncreaseFontColor(mpayUser.getIncreaseValue()));
			}
			mpayUser.setSvcId(1);
			mpayUser.setIdxClGrpCd("L103");
			mpayUser.setDimension("유료 이용자수");
			mpayUser.setPeriod("당월누적");
			mpayUser.setDimensionUnit("천명");

			// 거래액
			MailReport transactAmount = new MailReport();
			if (!NumberUtil.isEmpty(tstores.get(2).getDlyRsltVal())
					&& StringUtils.equals("L104", tstores.get(2).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), tstores.get(2).getDlyStrdDt())) {
				transactAmount.setBasicValue(tstores.get(2).getDlyRsltVal());
				transactAmount.setMilliBasicValue(transactAmount.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> transactAmounts = with(tstores).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L104", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double transactAmountSum = sum(transactAmounts, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal transactAmountAverage = BigDecimal.valueOf(transactAmountSum).divide(
						new BigDecimal(Integer.toString(transactAmounts.size())), Constants.ROUNDING_MODE);
				transactAmount.setIncreaseValue(NumberUtil.calculateGrowth(
						transactAmount.getBasicValue().doubleValue(), transactAmountAverage.doubleValue()));
				//
				Map<String, String> transactAmountEwmaUrl = Utils.getMailEwmaImage(mailEwma.getTstoreTransactAmountEwma(),
						mailEwma.getTstoreTransactAmountLcl(), mailEwma.getTstoreTransactAmountUcl(), transactAmount.getIncreaseValue());
				transactAmount.setBrandImageUrl(transactAmountEwmaUrl.get(Constants.BRAND_URL));
				transactAmount.setDimensionImageUrl(transactAmountEwmaUrl.get(Constants.DIMENSION_URL));
				transactAmount.setIncreaseColor(Utils.getIncreaseFontColor(transactAmount.getIncreaseValue()));
			}
			transactAmount.setSvcId(1);
			transactAmount.setIdxClGrpCd("L104");
			transactAmount.setDimension("거래액");
			transactAmount.setPeriod("일누적");
			transactAmount.setDimensionUnit("천원");

			MailReport mtransactAmount = new MailReport();
			if (!NumberUtil.isEmpty(tstores.get(3).getDlyRsltVal())
					&& StringUtils.equals("L105", tstores.get(3).getIdxClGrpCd())
					&& StringUtils.equals(jqGridRequest.getBasicDate(), tstores.get(3).getDlyStrdDt())) {
				mtransactAmount.setBasicValue(tstores.get(3).getDlyRsltVal());
				mtransactAmount.setMilliBasicValue(mtransactAmount.getBasicValue().divide(new BigDecimal("1000"), 0,
						RoundingMode.HALF_UP));
				List<BpmDlyPrst> mtransactAmounts = with(tstores).clone().retain(new Predicate<BpmDlyPrst>() {
					@Override
					public boolean apply(BpmDlyPrst bpmDlyPrst) {
						return (Integer.parseInt(bpmDlyPrst.getDlyStrdDt()) < Integer.parseInt(jqGridRequest
								.getBasicDate()) && StringUtils.equals("L105", bpmDlyPrst.getIdxClGrpCd()));
					}
				});
				double mtransactAmountSum = sum(mtransactAmounts, on(BpmDlyPrst.class).getDlyRsltVal().doubleValue());
				BigDecimal mtransactAmountAverage = BigDecimal.valueOf(mtransactAmountSum).divide(
						new BigDecimal(Integer.toString(mtransactAmounts.size())), Constants.ROUNDING_MODE);
				mtransactAmount.setIncreaseValue(NumberUtil.calculateGrowth(
						mtransactAmount.getBasicValue().doubleValue(), mtransactAmountAverage.doubleValue()));
				//
				Map<String, String> mtransactAmountEwmaUrl = Utils.getMailEwmaImage(mailEwma.getTstoreMtransactAmountEwma(),
						mailEwma.getTstoreMtransactAmountLcl(), mailEwma.getTstoreMtransactAmountUcl(), mtransactAmount.getIncreaseValue());
				mtransactAmount.setBrandImageUrl(mtransactAmountEwmaUrl.get(Constants.BRAND_URL));
				mtransactAmount.setDimensionImageUrl(mtransactAmountEwmaUrl.get(Constants.DIMENSION_URL));
				mtransactAmount.setIncreaseColor(Utils.getIncreaseFontColor(mtransactAmount.getIncreaseValue()));
			}
			mtransactAmount.setSvcId(1);
			mtransactAmount.setIdxClGrpCd("L105");
			mtransactAmount.setDimension("거래액");
			mtransactAmount.setPeriod("당월누적");
			mtransactAmount.setDimensionUnit("천원");

			List<MailReport> tstoreReports = Arrays.asList(payUser, mpayUser, transactAmount, mtransactAmount);
			mailReportMaps.put(Constants.SVC_TSTORE, tstoreReports);
		}
	}

	/**
	 * 경영실적 이메일 수신자 정보 삭제.
	 *
	 * @param jqGridRequest
	 * @return int
	 * @throws Exception
	 */
	@Override
	@Transactional
	public int deleteBpmMgntEmailSndUser(JqGridRequest jqGridRequest) throws Exception {
		String[] loginIdArray = jqGridRequest.getLoginIds().toArray(new String[jqGridRequest.getLoginIds().size()]);
		if (jqGridRequest.getItemCode() == 1)
			return orgUserRepository.deleteBpmMgntEmailSndUser(loginIdArray);
		else {
			return orgUserRepository.deleteBpmMgntEmailSndUserForKid(loginIdArray);
		}
	}

	/**
	 * 경영실적 이메일 수신자 등록.
	 *
	 * @param bpmMgntEmailSndUser
	 * @return int
	 * @throws Exception
	 */
	@Override
	@Transactional
	public int createBpmMgntEmailSndUser(BpmMgntEmailSndUser bpmMgntEmailSndUser) throws Exception {
		return orgUserRepository.createBpmMgntEmailSndUser(bpmMgntEmailSndUser);
	}

	/**
	 * @개요 : KID 메일 발송 정보 저장.
	 *
	 * @param jqGridRequest
	 *            메일 발송 날짜.
	 * @return BpmMgntEmailSndCtt 메일 내용 정보.
	 *
	 */
	@Override
	@Transactional
	public BpmMgntEmailSndCtt getMailContentForKid(JqGridRequest jqGridRequest) throws Exception {
		MailReports mailReports = this.getBusinessDatasForKid(jqGridRequest);
		mailReports.setYesterday(jqGridRequest.getBasicDate());
		BpmMgntEmailSndObj bpmMgntEmailSndObj = orgUserRepository.getBpmMgntEmailSndObj(3l);
		// 메일 발송 정보 셋팅.
		String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				bpmMgntEmailSndObj.getEmailTemplate(), CharEncoding.UTF_8, this.getVelocityMapForKid(mailReports));
		// 메일발송내용 저장.
		BpmMgntEmailSndCtt bpmMgntEmailSndCtt = new BpmMgntEmailSndCtt();
		bpmMgntEmailSndCtt.setAuditId(jqGridRequest.getUsername());
		bpmMgntEmailSndCtt.setEmailCtxtCtt(body);
		bpmMgntEmailSndCtt.setEmailSndTitleNm(bpmMgntEmailSndObj.getEmailSndTitleNm());
		bpmMgntEmailSndCtt.setSndDtm(jqGridRequest.getBasicDate());
		bpmMgntEmailSndCtt.setSndEmailAddr(bpmMgntEmailSndObj.getSndEmailAddr());
		bpmMgntEmailSndCtt.setSndObjId(3l);

		BpmMgntEmailSndCtt emailSndCtt = orgUserRepository.getBpmMgntEmailSndCtt(bpmMgntEmailSndCtt);
		if (emailSndCtt != null) {
			orgUserRepository.updateBpmMgntEmailSndCtt(bpmMgntEmailSndCtt);
		} else {
			orgUserRepository.createBpmMgntEmailSndCtt(bpmMgntEmailSndCtt);
		}
		return bpmMgntEmailSndCtt;
	}

	/**
	 * @개요 : 메일 템플릿에 매핑되는 정보 셋팅.
	 *
	 * @param mailReports
	 *            메일 입력 데이터.
	 * @return Map<String, Object> 템플릿 데이터.
	 *
	 */
	@Override
	public Map<String, Object> getVelocityMapForKid(MailReports mailReports) throws Exception {
		Map<String, Object> valuesMap = Maps.newHashMap();
		valuesMap.put("previousCommaDate", DateUtil.changeFormatDate(mailReports.getYesterday(),
				Constants.DATE_YMD_FORMAT, Constants.DATE_YMD_COMMA_FORMAT) + "(" + DateUtil.getDayOfWeek(mailReports.getYesterday(), true,
				Locale.KOREA) + ")");
		String thisDate =  DateUtil.changeFormatDate(mailReports.getYesterday(), Constants.DATE_YMD_FORMAT,
				Constants.DATE_MD_COMMA_FORMAT);
		valuesMap.put("thisDate", thisDate);
		// 최근 3개월 동일기간 대비(05.01 ~ 05.23
		String thisPeriod;
		if (StringUtils.equals(mailReports.getYesterday().substring(6), "01")) {
			thisPeriod = thisDate;
		} else {
			thisPeriod = mailReports.getYesterday().substring(4, 6) + ".01~" + thisDate;
		}
		valuesMap.put("thisPeriod", thisPeriod);

		// OCB DAU
		valuesMap.put("ocbDauBrandUrl", mailReports.getReports().get(Constants.SVC_OCB).get(0).getBrandImageUrl());
		valuesMap.put("ocbDauDimension", mailReports.getReports().get(Constants.SVC_OCB).get(0).getDimension());
		valuesMap.put("ocbDauDimensionUrl", mailReports.getReports().get(Constants.SVC_OCB).get(0).getDimensionImageUrl());
		valuesMap.put("ocbDauPeriod", mailReports.getReports().get(Constants.SVC_OCB).get(0).getPeriod());
		valuesMap.put("ocbDauBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_OCB).get(0).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("ocbDauDimensionUnit", mailReports.getReports().get(Constants.SVC_OCB).get(0).getDimensionUnit());
		valuesMap.put("ocbDauIncreaseValue", mailReports.getReports().get(Constants.SVC_OCB).get(0).getIncreaseValue());
		valuesMap.put("ocbDauIncreaseColor", mailReports.getReports().get(Constants.SVC_OCB).get(0).getIncreaseColor());
		// OCB 월누적 DAU
		valuesMap.put("ocbMauBrandUrl", mailReports.getReports().get(Constants.SVC_OCB).get(1).getBrandImageUrl());
		valuesMap.put("ocbMauDimension", mailReports.getReports().get(Constants.SVC_OCB).get(1).getDimension());
		valuesMap.put("ocbMauDimensionUrl", mailReports.getReports().get(Constants.SVC_OCB).get(1).getDimensionImageUrl());
		valuesMap.put("ocbMauPeriod", mailReports.getReports().get(Constants.SVC_OCB).get(1).getPeriod());
		valuesMap.put("ocbMauBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_OCB).get(
				1).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("ocbMauDimensionUnit", mailReports.getReports().get(Constants.SVC_OCB).get(1).getDimensionUnit());
		valuesMap.put("ocbMauIncreaseValue", mailReports.getReports().get(Constants.SVC_OCB).get(1).getIncreaseValue());
		valuesMap.put("ocbMauIncreaseColor", mailReports.getReports().get(Constants.SVC_OCB).get(1).getIncreaseColor());
		// OCB TR
		valuesMap.put("ocbTrBrandUrl", mailReports.getReports().get(Constants.SVC_OCB).get(2).getBrandImageUrl());
		valuesMap.put("ocbTrDimension", mailReports.getReports().get(Constants.SVC_OCB).get(2).getDimension());
		valuesMap.put("ocbTrDimensionUrl", mailReports.getReports().get(Constants.SVC_OCB).get(2).getDimensionImageUrl());
		valuesMap.put("ocbTrPeriod", mailReports.getReports().get(Constants.SVC_OCB).get(2).getPeriod());
		valuesMap.put("ocbTrBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_OCB).get(2).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("ocbTrDimensionUnit", mailReports.getReports().get(Constants.SVC_OCB).get(2).getDimensionUnit());
		valuesMap.put("ocbTrIncreaseValue", mailReports.getReports().get(Constants.SVC_OCB).get(2).getIncreaseValue());
		valuesMap.put("ocbTrIncreaseColor", mailReports.getReports().get(Constants.SVC_OCB).get(2).getIncreaseColor());
		// OCB 월누적 TR
		valuesMap.put("ocbMtrBrandUrl", mailReports.getReports().get(Constants.SVC_OCB).get(3).getBrandImageUrl());
		valuesMap.put("ocbMtrDimension", mailReports.getReports().get(Constants.SVC_OCB).get(3).getDimension());
		valuesMap.put("ocbMtrDimensionUrl", mailReports.getReports().get(Constants.SVC_OCB).get(3).getDimensionImageUrl());
		valuesMap.put("ocbMtrPeriod", mailReports.getReports().get(Constants.SVC_OCB).get(3).getPeriod());
		valuesMap.put("ocbMtrBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_OCB).get(3).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("ocbMtrDimensionUnit", mailReports.getReports().get(Constants.SVC_OCB).get(3).getDimensionUnit());
		valuesMap.put("ocbMtrIncreaseValue", mailReports.getReports().get(Constants.SVC_OCB).get(3).getIncreaseValue());
		valuesMap.put("ocbMtrIncreaseColor", mailReports.getReports().get(Constants.SVC_OCB).get(3).getIncreaseColor());
		valuesMap.put("ocbComment", mailReports.getOcbComment());

		// Syrup DAU
		valuesMap.put("syrupDauBrandUrl", mailReports.getReports().get(Constants.SVC_SYRUP).get(0).getBrandImageUrl());
		valuesMap.put("syrupDauDimension", mailReports.getReports().get(Constants.SVC_SYRUP).get(0).getDimension());
		valuesMap.put("syrupDauDimensionUrl", mailReports.getReports().get(Constants.SVC_SYRUP).get(0).getDimensionImageUrl());
		valuesMap.put("syrupDauPeriod", mailReports.getReports().get(Constants.SVC_SYRUP).get(0).getPeriod());
		valuesMap.put("syrupDauBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_SYRUP).get(0).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("syrupDauDimensionUnit", mailReports.getReports().get(Constants.SVC_SYRUP).get(0).getDimensionUnit());
		valuesMap.put("syrupDauIncreaseValue", mailReports.getReports().get(Constants.SVC_SYRUP).get(0).getIncreaseValue());
		valuesMap.put("syrupDauIncreaseColor", mailReports.getReports().get(Constants.SVC_SYRUP).get(0).getIncreaseColor());
		// Syrup 월누적 DAU
		valuesMap.put("syrupMauBrandUrl", mailReports.getReports().get(Constants.SVC_SYRUP).get(1).getBrandImageUrl());
		valuesMap.put("syrupMauDimension", mailReports.getReports().get(Constants.SVC_SYRUP).get(1).getDimension());
		valuesMap.put("syrupMauDimensionUrl", mailReports.getReports().get(Constants.SVC_SYRUP).get(1).getDimensionImageUrl());
		valuesMap.put("syrupMauPeriod", mailReports.getReports().get(Constants.SVC_SYRUP).get(1).getPeriod());
		valuesMap.put("syrupMauBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_SYRUP).get(1).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("syrupMauDimensionUnit", mailReports.getReports().get(Constants.SVC_SYRUP).get(1).getDimensionUnit());
		valuesMap.put("syrupMauIncreaseValue", mailReports.getReports().get(Constants.SVC_SYRUP).get(1).getIncreaseValue());
		valuesMap.put("syrupMauIncreaseColor", mailReports.getReports().get(Constants.SVC_SYRUP).get(1).getIncreaseColor());
		// Syrup TR
		valuesMap.put("syrupTrBrandUrl", mailReports.getReports().get(Constants.SVC_SYRUP).get(2).getBrandImageUrl());
		valuesMap.put("syrupTrDimension", mailReports.getReports().get(Constants.SVC_SYRUP).get(2).getDimension());
		valuesMap.put("syrupTrDimensionUrl", mailReports.getReports().get(Constants.SVC_SYRUP).get(2).getDimensionImageUrl());
		valuesMap.put("syrupTrPeriod", mailReports.getReports().get(Constants.SVC_SYRUP).get(2).getPeriod());
		valuesMap.put("syrupTrBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_SYRUP).get(2).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("syrupTrDimensionUnit", mailReports.getReports().get(Constants.SVC_SYRUP).get(2).getDimensionUnit());
		valuesMap.put("syrupTrIncreaseValue", mailReports.getReports().get(Constants.SVC_SYRUP).get(2).getIncreaseValue());
		valuesMap.put("syrupTrIncreaseColor", mailReports.getReports().get(Constants.SVC_SYRUP).get(2).getIncreaseColor());
		// Syrup 월누적 TR
		valuesMap.put("syrupMtrBrandUrl", mailReports.getReports().get(Constants.SVC_SYRUP).get(3).getBrandImageUrl());
		valuesMap.put("syrupMtrDimension", mailReports.getReports().get(Constants.SVC_SYRUP).get(3).getDimension());
		valuesMap.put("syrupMtrDimensionUrl", mailReports.getReports().get(Constants.SVC_SYRUP).get(3).getDimensionImageUrl());
		valuesMap.put("syrupMtrPeriod", mailReports.getReports().get(Constants.SVC_SYRUP).get(3).getPeriod());
		valuesMap.put("syrupMtrBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_SYRUP).get(3).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("syrupMtrDimensionUnit", mailReports.getReports().get(Constants.SVC_SYRUP).get(3).getDimensionUnit());
		valuesMap.put("syrupMtrIncreaseValue", mailReports.getReports().get(Constants.SVC_SYRUP).get(3).getIncreaseValue());
		valuesMap.put("syrupMtrIncreaseColor", mailReports.getReports().get(Constants.SVC_SYRUP).get(3).getIncreaseColor());

		valuesMap.put("syrupComment", mailReports.getSyrupComment());

		// sk11 구매확정액
		valuesMap.put("sk11GmvBrandUrl", mailReports.getReports().get(Constants.SVC_SK11).get(0).getBrandImageUrl());
		valuesMap.put("sk11GmvDimension", mailReports.getReports().get(Constants.SVC_SK11).get(0).getDimension());
		valuesMap.put("sk11GmvDimensionUrl", mailReports.getReports().get(Constants.SVC_SK11).get(0).getDimensionImageUrl());
		valuesMap.put("sk11GmvPeriod", mailReports.getReports().get(Constants.SVC_SK11).get(0).getPeriod());
		valuesMap.put("sk11GmvBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_SK11).get(0).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("sk11GmvDimensionUnit", mailReports.getReports().get(Constants.SVC_SK11).get(0).getDimensionUnit());
		valuesMap.put("sk11GmvIncreaseValue", mailReports.getReports().get(Constants.SVC_SK11).get(0).getIncreaseValue());
		valuesMap.put("sk11GmvIncreaseColor", mailReports.getReports().get(Constants.SVC_SK11).get(0).getIncreaseColor());
		// 월누적 구매확정액
		valuesMap.put("sk11MgmvBrandUrl", mailReports.getReports().get(Constants.SVC_SK11).get(1).getBrandImageUrl());
		valuesMap.put("sk11MgmvDimension", mailReports.getReports().get(Constants.SVC_SK11).get(1).getDimension());
		valuesMap.put("sk11MgmvDimensionUrl", mailReports.getReports().get(Constants.SVC_SK11).get(1).getDimensionImageUrl());
		valuesMap.put("sk11MgmvPeriod", mailReports.getReports().get(Constants.SVC_SK11).get(1).getPeriod());
		valuesMap.put("sk11MgmvBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_SK11).get(1).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("sk11MgmvDimensionUnit", mailReports.getReports().get(Constants.SVC_SK11).get(1).getDimensionUnit());
		valuesMap.put("sk11MgmvIncreaseValue", mailReports.getReports().get(Constants.SVC_SK11).get(1).getIncreaseValue());
		valuesMap.put("sk11MgmvIncreaseColor", mailReports.getReports().get(Constants.SVC_SK11).get(1).getIncreaseColor());
		// sk11 Mobile 구매확정액
		valuesMap.put("sk11MobileGmvBrandUrl", mailReports.getReports().get(Constants.SVC_SK11).get(2).getBrandImageUrl());
		valuesMap.put("sk11MobileGmvDimension", mailReports.getReports().get(Constants.SVC_SK11).get(2).getDimension());
		valuesMap.put("sk11MobileGmvDimensionUrl", mailReports.getReports().get(Constants.SVC_SK11).get(2).getDimensionImageUrl());
		valuesMap.put("sk11MobileGmvPeriod", mailReports.getReports().get(Constants.SVC_SK11).get(2).getPeriod());
		valuesMap.put("sk11MobileGmvBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_SK11).get(2).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("sk11MobileGmvDimensionUnit", mailReports.getReports().get(Constants.SVC_SK11).get(2).getDimensionUnit());
		valuesMap.put("sk11MobileGmvIncreaseValue", mailReports.getReports().get(Constants.SVC_SK11).get(2).getIncreaseValue());
		valuesMap.put("sk11MobileGmvIncreaseColor", mailReports.getReports().get(Constants.SVC_SK11).get(2).getIncreaseColor());
		// 월누적 Mobile 구매확정액
		valuesMap.put("sk11MobileMgmvBrandUrl", mailReports.getReports().get(Constants.SVC_SK11).get(3).getBrandImageUrl());
		valuesMap.put("sk11MobileMgmvDimension", mailReports.getReports().get(Constants.SVC_SK11).get(3).getDimension());
		valuesMap.put("sk11MobileMgmvDimensionUrl", mailReports.getReports().get(Constants.SVC_SK11).get(3).getDimensionImageUrl());
		valuesMap.put("sk11MobileMgmvPeriod", mailReports.getReports().get(Constants.SVC_SK11).get(3).getPeriod());
		valuesMap.put("sk11MobileMgmvBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_SK11).get(3).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("sk11MobileMgmvDimensionUnit", mailReports.getReports().get(Constants.SVC_SK11).get(3).getDimensionUnit());
		valuesMap.put("sk11MobileMgmvIncreaseValue", mailReports.getReports().get(Constants.SVC_SK11).get(3).getIncreaseValue());
		valuesMap.put("sk11MobileMgmvIncreaseColor", mailReports.getReports().get(Constants.SVC_SK11).get(3).getIncreaseColor());
		// sk11 Mobile Traffic
		valuesMap.put("sk11MobileLvBrandUrl", mailReports.getReports().get(Constants.SVC_SK11).get(4).getBrandImageUrl());
		valuesMap.put("sk11MobileLvDimension", mailReports.getReports().get(Constants.SVC_SK11).get(4).getDimension());
		valuesMap.put("sk11MobileLvDimensionUrl", mailReports.getReports().get(Constants.SVC_SK11).get(4).getDimensionImageUrl());
		valuesMap.put("sk11MobileLvPeriod", mailReports.getReports().get(Constants.SVC_SK11).get(4).getPeriod());
		valuesMap.put("sk11MobileLvBasicValue",	NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_SK11).get(4).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("sk11MobileLvDimensionUnit", mailReports.getReports().get(Constants.SVC_SK11).get(4).getDimensionUnit());
		valuesMap.put("sk11MobileLvIncreaseValue", mailReports.getReports().get(Constants.SVC_SK11).get(4).getIncreaseValue());
		valuesMap.put("sk11MobileLvIncreaseColor", mailReports.getReports().get(Constants.SVC_SK11).get(4).getIncreaseColor());
		// 월누적 Mobile Traffic
		valuesMap.put("sk11MobileMlvBrandUrl", mailReports.getReports().get(Constants.SVC_SK11).get(5).getBrandImageUrl());
		valuesMap.put("sk11MobileMlvDimension", mailReports.getReports().get(Constants.SVC_SK11).get(5).getDimension());
		valuesMap.put("sk11MobileMlvDimensionUrl", mailReports.getReports().get(Constants.SVC_SK11).get(5).getDimensionImageUrl());
		valuesMap.put("sk11MobileMlvPeriod", mailReports.getReports().get(Constants.SVC_SK11).get(5).getPeriod());
		valuesMap.put("sk11MobileMlvBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_SK11).get(5).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("sk11MobileMlvDimensionUnit", mailReports.getReports().get(Constants.SVC_SK11).get(5).getDimensionUnit());
		valuesMap.put("sk11MobileMlvIncreaseValue", mailReports.getReports().get(Constants.SVC_SK11).get(5).getIncreaseValue());
		valuesMap.put("sk11MobileMlvIncreaseColor", mailReports.getReports().get(Constants.SVC_SK11).get(5).getIncreaseColor());
		valuesMap.put("sk11Comment", mailReports.getSk11Comment());

		// T store 유료이용자
		valuesMap.put("tstorePayUserBrandUrl", mailReports.getReports().get(Constants.SVC_TSTORE).get(0).getBrandImageUrl());
		valuesMap.put("tstorePayUserDimension", mailReports.getReports().get(Constants.SVC_TSTORE).get(0).getDimension());
		valuesMap.put("tstorePayUserDimensionUrl", mailReports.getReports().get(Constants.SVC_TSTORE).get(0).getDimensionImageUrl());
		valuesMap.put("tstorePayUserPeriod", mailReports.getReports().get(Constants.SVC_TSTORE).get(0).getPeriod());
		valuesMap.put("tstorePayUserBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_TSTORE).get(0).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("tstorePayUserDimensionUnit", mailReports.getReports().get(Constants.SVC_TSTORE).get(0).getDimensionUnit());
		valuesMap.put("tstorePayUserIncreaseValue", mailReports.getReports().get(Constants.SVC_TSTORE).get(0).getIncreaseValue());
		valuesMap.put("tstorePayUserIncreaseColor", mailReports.getReports().get(Constants.SVC_TSTORE).get(0).getIncreaseColor());
		// 월누적 유료이용자
		valuesMap.put("tstoreMpayUserBrandUrl", mailReports.getReports().get(Constants.SVC_TSTORE).get(1).getBrandImageUrl());
		valuesMap.put("tstoreMpayUserDimension", mailReports.getReports().get(Constants.SVC_TSTORE).get(1).getDimension());
		valuesMap.put("tstoreMpayUserDimensionUrl", mailReports.getReports().get(Constants.SVC_TSTORE).get(1).getDimensionImageUrl());
		valuesMap.put("tstoreMpayUserPeriod", mailReports.getReports().get(Constants.SVC_TSTORE).get(1).getPeriod());
		valuesMap.put("tstoreMpayUserBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_TSTORE).get(1).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("tstoreMpayUserDimensionUnit", mailReports.getReports().get(Constants.SVC_TSTORE).get(1).getDimensionUnit());
		valuesMap.put("tstoreMpayUserIncreaseValue", mailReports.getReports().get(Constants.SVC_TSTORE).get(1).getIncreaseValue());
		valuesMap.put("tstoreMpayUserIncreaseColor", mailReports.getReports().get(Constants.SVC_TSTORE).get(1).getIncreaseColor());
		// T store 거래액
		valuesMap.put("tstoreTransactAmountBrandUrl", mailReports.getReports().get(Constants.SVC_TSTORE).get(2).getBrandImageUrl());
		valuesMap.put("tstoreTransactAmountDimension", mailReports.getReports().get(Constants.SVC_TSTORE).get(2).getDimension());
		valuesMap.put("tstoreTransactAmountDimensionUrl", mailReports.getReports().get(Constants.SVC_TSTORE).get(2).getDimensionImageUrl());
		valuesMap.put("tstoreTransactAmountPeriod", mailReports.getReports().get(Constants.SVC_TSTORE).get(2).getPeriod());
		valuesMap.put("tstoreTransactAmountBasicValue",	NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_TSTORE).get(2).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("tstoreTransactAmountDimensionUnit", mailReports.getReports().get(Constants.SVC_TSTORE).get(2).getDimensionUnit());
		valuesMap.put("tstoreTransactAmountIncreaseValue", mailReports.getReports().get(Constants.SVC_TSTORE).get(2).getIncreaseValue());
		valuesMap.put("tstoreTransactAmountIncreaseColor", mailReports.getReports().get(Constants.SVC_TSTORE).get(2).getIncreaseColor());
		// 월누적 거래액
		valuesMap.put("tstoreMtransactAmountBrandUrl", mailReports.getReports().get(Constants.SVC_TSTORE).get(3).getBrandImageUrl());
		valuesMap.put("tstoreMtransactAmountDimension", mailReports.getReports().get(Constants.SVC_TSTORE).get(3).getDimension());
		valuesMap.put("tstoreMtransactAmountDimensionUrl", mailReports.getReports().get(Constants.SVC_TSTORE).get(3).getDimensionImageUrl());
		valuesMap.put("tstoreMtransactAmountPeriod", mailReports.getReports().get(Constants.SVC_TSTORE).get(3).getPeriod());
		valuesMap.put("tstoreMtransactAmountBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_TSTORE).get(3).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("tstoreMtransactAmountDimensionUnit", mailReports.getReports().get(Constants.SVC_TSTORE).get(3).getDimensionUnit());
		valuesMap.put("tstoreMtransactAmountIncreaseValue", mailReports.getReports().get(Constants.SVC_TSTORE).get(3).getIncreaseValue());
		valuesMap.put("tstoreMtransactAmountIncreaseColor", mailReports.getReports().get(Constants.SVC_TSTORE).get(3).getIncreaseColor());
		valuesMap.put("tstoreComment", mailReports.getTstoreComment());

		// Tmap DAU
		valuesMap.put("tmapDauBrandUrl", mailReports.getReports().get(Constants.SVC_TMAP).get(0).getBrandImageUrl());
		valuesMap.put("tmapDauDimension", mailReports.getReports().get(Constants.SVC_TMAP).get(0).getDimension());
		valuesMap.put("tmapDauDimensionUrl", mailReports.getReports().get(Constants.SVC_TMAP).get(0).getDimensionImageUrl());
		valuesMap.put("tmapDauPeriod", mailReports.getReports().get(Constants.SVC_TMAP).get(0).getPeriod());
		valuesMap.put("tmapDauBasicValue",  NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_TMAP).get(0).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("tmapDauDimensionUnit", mailReports.getReports().get(Constants.SVC_TMAP).get(0).getDimensionUnit());
		valuesMap.put("tmapDauIncreaseValue", mailReports.getReports().get(Constants.SVC_TMAP).get(0).getIncreaseValue());
		valuesMap.put("tmapDauIncreaseColor", mailReports.getReports().get(Constants.SVC_TMAP).get(0).getIncreaseColor());
		// Tmap 월누적 DAU
		valuesMap.put("tmapMauBrandUrl", mailReports.getReports().get(Constants.SVC_TMAP).get(1).getBrandImageUrl());
		valuesMap.put("tmapMauDimension", mailReports.getReports().get(Constants.SVC_TMAP).get(1).getDimension());
		valuesMap.put("tmapMauDimensionUrl", mailReports.getReports().get(Constants.SVC_TMAP).get(1).getDimensionImageUrl());
		valuesMap.put("tmapMauPeriod", mailReports.getReports().get(Constants.SVC_TMAP).get(1).getPeriod());
		valuesMap.put("tmapMauBasicValue",	NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_TMAP).get(1).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("tmapMauDimensionUnit", mailReports.getReports().get(Constants.SVC_TMAP).get(1).getDimensionUnit());
		valuesMap.put("tmapMauIncreaseValue", mailReports.getReports().get(Constants.SVC_TMAP).get(1).getIncreaseValue());
		valuesMap.put("tmapMauIncreaseColor", mailReports.getReports().get(Constants.SVC_TMAP).get(1).getIncreaseColor());
		valuesMap.put("tmapComment", mailReports.getTmapComment());

		// T cloud UV
		valuesMap.put("tcloudUvBrandUrl", mailReports.getReports().get(Constants.SVC_TCLOUD).get(0).getBrandImageUrl());
		valuesMap.put("tcloudUvDimension", mailReports.getReports().get(Constants.SVC_TCLOUD).get(0).getDimension());
		valuesMap.put("tcloudUvDimensionUrl", mailReports.getReports().get(Constants.SVC_TCLOUD).get(0).getDimensionImageUrl());
		valuesMap.put("tcloudUvPeriod", mailReports.getReports().get(Constants.SVC_TCLOUD).get(0).getPeriod());
		valuesMap.put("tcloudUvBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_TCLOUD).get(0).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("tcloudUvDimensionUnit", mailReports.getReports().get(Constants.SVC_TCLOUD).get(0).getDimensionUnit());
		valuesMap.put("tcloudUvIncreaseValue", mailReports.getReports().get(Constants.SVC_TCLOUD).get(0).getIncreaseValue());
		valuesMap.put("tcloudUvIncreaseColor", mailReports.getReports().get(Constants.SVC_TCLOUD).get(0).getIncreaseColor());
		// 월누적 UV
		valuesMap.put("tcloudMuvBrandUrl", mailReports.getReports().get(Constants.SVC_TCLOUD).get(1).getBrandImageUrl());
		valuesMap.put("tcloudMuvDimension", mailReports.getReports().get(Constants.SVC_TCLOUD).get(1).getDimension());
		valuesMap.put("tcloudMuvDimensionUrl", mailReports.getReports().get(Constants.SVC_TCLOUD).get(1).getDimensionImageUrl());
		valuesMap.put("tcloudMuvPeriod", mailReports.getReports().get(Constants.SVC_TCLOUD).get(1).getPeriod());
		valuesMap.put("tcloudMuvBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_TCLOUD).get(1).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("tcloudMuvDimensionUnit", mailReports.getReports().get(Constants.SVC_TCLOUD).get(1).getDimensionUnit());
		valuesMap.put("tcloudMuvIncreaseValue", mailReports.getReports().get(Constants.SVC_TCLOUD).get(1).getIncreaseValue());
		valuesMap.put("tcloudMuvIncreaseColor", mailReports.getReports().get(Constants.SVC_TCLOUD).get(1).getIncreaseColor());
		valuesMap.put("tcloudComment", mailReports.getTcloudComment());

		// Hoppin 컨텐츠 이용건수
		valuesMap.put("hoppinContentsUserBrandUrl", mailReports.getReports().get(Constants.SVC_HOPPIN).get(0).getBrandImageUrl());
		valuesMap.put("hoppinContentsUserDimension", mailReports.getReports().get(Constants.SVC_HOPPIN).get(0).getDimension());
		valuesMap.put("hoppinContentsUserDimensionUrl", mailReports.getReports().get(Constants.SVC_HOPPIN).get(0).getDimensionImageUrl());
		valuesMap.put("hoppinContentsUserPeriod", mailReports.getReports().get(Constants.SVC_HOPPIN).get(0).getPeriod());
		valuesMap.put("hoppinContentsUserBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_HOPPIN).get(0).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("hoppinContentsUserDimensionUnit", mailReports.getReports().get(Constants.SVC_HOPPIN).get(0).getDimensionUnit());
		valuesMap.put("hoppinContentsUserIncreaseValue", mailReports.getReports().get(Constants.SVC_HOPPIN).get(0).getIncreaseValue());
		valuesMap.put("hoppinContentsUserIncreaseColor", mailReports.getReports().get(Constants.SVC_HOPPIN).get(0).getIncreaseColor());
		// 월누적 컨텐츠 이용건수
		valuesMap.put("hoppinMcontentsUserBrandUrl", mailReports.getReports().get(Constants.SVC_HOPPIN).get(1).getBrandImageUrl());
		valuesMap.put("hoppinMcontentsUserDimension", mailReports.getReports().get(Constants.SVC_HOPPIN).get(1).getDimension());
		valuesMap.put("hoppinMcontentsUserDimensionUrl", mailReports.getReports().get(Constants.SVC_HOPPIN).get(1).getDimensionImageUrl());
		valuesMap.put("hoppinMcontentsUserPeriod", mailReports.getReports().get(Constants.SVC_HOPPIN).get(1).getPeriod());
		valuesMap.put("hoppinMcontentsUserBasicValue", NumberUtil.formatNumberByPoint(mailReports.getReports().get(Constants.SVC_HOPPIN).get(1).getMilliBasicValue().doubleValue(), 0));
		valuesMap.put("hoppinMcontentsUserDimensionUnit", mailReports.getReports().get(Constants.SVC_HOPPIN).get(1).getDimensionUnit());
		valuesMap.put("hoppinMcontentsUserIncreaseValue", mailReports.getReports().get(Constants.SVC_HOPPIN).get(1).getIncreaseValue());
		valuesMap.put("hoppinMcontentsUserIncreaseColor", mailReports.getReports().get(Constants.SVC_HOPPIN).get(1).getIncreaseColor());
		valuesMap.put("hoppinComment", mailReports.getHoppinComment());
		return valuesMap;
	}

	/**
	 * @개요 : 메일 발송 및 결과 정보 저장(KID용).
	 *
	 * @param jqGridRequest
	 *            메일 발송 날짜.
	 * @return AjaxResult.
	 *
	 */
	@Override
	@Transactional
	public AjaxResult sendMailsForKid(JqGridRequest jqGridRequest) throws Exception {
		BpmMgntEmailSndCtt bpmMgntEmailSndCtt = new BpmMgntEmailSndCtt();
		bpmMgntEmailSndCtt.setSndDtm(jqGridRequest.getBasicDate());
		bpmMgntEmailSndCtt.setSndObjId(3l);
		BpmMgntEmailSndCtt emailSndCtt = orgUserRepository.getBpmMgntEmailSndCtt(bpmMgntEmailSndCtt);
		List<OrgUser> orgUsers = orgUserRepository.getEmailOrgUser(3l);
		int occurError = 0;
		for (OrgUser orguser : orgUsers) {
			try {
				if (StringUtils.isEmpty(orguser.getEmailAddr())) {
					log.info("email is null {}", orguser.getLoginId());
					continue;
				}
				SimpleMailMessage msg = new SimpleMailMessage();
				msg.setFrom("SK플래닛 보이저 <" + emailSndCtt.getSndEmailAddr() + ">");
				msg.setSubject(emailSndCtt.getEmailSndTitleNm());
				msg.setText(emailSndCtt.getEmailCtxtCtt());
				msg.setTo(orguser.getEmailAddr());
				mailServiceImpl.sendBoss(msg);

				BpmMgntEmailSndHistory bpmMgntEmailSndHistory = new BpmMgntEmailSndHistory();
				bpmMgntEmailSndHistory.setSndYn("Y");
				bpmMgntEmailSndHistory.setAuditId(jqGridRequest.getUsername());
				bpmMgntEmailSndHistory.setRcvEmailAddr(orguser.getEmailAddr());
				bpmMgntEmailSndHistory.setRcvId(orguser.getLoginId());
				bpmMgntEmailSndHistory.setSndDtm(jqGridRequest.getBasicDate());
				bpmMgntEmailSndHistory.setSndObjId(3l);
				orgUserRepository.createBpmMgntEmailSndHistory(bpmMgntEmailSndHistory);
			} catch (Exception e) {
				log.error("sendMailsForKid {}", e);
				occurError++;
				BpmMgntEmailSndHistory bpmMgntEmailSndHistory = new BpmMgntEmailSndHistory();
				bpmMgntEmailSndHistory.setSndYn("N");
				bpmMgntEmailSndHistory.setAuditId(jqGridRequest.getUsername());
				bpmMgntEmailSndHistory.setRcvEmailAddr(orguser.getEmailAddr());
				bpmMgntEmailSndHistory.setRcvId(orguser.getLoginId());
				bpmMgntEmailSndHistory.setSndDtm(jqGridRequest.getBasicDate());
				bpmMgntEmailSndHistory.setSndObjId(3l);
				orgUserRepository.createBpmMgntEmailSndHistory(bpmMgntEmailSndHistory);
			}
		}
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setAuditId(jqGridRequest.getUsername());
		if (occurError < 1) {
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} else {
			ajaxResult.setCode(998);
			ajaxResult.setMessage("mail_fail");
		}
		return ajaxResult;
	}
}
