package com.skplanet.bisportal.service.bip;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.bip.DayVisitor;
import com.skplanet.bisportal.repository.bip.StatisticsRepository;

/**
 * voyager 통계 Admin 서비스 구현 클래스.
 *
 * @author kyoungoh lee
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    public List<DayVisitor> getDayVisitor(JqGridRequest jqGridRequest) throws Exception {
        return statisticsRepository.getDayVisitor(jqGridRequest);
    }
}
