package com.skplanet.bisportal.common.utils;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import static ch.lambdaj.collection.LambdaCollections.with;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import ch.lambdaj.function.matcher.Predicate;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.model.ocb.RtdDau;

/**
 * Created by pepsi on 15. 3. 26..
 */
public class LambdajTest {
	@Test
	public void sumTest() throws Exception {
		List<RtdDau> ocbRdtDaus = Lists.newArrayList();
		RtdDau rtdDau1 = new RtdDau();
		rtdDau1.setHauAcm(new BigDecimal("200"));
		rtdDau1.setStrdDt("20150301");
		ocbRdtDaus.add(rtdDau1);
		RtdDau rtdDau2 = new RtdDau();
		rtdDau2.setHauAcm(new BigDecimal("200"));
		rtdDau2.setStrdDt("20150302");
		ocbRdtDaus.add(rtdDau2);
		RtdDau rtdDau3 = new RtdDau();
		rtdDau3.setHauAcm(new BigDecimal("200"));
		rtdDau3.setStrdDt("20150303");
		ocbRdtDaus.add(rtdDau3);
		RtdDau rtdDau4 = new RtdDau();
		rtdDau4.setHauAcm(new BigDecimal("100"));
		rtdDau4.setStrdDt("20150304");
		ocbRdtDaus.add(rtdDau4);
		List<RtdDau> thirteenRtdDau =  with(ocbRdtDaus).clone().retain(new Predicate<RtdDau>() {
			@Override
			public boolean apply(RtdDau rtdDau) { return Integer.parseInt(rtdDau.getStrdDt()) < 20150304; }
		});
		System.out.println(thirteenRtdDau);
		double totalOcb = sum(with(ocbRdtDaus).clone().retain(new Predicate<RtdDau>() {
			@Override
			public boolean apply(RtdDau rtdDau) { return Integer.parseInt(rtdDau.getStrdDt()) < 20150304; }
		}), on(RtdDau.class).getHauAcm().doubleValue());
		assertThat(totalOcb, is(600d));

		double totalHauAcm = sum(ocbRdtDaus, on(RtdDau.class).getHauAcm().doubleValue());
		assertThat(totalHauAcm, is(700d));
	}
}
