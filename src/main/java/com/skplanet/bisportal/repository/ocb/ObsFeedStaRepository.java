package com.skplanet.bisportal.repository.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsFeedSta;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cookatrice on 2014. 5. 15..
 */
@Repository
public class ObsFeedStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

    /**
     * Feed 노출 DAY - Grid(세로형) 기존 feed 클릭분리 (현 사용중....)
     * @param jqGridRequest
     * @return
     */
	public List<ObsFeedSta> getFeedsExposureForGridPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getFeedsExposureForGridPerDay", jqGridRequest);
	}

    /**
     * Feed 클릭 노출순서별 DAY - pivot(세로형) 기존 feed 클릭분리
     * @param jqGridRequest
     * @return
     */
	public List<ObsFeedSta> getFeedsExposureOrderForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getFeedsExposureOrderForPivotPerDay", jqGridRequest);
	}

    /**
     * Feed 클릭 노출순서별 DAY - Grid(세로형) 기존 feed 클릭분리
     * @param jqGridRequest
     * @return
     */
	public List<ObsFeedSta> getFeedsExposureOrderForGridPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getFeedsExposureOrderForGridPerDay", jqGridRequest);
	}

}
