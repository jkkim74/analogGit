package com.skplanet.bisportal.repository.bip;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.model.bip.BpmMgntRsltBatOpPrst;

/**
 * The BpmMgntRsltBatOpPrstRepository class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Repository
public class BpmMgntRsltBatOpPrstRepository {

	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * searchDate를 기준으로 7일전까지의 일자를 구한다.
	 *
	 * @param basicDate 조회 날짜
	 * @return BpmWkStrdInfo
	 */
	public List<BpmMgntRsltBatOpPrst> getBpmMgntRsltBatOpPrst(String basicDate) {
		return sqlSession.selectList("getBpmMgntRsltBatOpPrst", basicDate);
	}
}
