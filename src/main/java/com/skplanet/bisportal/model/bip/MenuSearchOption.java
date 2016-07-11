package com.skplanet.bisportal.model.bip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * The MenuSearchOption model class.
 *
 * @author sjune
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuSearchOption implements Serializable {
	private static final long serialVersionUID = 1165966116725930440L;
	private Long menuId;
	private String label;
	private String dateTypes;
	private String codeUrl;
	private String addType;
	private String calendarType;
	private String lastUpdate;
	private String projectId;
	private String projectName;
	private String auditId;
}
