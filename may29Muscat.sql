-- MySQL dump 10.13  Distrib 8.0.33, for Linux (x86_64)
--
-- Host: localhost    Database: muscat
-- ------------------------------------------------------
-- Server version	8.0.33-0ubuntu0.20.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `SPRING_SESSION`
--

DROP TABLE IF EXISTS `SPRING_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SPRING_SESSION` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SPRING_SESSION`
--

LOCK TABLES `SPRING_SESSION` WRITE;
/*!40000 ALTER TABLE `SPRING_SESSION` DISABLE KEYS */;
INSERT INTO `SPRING_SESSION` VALUES ('1cfa7ecd-07a0-4260-bf81-db5586bf9abe','2df7fb6f-91f6-4687-aca0-99d6a934f44a',1685337307391,1685339197497,1800,1685340997497,NULL);
/*!40000 ALTER TABLE `SPRING_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SPRING_SESSION_ATTRIBUTES`
--

DROP TABLE IF EXISTS `SPRING_SESSION_ATTRIBUTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SPRING_SESSION_ATTRIBUTES` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SPRING_SESSION_ATTRIBUTES`
--

LOCK TABLES `SPRING_SESSION_ATTRIBUTES` WRITE;
/*!40000 ALTER TABLE `SPRING_SESSION_ATTRIBUTES` DISABLE KEYS */;
INSERT INTO `SPRING_SESSION_ATTRIBUTES` VALUES ('1cfa7ecd-07a0-4260-bf81-db5586bf9abe','loggedIn',_binary '¨\Ì\0t\0true'),('1cfa7ecd-07a0-4260-bf81-db5586bf9abe','loggedMobile',_binary '¨\Ì\0t\0\n9111111111'),('1cfa7ecd-07a0-4260-bf81-db5586bf9abe','user',_binary '¨\Ì\0sr\0java.util.HashMap\⁄¡\√`\—\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0countryt\0Omant\0locationt\01234t\0idsr\0java.lang.Long;ã\‰êÃè#\ﬂ\0J\0valuexr\0java.lang.NumberÜ¨ïî\‡ã\0\0xp\0\0\0\0\0\0\0t\0userTypet\0Membert\0mobileNot\0\n9111111111t\0userNamet\0tuladhart\0emailt\0tula.vedi@gmail.comx'),('1cfa7ecd-07a0-4260-bf81-db5586bf9abe','userType',_binary '¨\Ì\0t\0Member');
/*!40000 ALTER TABLE `SPRING_SESSION_ATTRIBUTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_slot`
--

DROP TABLE IF EXISTS `book_slot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_slot` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `approved_by` varchar(255) DEFAULT NULL,
  `approved_time` datetime DEFAULT NULL,
  `book_time` datetime DEFAULT NULL,
  `booked_by` varchar(255) DEFAULT NULL,
  `booking_date` date DEFAULT NULL,
  `booking_no` varchar(255) DEFAULT NULL,
  `branch` varchar(255) DEFAULT NULL,
  `confirm_status` varchar(255) DEFAULT NULL,
  `court_code` varchar(255) DEFAULT NULL,
  `game_date` date DEFAULT NULL,
  `ref_no` varchar(255) DEFAULT NULL,
  `remarks_by_admin` varchar(255) DEFAULT NULL,
  `remarks_by_user` varchar(255) DEFAULT NULL,
  `slot_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_slot`
--

LOCK TABLES `book_slot` WRITE;
/*!40000 ALTER TABLE `book_slot` DISABLE KEYS */;
INSERT INTO `book_slot` VALUES (6,NULL,NULL,'2023-05-27 17:42:08','9111111111','2023-05-27',NULL,NULL,'pending','Ts-1','2023-05-27',NULL,NULL,NULL,'Ts-1-PH/1'),(7,'tula','2023-05-28 16:31:55','2023-05-27 17:45:05','9111111111','2023-05-28',NULL,NULL,'accepted','BD-1','2023-05-28',NULL,NULL,NULL,'BD-1-WEND/2'),(8,NULL,NULL,'2023-05-27 21:17:59','9111111111','2023-05-27',NULL,NULL,'pending','Ts-1','2023-05-27',NULL,NULL,NULL,'Ts-1-PH/8'),(9,NULL,NULL,'2023-05-27 21:34:40','9988776655','2023-05-27',NULL,NULL,'pending','Ts-1','2023-05-27',NULL,NULL,NULL,'Ts-1-PH/1'),(10,'tula','2023-05-28 16:39:15','2023-05-27 21:37:34','9111111111','2023-05-28',NULL,NULL,'rejected','Ts-1','2023-05-28',NULL,NULL,NULL,'Ts-1-WEND/2'),(11,'tula','2023-05-28 16:33:10','2023-05-28 14:49:03','9988776655','2023-05-28',NULL,NULL,'rejected','Ts-1','2023-05-28',NULL,NULL,NULL,'Ts-1-WEND/2'),(12,'tula','2023-05-28 16:33:03','2023-05-28 14:49:17','9988776655','2023-05-30',NULL,NULL,'accepted','Ts-1','2023-05-30',NULL,NULL,NULL,'Ts-1-REGL/3'),(13,NULL,NULL,'2023-05-29 10:51:21','9111111111','2023-05-29',NULL,NULL,'pending','Ts-1','2023-05-29',NULL,NULL,NULL,'Ts-1-REGL/1');
/*!40000 ALTER TABLE `book_slot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `court`
--

DROP TABLE IF EXISTS `court`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `court` (
  `id` int NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `end_date` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `start_date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `court`
--

LOCK TABLES `court` WRITE;
/*!40000 ALTER TABLE `court` DISABLE KEYS */;
INSERT INTO `court` VALUES (10,'Admin','Ts-1','2023-06-30','Tennis','2023-05-25'),(11,'Admin','BD-1','2023-06-27','Badminton','2023-05-27');
/*!40000 ALTER TABLE `court` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `day_type`
--

DROP TABLE IF EXISTS `day_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `day_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `day_type`
--

LOCK TABLES `day_type` WRITE;
/*!40000 ALTER TABLE `day_type` DISABLE KEYS */;
INSERT INTO `day_type` VALUES (1,'REGL','Regular'),(2,'PH','Public Holiday'),(3,'WEND','Weekend'),(4,'SPL','Special'),(5,'SPLPH','Special Public Holiday'),(8,'DOFF','day off');
/*!40000 ALTER TABLE `day_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (1,'BD','Badminton'),(2,'Ts','Tennis');
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `referral`
--

DROP TABLE IF EXISTS `referral`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `referral` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `referral`
--

LOCK TABLES `referral` WRITE;
/*!40000 ALTER TABLE `referral` DISABLE KEYS */;
INSERT INTO `referral` VALUES (1,'ADM','Admin'),(2,'MEM','Member'),(3,'VIP','VIP'),(4,'TMT','TopMgmt');
/*!40000 ALTER TABLE `referral` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slot`
--

DROP TABLE IF EXISTS `slot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slot` (
  `id` int NOT NULL AUTO_INCREMENT,
  `court_code` varchar(255) DEFAULT NULL,
  `day_type` varchar(255) DEFAULT NULL,
  `end_hour` varchar(255) DEFAULT NULL,
  `slot_code` varchar(255) DEFAULT NULL,
  `slot_length` int NOT NULL,
  `start_hour` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12627 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slot`
--

LOCK TABLES `slot` WRITE;
/*!40000 ALTER TABLE `slot` DISABLE KEYS */;
INSERT INTO `slot` VALUES (12551,'BD-1','REGL','07:30','BD-1-REGL/1',90,'06:00'),(12552,'BD-1','REGL','09:00','BD-1-REGL/2',90,'07:30'),(12553,'BD-1','REGL','10:30','BD-1-REGL/3',90,'09:00'),(12554,'BD-1','REGL','12:00','BD-1-REGL/4',90,'10:30'),(12569,'Ts-1','REGL','07:00','Ts-1-REGL/1',60,'06:00'),(12570,'Ts-1','REGL','08:00','Ts-1-REGL/2',60,'07:00'),(12571,'Ts-1','REGL','09:00','Ts-1-REGL/3',60,'08:00'),(12572,'Ts-1','REGL','10:00','Ts-1-REGL/4',60,'09:00'),(12573,'Ts-1','REGL','11:00','Ts-1-REGL/5',60,'10:00'),(12574,'Ts-1','REGL','12:00','Ts-1-REGL/6',60,'11:00'),(12575,'Ts-1','REGL','13:00','Ts-1-REGL/7',60,'12:00'),(12576,'Ts-1','REGL','14:00','Ts-1-REGL/8',60,'13:00'),(12577,'Ts-1','REGL','15:00','Ts-1-REGL/9',60,'14:00'),(12578,'Ts-1','REGL','16:00','Ts-1-REGL/10',60,'15:00'),(12579,'Ts-1','REGL','17:00','Ts-1-REGL/11',60,'16:00'),(12581,'BD-1','WEND','06:00','BD-1-WEND/1',60,'05:00'),(12582,'Ts-1','WEND','06:00','Ts-1-WEND/1',60,'05:00'),(12583,'BD-1','WEND','07:00','BD-1-WEND/2',60,'06:00'),(12584,'Ts-1','WEND','07:00','Ts-1-WEND/2',60,'06:00'),(12585,'BD-1','WEND','08:00','BD-1-WEND/3',60,'07:00'),(12586,'Ts-1','WEND','08:00','Ts-1-WEND/3',60,'07:00'),(12587,'BD-1','WEND','09:00','BD-1-WEND/4',60,'08:00'),(12588,'Ts-1','WEND','09:00','Ts-1-WEND/4',60,'08:00'),(12589,'BD-1','WEND','10:00','BD-1-WEND/5',60,'09:00'),(12590,'Ts-1','WEND','10:00','Ts-1-WEND/5',60,'09:00'),(12591,'BD-1','WEND','11:00','BD-1-WEND/6',60,'10:00'),(12592,'Ts-1','WEND','11:00','Ts-1-WEND/6',60,'10:00'),(12593,'BD-1','WEND','12:00','BD-1-WEND/7',60,'11:00'),(12594,'Ts-1','WEND','12:00','Ts-1-WEND/7',60,'11:00'),(12595,'BD-1','WEND','13:00','BD-1-WEND/8',60,'12:00'),(12596,'Ts-1','WEND','13:00','Ts-1-WEND/8',60,'12:00'),(12597,'BD-1','WEND','14:00','BD-1-WEND/9',60,'13:00'),(12598,'Ts-1','WEND','14:00','Ts-1-WEND/9',60,'13:00'),(12599,'BD-1','WEND','15:00','BD-1-WEND/10',60,'14:00'),(12600,'Ts-1','WEND','15:00','Ts-1-WEND/10',60,'14:00'),(12601,'BD-1','WEND','16:00','BD-1-WEND/11',60,'15:00'),(12602,'Ts-1','WEND','16:00','Ts-1-WEND/11',60,'15:00'),(12603,'BD-1','WEND','17:00','BD-1-WEND/12',60,'16:00'),(12604,'Ts-1','WEND','17:00','Ts-1-WEND/12',60,'16:00'),(12605,'BD-1','WEND','18:00','BD-1-WEND/13',60,'17:00'),(12606,'Ts-1','WEND','18:00','Ts-1-WEND/13',60,'17:00'),(12607,'BD-1','WEND','19:00','BD-1-WEND/14',60,'18:00'),(12608,'Ts-1','WEND','19:00','Ts-1-WEND/14',60,'18:00'),(12609,'BD-1','WEND','20:00','BD-1-WEND/15',60,'19:00'),(12610,'Ts-1','WEND','20:00','Ts-1-WEND/15',60,'19:00'),(12611,'Ts-1','PH','06:45','Ts-1-PH/1',45,'06:00'),(12612,'BD-1','PH','06:45','BD-1-PH/1',45,'06:00'),(12613,'Ts-1','PH','07:30','Ts-1-PH/2',45,'06:45'),(12614,'BD-1','PH','07:30','BD-1-PH/2',45,'06:45'),(12615,'Ts-1','PH','08:15','Ts-1-PH/3',45,'07:30'),(12616,'BD-1','PH','08:15','BD-1-PH/3',45,'07:30'),(12617,'Ts-1','PH','09:00','Ts-1-PH/4',45,'08:15'),(12618,'BD-1','PH','09:00','BD-1-PH/4',45,'08:15'),(12619,'Ts-1','PH','09:45','Ts-1-PH/5',45,'09:00'),(12620,'BD-1','PH','09:45','BD-1-PH/5',45,'09:00'),(12621,'Ts-1','PH','10:30','Ts-1-PH/6',45,'09:45'),(12622,'BD-1','PH','10:30','BD-1-PH/6',45,'09:45'),(12623,'Ts-1','PH','11:15','Ts-1-PH/7',45,'10:30'),(12624,'BD-1','PH','11:15','BD-1-PH/7',45,'10:30'),(12625,'Ts-1','PH','12:00','Ts-1-PH/8',45,'11:15'),(12626,'BD-1','PH','12:00','BD-1-PH/8',45,'11:15');
/*!40000 ALTER TABLE `slot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `special_dates`
--

DROP TABLE IF EXISTS `special_dates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `special_dates` (
  `id` int NOT NULL AUTO_INCREMENT,
  `court_code` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `day_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `special_dates`
--

LOCK TABLES `special_dates` WRITE;
/*!40000 ALTER TABLE `special_dates` DISABLE KEYS */;
INSERT INTO `special_dates` VALUES (2,'Ts-1','2023-05-27','PH');
/*!40000 ALTER TABLE `special_dates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `country` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `mobile_no` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `referral` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (7,'Oman','tuladharreddy345@gmail.com',NULL,'India','9441370993','1234','ADM','tula','Admin'),(8,'Oman','tula.vedi@gmail.com',NULL,'1234','9111111111','1234','MEM','tuladhar','Member'),(9,'Oman','tuladharreddy345@gmail.com',NULL,'muscat','9988776655','1234','MEM','tula','Member');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-29 11:46:55
