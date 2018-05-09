1. 导出数据库
mysqldump --host=172.188.0.11 -uroot -p870712 iot > iot.sql
2. 导入数据库
mysql --host=172.188.0.11 -P3306 -uroot -p870712 iot < iot.sql
