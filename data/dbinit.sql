drop table if exists account
CREATE TABLE account(account_id int(11) NOT NULL AUTO_INCREMENT,role_id int(11) DEFAULT NULL,account varchar(50) DEFAULT NULL, password varchar(80) DEFAULT NULL, name varchar(50) DEFAULT NULL, sex varchar(20) DEFAULT NULL, email varchar(50) DEFAULT NULL, mobile varchar(30) DEFAULT NULL, is_enable smallint(6) DEFAULT '0', address varchar(100) DEFAULT NULL, photo_url varchar(100) DEFAULT NULL, remark varchar(500) DEFAULT NULL, PRIMARY KEY (account_id), UNIQUE KEY AK_ACCOUNT (account), UNIQUE KEY AK_MOBILE (mobile), UNIQUE KEY AK_EMAIL (email))
drop table if exists log
CREATE TABLE log (log_id bigint(20) unsigned NOT NULL,code varchar(80) DEFAULT NULL,op_account varchar(50) DEFAULT NULL,op_name varchar(50) DEFAULT NULL,role_id int(10) DEFAULT NULL,role_name varchar(50) DEFAULT NULL,description varchar(500) DEFAULT NULL,PRIMARY KEY (log_id))
mysql:drop index if exists idx_code_acc on log
h2:drop index if exists idx_code_acc
create index idx_code_acc  on log(code,op_account)
drop table if exists popedom
CREATE TABLE popedom ( popedom_id int(11) NOT NULL AUTO_INCREMENT, name varchar(90)  DEFAULT NULL, code varchar(50)  DEFAULT NULL, remark varchar(200) DEFAULT NULL, PRIMARY KEY (popedom_id), UNIQUE KEY idx_code (code))
drop table if exists role
CREATE TABLE role ( role_id int(11) unsigned NOT NULL AUTO_INCREMENT, name varchar(50)  DEFAULT NULL, PRIMARY KEY (role_id))
drop table if exists role_popedom
CREATE TABLE role_popedom(role_id int(11) DEFAULT NULL,popedom_id int(11) DEFAULT NULL,UNIQUE KEY role_id (role_id,popedom_id))
INSERT INTO role VALUES (1,'超级管理员')
INSERT INTO account VALUES (1,1,'administrator','#$#RyK10fWa$eBEHCX6VRk1msyBwRj2je/','超级管理员','男','xsy870712@163.com','15315086265',1,NULL,NULL,NULL)
create table gateway(gate_id int(11) unsigned not null auto_increment, host_name varchar(60), port int(11), protocol_id int(11), config_properties text, created_time TIMESTAMP, last_modify_time TIMESTAMP, PRIMARY KEY (gate_id))
create table protocol(protocol_id int(11) unsigned not null auto_increment, name varchar(60), config_properties text, created_time TIMESTAMP, last_modify_time TIMESTAMP, PRIMARY KEY (protocol_id))
