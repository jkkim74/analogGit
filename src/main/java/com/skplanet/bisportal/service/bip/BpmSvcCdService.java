package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmSvcCd;

import java.util.List;

/**
 * The BpmSvcCdService interface
 * 
 * @author sjune
 */
public interface BpmSvcCdService {
	/**
	 * 서비스 코드 목록(조회대상 대분류)을 조회한다.
	 *
	 * @return BpmSvcCd
	 */
	List<BpmSvcCd> getBpmSvcs();

	/**
	 * 지표구분 그룹 코드(조회대상 중분류) 목록을 조회한다.
	 *
	 * @param whereCondition
	 * @return BpmSvcCd
	 */
	List<BpmSvcCd> getBpmCycleToGrps(WhereCondition whereCondition);

	/**
	 * 지표구분 코드(조회대상 소분류) 목록을 조회한다.
	 *
	 * @param whereCondition
	 * @return BpmSvcCd
	 */
	List<BpmSvcCd> getBpmGrpToCls(WhereCondition whereCondition);

	/**
	 * 주차 코드를 조회한다.
	 *
	 * @param wkStcStrdYmw
	 * @return BpmSvcCd
	 */
	List<BpmSvcCd> getBpmWkStrds(String wkStcStrdYmw);
}
