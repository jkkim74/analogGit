create table if not exists MOCK_TB_SEL_TRX_HIST (
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
insert into MOCK_TB_SEL_TRX_HIST (rcv_dt, rcv_seq, appr_dttm, rep_appr_no, appr_no, sale_dttm, mbr_id, card_dtl_grp, card_no, alcmpn, mcnt, stlmt_mcnt, pnt_knd, slip, sale_amt, pnt, cs_mbr_cmmsn, cmmsn, pmnt_way, org, oil_prdct_sgrp, sale_qty, cpn_prd, trx_type) values ('2016-02-11', '2435-609', '2016-02-07 15:34:55', null, null, '2016-05-14 13:40:41', '1', 'bankcard', '5610766057851286', 'Marquardt Group', 'Ebert, Feeney and Windler', 'Heidenreich-Grady', null, null, 2495, 379, 92, 66, null, 'Donnelly, Beahan and Mertz', null, 19, '69.124.50.39', '01');
insert into MOCK_TB_SEL_TRX_HIST (rcv_dt, rcv_seq, appr_dttm, rep_appr_no, appr_no, sale_dttm, mbr_id, card_dtl_grp, card_no, alcmpn, mcnt, stlmt_mcnt, pnt_knd, slip, sale_amt, pnt, cs_mbr_cmmsn, cmmsn, pmnt_way, org, oil_prdct_sgrp, sale_qty, cpn_prd, trx_type) values ('2016-05-15', null, '2016-08-25 12:07:38', '80217', null, '2016-08-18 13:27:19', '1', 'jcb', '3554007604654730', 'MacGyver Inc', 'Stark-Parker', 'Raynor, Klein and Stamm', null, null, 5877, 259, 24, 63, null, 'Marks-Kohler', null, 58, '37.243.217.217', '01');
insert into MOCK_TB_SEL_TRX_HIST (rcv_dt, rcv_seq, appr_dttm, rep_appr_no, appr_no, sale_dttm, mbr_id, card_dtl_grp, card_no, alcmpn, mcnt, stlmt_mcnt, pnt_knd, slip, sale_amt, pnt, cs_mbr_cmmsn, cmmsn, pmnt_way, org, oil_prdct_sgrp, sale_qty, cpn_prd, trx_type) values ('2016-08-25', null, '2016-01-17 00:51:48', null, '4620-378', '2016-03-23 14:23:20', '1', 'diners-club-us-ca', '5571621015676233', 'Cummings, Borer and Farrell', 'Runte, Bradtke and Batz', 'O''Keefe and Sons', null, null, 8618, 149, 21, 18, null, 'Lowe-Harvey', null, 21, '169.60.71.124', '01');
insert into MOCK_TB_SEL_TRX_HIST (rcv_dt, rcv_seq, appr_dttm, rep_appr_no, appr_no, sale_dttm, mbr_id, card_dtl_grp, card_no, alcmpn, mcnt, stlmt_mcnt, pnt_knd, slip, sale_amt, pnt, cs_mbr_cmmsn, cmmsn, pmnt_way, org, oil_prdct_sgrp, sale_qty, cpn_prd, trx_type) values ('2015-10-30', '6200-595', '2015-12-10 20:46:16', null, null, '2016-01-10 23:06:00', '1', 'jcb', '3586964561999023', 'Kertzmann, Ankunding and Beatty', 'Crooks-Marvin', 'Pfannerstill and Sons', null, null, 5155, 627, 97, 32, null, 'Welch LLC', null, 100, '168.11.26.26', '01');
insert into MOCK_TB_SEL_TRX_HIST (rcv_dt, rcv_seq, appr_dttm, rep_appr_no, appr_no, sale_dttm, mbr_id, card_dtl_grp, card_no, alcmpn, mcnt, stlmt_mcnt, pnt_knd, slip, sale_amt, pnt, cs_mbr_cmmsn, cmmsn, pmnt_way, org, oil_prdct_sgrp, sale_qty, cpn_prd, trx_type) values ('2015-11-10', null, '2015-12-30 00:56:25', '439 24', null, '2016-01-19 14:32:08', '1', 'diners-club-carte-blanche', '30213589154825', 'Leannon-Johns', 'Collier-Cremin', 'Klein Group', null, null, 5034, 285, 69, 75, null, 'Prohaska-Blanda', null, 14, '244.113.35.44', '02');
insert into MOCK_TB_SEL_TRX_HIST (rcv_dt, rcv_seq, appr_dttm, rep_appr_no, appr_no, sale_dttm, mbr_id, card_dtl_grp, card_no, alcmpn, mcnt, stlmt_mcnt, pnt_knd, slip, sale_amt, pnt, cs_mbr_cmmsn, cmmsn, pmnt_way, org, oil_prdct_sgrp, sale_qty, cpn_prd, trx_type) values ('2016-07-07', null, '2015-11-12 03:19:39', null, null, '2016-02-28 00:46:39', '1', 'jcb', '3587776040705154', 'Schmitt-Bradtke', 'Nolan-Torp', 'Green, Krajcik and Towne', null, null, 5621, 885, 62, 14, null, 'Johnston LLC', null, 45, '98.16.129.151', '02');
insert into MOCK_TB_SEL_TRX_HIST (rcv_dt, rcv_seq, appr_dttm, rep_appr_no, appr_no, sale_dttm, mbr_id, card_dtl_grp, card_no, alcmpn, mcnt, stlmt_mcnt, pnt_knd, slip, sale_amt, pnt, cs_mbr_cmmsn, cmmsn, pmnt_way, org, oil_prdct_sgrp, sale_qty, cpn_prd, trx_type) values ('2016-06-15', null, '2015-12-14 22:34:40', null, '95234', '2015-11-23 14:48:16', '1', 'jcb', '3574802705709425', 'Schulist, Hilll and Kris', 'Dibbert Inc', 'Corwin LLC', null, null, 4101, 536, 95, 94, null, 'Emard and Sons', null, 47, '159.29.8.99', '02');
insert into MOCK_TB_SEL_TRX_HIST (rcv_dt, rcv_seq, appr_dttm, rep_appr_no, appr_no, sale_dttm, mbr_id, card_dtl_grp, card_no, alcmpn, mcnt, stlmt_mcnt, pnt_knd, slip, sale_amt, pnt, cs_mbr_cmmsn, cmmsn, pmnt_way, org, oil_prdct_sgrp, sale_qty, cpn_prd, trx_type) values ('2015-12-10', null, '2016-05-07 02:55:51', '78345 CEDEX', null, '2016-06-02 21:45:36', '1', 'diners-club-carte-blanche', '30245582852310', 'Trantow, Beatty and King', 'Adams Inc', 'Rogahn Inc', null, null, 8042, 906, 61, 74, null, 'Yost-Stehr', null, 20, '30.196.200.80', '02');
insert into MOCK_TB_SEL_TRX_HIST (rcv_dt, rcv_seq, appr_dttm, rep_appr_no, appr_no, sale_dttm, mbr_id, card_dtl_grp, card_no, alcmpn, mcnt, stlmt_mcnt, pnt_knd, slip, sale_amt, pnt, cs_mbr_cmmsn, cmmsn, pmnt_way, org, oil_prdct_sgrp, sale_qty, cpn_prd, trx_type) values ('2015-12-16', null, '2015-12-28 23:37:07', null, '3830-455', '2015-10-14 18:09:26', '1', 'bankcard', '5602248095379760', 'Dare, Ritchie and Lebsack', 'Roob-Kohler', 'Koepp, Gleason and Grimes', null, null, 6337, 178, 93, 13, null, 'Krajcik-Corwin', null, 70, '58.26.18.61', '01');
insert into MOCK_TB_SEL_TRX_HIST (rcv_dt, rcv_seq, appr_dttm, rep_appr_no, appr_no, sale_dttm, mbr_id, card_dtl_grp, card_no, alcmpn, mcnt, stlmt_mcnt, pnt_knd, slip, sale_amt, pnt, cs_mbr_cmmsn, cmmsn, pmnt_way, org, oil_prdct_sgrp, sale_qty, cpn_prd, trx_type) values ('2016-03-25', '25646', '2016-09-11 01:08:37', null, null, '2015-10-19 15:41:05', '1', 'maestro', '5018731090629679980', 'Toy, Lakin and Gorczany', 'Daugherty-Nienow', 'Reynolds, Legros and Fay', null, null, 6082, 430, 11, 58, null, 'O''Kon Inc', null, 5, '203.209.152.17', '02');