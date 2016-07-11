package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.bip.DayVisitor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lko on 2014-09-30.
 */
@Repository
public class StatisticsRepository {
    @Resource(name="bipSqlSession")
    private SqlSessionTemplate sqlSession;

    public List<DayVisitor> getDayVisitor(JqGridRequest jqGridRequest) {
        return sqlSession.selectList("getDayVisitor", jqGridRequest);
    }
}
