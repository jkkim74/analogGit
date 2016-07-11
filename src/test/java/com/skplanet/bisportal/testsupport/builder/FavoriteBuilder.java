package com.skplanet.bisportal.testsupport.builder;

import com.skplanet.bisportal.model.bip.Favorite;

/**
 * The FavoriteBuilder class.
 * 
 * @author sjune
 */
public class FavoriteBuilder {
	private String serviceCode;
	private String categoryCode;
	private String menuCode;
	private String serviceName;
	private String categoryName;
	private String menuName;
	private String username;
	private Integer orderIdx;
	private String lastUpdate;
	private Long commonCodeId;
	private String commonCodeName;
	private String menuId;

	private FavoriteBuilder() {
	}

	public static FavoriteBuilder aFavorite() {
		return new FavoriteBuilder();
	}

	public FavoriteBuilder serviceCode(String serviceCode) {
		this.serviceCode = serviceCode;
		return this;
	}

	public FavoriteBuilder categoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
		return this;
	}

	public FavoriteBuilder menuCode(String menuCode) {
		this.menuCode = menuCode;
		return this;
	}

	public FavoriteBuilder commonCodeId(Long commonCodeId) {
		this.commonCodeId = commonCodeId;
		return this;
	}

	public FavoriteBuilder username(String username) {
		this.username = username;
		return this;
	}

	public FavoriteBuilder lastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
		return this;
	}

	public FavoriteBuilder serviceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}

	public FavoriteBuilder categoryName(String categoryName) {
		this.categoryName = categoryName;
		return this;
	}

	public FavoriteBuilder menuName(String menuName) {
		this.menuName = menuName;
		return this;
	}

	public FavoriteBuilder orderIdx(Integer orderIdx) {
		this.orderIdx = orderIdx;
		return this;
	}

	public FavoriteBuilder commonCodeName(String commonCodeName) {
		this.commonCodeName = commonCodeName;
		return this;
	}

	public FavoriteBuilder menuId(String menuId) {
		this.menuId = menuId;
		return this;
	}

	public Favorite build() {
		Favorite favorite = new Favorite();
		favorite.setServiceCode(serviceCode);
		favorite.setCategoryCode(categoryCode);
		favorite.setMenuCode(menuCode);
		favorite.setServiceName(serviceName);
		favorite.setCategoryName(categoryName);
		favorite.setMenuName(menuName);
		favorite.setOrderIdx(orderIdx);
		favorite.setUsername(username);
		favorite.setCommonCodeId(commonCodeId);
		favorite.setCommonCodeName(commonCodeName);
		favorite.setMenuId(menuId);
		favorite.setLastUpdate(lastUpdate);
		return favorite;
	}

}
