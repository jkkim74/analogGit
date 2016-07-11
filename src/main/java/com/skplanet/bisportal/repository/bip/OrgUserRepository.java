package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.bip.*;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * The OrgUserRepository class.
 * 
 * @author sjune
 */
@Slf4j
@Repository
public class OrgUserRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	public List<OrgUser> getOrgUserTree(OrgUser orgUser) {
		return sqlSession.selectList("getOrgUserTree", orgUser);
	}

	public List<ComDept> getOrgGrid(OrgUser orgUser) {
		return sqlSession.selectList("getOrgGrid", orgUser);
	}

	public List<OrgUser> getOrgTree() {
		return sqlSession.selectList("getOrgTree");
	}

	public List<OrgUser> getOrgUsers(OrgUser orgUser) {
		return sqlSession.selectList("getOrgUserTree", orgUser);
	}

	public List<OrgUser> getEmailOrgUser(Long sndObjId) {
		return sqlSession.selectList("getEmailOrgUser", sndObjId);
	}

//	public List<BpmDlyPrst> getMailDatas(JqGridRequest jqGridRequest) {
//		return sqlSession.selectList("getMailDatas", jqGridRequest);
//	}

	/**
	 * OCB/Syrup의 경영실적 이메일 발송 지표를 조회.
	 *
	 * @param jqGridRequest
	 */
	public List<BpmDlyPrst> getBusinessOcbSyrupDatas(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getBusinessOcbSyrupDatas", jqGridRequest);
	}

	/**
	 * OCB/Syrup 이외의 경영실적 이메일 발송 지표를 조회.
	 *
	 * @param jqGridRequest
	 */
	public List<BpmDlyPrst> getBusinessDatas(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getBusinessDatas", jqGridRequest);
	}

	public List<BpmMgntRsltScrnCmnt> getBpmMgntRsltScrnCmnts(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getBpmMgntRsltScrnCmnts", jqGridRequest);
	}

	public void createBpmMgntRsltScrnCmnt(BpmMgntRsltScrnCmnt bpmMgntRsltScrnCmnt) {
		sqlSession.insert("createBpmMgntRsltScrnCmnt", bpmMgntRsltScrnCmnt);
	}

	public int deleteBpmMgntEmailSndUser(String[] orgUserArray) {
		return sqlSession.delete("deleteBpmMgntEmailSndUser", orgUserArray);
	}

	public int deleteBpmMgntEmailSndUserForKid(String[] orgUserArray) {
		return sqlSession.delete("deleteBpmMgntEmailSndUserForKid", orgUserArray);
	}

	public int createBpmMgntEmailSndUser(BpmMgntEmailSndUser bpmMgntEmailSndUser) {
		return sqlSession.insert("createBpmMgntEmailSndUser", bpmMgntEmailSndUser);
	}

	public BpmMgntEmailSndCtt getBpmMgntEmailSndCtt(BpmMgntEmailSndCtt bpmMgntEmailSndCtt) {
		return sqlSession.selectOne("getBpmMgntEmailSndCtt", bpmMgntEmailSndCtt);
	}

	public BpmMgntEmailSndObj getBpmMgntEmailSndObj(Long sndObjId) {
		return sqlSession.selectOne("getBpmMgntEmailSndObj", sndObjId);
	}

	public int createBpmMgntEmailSndCtt(BpmMgntEmailSndCtt bpmMgntEmailSndCtt) {
		return sqlSession.insert("createBpmMgntEmailSndCtt", bpmMgntEmailSndCtt);
	}

	public int updateBpmMgntEmailSndCtt(BpmMgntEmailSndCtt bpmMgntEmailSndCtt) {
		return sqlSession.insert("updateBpmMgntEmailSndCtt", bpmMgntEmailSndCtt);
	}

	public int createBpmMgntEmailSndHistory(BpmMgntEmailSndHistory bpmMgntEmailSndHistory) {
		return sqlSession.insert("createBpmMgntEmailSndHistory", bpmMgntEmailSndHistory);
	}
}
