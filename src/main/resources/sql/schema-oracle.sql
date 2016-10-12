SET DATABASE SQL SYNTAX ORA TRUE;

CREATE TABLE "MART_MBR_ID_MST" ( 
	"MBR_ID" VARCHAR2(9 BYTE), 
	"OCBCOM_LGN_ID" VARCHAR2(100 BYTE), 
	"CI_NO" VARCHAR2(88 BYTE), 
	"CARD_NO" VARCHAR2(88 BYTE), 
	"SYW_MBR_ID" VARCHAR2(100 BYTE), 
	"EVS_MBR_ID" VARCHAR2(100 BYTE), 
	"MBR_KOR_NM" VARCHAR2(100 BYTE), 
	"LD_DTTM" VARCHAR2(14 BYTE), 
	"MART_UPD_DTTM" VARCHAR2(14 BYTE)
);


CREATE TABLE IF NOT EXISTS "DW_MBR_MST" ( 
	"MBRSHP_PGM_ID" VARCHAR2(1 BYTE), 
	"MBR_ID" VARCHAR2(9 BYTE), 
	"MBR_FG_CD" VARCHAR2(1 BYTE), 
	"MBR_KOR_NM" VARCHAR2(100 BYTE), 
	"MBR_ENG_NM" VARCHAR2(100 BYTE), 
	"MBR_STS_CD" VARCHAR2(1 BYTE), 
	"HOME_TEL_NO" VARCHAR2(13 BYTE), 
	"ORIHOME_ZIPCD" VARCHAR2(7 BYTE), 
	"ORIHOME_BASIC_ADDR" VARCHAR2(300 BYTE), 
	"ORIHOME_DTL_ADDR" VARCHAR2(300 BYTE), 
	"HOME_ZIPCD" VARCHAR2(6 BYTE), 
	"HOME_BASIC_ADDR" VARCHAR2(300 BYTE), 
	"HOME_DTL_ADDR" VARCHAR2(300 BYTE), 
	"ROAD_NM_HOME_ZIPCD" VARCHAR2(6 BYTE), 
	"ROAD_HOME_BASIC_ADDR" VARCHAR2(300 BYTE), 
	"ROAD_HOME_DTL_ADDR" VARCHAR2(300 BYTE), 
	"HOME_HJD_SGRP_CD" VARCHAR2(8 BYTE), 
	"HOME_BJD_SGRP_CD" VARCHAR2(10 BYTE), 
	"HOME_ADDR_RSLT_CD" VARCHAR2(6 BYTE), 
	"HOME_ROAD_ADDR_RSLT_CD" VARCHAR2(6 BYTE), 
	"HOME_APT_RSLT_CD" VARCHAR2(3 BYTE), 
	"JOBP_TEL_NO" VARCHAR2(13 BYTE), 
	"JOBP_ETSN_NO" VARCHAR2(4 BYTE), 
	"ORIJOBP_ZIPCD" VARCHAR2(7 BYTE), 
	"ORIJOBP_BASIC_ADDR" VARCHAR2(300 BYTE), 
	"ORIJOBP_DTL_ADDR" VARCHAR2(300 BYTE), 
	"JOBP_ZIPCD" VARCHAR2(6 BYTE), 
	"JOBP_BASIC_ADDR" VARCHAR2(300 BYTE), 
	"JOBP_DTL_ADDR" VARCHAR2(300 BYTE), 
	"ROAD_NM_JOBP_ZIPCD" VARCHAR2(6 BYTE), 
	"ROAD_JOBP_BASIC_ADDR" VARCHAR2(300 BYTE), 
	"ROAD_JOBP_DTL_ADDR" VARCHAR2(300 BYTE), 
	"JOBP_HJD_SGRP_CD" VARCHAR2(8 BYTE), 
	"JOBP_BJD_SGRP_CD" VARCHAR2(10 BYTE), 
	"JOBP_ADDR_RSLT_CD" VARCHAR2(6 BYTE), 
	"JOBP_ROAD_ADDR_RSLT_CD" VARCHAR2(6 BYTE), 
	"JOBP_APT_RSLT_CD" VARCHAR2(3 BYTE), 
	"CLPHN_SVC_CD" VARCHAR2(3 BYTE), 
	"CLPHN_NO" VARCHAR2(16 BYTE), 
	"EMAIL_ADDR" VARCHAR2(200 BYTE), 
	"HOME_TEL_CORCT_FG_CD" VARCHAR2(1 BYTE), 
	"JOBP_TEL_CORCT_FG_CD" VARCHAR2(1 BYTE), 
	"CLPHN_CORCT_FG_CD" VARCHAR2(1 BYTE), 
	"HOME_TEL_LENGTH_CORCT_YN" VARCHAR2(1 BYTE), 
	"JOBP_TEL_LENGTH_CORCT_YN" VARCHAR2(1 BYTE), 
	"CLPHN_LENGTH_CORCT_YN" VARCHAR2(1 BYTE), 
	"HOME_ADDR_CORCT_FG_CD" VARCHAR2(1 BYTE), 
	"JOBP_ADDR_CORCT_FG_CD" VARCHAR2(1 BYTE), 
	"REG_DTTM" DATE, 
	"UPD_DTTM" DATE, 
	"EXTRCT_DT" VARCHAR2(8 BYTE), 
	"LD_DTTM" DATE, 
	"DW_UPD_DTTM" DATE
);


