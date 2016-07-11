package com.skplanet.bisportal.repository.bip;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.common.model.JiraApiRequest;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.HelpDeskRequest;
import com.skplanet.bisportal.model.bip.HelpDesk;

/**
 * HelpDeskRepository class.
 * 
 * @author cookatrice
 */

@Repository
public class HelpDeskRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	public int increseHitCount(HelpDeskRequest helpDeskRequest) {
		return sqlSession.update("increseHitCount", helpDeskRequest);
	}

	public List<HelpDesk> getHelpDeskPrevNextItem(HelpDeskRequest helpDeskRequest) {
		return sqlSession.selectList("getHelpDeskPrevNextItem", helpDeskRequest);
	}

	public int deleteHelpDeskSelection(HelpDesk helpDesk) {
		return sqlSession.delete("deleteHelpDeskSelection", helpDesk);
	}

	public List<HelpDesk> getMainPageNotice(){
		return sqlSession.selectList("getMainPageNotice");
	}

	public List<HelpDesk> getLastMainPageNotice(){
		return sqlSession.selectList("getLastMainPageNotice");
	}

	public List<HelpDesk> getHelpDeskNoticeList(HelpDeskRequest helpDeskRequest) {
		return sqlSession.selectList("getHelpDeskNoticeList", helpDeskRequest);
	}

	public int insertHelpDeskNotice(HelpDesk helpDesk) {
		return sqlSession.insert("insertHelpDeskNotice", helpDesk);
	}

	public int updateHelpDeskNotice(HelpDesk helpDesk) {
		return sqlSession.update("updateHelpDeskNotice", helpDesk);
	}

	public int updateLastMainPageNotice(HelpDesk helpDesk) {
		return sqlSession.update("updateLastMainPageNotice", helpDesk);
	}

	public List<HelpDesk> getHelpDeskFaqList(HelpDeskRequest helpDeskRequest) {
		return sqlSession.selectList("getHelpDeskFaqList", helpDeskRequest);
	}

	public int insertHelpDeskFaq(HelpDesk helpDesk) {
		return sqlSession.insert("insertHelpDeskFaq", helpDesk);
	}

	public int updateHelpDeskFaq(HelpDesk helpDesk) {
		return sqlSession.update("updateHelpDeskFaq", helpDesk);
	}

	public List<HelpDesk> getHelpDeskQnaCount(HelpDeskRequest helpDeskRequest) {
		return sqlSession.selectList("getHelpDeskQnaCount", helpDeskRequest);
	}

	public List<HelpDesk> getHelpDeskQnaList(HelpDeskRequest helpDeskRequest) {
		return sqlSession.selectList("getHelpDeskQnaList", helpDeskRequest);
	}

	public int insertHelpDeskQna(HelpDesk helpDesk) {
		return sqlSession.insert("insertHelpDeskQna", helpDesk);
	}

	public int updateHelpDeskQna(HelpDesk helpDesk) {
		return sqlSession.update("updateHelpDeskQna", helpDesk);
	}

	public int updateHelpDeskQnaReplyCompletion(HelpDesk helpDesk) {
		return sqlSession.update("updateHelpDeskQnaReplyCompletion", helpDesk);
	}

	public List<HelpDesk> getHelpDeskWorkRequestListCount() {
		return sqlSession.selectList("getHelpDeskWorkRequestListCount");
	}

	public List<HelpDesk> getHelpDeskWorkRequestList(HelpDeskRequest helpDeskRequest) {
		return sqlSession.selectList("getHelpDeskWorkRequestList", helpDeskRequest);
	}

	public List<JiraApiRequest> getHelpDeskWorkRequestApprovalPerson(String searchId) {
		return sqlSession.selectList("getHelpDeskWorkRequestApprovalPerson", searchId);
	}

	public List<JiraApiRequest> getHelpDeskWorkRequestReferencePersons(String searchName) {
		return sqlSession.selectList("getHelpDeskWorkRequestReferencePersons", searchName);
	}

	public List<HelpDesk> getPopupYList() {
		return sqlSession.selectList("getPopupYList");
	}

	public List<HelpDesk> getPopupList() {
		return sqlSession.selectList("getPopupList");
	}

	public List<HelpDesk> getSelectedPopupInfo(String id) {
		return sqlSession.selectList("getSelectedPopupInfo", id);
	}

	public int insertPopupNotice(HelpDesk helpDesk) {
		return sqlSession.insert("insertPopupNotice", helpDesk);
	}

	public int updatePopupNotice(HelpDesk helpDesk) {
		return sqlSession.update("updatePopupNotice", helpDesk);
	}

	public int quickClosePopup(HelpDesk helpDesk) {
		return sqlSession.update("quickClosePopup", helpDesk);
	}

}
