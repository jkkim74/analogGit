package com.skplanet.bisportal.model.acl;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by pepsi on 2014. 10. 6..
 */
@Data
public class Login implements Serializable {
	private static final long serialVersionUID = -1758481316959964792L;
	private String username;
	private String password;
	private boolean loginCheck;
	private String returnUrl;
}
