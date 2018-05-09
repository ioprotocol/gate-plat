-- MySQL dump 10.13  Distrib 5.7.20, for Linux (x86_64)
--
-- Host: mysql1.net.com    Database: mis
-- ------------------------------------------------------
-- Server version	5.7.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `account_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `account` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(80) CHARACTER SET utf8 DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sex` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mobile` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `is_enable` smallint(6) DEFAULT '0',
  `address` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `photo_url` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `AK_ACCOUNT` (`account`),
  UNIQUE KEY `AK_MOBILE` (`mobile`),
  UNIQUE KEY `AK_EMAIL` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,1,'administrator','7CA20707A9B922E4DEC5392B247681D88BE9CC7EFB5942A2F427B6CD','超级管理员','男','xsy870712@163.com','15315086265',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `log_id` bigint(20) unsigned NOT NULL,
  `code` varchar(80) DEFAULT NULL,
  `op_account` varchar(50) DEFAULT NULL,
  `op_name` varchar(50) DEFAULT NULL,
  `role_id` int(10) DEFAULT NULL,
  `role_name` varchar(50) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `idx_code_acc` (`code`,`op_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
INSERT INTO `log` VALUES (1522548335327000,'登录失败','administrator','administrator',NULL,NULL,'帐号:administrator,密码:1234567,IP:'),(1522548346275001,'登录成功','administrator','administrator',NULL,NULL,'[Y]-->token:eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk'),(1522548346564002,'帐号查询单个','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/account/current/login||null||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36;Referer=ht'),(1522548350042003,'性能监控','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/performance||offset=0&rows=20&rateUnit=seconds&durationUnit=milliseconds&sort=mean&isDesc=true||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu'),(1522548350154004,'性能监控','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/performance||offset=0&rows=20&rateUnit=seconds&durationUnit=milliseconds&sort=mean&isDesc=true||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu'),(1522548374951005,'查询所有角色','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/role||null||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36;Referer=http://localhost:95'),(1522548374960006,'帐号查询所有','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36500||/api/account||offset=0&rows=20&keyword=&roleId=||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/53'),(1522548377353007,'查询所有角色','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36500||/api/role||null||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36;Referer=http://localhost:95'),(1522548377355008,'帐号查询单个','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/account/current/login||null||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36;Referer=ht'),(1522548378637009,'查询所有角色','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/role||offset=0&rows=20||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36;Referer=http://'),(1522548380626010,'查询角色','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/role/popedom/1||null||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36;Referer=http://lo'),(1522548398483011,'查询备份','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/sysbackup||offset=0&rows=20||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36;Referer=ht'),(1522548405238012,'查询备份','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/sysbackup||offset=0&rows=20||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36;Referer=ht'),(1522548408123013,'查询备份','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/sysbackup||offset=0&rows=20||Host=127.0.0.1:8080;Connection=keep-alive;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36;Referer=ht'),(1522548409127014,'系统备份','administrator','超级管理员',1,'超级管理员','[Y]127.0.0.1:36472||/api/sysbackup||null||Host=127.0.0.1:8080;Connection=keep-alive;Content-Length=0;Accept=application/json, text/plain, */*;Origin=http://localhost:9528;X-Token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50IjoiYWRtaW5pc3RyYXRvciIsImlhdCI6MTUyMjU0ODM0NiwiZXhwIjoxNTIyNTU5MTQ2fQ.Lmp_fe3XobtXQtd9IV8bXQtZ0wdMa2v1r-nngkRsEqk;User-Agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36;Refer');
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `popedom`
--

DROP TABLE IF EXISTS `popedom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `popedom` (
  `popedom_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) CHARACTER SET utf8 DEFAULT NULL,
  `code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`popedom_id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `popedom`
--

