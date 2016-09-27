SET DATABASE SQL SYNTAX MYS TRUE;

CREATE TABLE UPLOAD_PROGRESS (
    PAGE_ID         VARCHAR(16)     NOT NULL,
    USERNAME        VARCHAR(64)     NOT NULL,
    COLUMN_NAME     VARCHAR(32),
    FILENAME		VARCHAR(256),
    UPLOAD_STATUS   VARCHAR(16)     NOT NULL,
    
    PRIMARY KEY (PAGE_ID, USERNAME)
);

CREATE TABLE USERS (
	USERNAME VARCHAR(50) NOT NULL PRIMARY KEY,
	PASSWORD VARCHAR(50) NOT NULL,
	ENABLED BOOLEAN NOT NULL
);

CREATE TABLE AUTHORITIES (
	USERNAME VARCHAR(50) NOT NULL,
	AUTHORITY VARCHAR(50) NOT NULL,
	FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME)
);
CREATE UNIQUE INDEX IX_AUTH_USERNAME ON AUTHORITIES (USERNAME,AUTHORITY);

CREATE TABLE PAGE_ACCESS (
	USERNAME VARCHAR(50) NOT NULL,
	PAGE_ID VARCHAR(16) NOT NULL
	/*FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME)*/
);
CREATE UNIQUE INDEX IX_PAGE_USERNAME ON PAGE_ACCESS (USERNAME,PAGE_ID);

insert into users(username, password, enabled) values('user', '', 1);
insert into authorities(username, authority) values('user', 'ROLE_USER');
insert into authorities(username, authority) values('user', 'ROLE_ADMIN');
insert into page_access(username, page_id) values('user', 'PAN0101');
insert into page_access(username, page_id) values('user', 'PAN0102');
insert into page_access(username, page_id) values('user', 'PAN0103');
insert into page_access(username, page_id) values('user', 'PAN0104');
insert into page_access(username, page_id) values('user', 'PAN0105');
insert into page_access(username, page_id) values('user', 'PAN0106');
insert into page_access(username, page_id) values('user', 'PAN0002');
insert into page_access(username, page_id) values('user', 'PAN0003');
