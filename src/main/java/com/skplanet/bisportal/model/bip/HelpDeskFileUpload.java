package com.skplanet.bisportal.model.bip;

import java.io.Serializable;

import lombok.Data;

/**
 * @author cookatrice
 */
@Data
public class HelpDeskFileUpload implements Serializable {
	private static final long serialVersionUID = -7461811240764850495L;
    private Long sn; // SN NUMBER(20,0)
    private Long id; // HELP DESK ID NUMBER(20,0)
	private String fileName; // 파일명 VARCHAR2(100 BYTE)
	private String fileSavePath; // 파일저장경로 VARCHAR2(100 BYTE)
    private Long fileSize;    //  파일사이즈
    private String uploadDate;  //  저장날짜
	private String deleteYn; // 삭제여부 VARCHAR2(1 BYTE)
    private String uuid;    //UUID varchar2(100 byte)
    private Long fileMetaId;    //file meta id number(20)
}
