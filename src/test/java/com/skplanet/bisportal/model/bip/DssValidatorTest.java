package com.skplanet.bisportal.model.bip;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
/**
 * Created by seoseungho on 2014. 10. 17..
 */
public class DssValidatorTest {

    @Test
    public void testFailValidate() {
        Dss dss = new Dss();
        dss.setSubject("subject");
        dss.setContent("content");
        dss.setAnalysisStart("20140808");
        dss.setAnalysisEnd("2014080");
        dss.setDataStart("20140707");
        dss.setDataEnd("20140709");
        dss.setDataSource("hi");
        dss.setSampleSize(123);

        BindingResult bindingResult = new BeanPropertyBindingResult(dss, "command");
        new DssValidator().validate(dss, bindingResult);
        assertEquals(bindingResult.hasErrors(), true);
    }

    @Test
    public void testSuccessValidate() {
        Dss dss = new Dss();
        dss.setBmIdList(new ArrayList<Long>(Arrays.asList(1l, 2l, 3l, 5l, 8l, 13l, 21l)));
        dss.setSubject("subject");
        dss.setContent("content");
        dss.setAnalysisStart("20140808");
        dss.setAnalysisEnd("2014080");
        dss.setDataStart("20140707");
        dss.setDataEnd("20140709");
        dss.setDataSource("hi");
        dss.setSampleSize(123);

        BindingResult bindingResult = new BeanPropertyBindingResult(dss, "command");
        new DssValidator().validate(dss, bindingResult);
        assertEquals(bindingResult.hasErrors(), false);
    }
}
