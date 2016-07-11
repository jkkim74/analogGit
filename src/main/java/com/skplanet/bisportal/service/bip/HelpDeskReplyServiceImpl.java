package com.skplanet.bisportal.service.bip;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.HelpDeskRequest;
import com.skplanet.bisportal.model.bip.HelpDeskReply;
import com.skplanet.bisportal.repository.bip.HelpDeskReplyRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cookatrice
 */
@Service
public class HelpDeskReplyServiceImpl implements HelpDeskReplyService {
	@Autowired
	private HelpDeskReplyRepository helpDeskReplyRepository;

	@Override
	public List<HelpDeskReply> selectHelpDeskQnaReply(HelpDeskRequest helpDeskRequest) {
		return helpDeskReplyRepository.selectHelpDeskQnaReply(helpDeskRequest);
	}

	@Override
    @Transactional
	public int insertHelpDeskQnaReply(HelpDeskReply helpDeskReply) {
		return helpDeskReplyRepository.insertHelpDeskQnaReply(helpDeskReply);
	}

	@Override
    @Transactional
	public int updateHelpDeskQnaReply(HelpDeskReply helpDeskReply) {
		return helpDeskReplyRepository.updateHelpDeskQnaReply(helpDeskReply);
	}

	@Override
    @Transactional
	public int deleteHelpDeskQnaReply(HelpDeskReply helpDeskReply) {
		return helpDeskReplyRepository.deleteHelpDeskQnaReply(helpDeskReply);
	}

}
