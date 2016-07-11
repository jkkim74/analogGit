package com.skplanet.bisportal.controller.bip;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skplanet.bisportal.common.model.HelpDeskRequest;
import com.skplanet.bisportal.model.bip.HelpDeskReply;
import com.skplanet.bisportal.service.bip.HelpDeskReplyService;

/**
 * @author cookatrice
 */
@Controller
@RequestMapping("/helpdesk/reply")
public class HelpDeskReplyController {
	@Autowired
	private HelpDeskReplyService helpDeskReplyServiceImpl;

	/**
	 * 답글조회
	 */
	@RequestMapping(value = "/qna/select", method = RequestMethod.POST)
	@ResponseBody
	public List<HelpDeskReply> selectHelpDeskQnaReply(@RequestBody HelpDeskRequest helpDeskRequest) {
		return helpDeskReplyServiceImpl.selectHelpDeskQnaReply(helpDeskRequest);
	}

	/**
	 * 답글삽입
	 */
	@RequestMapping(value = "/qna/insert", method = RequestMethod.POST)
	@ResponseBody
	public int insertHelpDeskQnaReply(@RequestBody HelpDeskReply helpDeskReply) {
		return helpDeskReplyServiceImpl.insertHelpDeskQnaReply(helpDeskReply);
	}

	/**
	 * 답글변경
	 */
	@RequestMapping(value = "/qna/update", method = RequestMethod.POST)
	@ResponseBody
	public int updateHelpDeskQnaReply(@RequestBody HelpDeskReply helpDeskReply) {
		return helpDeskReplyServiceImpl.updateHelpDeskQnaReply(helpDeskReply);
	}

	/**
	 * 답글삭제(delete_yn = y처리)
	 */
	@RequestMapping(value = "/qna/delete", method = RequestMethod.POST)
	@ResponseBody
	public int deleteHelpDeskQnaReply(@RequestBody HelpDeskReply helpDeskReply) {
		return helpDeskReplyServiceImpl.deleteHelpDeskQnaReply(helpDeskReply);
	}
}
