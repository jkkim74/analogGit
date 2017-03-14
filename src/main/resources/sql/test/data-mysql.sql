insert into users(username, password, enabled, pts_username, create_dttm, begin_dttm) values('user', 'user', 1, 'co23835', '20160801090000', '20160801090000');
insert into users(username, password, enabled, pts_username, create_dttm, begin_dttm) values('user1', 'user1', 1, 'co23831', '20160801090000', '20160801090000');
insert into users(username, password, enabled, pts_username, create_dttm, begin_dttm) values('user2', 'user2', 1, 'co23832', '20160801090000', '20160801090000');
insert into users(username, password, enabled, pts_username, create_dttm, begin_dttm) values('user3', 'user3', 1, 'co23833', '20160801090000', '20160801090000');
insert into users(username, password, enabled, pts_username, create_dttm, begin_dttm) values('user4', 'user4', 0, 'co23834', '20160801090000', '20160801090000');
insert into users(username, password, enabled, pts_username, create_dttm, begin_dttm) values('user5', 'user5', 0, 'co23835', '20160801090000', '20160801090000');

insert into authorities(username, authority, masking_yn) values('user', 'ROLE_USER', 1);
insert into authorities(username, authority, masking_yn) values('user', 'ROLE_ADMIN', 1);
insert into authorities(username, authority, masking_yn) values('user1', 'ROLE_USER', 1);
insert into authorities(username, authority, masking_yn) values('user2', 'ROLE_USER', 1);
insert into authorities(username, authority, masking_yn) values('user2', 'ROLE_ADMIN', 1);
insert into authorities(username, authority, masking_yn) values('user3', 'ROLE_USER', 0);
insert into authorities(username, authority, masking_yn) values('user4', 'ROLE_USER', 0);
insert into authorities(username, authority, masking_yn) values('user4', 'ROLE_ADMIN', 0);
insert into authorities(username, authority, masking_yn) values('user5', 'ROLE_USER', 0);

insert into menu_access(username, menu_id) values('user', 'QCTEST');
insert into menu_access(username, menu_id) values('user', 'PAN0002');
insert into menu_access(username, menu_id) values('user', 'PAN0003');
insert into menu_access(username, menu_id) values('user', 'PAN0101');
insert into menu_access(username, menu_id) values('user', 'PAN0102');
insert into menu_access(username, menu_id) values('user', 'PAN0103');
insert into menu_access(username, menu_id) values('user', 'PAN0104');
insert into menu_access(username, menu_id) values('user', 'PAN0105');
insert into menu_access(username, menu_id) values('user', 'PAN0106');
insert into menu_access(username, menu_id) values('user', 'PAN0107');
insert into menu_access(username, menu_id) values('user1', 'PAN0101');
insert into menu_access(username, menu_id) values('user1', 'PAN0102');
insert into menu_access(username, menu_id) values('user2', 'PAN0002');
insert into menu_access(username, menu_id) values('user2', 'PAN0003');
insert into menu_access(username, menu_id) values('user2', 'PAN0101');
insert into menu_access(username, menu_id) values('user2', 'PAN0103');
insert into menu_access(username, menu_id) values('user3', 'PAN0104');
insert into menu_access(username, menu_id) values('user3', 'PAN0105');
insert into menu_access(username, menu_id) values('user4', 'PAN0102');
insert into menu_access(username, menu_id) values('user4', 'PAN0104');
insert into menu_access(username, menu_id) values('user4', 'PAN0107');
insert into menu_access(username, menu_id) values('user4', 'PAN0002');
insert into menu_access(username, menu_id) values('user4', 'PAN0003');
insert into menu_access(username, menu_id) values('user5', 'PAN0107');

insert into SINGLE_REQUEST_PROGRESS (SN,USERNAME,MEMBER_ID,EXTRACT_TARGET,EXTRACT_COND,PERIOD_TYPE,PERIOD_FROM,PERIOD_TO,STATUS) values('999991', 'user', '101700393', 'tr','t','sale_dt','20100101','20170314','FINISHED');
insert into SINGLE_REQUEST_PROGRESS (SN,USERNAME,MEMBER_ID,EXTRACT_TARGET,EXTRACT_COND,PERIOD_TYPE,PERIOD_FROM,PERIOD_TO,STATUS) values('999992', 'user', '101700393', 'tr','t','sale_dt','20100101','20170314','FINISHED');
insert into SINGLE_REQUEST_PROGRESS (SN,USERNAME,MEMBER_ID,EXTRACT_TARGET,EXTRACT_COND,PERIOD_TYPE,PERIOD_FROM,PERIOD_TO,STATUS) values('999993', 'user', '101700393', 'tr','t','sale_dt','20100101','20170314','FINISHED');
insert into SINGLE_REQUEST_PROGRESS (SN,USERNAME,MEMBER_ID,EXTRACT_TARGET,EXTRACT_COND,PERIOD_TYPE,PERIOD_FROM,PERIOD_TO,STATUS) values('999994', 'user', '101700393', 'tr','t','sale_dt','20100101','20170314','FINISHED');
insert into SINGLE_REQUEST_PROGRESS (SN,USERNAME,MEMBER_ID,EXTRACT_TARGET,EXTRACT_COND,PERIOD_TYPE,PERIOD_FROM,PERIOD_TO,STATUS) values('999995', 'user', '101700393', 'tr','t','sale_dt','20100101','20170314','FINISHED');
insert into SINGLE_REQUEST_PROGRESS (SN,USERNAME,MEMBER_ID,EXTRACT_TARGET,EXTRACT_COND,PERIOD_TYPE,PERIOD_FROM,PERIOD_TO,STATUS) values('999996', 'user', '101700393', 'tr','t','sale_dt','20100101','20170314','FINISHED');
insert into SINGLE_REQUEST_PROGRESS (SN,USERNAME,MEMBER_ID,EXTRACT_TARGET,EXTRACT_COND,PERIOD_TYPE,PERIOD_FROM,PERIOD_TO,STATUS) values('999997', 'user', '101700393', 'tr','t','sale_dt','20100101','20170314','PROCESSING');
insert into SINGLE_REQUEST_PROGRESS (SN,USERNAME,MEMBER_ID,EXTRACT_TARGET,EXTRACT_COND,PERIOD_TYPE,PERIOD_FROM,PERIOD_TO,STATUS) values('999998', 'user', '101700393', 'tr','t','sale_dt','20100101','20170314','PROCESSING');
insert into SINGLE_REQUEST_PROGRESS (SN,USERNAME,MEMBER_ID,EXTRACT_TARGET,EXTRACT_COND,PERIOD_TYPE,PERIOD_FROM,PERIOD_TO,STATUS) values('999999', 'user', '101700393', 'tr','t','sale_dt','20100101','20170314','PROCESSING');


















