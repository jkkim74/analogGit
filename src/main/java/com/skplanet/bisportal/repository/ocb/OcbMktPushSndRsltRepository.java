package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsDPushRespSta;

/**
 * Created by lko on 2014-10-14.
 */
@Repository
public class OcbMktPushSndRsltRepository {
    @Resource(name="ocbSqlSession")
    private SqlSessionTemplate sqlSession;

    /**
     * OCB 마케팅 push 발송 결과를 조회한다.
     *
     * @param jqGridRequest
     * @return OcbMktPushSndRslt
     */
    public List<ObsDPushRespSta> getMktPushSndRslt(JqGridRequest jqGridRequest) {
        return sqlSession.selectList("getMktPushSndRslt", jqGridRequest);
    }
}
