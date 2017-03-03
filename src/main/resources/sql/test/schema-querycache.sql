create table MOCK_TB_SEL_TRX_HIST (
	rcv_dt DATE,
	rcv_seq VARCHAR(50),
	appr_dttm DATE,
	rep_appr_no VARCHAR(50),
	appr_no VARCHAR(50),
	sale_dttm DATE,
	mbr_id VARCHAR(50),
	card_dtl_grp VARCHAR(50),
	card_no VARCHAR(50),
	alcmpn VARCHAR(50),
	mcnt VARCHAR(50),
	stlmt_mcnt VARCHAR(50),
	pnt_knd VARCHAR(50),
	slip VARCHAR(50),
	sale_amt INT,
	pnt INT,
	cs_mbr_cmmsn INT,
	cmmsn INT,
	pmnt_way VARCHAR(50),
	org VARCHAR(50),
	oil_prdct_sgrp VARCHAR(50),
	sale_qty INT,
	cpn_prd VARCHAR(20),
	trx_type VARCHAR(50)
);

CREATE TABLE MOCK_TB_SEL_TRX_HIST_QC (
 	rcv_dt varchar(100)	--접수일시
	,appr_dttm varchar(100)	--승인일시
	,rep_appr_no varchar(100)	--대표 승인번호
	,appr_no varchar(100)	--승인번호
	,sale_dttm varchar(100)	--매출일시
	,mbr_id varchar(100)	--회원ID
	,card_dtl_grp_Cd varchar(100)	--카드코드
	,DTL_CD_NM varchar(100)	--카드코드명
	,card_no varchar(100)	--카드번호
	,stlmt_alcmpn_cd varchar(100)	--정산제휴사코드
	,alcmpn_nm varchar(100)	--정산제휴사명
	,stlmt_mcnt_cd varchar(100)	--정산가맹점코드
	,mcnt_nm varchar(100)	--정산가맹점명
	,alcmpn_cd varchar(100)	--발생제휴사코드
	,alcmpn_nm2 varchar(100)	--발생제휴사명
	,mcnt_cd varchar(100)	--발생가맹점코드
	,mcnt_nm2 varchar(100)	--발생가맹점명
	,pnt_knd_cd varchar(100)	--포인트종류코드
	,DTL_CD_NM2 varchar(100)	--포인트종류명
	,slip_cd varchar(100)	--전표코드
	,DTL_CD_NM3 varchar(100)	--전표명
	,sale_amt varchar(100)	--매출금액
	,pnt varchar(100)	--포인트
	,cs_mbr_cmmsn varchar(100)	--제휴사연회비
	,cmmsn varchar(100)	--수수료
	,pmnt_way_cd varchar(100)	--지불수단코드
	,DTL_CD_NM4 varchar(100)	--지불수단명
	,org_cd varchar(100)	--기관코드
	,DTL_CD_NM5 varchar(100)	--기관명
	,oil_prdct_sgrp_cd varchar(100)	--유종코드
	,DTL_CD_NM6 varchar(100)	--유종명
	,cpn_prd_cd varchar(100)	--쿠폰코드
	,prd_nm varchar(100)	--쿠폰명
);