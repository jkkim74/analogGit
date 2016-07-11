package com.skplanet.bisportal.repository.bip;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.HelpDeskRequest;
import com.skplanet.bisportal.model.bip.HelpDeskReply;

/**
 * HelpDeskReplyRepository class.
 * 
 * @author cookatrice
 */
@Repository
public class HelpDeskReplyRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	public List<HelpDeskReply> selectHelpDeskQnaReply(HelpDeskRequest helpDeskRequest) {
		return sqlSession.selectList("selectHelpDeskQnaReply", helpDeskRequest);

	}

	public int insertHelpDeskQnaReply(HelpDeskReply helpDeskReply) {
		return sqlSession.insert("insertHelpDeskQnaReply", helpDeskReply);
	}

	public int updateHelpDeskQnaReply(HelpDeskReply helpDeskReply) {
		return sqlSession.delete("updateHelpDeskQnaReply", helpDeskReply);
	}

	public int deleteHelpDeskQnaReply(HelpDeskReply helpDeskReply) {
		return sqlSession.delete("deleteHelpDeskQnaReply", helpDeskReply);
	}

}
