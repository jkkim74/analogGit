package com.skplanet.bisportal.repository.bip;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.model.bip.ComDept;
import com.skplanet.bisportal.model.bip.ComPerson;

/**
 * 조직도/직원 수신 관리 리파지토리 클래스.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Repository
public class DeptPersonRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 조직 수신 정보 조회
	 *
	 * @param sendDt
	 * @return list of ComDept
	 */
	public List<ComDept> getDeptInfo(String sendDt) {
		return sqlSession.selectList("getDeptInfo", sendDt);
	}

	/**
	 * 직원 수신 정보 조회
	 *
	 * @param sendDt
	 * @return list of ComPerson
	 */
	public List<ComPerson> getPersonInfo(String sendDt) {
		return sqlSession.selectList("getPersonInfo", sendDt);
	}

	/**
	 * 조직 수신 정보 조회
	 *
	 * @param orgCd
	 * @return list of ComDept
	 */
	public ComDept getComDept(String orgCd) {
		return sqlSession.selectOne("getComDept", orgCd);
	}

	/**
	 * 직원 수신 정보 조회
	 *
	 * @param loginId
	 * @return list of ComPerson
	 */
	public ComPerson getComPerson(String loginId) {
		return sqlSession.selectOne("getComPerson", loginId);
	}

	/**
	 * 조직도 정보 동기화
	 *
	 * @param comDept
	 * @return insert rows
	 */
	public int createComDept(ComDept comDept) {
		return sqlSession.insert("createComDept", comDept);
	}

	/**
	 * 직원 정보 등록
	 *
	 * @param comPerson
	 * @return insert rows
	 */
	public int createComPerson(ComPerson comPerson) {
		return sqlSession.insert("createComPerson", comPerson);
	}

	/**
	 * 직원 정보 변경
	 *
	 * @param comPerson
	 * @return insert rows
	 */
	public int updateComPerson(ComPerson comPerson) {
		return sqlSession.update("updateComPerson", comPerson);
	}

	/**
	 * 조직 히스토리 정보 등록
	 *
	 * @param orgCd
	 * @return insert rows
	 */
	public int createComDeptHst(String orgCd) {
		return sqlSession.insert("createComDeptHst", orgCd);
	}


	/**
	 * 직원 히스토리 정보 등록
	 *
	 * @param loginId
	 * @return insert rows
	 */
	public int createComPersonHst(String loginId) {
		return sqlSession.insert("createComPersonHst", loginId);
	}
}
