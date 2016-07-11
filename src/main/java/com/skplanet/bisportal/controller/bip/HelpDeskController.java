package com.skplanet.bisportal.controller.bip;

import com.google.common.collect.Maps;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.HelpDeskRequest;
import com.skplanet.bisportal.common.model.JiraApiRequest;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.model.bip.HelpDesk;
import com.skplanet.bisportal.service.bip.HelpDeskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author cookatrice
 */
@Slf4j
@Controller
@RequestMapping("/helpdesk")
public class HelpDeskController {
	public static final String RESULT_CODE_SUCCESS = "0000";
	public static final String RESULT_CODE_INTERNAL_ERROR = "1000";

	@Autowired
	private HelpDeskService helpDeskServiceImpl;

	/**
	 * common 조회수 증가
	 */
	@RequestMapping(value = "/increseHitCount", method = RequestMethod.GET)
	@ResponseBody
	public int increseHitCount(HelpDeskRequest helpDeskRequest) {
		return helpDeskServiceImpl.increseHitCount(helpDeskRequest);
	}

	/**
	 * common 선택 게시물 이전, 이후 게시물 리턴
	 */
	@RequestMapping(value = "/prevNextItem", method = RequestMethod.POST)
	@ResponseBody
	public List<HelpDesk> getHelpDeskPrevNextItem(@RequestBody HelpDeskRequest helpDeskRequest) {
		return helpDeskServiceImpl.getHelpDeskPrevNextItem(helpDeskRequest);
	}

	/**
	 * common 선택 게시물 삭제(delete_yn = y처리)
	 */
	@RequestMapping(value = "/deleteSelection", method = RequestMethod.POST)
	@ResponseBody
	public int deleteHelpDeskSelection(@RequestBody HelpDesk helpDesk) {
		return helpDeskServiceImpl.deleteHelpDeskSelection(helpDesk);
	}

	/**
	 * 메인페이지 공지사항 조회 (main page important notice, just 1 or not)
	 */
	@RequestMapping(value = "/mainPage/notice", method = RequestMethod.GET)
	@ResponseBody
	public List<HelpDesk> getMainPageNotice() {
		return helpDeskServiceImpl.getMainPageNotice();
	}

	/**
	 * notice 공지사항 리스트
	 */
	@RequestMapping(value = "/notice/list", method = RequestMethod.POST)
	@ResponseBody
	public List<HelpDesk> getHelpDeskNoticeList(@RequestBody HelpDeskRequest helpDeskRequest) {
		return helpDeskServiceImpl.getHelpDeskNoticeList(helpDeskRequest);
	}

