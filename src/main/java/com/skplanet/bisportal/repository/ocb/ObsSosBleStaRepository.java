package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsSosBleSta;

/**
 * The ObsSosBleStaRepository class.
 *
 * @author cookatrice
 *
 */
@Repository
public class ObsSosBleStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	public List<ObsSosBleSta> getBleDiffData(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getBleDiffData", jqGridRequest);
	}
}
