package com.skplanet.bisportal.common.model;

import com.google.common.collect.Maps;
import com.skplanet.bisportal.common.utils.Utils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * The SummaryReportRowMap class.
 *
 * @author sjune
 */

@Data
public class SummaryReportRowMap implements Serializable {
	private static final long serialVersionUID = -5960600525945556048L;
	private Map<String, SummaryReportRow> rows;

	public SummaryReportRowMap() {
		this.rows = Maps.newLinkedHashMap();
	}

	/**
	 * 신규 row를 추가하거나 기존 row를 반환한다.
	 * 
	 * @param measure
	 * @return SummaryReportRow
	 */
	public SummaryReportRow addOrGetRow(String measure) {
		if (this.rows.get(measure) == null) {
			this.rows.put(measure, new SummaryReportRow(measure));
		}

		return this.rows.get(measure);
	}

	/**
	 * rowMap을 리스트로 반환한다.
	 * 
	 * @return
	 */
	public List<SummaryReportRow> toList() throws Exception {
        return Utils.mapToList(this.getRows());
	}
}
