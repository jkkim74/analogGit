package com.skplanet.bisportal.model.bip;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * The CommonCodeTest class.
 *
 * @author sjune
 */
public class CommonCodeTest {
	@Test
	public void testSysIndCd() {
		assertThat(CommonCode.SysIndCd.OBS.name(), is("OBS"));
	}

	@Test
	public void testComGrpCd() {
		assertThat(CommonCode.ComGrpCd.OBS001.name(), is("OBS001"));
	}

	@Test
	public void testMesurObsVstPageSta() {
		assertThat(CommonCode.MesurObsVstPageSta.VST_CNT.name(), is("VST_CNT"));
		assertThat(CommonCode.MesurObsVstPageSta.VST_CNT.label(), is("방문횟수"));

		assertThat(CommonCode.MesurObsVstPageSta.TIME_SPT_F_VST.name(), is("TIME_SPT_F_VST"));
		assertThat(CommonCode.MesurObsVstPageSta.TIME_SPT_F_VST.label(), is("Time spent for visits"));
	}
}
