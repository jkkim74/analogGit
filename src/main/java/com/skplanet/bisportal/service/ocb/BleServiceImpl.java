package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.ComStdDt;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.BleNewTech;
import com.skplanet.bisportal.repository.ocb.BleNewTechRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isSum;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * Created by lko on 2014-11-21.
 */
@Service
public class BleServiceImpl implements BleService {
    @Autowired
    private BleNewTechRepository bleNewTechRepository;

    @Override
    public List<BleNewTech> getMerchantDetail(JqGridRequest jqGridRequest) throws Exception {
        return bleNewTechRepository.getMerchantDetail(jqGridRequest);
    }

    @Override
    public List<BleNewTech> getServiceForGrid(JqGridRequest jqGridRequest) throws Exception {
           if (isSum(jqGridRequest.getDateType())) {
               return bleNewTechRepository.getNewTechBySumTotal(jqGridRequest);
           } else if (isDay(jqGridRequest.getDateType())) {
               return bleNewTechRepository.getNewTechByDailyTotal(jqGridRequest);
           } else if(isWeek(jqGridRequest.getDateType())) {
               List<ComStdDt>  comStdDts = bleNewTechRepository.getWeekNumberOfMonth(jqGridRequest);
               if (!CollectionUtils.isEmpty(comStdDts)) {
                   jqGridRequest.setStartDate(comStdDts.get(0).getStdYmWkCd());
                   jqGridRequest.setEndDate(comStdDts.get(1).getStdYmWkCd());
                   return bleNewTechRepository.getNewTechByWeeklyTotal(jqGridRequest);
               }
           } else {//월별
               return bleNewTechRepository.getNewTechByMonthlyTotal(jqGridRequest);
           }
        return null;
    }

    @Override
    public List<BleNewTech> getServiceForExcel(JqGridRequest jqGridRequest) throws Exception {
        if (isSum(jqGridRequest.getDateType())) {
            return bleNewTechRepository.getNewTechBySumTotalForExcel(jqGridRequest);
        } else if (isDay(jqGridRequest.getDateType())) {
            return bleNewTechRepository.getNewTechByDailyTotalForExcel(jqGridRequest);
        } else if(isWeek(jqGridRequest.getDateType())) {
            List<ComStdDt>  comStdDts = bleNewTechRepository.getWeekNumberOfMonth(jqGridRequest);
            if (!CollectionUtils.isEmpty(comStdDts)) {
                jqGridRequest.setStartDate(comStdDts.get(0).getStdYmWkCd());
                jqGridRequest.setEndDate(comStdDts.get(1).getStdYmWkCd());
                return bleNewTechRepository.getNewTechByWeeklyTotalForExcel(jqGridRequest);
            }
        } else {//월별
            return bleNewTechRepository.getNewTechByMonthlyTotalForExcel(jqGridRequest);
        }
        return null;
    }
}
