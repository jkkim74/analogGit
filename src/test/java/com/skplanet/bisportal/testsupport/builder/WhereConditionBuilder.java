package com.skplanet.bisportal.testsupport.builder;

import com.skplanet.bisportal.common.model.WhereCondition;

/**
 * The WhereConditionBuilder class.
 * 
 * @author sjune
 */
public class WhereConditionBuilder {
	private Long svcId;
	private String idxClGrpCd;
	private String idxClCd;
	private String idxCttCd;
	private String lnkgCyclCd;
	private String startDate;
	private String endDate;
    private String startWeekNumber;
    private String endWeekNumber;

	private WhereConditionBuilder() {
	}

	public static WhereConditionBuilder aWhereCondition() {
		return new WhereConditionBuilder();
	}

	public WhereConditionBuilder svcId(Long svcId) {
		this.svcId = svcId;
		return this;
	}

	public WhereConditionBuilder idxClGrpCd(String idxClGrpCd) {
		this.idxClGrpCd = idxClGrpCd;
		return this;
	}

	public WhereConditionBuilder idxClCd(String idxClCd) {
		this.idxClCd = idxClCd;
		return this;
	}

	public WhereConditionBuilder idxCttCd(String idxCttCd) {
		this.idxCttCd = idxCttCd;
		return this;
	}

	public WhereConditionBuilder lnkgCyclCd(String lnkgCyclCd) {
		this.lnkgCyclCd = lnkgCyclCd;
		return this;
	}

	public WhereConditionBuilder startDate(String startDate) {
		this.startDate = startDate;
		return this;
	}

	public WhereConditionBuilder endDate(String endDate) {
		this.endDate = endDate;
		return this;
	}
	public WhereConditionBuilder startWeekNumber(String startWeekNumber) {
		this.startWeekNumber = startWeekNumber;
		return this;
	}

	public WhereConditionBuilder endWeekNumber(String endWeekNumber) {
		this.endWeekNumber = endWeekNumber;
		return this;
	}

	public WhereCondition build() {
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setSvcId(svcId);
		whereCondition.setIdxClGrpCd(idxClGrpCd);
		whereCondition.setIdxClCd(idxClCd);
		whereCondition.setIdxCttCd(idxCttCd);
		whereCondition.setLnkgCyclCd(lnkgCyclCd);
		whereCondition.setStartDate(startDate);
		whereCondition.setEndDate(endDate);
		whereCondition.setStartWeekNumber(startWeekNumber);
		whereCondition.setEndWeekNumber(endWeekNumber);
		return whereCondition;
	}
}
