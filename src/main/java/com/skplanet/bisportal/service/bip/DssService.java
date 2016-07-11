package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.model.bip.Dss;
import com.skplanet.bisportal.model.bip.DssRequest;
import com.skplanet.bisportal.model.bip.TreeMenu;

import java.util.List;

/**
 * Created by seoseungho on 2014. 9. 15..
 */
public interface DssService {
    // BM 리스트를 조회
    List<TreeMenu> getBmList();

    // DSS 조회
    Dss getDss(long dssId);

    // index page lastest DSS 조회
    Dss getLatestDss();

    // DSS 등록
    int addDss(Dss dss);

    // DSS 수정
    int updateDss(Dss dss);

    // 다음 DSS 조회
    Dss getNextDss(long dssId);

    // 이전 DSS 조회
    Dss getPreviousDss(long dssId);

    // DSS 삭제
    int deleteDss(long dssId, String deleteId);

    // DSS 목록 조회
    List<Dss> getDssList(DssRequest dssRequest);
}
