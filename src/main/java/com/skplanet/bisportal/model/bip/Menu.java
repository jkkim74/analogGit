package com.skplanet.bisportal.model.bip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * The Menu model class.
 *
 * @author sjune
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu implements Serializable {
	private static final long serialVersionUID = 6985363488588206952L;
	private Long id;
	private String code;
	private String name;
	private String rootName;
	private String menuLevel;
	private String pathName;
	private Long parentId;
	private Long commonCodeId;
	private String commonCodeName;
	private String imageUrl;
	private Long oprSvcId;
	private Integer orderIdx;
	private String visibleYn;
	private String deleteYn;
	private String menuSearchOptionYn;
	private String summaryReportYn;
	private String lastUpdate;
	private String roleName;
	private String userNm;
	private String loginId = "0";
	private String authority;
	private String auditId;
	private MenuSearchOption menuSearchOption;

	private String key;
	private String title;
	private boolean folder;
	private boolean expand;
	private String maxLvl;
	private String lvl;
	private String menuId;
	private String menuNm;
	private String serviceName;
	private String categoryName;
	private String state;
	private List<Menu> children = Lists.newArrayList();

	public MenuSearchOption getMenuSearchOption() {
		if (menuSearchOption == null) {
			menuSearchOption = new MenuSearchOption();
		}
		return menuSearchOption;
	}
}
