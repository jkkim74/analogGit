package com.skplanet.bisportal.service.ocb;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsDPushRespSta;

/**
 * Created by lko on 2014-10-14.
 */
public interface OcbMktPushSndRsltService {
    /*
    마케팅 push 발송 결과
     */
    List<ObsDPushRespSta> getMktPushSndRslt(JqGridRequest jqGridRequest);
}
