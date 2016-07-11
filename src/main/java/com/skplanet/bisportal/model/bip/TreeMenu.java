package com.skplanet.bisportal.model.bip;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The TreeMenu class.
 * 
 * <pre>
 * 메뉴를 tree로 구성하기 위한 model
 * </pre>
 *
 * @author sjune
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TreeMenu extends Menu implements Serializable {
	private static final long serialVersionUID = 1924953759056918893L;
	private MenuSearchOption menuSearchOption;
}
