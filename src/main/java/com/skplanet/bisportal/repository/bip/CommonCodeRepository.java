package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.model.bip.CommonCode;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by sjune on 2014-08-18.
 *
 * @author sjune
 */
@Repository
public class CommonCodeRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 날짜기준 고객별 액션 - 일자별
	 * 
	 * @param name
	 * @return commonCode by name
	 */
	public CommonCode getCommonCodeByName(String name) {
		return sqlSession.selectOne("getCommonCodeByName", name);
	}
}
