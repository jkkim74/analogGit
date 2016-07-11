package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsMktPushSend;

/**
 * The ObsMktPushSendRepository class.
 *
 * Created by ophelisis on 2015. 8. 12..
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 */
@Repository
public class ObsMktPushSendRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 마케팅 PUSH (일별)
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsMktPushSend> getMarketingPush(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getMarketingPush", jqGridRequest);
	}
}
