package com.skplanet.bisportal.model.bip;

import java.io.Serializable;

import lombok.Data;

/**
 * @author cookatrice
 */
@Data
public class CommonGroupCode implements Serializable {
	private static final long serialVersionUID = 3154754912694491638L;

	private String groupCodeId; // 그룹코드id varchar2(50 byte)
	private String groupCodeName; // 그룹코드명 varchar2(50 byte)
	private String codeId; // 코드id varchar2(50 byte)
	private String codeName; // 코드명 varchar2(50 byte)
	private Long sort; // 정렬순서 number(4,0)
	private String upperGroupCodeId; // 상위그룹코드id varchar2(50 byte)
	private String upperCodeId; // 상위코드id varchar2(50 byte)
	private String useYn; // 사용여부 varchar2(1 byte)

}
