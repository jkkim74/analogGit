package com.skplanet.bisportal.repository.mstr;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.model.mstr.MstrWeek;

/**
 * The MstrBipRepository class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Repository
public class MstrBipRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 기준일에 대한 주차 정보
	 *
	 * @param stnDate
	 * @return
	 */
	public List<MstrWeek> getWeeks(String stnDate) {
		return sqlSession.selectList("getWeeks", stnDate);
	}
}
