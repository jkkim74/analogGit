package com.skplanet.bisportal.service.bip;

import java.util.List;

import com.skplanet.bisportal.common.model.JiraApiRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.HelpDeskRequest;
import com.skplanet.bisportal.model.bip.HelpDesk;
import com.skplanet.bisportal.repository.bip.HelpDeskRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cookatrice
 */
@Service
public class HelpDeskServiceImpl implements HelpDeskService {
	@Autowired
	private HelpDeskRepository helpDeskRepository;

	@Override
	@Transactional
	public int increseHitCount(HelpDeskRequest helpDeskRequest) {
		return helpDeskRepository.increseHitCount(helpDeskRequest);
	}

	@Override
	public List<HelpDesk> getHelpDeskPrevNextItem(HelpDeskRequest helpDeskRequest) {
		return helpDeskRepository.getHelpDeskPrevNextItem(helpDeskRequest);
	}

	@Override
	@Transactional
	public int deleteHelpDeskSelection(HelpDesk helpDesk) {
		return helpDeskRepository.deleteHelpDeskSelection(helpDesk);
	}

	@Override
	public List<HelpDesk> getMainPageNotice() {
		return helpDeskRepository.getMainPageNotice();
	}

	@Override
	public List<HelpDesk> getLastMainPageNotice() {
		return helpDeskRepository.getLastMainPageNotice();
	}

	@Override
	public List<HelpDesk> getHelpDeskNoticeList(HelpDeskRequest helpDeskRequest) {
		return helpDeskRepository.getHelpDeskNoticeList(helpDeskRequest);
	}

	@Override
	@Transactional
	public int insertHelpDeskNotice(HelpDesk helpDesk) {
		return helpDeskRepository.insertHelpDeskNotice(helpDesk);
	}

	@Override
	@Transactional
	public int updateHelpDeskNotice(HelpDesk helpDesk) {
		return helpDeskRepository.updateHelpDeskNotice(helpDesk);
	}

	@Override
	@Transactional
	public int updateLastMainPageNotice(HelpDesk helpDesk) {
		return helpDeskRepository.updateLastMainPageNotice(helpDesk);
	}

	@Override
	public List<HelpDesk> getHelpDeskFaqList(HelpDeskRequest helpDeskRequest) {
		return helpDeskRepository.getHelpDeskFaqList(helpDeskRequest);
	}

	@Override
	@Transactional
	public int insertHelpDeskFaq(HelpDesk helpDesk) {
		return helpDeskRepository.insertHelpDeskFaq(helpDesk);
	}

	@Override
	@Transactional
	public int updateHelpDeskFaq(HelpDesk helpDesk) {
		return helpDeskRepository.updateHelpDeskFaq(helpDesk);
	}

	@Override
	public List<HelpDesk> getHelpDeskQnaCount(HelpDeskRequest helpDeskRequest) {
		return helpDeskRepository.getHelpDeskQnaCount(helpDeskRequest);
	}

	@Override
	public List<HelpDesk> getHelpDeskQnaList(HelpDeskRequest helpDeskRequest) {
		return helpDeskRepository.getHelpDeskQnaList(helpDeskRequest);
	}

	@Override
	@Transactional
	public int insertHelpDeskQna(HelpDesk helpDesk) {
		return helpDeskRepository.insertHelpDeskQna(helpDesk);
	}

	@Override
	@Transactional
	public int updateHelpDeskQna(HelpDesk helpDesk) {
		return helpDeskRepository.updateHelpDeskQna(helpDesk);
	}

	@Override
	@Transactional
	public int updateHelpDeskQnaReplyCompletion(HelpDesk helpDesk) {
		return helpDeskRepository.updateHelpDeskQnaReplyCompletion(helpDesk);
	}

	@Override
	public List<HelpDesk> getHelpDeskWorkRequestListCount() {
		return helpDeskRepository.getHelpDeskWorkRequestListCount();
	}

	@Override
	public List<HelpDesk> getHelpDeskWorkRequestList(HelpDeskRequest helpDeskRequest) {
		return helpDeskRepository.getHelpDeskWorkRequestList(helpDeskRequest);
	}

	@Override
	public List<JiraApiRequest> getHelpDeskWorkRequestApprovalPerson(String searchId) {
		return helpDeskRepository.getHelpDeskWorkRequestApprovalPerson(searchId);
	}

	@Override
	public List<JiraApiRequest> getHelpDeskWorkRequestReferencePersons(String searchName) {
		return helpDeskRepository.getHelpDeskWorkRequestReferencePersons(searchName);
	}

	@Override
	public List<HelpDesk> getPopupList() {
		return helpDeskRepository.getPopupList();
	}

	@Override
	public List<HelpDesk> getPopupYList() {
		return helpDeskRepository.getPopupYList();
	}

	@Override
	public List<HelpDesk> getSelectedPopupInfo(String id) {
		return helpDeskRepository.getSelectedPopupInfo(id);
	}

	@Override
    @Transactional
	public int insertPopupNotice(HelpDesk helpDesk) {
		return helpDeskRepository.insertPopupNotice(helpDesk);
	}

	@Override
    @Transactional
	public int updatePopupNotice(HelpDesk helpDesk) {
		return helpDeskRepository.updatePopupNotice(helpDesk);
	}

	@Override
	@Transactional
	public int quickClosePopup(HelpDesk helpDesk) {
		return helpDeskRepository.quickClosePopup(helpDesk);
	}

}