CREATE TABLE IF NOT EXISTS "DW_MBR_UPD_HST" ( 
	"MBRSHP_PGM_ID" VARCHAR2(1 BYTE), 
	"MBR_ID" VARCHAR2(9 BYTE), 
	"MBR_UPD_ITM_CD" VARCHAR2(4 BYTE), 
	"UPD_DT" VARCHAR2(8 BYTE), 
	"UPD_TM" VARCHAR2(6 BYTE), 
	"UPD_PREV_DATA" VARCHAR2(300 BYTE), 
	"UPD_AF_DATA" VARCHAR2(300 BYTE), 
	"INFL_CHNL_CD" VARCHAR2(10 BYTE), 
	"REGR_ID" VARCHAR2(8 BYTE), 
	"REG_DTTM" DATE, 
	"UPDR_ID" VARCHAR2(8 BYTE), 
	"UPD_DTTM" DATE, 
	"EXTRCT_DT" VARCHAR2(8 BYTE), 
	"LD_DTTM" DATE, 
	"DW_UPD_DTTM" DATE
);


CREATE TABLE IF NOT EXISTS "DW_MBR_CI_INFO" ( 
	"MBRSHP_PGM_ID" VARCHAR2(1 BYTE), 
	"MBR_ID" VARCHAR2(9 BYTE), 
	"CI_NO" VARCHAR2(88 BYTE), 
	"INDIV_IDNTF_REF_NO" VARCHAR2(10 BYTE), 
	"IPIN_DUP_CHK_NO" VARCHAR2(64 BYTE), 
	"SPPSTN_RNO_TP_CD" VARCHAR2(2 BYTE), 
	"REGR_ID" VARCHAR2(8 BYTE), 
	"REG_DTTM" DATE, 
	"UPDR_ID" VARCHAR2(8 BYTE), 
	"UPD_DTTM" DATE, 
	"EXTRCT_DT" VARCHAR2(8 BYTE), 
	"LD_DTTM" DATE, 
	"DW_UPD_DTTM" DATE
);


CREATE TABLE IF NOT EXISTS "MART_MBR_INQ_INFO" ( 
	"MBR_ID" VARCHAR2(9 BYTE), 
	"CUST_ID" VARCHAR2(11 BYTE), 
	"PNT_EXTNCT_YN" VARCHAR2(1 BYTE), 
	"LEGL_BTHDT" VARCHAR2(8 BYTE), 
	"LEGL_GNDR_FG_CD" VARCHAR2(1 BYTE), 
	"FST_CARD_RREG_DT" VARCHAR2(8 BYTE), 
	"DESTR_EXPCTN_DT" VARCHAR2(8 BYTE), 
	"FNL_ERNG_DT" VARCHAR2(8 BYTE), 
	"FNL_CARD_RREG_DT" VARCHAR2(8 BYTE), 
	"FNL_CNSOF_ENQ_DT" VARCHAR2(8 BYTE), 
	"FNL_PRD_PCHS_DT" VARCHAR2(8 BYTE), 
	"FNL_PNT_INQ_DT" VARCHAR2(8 BYTE), 
	"LD_DTTM" DATE, 
	"MART_UPD_DTTM" DATE
);


