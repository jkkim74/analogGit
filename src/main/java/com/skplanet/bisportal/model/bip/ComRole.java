package com.skplanet.bisportal.model.bip;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * The ComRole class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComRole implements Serializable {
	private static final long serialVersionUID = 8671916968905279842L;
	private long id;
	private String name;
	private String description;
	private String deleteYn;
	private String roleType;
	private String state;
	private String auditId;
	private String auditDtm;
	private String aplyStaDt;
	private String aplyEndDt;
}
