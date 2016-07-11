package com.skplanet.bisportal.service.syrup;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwPushTickerStatDtl;
import com.skplanet.bisportal.repository.syrup.SmwPushTickerStatDtlRepository;

/**
 * Created by lko on 2014-12-05.
 */
@Service
public class EtcServiceImpl implements EtcService{
    @Autowired
    private SmwPushTickerStatDtlRepository smwPushTickerStatDtlRepository;

    @Override
    public List<SmwPushTickerStatDtl> getSmwPushTickerStatDtlGrid(JqGridRequest jqGRidRequest) {
        if(isDay(jqGRidRequest.getDateType())) {
            return smwPushTickerStatDtlRepository.getSmwPushTickerStatDtlPerDay(jqGRidRequest);
        } else if(isWeek(jqGRidRequest.getDateType())) {
            return smwPushTickerStatDtlRepository.getSmwPushTickerStatDtlWeek(jqGRidRequest);
        } else {
            return smwPushTickerStatDtlRepository.getSmwPushTickerStatDtlPerMonth(jqGRidRequest);
        }
    }
}