CREATE TABLE IF NOT EXISTS "MART_CLPHN_NO_DUP_INFO" ( 
	"CLPHN_NO" VARCHAR2(16 BYTE), 
	"MBR_ID" VARCHAR2(9 BYTE), 
	"LD_DTTM" DATE, 
	"MART_UPD_DTTM" DATE
);


CREATE TABLE IF NOT EXISTS "MART_EMAIL_ADDR_DUP_INFO" ( 
	"EMAIL_ADDR" VARCHAR2(200 BYTE), 
	"MBR_ID" VARCHAR2(9 BYTE), 
	"LD_DTTM" DATE, 
	"MART_UPD_DTTM" DATE
);


CREATE TABLE IF NOT EXISTS "DW_OCB_INTG_CD" ( 
	"DOMN_ID" VARCHAR2(50 BYTE), 
	"DTL_CD" VARCHAR2(50 BYTE), 
	"APPL_END_DT" VARCHAR2(8 BYTE), 
	"APPL_START_DT" VARCHAR2(8 BYTE), 
	"DOMN_KOR_NM" VARCHAR2(200 BYTE), 
	"DOMN_ENG_NM" VARCHAR2(50 BYTE), 
	"DTL_CD_NM" VARCHAR2(200 BYTE), 
	"DTL_CD_DESC" VARCHAR2(400 BYTE), 
	"HRNK_DOMN_ID" VARCHAR2(50 BYTE), 
	"HRNK_DTL_CD" VARCHAR2(50 BYTE), 
	"REMARK" VARCHAR2(300 BYTE), 
	"REG_DTTM" VARCHAR2(14 BYTE), 
	"DW_UPD_DTTM" VARCHAR2(14 BYTE), 
	"IND_CD" VARCHAR2(1 BYTE)
);


CREATE TABLE "MART_MBR_TGTNG_MST" (
	"MBR_ID" VARCHAR2(9 BYTE), 
	"UNITED_ID" VARCHAR2(14 BYTE), 
	"GNDR_FG_CD" VARCHAR2(1 BYTE), 
	"AGE" NUMBER(3,0), 
	"HOME_HJD_LGRP_CD" VARCHAR2(8 BYTE), 
	"HOME_HJD_MGRP_CD" VARCHAR2(8 BYTE), 
	"JOBP_HJD_LGRP_CD" VARCHAR2(8 BYTE), 
	"JOBP_HJD_MGRP_CD" VARCHAR2(8 BYTE), 
	"MRRG_YN" VARCHAR2(1 BYTE), 
	"OCB_MKTNG_AGRMT_YN" VARCHAR2(1 BYTE), 
	"EMAIL_RCV_AGRMT_YN" VARCHAR2(1 BYTE), 
	"ADVT_SMS_RCV_AGRMT_YN" VARCHAR2(1 BYTE), 
	"IFRMT_SMS_RCV_AGRMT_YN" VARCHAR2(1 BYTE), 
	"PUSH_RCV_AGRMT_YN" VARCHAR2(1 BYTE), 
	"BNFT_MLF_PUSH_AGRMT_YN" VARCHAR2(1 BYTE), 
	"PNT_USE_RSVNG_PUSH_AGRMT_YN" VARCHAR2(1 BYTE), 
	"TUSE_PUSH_AGRMT_YN" VARCHAR2(1 BYTE), 
	"COIN_NOTI_PUSH_AGRMT_YN" VARCHAR2(1 BYTE), 
	"LOC_UTLZ_AGRMT_YN" VARCHAR2(1 BYTE), 
	"MLF_SHP_AGRMT_YN" VARCHAR2(1 BYTE), 
	"MLF_TRDAREA_AGRMT_YN" VARCHAR2(1 BYTE), 
	"OCBCOM_JOIN_YN" VARCHAR2(1 BYTE), 
	"OCB_APP_JOIN_YN" VARCHAR2(1 BYTE), 
	"OCB_PLUS_JOIN_YN" VARCHAR2(1 BYTE), 
	"SEG629_CD" VARCHAR2(2 BYTE), 
	"TRFC_SEG_CD" VARCHAR2(2 BYTE), 
	"OCBCOM_MTH07_FNL_LGN_DT" VARCHAR2(8 BYTE), 
	"OCB_APP_MTH07_FNL_USE_DT" VARCHAR2(8 BYTE), 
	"APP_ATDC_MTH07_FNL_USE_DT" VARCHAR2(8 BYTE), 
	"RLET_MTH07_FNL_USE_DT" VARCHAR2(8 BYTE), 
	"GAME_MTH07_FNL_USE_DT" VARCHAR2(8 BYTE), 
	"OCB_PLUS_MTH07_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"LCKR_MTH07_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"MZM_MTH07_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"AZM_MTH07_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"PRD_LFL_MTH07_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"MBL_CARD_POSS_YN" VARCHAR2(1 BYTE), 
	"SKP_CARD_POSS_YN" VARCHAR2(1 BYTE), 
	"CRDT_CARD_POSS_YN" VARCHAR2(1 BYTE), 
	"CHKCRD_POSS_YN" VARCHAR2(1 BYTE), 
	"CMNCN_CARD_POSS_YN" VARCHAR2(1 BYTE), 
	"ENCLEAN_BNS_CARD_POSS_YN" VARCHAR2(1 BYTE), 
	"FREI_WLFR_CARD_POSS_YN" VARCHAR2(1 BYTE), 
	"RFL_EXCS_CARD_POSS_YN" VARCHAR2(1 BYTE), 
	"RFL_MTH07_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"CMNCN_MTH07_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"FNC_MTH07_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"ETC_MTH07_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"ONLN_MTH07_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"ONLN_MTH07_CPN_FNL_YACHV_DT" VARCHAR2(8 BYTE), 
	"AVL_PNT" NUMBER(13,0), 
	"AVL_PNT_BASE_DT" VARCHAR2(8 BYTE), 
	"LD_DTTM" VARCHAR2(14 BYTE), 
	"MART_UPD_DTTM" VARCHAR2(14 BYTE)
);


