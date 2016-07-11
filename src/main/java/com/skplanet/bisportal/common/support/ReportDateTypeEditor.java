package com.skplanet.bisportal.common.support;

import com.skplanet.bisportal.common.model.ReportDateType;

import java.beans.PropertyEditorSupport;

/**
 * The ReportDateTypeEditor class
 *
 * @author sjune
 */
public class ReportDateTypeEditor extends PropertyEditorSupport {
	@Override
	public String getAsText() {
		return super.getAsText();
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		this.setValue(Enum.valueOf(ReportDateType.class, text.toUpperCase()));
	}
}
