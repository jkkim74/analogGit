package com.skplanet.bisportal.repository.bip;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.model.bip.CommonGroupCode;

/**
 * CommonGroupCodeRepository class.
 * 
 * @author cookatrice
 */

@Repository
public class CommonGroupCodeRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	public List<CommonGroupCode> getCodesByGroupCodeId(String groupCodeId) {
		return sqlSession.selectList("getCodesByGroupCodeId", groupCodeId);
	}

}