LOCK TABLES `popedom` WRITE;
/*!40000 ALTER TABLE `popedom` DISABLE KEYS */;
INSERT INTO `popedom` VALUES (1,'日志查询','/.+/log/GET','查询系统的访问日志'),(2,'日志删除','/.+/log/DELETE','清空系统的访问日志'),(3,'性能监控','/.+/performance/GET','管理员的功能，可以监控平台接口使用情况'),(4,'查询备份','/.+/sysbackup/GET','查询系统中的数据库备份文件'),(5,'上传备份','/.+/sysbackup/POST','从本地电脑上传备份文件到服务器'),(6,'系统备份','/.+/sysbackup/PUT','执行一次服务器数据库备份'),(7,'删除备份','/.+/sysbackup/DELETE','删除系统中的某一个数据库备份文件'),(8,'系统恢复','/.+/sysrestore/PUT','恢复某一个备份文件到数据库中'),(9,'vue[主面板]','vuedashboard','网页权限，系统首页'),(10,'vue[系统管理]','vuesystemmgr','网页权限，系统管理菜单'),(11,'vue[账号管理]','vueaccountmgr','网页权限，帐号管理页面'),(12,'vue[在线用户]','vueonlineaccount','网页权限，在线用户页面'),(13,'vue[密码修改]','vueaccountpwd','网页权限，密码修改页面'),(14,'vue[账号信息]','vueaccountinfo','网页权限，当前登录帐号信息页面'),(15,'vue[角色管理]','vuerolemgr','网页权限，角色管理页面'),(16,'vue[数据备份]','vuedbback','网页权限，数据备份页面'),(17,'vue[日志管理]','vuelogmgr','网页权限，日志管理页面'),(18,'vue[性能统计]','vueperformance','网页权限，性能统计页面'),(19,'增加角色','/.+/role/POST','管理员功能，创建系统角色'),(20,'删除角色','/.+/role/.+/DELETE','管理员功能，删除系统角色'),(21,'更新角色','/.+/role/PUT','管理员功能，更新系统角色信息'),(22,'查询角色','/.+/role/.+/GET','查询系统的单个角色信息'),(23,'查询所有角色','/.+/role/GET','查询系统的所有角色信息'),(24,'权限修改','/.+/role/popedom/PUT','管理员功能，分配每个系统角色的操作权限'),(25,'角色权限查询','/.+/role/popedom/.+/GET','管理员功能，查询每个角色拥有的操作权限集合'),(26,'界面权限查询','/.+/role/popedom/vue/.+/GET','基本功能，查询角色拥有的前端界面集合(可见)'),(27,'接口权限查询','/.+/role/popedom/api/.+/GET','管理员功能，查询角色拥有的后台接口权限'),(28,'帐号添加','/.+/account/POST','管理员的功能，可以添加各种角色的帐号'),(29,'账号删除','/.+/account/.+/DELETE','管理员的功能，可以删除系统中的所有帐号'),(30,'账号修改','/.+/account/PUT','基本功能，修改帐号的基本信息，如姓名、电话、邮箱等'),(31,'账号停启用','/.+/account/enable/PUT','管理员的功能，可以停用或者启用某个帐号'),(32,'帐号查询单个','/.+/account/.+/GET','管理员的功能，查询特定帐号的信息'),(33,'帐号查询所有','/.+/account/GET','管理员的功能，查询系统中的所有帐号信息'),(34,'当前登录账号','/.+/account/current/login/GET','基本功能，查询当前登录帐号的基本信息'),(35,'帐号密码重置','/.+/account/resetpwd/.+/PUT','管理员的功能，某个帐号密码忘记后，可以通过此接口对密码进行重置'),(36,'修改帐号密码','/.+/account/editpwd/PUT','基本功能，自助修改自己帐号的密码'),(37,'退出帐号登录','/.+/account/logout/PUT','基本功能，清除服务器session信息，并退出登录'),(38,'在线用户','/.+/account/session/GET','查询系统当前活跃用户');
/*!40000 ALTER TABLE `popedom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'超级管理员');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_popedom`
--

DROP TABLE IF EXISTS `role_popedom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_popedom` (
  `role_id` int(11) DEFAULT NULL,
  `popedom_id` int(11) DEFAULT NULL,
  UNIQUE KEY `role_id` (`role_id`,`popedom_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_popedom`
--

LOCK TABLES `role_popedom` WRITE;
/*!40000 ALTER TABLE `role_popedom` DISABLE KEYS */;
INSERT INTO `role_popedom` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34),(1,35),(1,36),(1,37),(1,38);
/*!40000 ALTER TABLE `role_popedom` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-01 10:06:50
