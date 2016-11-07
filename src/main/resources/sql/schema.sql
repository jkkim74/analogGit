CREATE TABLE IF NOT EXISTS USERS (
	USERNAME VARCHAR(50) NOT NULL PRIMARY KEY,
	PASSWORD VARCHAR(50),
	ENABLED TINYINT NOT NULL,
	PTS_USERNAME VARCHAR(50),
	BEGIN_DTTM VARCHAR(14),
	END_DTTM VARCHAR(14),
	CREATE_DTTM VARCHAR(14)
);

CREATE TABLE IF NOT EXISTS AUTHORITIES (
	USERNAME VARCHAR(50) NOT NULL,
	AUTHORITY VARCHAR(50) NOT NULL,

	UNIQUE INDEX IX_AUTH_USERNAME (USERNAME, AUTHORITY),
	FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME)
);

CREATE TABLE IF NOT EXISTS PAGE_ACCESS (
	USERNAME VARCHAR(50) NOT NULL,
	PAGE_ID VARCHAR(16) NOT NULL,

	UNIQUE INDEX IX_PAGE_USERNAME (USERNAME, PAGE_ID),
	FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME)
);

CREATE TABLE IF NOT EXISTS UPLOAD_PROGRESS (
    USERNAME VARCHAR(50) NOT NULL,
    PAGE_ID VARCHAR(16) NOT NULL,
    COLUMN_NAME VARCHAR(32),
    FILENAME VARCHAR(256),
    UPLOAD_STATUS VARCHAR(16) NOT NULL,
    
    PRIMARY KEY(USERNAME, PAGE_ID),
    FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME)
);

CREATE TABLE IF NOT EXISTS IDMS_ACCESS_LOG (
	USERNAME VARCHAR(50) NOT NULL,
	USER_IP VARCHAR(45) NOT NULL,
	LOGIN_DTTM VARCHAR(14) NOT NULL,
	LOGOUT_DTTM VARCHAR(14),
	
	UNIQUE INDEX IX_ACCESS_LOG (USERNAME, USER_IP, LOGIN_DTTM)
);

CREATE TABLE IF NOT EXISTS IDMS_MEMBER_SEARCH_LOG (
	SEL_DTTM VARCHAR(14) NOT NULL,
	WAS_IP VARCHAR(45) NOT NULL,
	USERNAME VARCHAR(50) NOT NULL,
	USER_IP VARCHAR(45) NOT NULL,
	MBR_ID VARCHAR(9),
	MBR_KOR_NM VARCHAR(50),
	PAGE_ID VARCHAR(16),
	FUNC_CD VARCHAR(16),
	MBR_CNT INT,
	
	UNIQUE INDEX IX_MEMBER_SEARCH_LOG (SEL_DTTM, WAS_IP, USERNAME, USER_IP)
);

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
