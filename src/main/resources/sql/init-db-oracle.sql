SET DATABASE SQL SYNTAX ORA TRUE;

CREATE TABLE IF NOT EXISTS MBRT_MBR_ID_MST (
    MBR_ID          VARCHAR2(9),
    OCBCOM_LGN_ID   VARCHAR2(100),
    CI_NO           VARCHAR2(88),
    CARD_NO         VARCHAR2(88),
    SYW_MBR_ID      VARCHAR2(100),
    EVS_MBR_ID      VARCHAR2(100),
    MBR_KOR_NM      VARCHAR2(100),
    LD_DTTM         VARCHAR2(14),
    MART_UPD_DTTM   VARCHAR2(14)
);