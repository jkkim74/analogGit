package com.skplanet.bisportal.repository.tcloud;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.tcloud.TCloudMenuStat;

/**
 * The CloudRepository class.
 * 
 * @author seoseungho (seosh81@gmail.com)
 * 
 */
@Repository
public class CloudRepository {
	@Resource(name = "tcloudSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * tcloud 일별 데이터 셋
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<TCloudMenuStat> getTCloudMenuStatPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getTCloudMenuStatPerDay", jqGridRequest);
	}

	/**
	 * tcloud 월별 데이터 셋
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<TCloudMenuStat> getTCloudMenuStatPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getTCloudMenuStatPerMonth", jqGridRequest);
	}

}
