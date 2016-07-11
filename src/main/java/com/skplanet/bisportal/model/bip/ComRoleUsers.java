package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * The ComRoleUsers class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class ComRoleUsers implements Serializable {
	private static final long serialVersionUID = 8656656243271686292L;
	private List<ComRoleUser> roleCounts;
	private List<ComRoleUser> roleUsers;
}
