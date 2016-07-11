package com.skplanet.bisportal.service.bip;

import java.util.List;

import com.skplanet.bisportal.common.model.HelpDeskRequest;
import com.skplanet.bisportal.common.model.JiraApiRequest;
import com.skplanet.bisportal.model.bip.HelpDesk;

/**
 * @author cookatrice
 */
public interface HelpDeskService {
    int increseHitCount(HelpDeskRequest helpDeskRequest);

    List<HelpDesk> getHelpDeskPrevNextItem(HelpDeskRequest helpDeskRequest);

	int deleteHelpDeskSelection(HelpDesk helpDesk);

	List<HelpDesk> getMainPageNotice();

	List<HelpDesk> getLastMainPageNotice();

	List<HelpDesk> getHelpDeskNoticeList(HelpDeskRequest helpDeskRequest);

	int insertHelpDeskNotice(HelpDesk helpDesk);

	int updateHelpDeskNotice(HelpDesk helpDesk);

	int updateLastMainPageNotice(HelpDesk helpDesk);

	List<HelpDesk> getHelpDeskFaqList(HelpDeskRequest helpDeskRequest);

	int insertHelpDeskFaq(HelpDesk helpDesk);

    int updateHelpDeskFaq(HelpDesk helpDesk);

	List<HelpDesk> getHelpDeskQnaCount(HelpDeskRequest helpDeskRequest);

	List<HelpDesk> getHelpDeskQnaList(HelpDeskRequest helpDeskRequest);

	int insertHelpDeskQna(HelpDesk helpDesk);

	int updateHelpDeskQna(HelpDesk helpDesk);

	int updateHelpDeskQnaReplyCompletion(HelpDesk helpDesk);

    List<HelpDesk> getHelpDeskWorkRequestListCount();

    List<HelpDesk> getHelpDeskWorkRequestList(HelpDeskRequest helpDeskRequest);

    //todo include work request services
    // controller와 service는 카테고리...
    List<JiraApiRequest> getHelpDeskWorkRequestApprovalPerson(String searchId);

    List<JiraApiRequest> getHelpDeskWorkRequestReferencePersons(String searchName);

    /**
     * 팝업창 관리
     * getPopupList() : 팝업리스트 조회(all)
     * getPopupYList() : 팝업리스트 조회(popup_yn = 'Y')
     * getSelectedPopupInfo() : 선택 팝업 조회
     * insertPopupNotice() : 공지사항추가(팝업페이지 관리)
     * updatePopupNotice() : 공지사항수정(팝업페이지 관리)
     */
    List<HelpDesk> getPopupList();

    List<HelpDesk> getPopupYList();

    List<HelpDesk> getSelectedPopupInfo(String id);

	int insertPopupNotice(HelpDesk helpDesk);

	int updatePopupNotice(HelpDesk helpDesk);

    int quickClosePopup(HelpDesk helpDesk);
}
