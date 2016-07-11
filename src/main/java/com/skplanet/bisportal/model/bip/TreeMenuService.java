package com.skplanet.bisportal.model.bip;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * The TreeMenuService class
 *
 * <pre>
 * 메뉴를 service tree로 구성하기 위한 model
 * </pre>
 * 
 * @author sjune
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TreeMenuService extends Menu implements Serializable {
	private static final long serialVersionUID = -3557770432085683087L;
	private List<TreeMenuCategory> categories;

	public List<TreeMenuCategory> getCategories() {
		if (categories == null) {
			categories = Lists.newArrayList();
		}
		return categories;
	}
}
