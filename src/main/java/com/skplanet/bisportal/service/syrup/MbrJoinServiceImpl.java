package com.skplanet.bisportal.service.syrup;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwMbrJoin;
import com.skplanet.bisportal.repository.syrup.SmwMbrJoinRepository;
import com.skplanet.bisportal.repository.syrup.SvcCdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.getWkFrto;
import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * Created by lko on 2014-11-20.
 */
@Service
public class MbrJoinServiceImpl implements MbrJoinService {
    @Autowired
    private SmwMbrJoinRepository smwMbrJoinRepository;

    @Autowired
    private SvcCdRepository svcCdRepository;

    @Override
    public List<SmwMbrJoin> getMbrJoinForGrid(JqGridRequest jqGRidRequest) {
        if(isDay(jqGRidRequest.getDateType())) {
            return smwMbrJoinRepository.getMbrJoinPerDay(jqGRidRequest);
        } else if(isWeek(jqGRidRequest.getDateType())) {
            jqGRidRequest = getWkFrto(jqGRidRequest, svcCdRepository.getWkStrdFrto(jqGRidRequest));
            return smwMbrJoinRepository.getMbrJoinPerWeek(jqGRidRequest);
        } else {
            return smwMbrJoinRepository.getMbrJoinPerMonth(jqGRidRequest);
        }
    }
}
