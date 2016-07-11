package com.skplanet.bisportal.service.ocb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsDPushRespSta;
import com.skplanet.bisportal.repository.ocb.OcbMktPushSndRsltRepository;

/**
 * ocb 마케팅 push 발송 결과
 *
 * @author kyoungoh lee
 */
@Service
public class OcbMktPushSndRsltServiceImpl implements OcbMktPushSndRsltService {
	@Autowired
	private OcbMktPushSndRsltRepository ocbMktPushSndRsltRepository;

	@Override
	public List<ObsDPushRespSta> getMktPushSndRslt(JqGridRequest jqGridRequest) {
		return ocbMktPushSndRsltRepository.getMktPushSndRslt(jqGridRequest);
	}
}
