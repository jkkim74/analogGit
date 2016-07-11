package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * The CommonCode class.
 * 
 * @author sjune
 */
@Data
public class CommonCode implements Serializable {
	private static final long serialVersionUID = -6255827490706388327L;
	private Long id;
	private String name;
	private String description;
	private String deleteYn;

	/**
	 * COM_INTG_COM_CD > SYS_IND_CD
	 */
	public enum SysIndCd {
		OBS
	}

	/**
	 * COM_INTG_COM_CD > COM_GRP_CD
	 */
	public enum ComGrpCd {
		OBS001
	}

	/**
	 * UV, PV, LV measure 구분
	 */
	public enum MesurVst {
		UV("UV"), LV("LV"), PV("PV");

		private String label;

		private MesurVst(String label) {
			this.label = label;
		}

		public String label() {
			return this.label;
		}
	}

	/**
	 * OBS_D_VST_PAGE_STA measure 구분
	 */
	public enum MesurObsVstPageSta {
		LV("LV"), PV("PV"), UV("UV"), VST_CNT("방문횟수"), TIME_SPT_F_VST("Time spent for visits");

		private String label;

		private MesurObsVstPageSta(String label) {
			this.label = label;
		}

		public String label() {
			return this.label;
		}
	}

	/**
	 * OBS_D_VST_PAGE_STA measure 구분
	 */
	public enum MesurObsCntntFlyrDetlSta {
		CLICK_CNT_ON_UV("UV"), CLICK_CNT_ON_LV("LV"), CLICK_CNT_ON_PV("PV");

		private String label;

		private MesurObsCntntFlyrDetlSta(String label) {
			this.label = label;
		}

		public String label() {
			return this.label;
		}
	}

    /**
     * 메뉴 공통 코드명
     */
	public enum MenuCommonCodeName {
		BS, BC, BM, RS, RC, RM, AS, AC, AM, HS, HC, HM
	}

	/**
	 * ocb > 컨텐츠 상세분석 > feed 통계 타입 구분
	 */
	public enum FeedTypeEnum {
		TYPE1("전체(5.1.x 미만포함)"), TYPE2("5.1.x 이상");

		private String label;

		private FeedTypeEnum(String label) {
			this.label = label;
		}

		public String label() {
			return this.label;
		}
	}

	/**
	 * T map > T map KPI >일별 KPI 관리 > KPI 구분
	 */
	public enum KpiCodeEnum {
		TYPE_01("04","Active UV"), TYPE_02("02","T map"), TYPE_03("03","T map 대중교통");

		private String label1;
		private String label2;

		private KpiCodeEnum(String label1, String label2) {
			this.label1 = label1;
			this.label2 = label2;
		}

		public String label1() {
			return this.label1;
		}

		public String label2() {
			return this.label2;
		}
	}
}