	/**
	 * notice 공지사항 등록
	 */
	@RequestMapping(value = "/notice/insert", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertHelpDeskNotice(@RequestBody HelpDesk helpDesk) {
		Map<String, Object> result = Maps.newHashMap();
		result.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
		// check main notice(update latest notice's period)
		if (StringUtils.equals("important", helpDesk.getCategory())) {
			List<HelpDesk> tmpNotice = helpDeskServiceImpl.getLastMainPageNotice();
			if (tmpNotice.size() > 0) {
				HelpDesk lastNotice = tmpNotice.get(0);
				if (DateUtil.compareStartDate(lastNotice.getPopupStartDate(), helpDesk.getPopupStartDate())) {
					HelpDesk updateNotice = helpDesk;
					updateNotice.setId(lastNotice.getId());
					if (helpDeskServiceImpl.updateLastMainPageNotice(updateNotice) > 0) {
						result.put("infoMessage", "update success.");
					}
				} else {
					result.put("errMessage", "기 등록된 날짜보다 이전 시작날짜 입니다. 날짜를 확인하세요.");
					return result;
				}
			} else {
				result.put("infoMessage", "99991231이 없음, just insert.");
			}
		}
		// insert logic
		if (helpDeskServiceImpl.insertHelpDeskNotice(helpDesk) > 0) {
			result.put(Constants.CODE, RESULT_CODE_SUCCESS);
			result.put("contentId", helpDesk.getId());
		}
		return result;
	}

	/**
	 * notice 공지사항 수정
	 */
	@RequestMapping(value = "/notice/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateHelpDeskNotice(@RequestBody HelpDesk helpDesk) {
		Map<String, Object> result = Maps.newHashMap();
		result.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
		if (helpDeskServiceImpl.updateHelpDeskNotice(helpDesk) > 0) {
			result.put(Constants.CODE, RESULT_CODE_SUCCESS);
			result.put("contentId", helpDesk.getId());
		}
		return result;
	}

	/**
	 * faq faq 리스트
	 */
	@RequestMapping(value = "/faq/list", method = RequestMethod.POST)
	@ResponseBody
	public List<HelpDesk> getHelpDeskFaqList(@RequestBody HelpDeskRequest helpDeskRequest) {
		return helpDeskServiceImpl.getHelpDeskFaqList(helpDeskRequest);
	}

	/**
	 * faq faq 등록
	 */
	@RequestMapping(value = "/faq/insert", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertHelpDeskFaq(@RequestBody HelpDesk helpDesk) {
		Map<String, Object> result = Maps.newHashMap();
		result.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
		if (helpDeskServiceImpl.insertHelpDeskFaq(helpDesk) > 0) {
			result.put(Constants.CODE, RESULT_CODE_SUCCESS);
			result.put("contentId", helpDesk.getId());
		}
		return result;
	}

	/**
	 * faq faq 수정
	 */
	@RequestMapping(value = "/faq/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateHelpDeskFaq(@RequestBody HelpDesk helpDesk) {
		Map<String, Object> result = Maps.newHashMap();
		result.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
		if (helpDeskServiceImpl.updateHelpDeskFaq(helpDesk) > 0) {
			result.put(Constants.CODE, RESULT_CODE_SUCCESS);
			result.put("contentId", helpDesk.getId());
		}
		return result;
	}

	/**
	 * qna total count of qna list for pagination
	 */
	@RequestMapping(value = "/qna/listCount", method = RequestMethod.POST)
	@ResponseBody
	public List<HelpDesk> getHelpDeskQnaCount(@RequestBody HelpDeskRequest helpDeskRequest) {
		return helpDeskServiceImpl.getHelpDeskQnaCount(helpDeskRequest);
	}

	/**
	 * qna qna 리스트
	 */
	@RequestMapping(value = "/qna/list", method = RequestMethod.POST)
	@ResponseBody
	public List<HelpDesk> getHelpDeskQnaList(@RequestBody HelpDeskRequest helpDeskRequest) {
		return helpDeskServiceImpl.getHelpDeskQnaList(helpDeskRequest);
	}

	/**
	 * qna qna 등록
	 */
	@RequestMapping(value = "/qna/insert", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertHelpDeskQna(@RequestBody HelpDesk helpDesk) {
		Map<String, Object> result = Maps.newHashMap();
		result.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
		if (helpDeskServiceImpl.insertHelpDeskQna(helpDesk) > 0) {
			result.put(Constants.CODE, RESULT_CODE_SUCCESS);
			result.put("contentId", helpDesk.getId());
		}
		return result;
	}

	/**
	 * qna qna 수정
	 */
	@RequestMapping(value = "/qna/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateHelpDeskQna(@RequestBody HelpDesk helpDesk) {
		Map<String, Object> result = Maps.newHashMap();
		result.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
		if (helpDeskServiceImpl.updateHelpDeskQna(helpDesk) > 0) {
			result.put(Constants.CODE, RESULT_CODE_SUCCESS);
			result.put("contentId", helpDesk.getId());
		}
		return result;
	}

	/**
	 * qna qna 답변완료
	 */
	@RequestMapping(value = "/qna/updateReplyCompletion", method = RequestMethod.POST)
	@ResponseBody
	public int updateHelpDeskQnaReplyCompletion(@RequestBody HelpDesk helpDesk) {
		return helpDeskServiceImpl.updateHelpDeskQnaReplyCompletion(helpDesk);
	}

	/**
	 * workRequest total count of 작업요청 list for pagination
	 */
	@RequestMapping(value = "/workRequest/listCount", method = RequestMethod.GET)
	@ResponseBody
	public List<HelpDesk> getHelpDeskWorkRequestListCount() {
		return helpDeskServiceImpl.getHelpDeskWorkRequestListCount();
	}

	/**
	 * workRequest 작업요청 리스트
	 */
	@RequestMapping(value = "/workRequest/list", method = RequestMethod.GET)
	@ResponseBody
	public List<HelpDesk> getHelpDeskWorkRequestList(HelpDeskRequest helpDeskRequest) {
		return helpDeskServiceImpl.getHelpDeskWorkRequestList(helpDeskRequest);
	}

	/**
	 * 결재자 찾기
	 */
	@RequestMapping(value = "/getApproval", method = RequestMethod.GET)
	@ResponseBody
	public List<JiraApiRequest> getApproval(@RequestParam(required = true) String searchId) {
		return helpDeskServiceImpl.getHelpDeskWorkRequestApprovalPerson(searchId);
	}

	/**
	 * 참조자 찾기
	 */
	@RequestMapping(value = "/getReference", method = RequestMethod.GET)
	@ResponseBody
	public List<JiraApiRequest> getReference(@RequestParam(required = true) String searchName) {
		return helpDeskServiceImpl.getHelpDeskWorkRequestReferencePersons(searchName);
	}

	/**
	 * notice 팝업 리스트(all)
	 */
	@RequestMapping(value = "/popup/popupList", method = RequestMethod.GET)
	@ResponseBody
	public List<HelpDesk> getPopupList() {
		return helpDeskServiceImpl.getPopupList();
	}

	/**
	 * notice 팝업 리스트(popup_yn = 'Y')
	 */
	@RequestMapping(value = "/popup/popupYList", method = RequestMethod.GET)
	@ResponseBody
	public List<HelpDesk> getPopupYList() {
		return helpDeskServiceImpl.getPopupYList();
	}

	/**
	 * notice 선택 팝업 정보
	 */
	@RequestMapping(value = "/popup/selectedPopupInfo", method = RequestMethod.GET)
	@ResponseBody
	public List<HelpDesk> getSelectedPopupInfo(@RequestParam(required = true) String id) {
		return helpDeskServiceImpl.getSelectedPopupInfo(id);
	}

	/**
	 * notice 팝업 등록 deprecated...:( just use insertHelpDeskNotice();
	 */
	@RequestMapping(value = "/popup/insert", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertPopupNotice(@RequestBody HelpDesk helpDesk) {
		Map<String, Object> result = Maps.newHashMap();
		result.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
		if (helpDeskServiceImpl.insertPopupNotice(helpDesk) > 0) {
			result.put(Constants.CODE, RESULT_CODE_SUCCESS);
			result.put("contentId", helpDesk.getId());
		}
		return result;
	}

	/**
	 * notice 팝업 수정 deprecated...:( just use updatehelpDeskNotice();
	 */
	@RequestMapping(value = "/popup/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updatePopupNotice(@RequestBody HelpDesk helpDesk) {
		Map<String, Object> result = Maps.newHashMap();
		result.put(Constants.CODE, RESULT_CODE_INTERNAL_ERROR);
		if (helpDeskServiceImpl.updatePopupNotice(helpDesk) > 0) {
			result.put(Constants.CODE, RESULT_CODE_SUCCESS);
			result.put("contentId", helpDesk.getId());
		}
		return result;
	}

	/**
	 * notice 팝업 삭제 ---> deleteHelpDeskSelection 동일 사용
	 */

	/**
	 * notice 팝업 quick close. (popup_yn = 'N', popup_order='' 처리)
	 */
	@RequestMapping(value = "/popup/quickClose", method = RequestMethod.POST)
	@ResponseBody
	public int quickClosePopup(@RequestBody HelpDesk helpDesk) {
		return helpDeskServiceImpl.quickClosePopup(helpDesk);
	}
}
