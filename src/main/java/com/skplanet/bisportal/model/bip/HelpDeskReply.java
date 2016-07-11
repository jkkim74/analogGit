package com.skplanet.bisportal.model.bip;

import java.io.Serializable;

import lombok.Data;

/**
 * @author cookatrice
 */
@Data
public class HelpDeskReply implements Serializable {
	private static final long serialVersionUID = 6930562253942910801L;
    private Long sn; // 리플번호 NUMBER(10,0)
    private Long id; // HELP DESK ID NUMBER(20,0)
	private String context; // 리플내용 VARCHAR2(500 BYTE)
	private String deleteYn; // 삭제여부 VARCHAR2(1 BYTE)
	private String writer; // 작성자 VARCHAR2(30 BYTE)
	private String writerId; // 작성자ID VARCHAR2(30 BYTE)
	private String writeDate; // 작성일자 CHAR(14 BYTE)
	private String writeIp; // 작성IP VARCHAR2(15 BYTE)
	private String updateDate; // 수정일자 CHAR(14 BYTE)
	private String updateIp; // 수정IP VARCHAR2(15 BYTE)

}
