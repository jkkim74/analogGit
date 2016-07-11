package com.skplanet.bisportal.model.bip;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.google.common.collect.Lists;

/**
 * The TreeMenuCategory class.
 *
 * <pre>
 * 메뉴를 category tree로 구성하기 위한 model
 * </pre>
 * 
 * @author sjune
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TreeMenuCategory extends Menu implements Serializable {
	private static final long serialVersionUID = -8670101373449864287L;
	private List<TreeMenu> menus;

	public List<TreeMenu> getMenus() {
		if (menus == null) {
			menus = Lists.newArrayList();
		}
		return menus;
	}
}
