package com.skplanet.bisportal.repository.sankey;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.model.sankey.SankeyRequest;
import com.skplanet.bisportal.model.sankey.SankeyResponse;

/**
 * Created by cookatrice on 2015. 8. 11..
 */
@Repository
public class SankeyRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * load sankey chart data set
	 * @param sankeyRequest
	 * @return
	 */
	public List<SankeyResponse> getSankeyDataSet(SankeyRequest sankeyRequest) {
//		return sqlSession.selectList("getSankeyDataSet_bak", sankeyRequest);
		return sqlSession.selectList("getSankeyDataSet", sankeyRequest);
	}

}
