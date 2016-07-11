package com.skplanet.bisportal.repository.bip;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.bip.BpmIdxCttMappInfo;
import com.skplanet.bisportal.model.bip.BpmIdxCttMappInfoTmp;
import com.skplanet.bisportal.model.bip.HandInput;

/**
 * 경영 실적 Admin 리파지토리 클래스.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Repository
public class HandInputRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * Tmap 수기 입력을 위한 일 데이터 조회
	 *
	 * @param jqGridRequest
	 * @return list of HandInput
	 */
	public List<HandInput> getTMapCttMappInfoPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getTMapCttMappInfoPerDay", jqGridRequest);
	}

	/**
	 * Tmap 수기 입력을 위한 주 데이터 조회
	 * 
	 * @param jqGridRequest
	 * @return list of HandInput
	 */
	public List<HandInput> getTMapCttMappInfoPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getTMapCttMappInfoPerWeek", jqGridRequest);
	}

	/**
	 * Tmap 수기 입력을 위한 월 데이터 조회
	 *
	 * @param jqGridRequest
	 * @return list of HandInput
	 */
	public List<HandInput> getTMapCttMappInfoPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getTMapCttMappInfoPerMonth", jqGridRequest);
	}

	/**
	 * Syrup 수기 입력을 위한 일 데이터 조회
	 *
	 * @param jqGridRequest
	 * @return list of HandInput
	 */
	public List<HandInput> getSyrupCttMappInfoPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSyrupCttMappInfoPerDay", jqGridRequest);
	}

	/**
	 * Syrup 수기 입력을 위한 월 데이터 조회
	 *
	 * @param jqGridRequest
	 * @return list of HandInput
	 */
	public List<HandInput> getSyrupCttMappInfoPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSyrupCttMappInfoPerMonth", jqGridRequest);
	}

	/**
	 * Syrup 수기 입력을 위한 월 데이터 조회
	 *
	 * @param svcId
	 * @return count of batch jobs
	 */
	public Integer getBatchJobCheck(Integer svcId) {
		return sqlSession.selectOne("getBatchJobCheck", svcId);
	}

	/**
	 * Tmap Tmp 테이블 삭제
	 *
	 * @param bpmIdxCttMappInfoTmp
	 * @return deleted rows
	 */
	public int deleteBpmIdxCttMappInfoTmp(BpmIdxCttMappInfoTmp bpmIdxCttMappInfoTmp) {
		return sqlSession.delete("deleteBpmIdxCttMappInfoTmp", bpmIdxCttMappInfoTmp);
	}

	public BpmIdxCttMappInfo selectBpmIdxCttMappInfo(String mappStrdDt) {
		return sqlSession.selectOne("selectBpmIdxCttMappInfo", mappStrdDt);
	}

	public int saveBpmIdxCttMappInfo(BpmIdxCttMappInfo bpmIdxCttMappInfo) {
		return sqlSession.insert("saveBpmIdxCttMappInfo", bpmIdxCttMappInfo);
	}

	public int insertBpmIdxCttMappInfoTmp(BpmIdxCttMappInfoTmp bpmIdxCttMappInfoTmp) {
		return sqlSession.insert("insertBpmIdxCttMappInfoTmp", bpmIdxCttMappInfoTmp);
	}
}
