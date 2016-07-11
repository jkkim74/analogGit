package com.skplanet.bisportal.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.skplanet.bisportal.model.bip.Dss;
import com.skplanet.bisportal.service.bip.DssService;

/**
 * Created by seoseungho on 2014. 10. 24..
 */
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Slf4j
public class DssServiceImplIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private DssService dssService;

    @Resource(name = "bipDataSource")
    public void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private Dss dss;
    private String userId = "PP44632";

    @Before
    public void setup() {
        dss = new Dss();
        dss.setSubject("test subject");
        dss.setBmIdList(new ArrayList<Long>(272));
        dss.setContent("test content");
        dss.setFutureWork("test futureWork");
        dss.setConclusion("test conclusion");
        dss.setDataSource("test datasource");
        dss.setSampleSize(5252);
        dss.setAnalysisStart("2014-10-24T02:26:15.205Z");
        dss.setAnalysisEnd("2014-10-24T02:26:15.205Z");
        dss.setDataStart("2014-10-24T02:26:15.205Z");
        dss.setDataEnd("2014-10-24T02:26:15.205Z");
        dss.setCreateId("PP44632");
        dssService.addDss(dss);
    }

    @Test
    public void getDssTest() {
        Dss dssFromDB = dssService.getDss(dss.getId());
        assertThat(dss.getId(), is(dssFromDB.getId()));

        Dss notExistDss = dssService.getDss(Long.MAX_VALUE);
        assertThat(notExistDss, is(nullValue()));
    }

    @Test
    public void deleteDssTest() {
        dssService.deleteDss(dss.getId(), userId);

        Dss dssFromDB = dssService.getDss(dss.getId());
        assertThat(dssFromDB, is(nullValue()));
    }
}
