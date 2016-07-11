package com.skplanet.bisportal.model.mstr;

import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

/**
 * The MstrMenu class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MstrMenu {
	private String objectId;
	private String sessionId;
	private String parentObjectId;
	private String objectName;
	private int objectType;
	private String projectId;
	private int subType;
	private int unitType;

	private String key;
	private String title;
	private boolean isFolder;
	private boolean expand;
	private String maxLvl;
	private String lvl;
	private String menuId;
	private List<Mstr> mstrs;
	private List<MstrMenu> children = Lists.newArrayList();

	public void setIsFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public boolean getIsFolder() {
		return this.isFolder;
	}
}
