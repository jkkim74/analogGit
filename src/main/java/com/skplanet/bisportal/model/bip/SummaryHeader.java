package com.skplanet.bisportal.model.bip;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasicDateWeekNumber;

/**
 * The WeekHeader(주 헤더용 데이터) class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SummaryHeader<T> {
	private BasicDateWeekNumber weekNumber;
	private List<T> periods;
}
