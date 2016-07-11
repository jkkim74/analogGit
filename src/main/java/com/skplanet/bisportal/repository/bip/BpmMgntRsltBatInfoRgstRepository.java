package com.skplanet.bisportal.repository.bip;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.model.bip.BpmMgntRsltBatInfoRgst;

/**
 * The BpmMgntRsltBatInfoRgstRepository class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Repository
public class BpmMgntRsltBatInfoRgstRepository {

	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 전체 배치 관리 정보를 조회한다.
	 *
	 * @return BpmMgntRsltBatInfoRgst
	 */
	public List<BpmMgntRsltBatInfoRgst> getAllBpmMgntRsltBatInfoRgst() {
		return sqlSession.selectList("getAllBpmMgntRsltBatInfoRgst");
	}
}
