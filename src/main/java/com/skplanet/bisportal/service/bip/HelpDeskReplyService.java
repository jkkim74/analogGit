package com.skplanet.bisportal.service.bip;

import java.util.List;

import com.skplanet.bisportal.common.model.HelpDeskRequest;
import com.skplanet.bisportal.model.bip.HelpDeskReply;

/**
 * @author cookatrice
 */
public interface HelpDeskReplyService {
	List<HelpDeskReply> selectHelpDeskQnaReply(HelpDeskRequest helpDeskRequest);

	int insertHelpDeskQnaReply(HelpDeskReply helpDeskReply);

	int updateHelpDeskQnaReply(HelpDeskReply helpDeskReply);

	int deleteHelpDeskQnaReply(HelpDeskReply helpDeskReply);
}