CREATE TABLE "WEB_CMPGN_MST" (
	"CMPGN_ID" VARCHAR2(10 BYTE), 
	"CMPGN_NM" VARCHAR2(100 BYTE), 
	"MERGE_DT" VARCHAR2(8 BYTE), 
	"SND_DT" VARCHAR2(8 BYTE), 
	"OBJ_REG_FG_CD" VARCHAR2(10 BYTE), 
	"TOT_CNT" NUMBER(15,0), 
	"DUP_DEL_CNT" NUMBER(15,0), 
	"CMPGN_SND_CHNL_FG_CD" VARCHAR2(10 BYTE), 
	"STS_FG_CD" VARCHAR2(10 BYTE), 
	"UPD_ID" VARCHAR2(10 BYTE), 
	"UPT_DTTM" DATE, 
	"CRT_ID" VARCHAR2(10 BYTE), 
	"CRT_DTTM" DATE,
	
	CONSTRAINT "WEB_CMPGN_MST_PK" PRIMARY KEY ("CMPGN_ID")
);


CREATE TABLE "WEB_CMPGN_DTL" (
	"CELL_ID" VARCHAR2(10 BYTE), 
	"CMPGN_ID" VARCHAR2(10 BYTE), 
	"EXTRCT_CNT" NUMBER(15,0), 
	"FNL_EXTRCT_CNT" NUMBER(15,0), 
	"MERGE_DT" VARCHAR2(8 BYTE), 
	"SND_DT" VARCHAR2(8 BYTE), 
	"CELL_DESC" VARCHAR2(100 BYTE), 
	"STS_FG_CD" VARCHAR2(10 BYTE), 
	"UPD_ID" VARCHAR2(10 BYTE), 
	"UPT_DTTM" DATE, 
	"CRT_ID" VARCHAR2(10 BYTE), 
	"CRT_DTTM" DATE,
	
	CONSTRAINT "WEB_CMPGN_DTL_PK" PRIMARY KEY ("CELL_ID")
); 


CREATE TABLE "WEB_CMPGN_TGTNG_INFO" (
	"CMPGN_ID" VARCHAR2(10 BYTE), 
	"TGTNG_CNDTN_CLMN_NM" VARCHAR2(100 BYTE), 
	"TGTNG_CNDTN_CLMN_VAL" VARCHAR2(20 BYTE), 
	"UPD_ID" VARCHAR2(10 BYTE), 
	"UPT_DTTM" DATE, 
	"CRT_ID" VARCHAR2(10 BYTE), 
	"CRT_DTTM" DATE,
	
	CONSTRAINT "WEB_CMPGN_TGTNG_INFO_PK" PRIMARY KEY ("CMPGN_ID", "TGTNG_CNDTN_CLMN_NM", "TGTNG_CNDTN_CLMN_VAL")
);
