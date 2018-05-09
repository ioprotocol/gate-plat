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
INSERT INTO `account` VALUES (1,1,'administrator','8495179A39AE1BF4F5A6084A9388D5BE4D38EDF026922D41862F89B0','超级管理员','男','xsy870712@163.com','15315086265',1,NULL,NULL,NULL);
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
  PRIMARY KEY (`popedom_id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `popedom`
--

LOCK TABLES `popedom` WRITE;
/*!40000 ALTER TABLE `popedom` DISABLE KEYS */;
INSERT INTO `popedom` VALUES (1,'*日志查询','/[a-zA-Z]+/log/GET'),(2,'*日志删除','/[a-zA-Z]+/log/DELETE'),(3,'*系统备份','/[a-zA-Z]+/sysbackup/PUT'),(4,'*系统恢复','/[a-zA-Z]+/sysrestore/PUT'),(5,'*下载备份','/[a-zA-Z]+/sysdownload/GET'),(6,'系统管理','vuesystemmgr'),(7,'账号管理','vueaccountmgr'),(8,'密码修改','vueaccountpwd'),(9,'账号信息','vueaccountinfo'),(10,'角色管理','vuerolemgr'),(11,'数据备份','vuedbback'),(12,'日志管理','vuelogmgr'),(13,'*角色增加','/[a-zA-Z]+/role/POST'),(14,'*角色删除','/[a-zA-Z]+/role/[0-9]+/DELETE'),(15,'*角色更新','/[a-zA-Z]+/role/PUT'),(16,'*角色查询','/[a-zA-Z]+/role/[0-9]+/GET'),(17,'*角色查询所有','/[a-zA-Z]+/role/GET'),(18,'*权限修改','/[a-zA-Z]+/role/popedom/PUT'),(19,'*权限查询','/[a-zA-Z]+/role/popedom/[0-9]+/GET'),(20,'*账号添加','/[a-zA-Z]+/account/POST'),(21,'*账号删除','/[a-zA-Z]+/account/[0-9]+/DELETE'),(22,'*账号更新','/[a-zA-Z]+/account/PUT'),(23,'*账号查询','/[a-zA-Z]+/account/[0-9]+/GET'),(24,'*账号查询所有','/[a-zA-Z]+/account/GET'),(25,'*登录账号','/[a-zA-Z]+/account/current/login/GET'),(26,'*密码修改','/[a-zA-Z]+/account/resetpwd/[0-9]+/PUT');
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
INSERT INTO `role_popedom` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26);
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

-- Dump completed on 2018-02-10 20:43:55
