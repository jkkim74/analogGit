package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.model.bip.EisSvcComCd;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sjune on 2014-07-01.
 * 
 * @author sjune
 */
@Repository
public class EisSvcComCdRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * svcId로 EIS 서비스 공통코드 조회
	 * 
	 * @param svcId
	 * @return list of EisSvcComCd
	 */
	public List<EisSvcComCd> getComCdsBySvcId(Long svcId) {
		return sqlSession.selectList("getComCdsBySvcId", svcId);
	}
}
