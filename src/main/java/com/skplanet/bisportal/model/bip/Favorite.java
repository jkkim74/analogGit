package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * The Favorite model class.
 * 
 * @author sjune
 */
@Data
public class Favorite implements Serializable {
	private static final long serialVersionUID = -7119106947399330538L;
	public static final Favorite EMPTY = new Favorite();
	private Long id;
	private String serviceCode;
	private String categoryCode;
	private String menuCode;
    private String serviceName;
    private String categoryName;
    private String menuName;
	private String username;
	private String lastUpdate;
    private Long commonCodeId;
    private String commonCodeName;
    private Integer orderIdx;
	private String menuId;  //20151012 refactoring favorite
}
