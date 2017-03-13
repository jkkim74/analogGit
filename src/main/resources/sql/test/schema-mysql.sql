SET DATABASE SQL SYNTAX MYS TRUE;

CREATE TABLE IF NOT EXISTS USERS (
	USERNAME VARCHAR(50) NOT NULL PRIMARY KEY,
	PASSWORD VARCHAR(50),
	ENABLED BOOLEAN NOT NULL,
	PTS_USERNAME VARCHAR(50),
	BEGIN_DTTM VARCHAR(14),
	END_DTTM VARCHAR(14),
	CREATE_DTTM VARCHAR(14)
);

CREATE TABLE IF NOT EXISTS AUTHORITIES (
	USERNAME VARCHAR(50) NOT NULL,
	AUTHORITY VARCHAR(50) NOT NULL,
	MASKING_YN BOOLEAN,

	FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME)
);
CREATE UNIQUE INDEX IX_AUTH_USERNAME ON AUTHORITIES (USERNAME, AUTHORITY);

CREATE TABLE IF NOT EXISTS MENU_ACCESS (
	USERNAME VARCHAR(50) NOT NULL,
	MENU_ID VARCHAR(16) NOT NULL,

	FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME)
);
CREATE UNIQUE INDEX IX_MENU_USERNAME ON MENU_ACCESS (USERNAME, MENU_ID);

CREATE TABLE IF NOT EXISTS MENU_PROGRESS (
    MENU_ID VARCHAR(16) NOT NULL,
    USERNAME VARCHAR(50) NOT NULL,
    PARAM VARCHAR(32),
    FILENAME VARCHAR(256),
    STATUS VARCHAR(16) NOT NULL,
    
    PRIMARY KEY(MENU_ID, USERNAME),
    FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME)
);

CREATE TABLE IF NOT EXISTS IDMS_ACCESS_LOG (
	USERNAME VARCHAR(50) NOT NULL,
	USER_IP VARCHAR(45) NOT NULL,
	LOGIN_DTTM VARCHAR(14) NOT NULL,
	LOGOUT_DTTM VARCHAR(14)
);
CREATE UNIQUE INDEX IX_ACCESS_LOG ON IDMS_ACCESS_LOG(USERNAME, USER_IP, LOGIN_DTTM);

CREATE TABLE IF NOT EXISTS IDMS_MEMBER_SEARCH_LOG (
	SEL_DTTM VARCHAR(14) NOT NULL,
	WAS_IP VARCHAR(45) NOT NULL,
	USERNAME VARCHAR(50) NOT NULL,
	USER_IP VARCHAR(45) NOT NULL,
	MBR_ID VARCHAR(9),
	MBR_KOR_NM VARCHAR(50),
	MENU_ID VARCHAR(16),
	FUNC_CD VARCHAR(16),
	MBR_CNT INT
);
CREATE INDEX IX_MEMBER_SEARCH_LOG ON IDMS_MEMBER_SEARCH_LOG(SEL_DTTM, WAS_IP, USERNAME, USER_IP);

CREATE TABLE IF NOT EXISTS OAUTH_ACCESS_TOKEN (
	TOKEN_ID VARCHAR(256),
	TOKEN BLOB,
	AUTHENTICATION_ID VARCHAR(255) PRIMARY KEY,
	USER_NAME VARCHAR(256),
	CLIENT_ID VARCHAR(256),
	AUTHENTICATION BLOB,
	REFRESH_TOKEN VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS OAUTH_REFRESH_TOKEN (
	TOKEN_ID VARCHAR(256),
	TOKEN BLOB,
	AUTHENTICATION BLOB
);

CREATE TABLE IF NOT EXISTS SINGLE_REQUEST_PROGRESS (
	SN int NOT NULL auto_increment,
	USERNAME VARCHAR(50) DEFAULT NULL,
	MEMBER_ID VARCHAR(50) NOT NULL,
	EXTRACT_TARGET VARCHAR(50) NOT NULL,
	EXTRACT_COND VARCHAR(50) NOT NULL,
	PERIOD_TYPE VARCHAR(50) NOT NULL,
	PERIOD_FROM VARCHAR(50) NOT NULL,
	PERIOD_TO VARCHAR(50) NOT NULL,
	STATUS VARCHAR(50) DEFAULT NULL,
	REQ_START_TIME datetime DEFAULT NULL,
	REQ_FINISH_TIME datetime DEFAULT NULL,
	PRIMARY KEY (SN)
);
