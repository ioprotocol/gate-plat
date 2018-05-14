drop table if exists account
CREATE TABLE account(account_id int(11) NOT NULL AUTO_INCREMENT,role_id int(11) DEFAULT NULL,account varchar(50) DEFAULT NULL, password varchar(80) DEFAULT NULL, name varchar(50) DEFAULT NULL, sex varchar(20) DEFAULT NULL, email varchar(50) DEFAULT NULL, mobile varchar(30) DEFAULT NULL, is_enable smallint(6) DEFAULT '0', address varchar(100) DEFAULT NULL, photo_url varchar(100) DEFAULT NULL, remark varchar(500) DEFAULT NULL, PRIMARY KEY (account_id), UNIQUE KEY AK_ACCOUNT (account), UNIQUE KEY AK_MOBILE (mobile), UNIQUE KEY AK_EMAIL (email))
drop table if exists log
CREATE TABLE log (log_id bigint(20) unsigned NOT NULL,code varchar(80) DEFAULT NULL,op_account varchar(50) DEFAULT NULL,op_name varchar(50) DEFAULT NULL,role_id int(10) DEFAULT NULL,role_name varchar(50) DEFAULT NULL,description varchar(500) DEFAULT NULL,PRIMARY KEY (log_id))
mysql:drop index if exists idx_code_acc on log
h2:drop index if exists idx_code_acc
create index idx_code_acc  on log(code,op_account)
drop table if exists popedom
CREATE TABLE popedom ( popedom_id int(11) NOT NULL AUTO_INCREMENT, name varchar(60)  DEFAULT NULL, code varchar(50)  DEFAULT NULL, remark varchar(200) DEFAULT NULL, PRIMARY KEY (popedom_id), UNIQUE KEY idx_code (code))
drop table if exists role
CREATE TABLE role ( role_id int(11) unsigned NOT NULL AUTO_INCREMENT, name varchar(50)  DEFAULT NULL, PRIMARY KEY (role_id))
drop table if exists role_popedom
CREATE TABLE role_popedom(role_id int(11) DEFAULT NULL,popedom_id int(11) DEFAULT NULL,UNIQUE KEY role_id (role_id,popedom_id))
INSERT INTO role VALUES (1,'超级管理员')
INSERT INTO account VALUES (1,1,'administrator','8495179A39AE1BF4F5A6084A9388D5BE4D38EDF026922D41862F89B0','超级管理员','男','xsy870712@163.com','15315086265',1,NULL,NULL,NULL)
