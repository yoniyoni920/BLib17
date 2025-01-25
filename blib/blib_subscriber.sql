CREATE DATABASE  IF NOT EXISTS `blib` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `blib`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: blib
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `authors` varchar(45) DEFAULT NULL,
  `genre` varchar(45) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'Superman',' John Byrne','Superhero','Born in England and raised in Canada, John Byrne discovered superheroes through The Adventures of Superman on television. After studying at the Alberta College of Art and Design, he broke into comics first with Skywald and then at Charlton, where he created the character Rog-2000. Following his tenure at Charlton, Byrne moved to Marvel, where his acclaimed runs on The Uncanny X-Men and The Fantastic Four soon made him one of the most popular artists in the industry. In 1986 he came to DC to revamp Superman from the ground up, and since then he has gone on to draw and/or write every major character at both DC and Marvel. ','superman.jpg','A'),(2,'Batman Vol. 1','Bob Kane, John Broome','Superhero','The Caped Crusader has never been stopped. Not by the Joker. Not by Two-Face. Not even by the entire Justice League. But now, Batman must face his most challenging foe ever - a hero who wants to save Gotham.. from the Batman. ','batman.jpg','B'),(3,'One Piece Vol. 1','Eichiro Oda','Shounen Jump','The One Piece is Real','luffy.jpg','C'),(4,'City of Orange','David Yoon','Sci-Fi','A man who can not remember his own name wakes up in an apocalyptic landscape, injured and alone. He has vague memories of life before, but he can\'t see it clearly and can\'t grasp how his current situation came to be. He must learn to survive by finding sources of water and foraging for food. Then he encounters a boy--and he realizes nothing is what he thought it was, neither the past nor the present. ','pic1.jpg','D'),(5,'The Let Them Theory','Mel Robbins','Motivation','A Life-Changing Tool Millions Of People Can\'t Stop Talking About','TheLetThemTheoryBookCover.jpg','E'),(6,'The Women','Kristin Hannah','Fiction','From the celebrated author of The Nightingale and The Four Winds comes Kristin Hannah\'s The Women—at once an intimate portrait of coming of age in a dangerous time and an epic tale of a nation divided','TheWomenBookCover.jpg','F'),(7,'The God of the Woods','Liz Moore','Fiction','When a teenager vanishes from her Adirondack summer camp, two worlds collide','TheGodOfTheWoodsBookCover.jpg','G'),(8,'The Unseen World','Liz Moore','Fiction','The moving story of a daughter’s quest to discover the truth about her beloved father’s hidden past.','TheUnseenWorldBookCover.jpg','H'),(9,'Chainsaw Man Vol. 1','Tatsuki Fujimoto','Shounen Jump','Denji\'s a poor young man who\'ll do anything for money, even hunting down Devils with his pet devil-dog Pochita. He\'s a simple man with simple dreams, drowning under a mountain of debt. But his sad life gets turned upside down when he\'s betrayed by someone he trusts. Now with the power of a Devil inside him, Denji\'s become a whole new man-Chainsaw Man! ','csm1.jpg','C');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_copy`
--

DROP TABLE IF EXISTS `book_copy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_copy` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL,
  `lend_date` datetime DEFAULT NULL,
  `return_date` datetime DEFAULT NULL,
  `borrow_subscriber_id` int DEFAULT NULL,
  `is_lost` tinyint DEFAULT '0',
  `is_late` tinyint DEFAULT '0',
  `is_waiting` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `bookId_idx` (`book_id`),
  KEY `subscriber_id_idx` (`borrow_subscriber_id`),
  CONSTRAINT `borrow_subscriber_id` FOREIGN KEY (`borrow_subscriber_id`) REFERENCES `subscriber` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_copy`
--

LOCK TABLES `book_copy` WRITE;
/*!40000 ALTER TABLE `book_copy` DISABLE KEYS */;
INSERT INTO `book_copy` VALUES (1,1,'2025-01-25 12:11:38','2025-02-08 12:11:38',3,0,0,0),(2,3,NULL,NULL,NULL,0,1,0),(3,1,NULL,NULL,NULL,0,0,0),(4,2,NULL,NULL,NULL,0,0,0),(5,2,NULL,NULL,NULL,0,1,0),(6,4,NULL,NULL,NULL,0,0,0),(7,5,NULL,NULL,NULL,0,0,0),(8,6,NULL,NULL,NULL,0,0,0),(9,7,NULL,NULL,NULL,0,0,0),(10,8,NULL,NULL,NULL,0,0,0),(11,9,NULL,NULL,NULL,0,0,0),(12,9,NULL,NULL,NULL,0,0,0);
/*!40000 ALTER TABLE `book_copy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_order`
--

DROP TABLE IF EXISTS `book_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `subscriber_id` int NOT NULL,
  `book_id` int NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ordered_until` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `subscriber_id_idx` (`subscriber_id`),
  KEY `book_id_idx` (`book_id`),
  CONSTRAINT `book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `subscriber` FOREIGN KEY (`subscriber_id`) REFERENCES `subscriber` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_order`
--

LOCK TABLES `book_order` WRITE;
/*!40000 ALTER TABLE `book_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `book_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow_report`
--

DROP TABLE IF EXISTS `borrow_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_id` int DEFAULT NULL,
  `book_copy_id` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `late_return_date` date DEFAULT NULL,
  `is_late` tinyint DEFAULT NULL,
  `report_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `book_id_idx` (`book_id`),
  CONSTRAINT `book_id` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow_report`
--

LOCK TABLES `borrow_report` WRITE;
/*!40000 ALTER TABLE `borrow_report` DISABLE KEYS */;
INSERT INTO `borrow_report` VALUES (9,1,3,'2024-12-04','2024-12-18',NULL,1,'2024-12-01');
/*!40000 ALTER TABLE `borrow_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (11,'2025-01-22 02:34:24','generate-reports'),(12,'2025-01-25 13:36:50','check-borrows'),(17,'2025-01-23 21:57:23','send-reminders'),(18,'2025-01-25 13:36:50','cancel-orders');
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `subscriber_id` int NOT NULL,
  `message` varchar(200) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `is_new` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (14,1,'Extended Borrow Duration for Book: Batman Vol. 1 (Copy 5) by 14 Days','2025-01-23 00:00:00',0),(15,1,'Extended Borrow Duration for Book: Batman Vol. 1 (Copy 5) by 14 Days','2025-01-23 00:00:00',0),(16,1,'Extended Borrow Duration for Book: Batman Vol. 1 (Copy 5) by 14 Days','2025-01-23 00:00:00',0),(17,1,'Extended Borrow Duration for Book: Batman Vol. 1 (Copy 5) by 14 Days','2025-01-23 06:15:46',0);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriber`
--

DROP TABLE IF EXISTS `subscriber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscriber` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `phone_number` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `frozen_until` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriber`
--

LOCK TABLES `subscriber` WRITE;
/*!40000 ALTER TABLE `subscriber` DISABLE KEYS */;
INSERT INTO `subscriber` VALUES (1,1,'0501234567','luffydafloffi@gmail.com','2025-01-10'),(2,5,'0521479856','shalom@gmail.com',NULL),(3,3,'0535201682','danielandro11@gmail.com',NULL),(4,4,'0508797841','ma@gmail.com','2025-01-10'),(5,6,'0508797111','elias@elias.elias',NULL);
/*!40000 ALTER TABLE `subscriber` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriber_history`
--

DROP TABLE IF EXISTS `subscriber_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscriber_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `action` varchar(45) NOT NULL,
  `subscriber_id` int NOT NULL,
  `book_copy_id` int DEFAULT NULL,
  `book_id` int DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_date` datetime DEFAULT NULL,
  `librarian_user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriber_history`
--

LOCK TABLES `subscriber_history` WRITE;
/*!40000 ALTER TABLE `subscriber_history` DISABLE KEYS */;
INSERT INTO `subscriber_history` VALUES (127,'FREEZE_SUBSCRIBER',3,NULL,NULL,'2024-11-03 00:00:00','2024-12-03 00:00:00',NULL),(128,'FREEZE_SUBSCRIBER',3,NULL,NULL,'2024-12-24 00:00:00','2025-01-23 00:00:00',NULL),(129,'BORROW_BOOK',3,1,NULL,'2024-12-03 00:00:00','2024-12-17 00:00:00',NULL),(130,'LATE_RETURN',3,1,NULL,'2024-12-17 00:00:00',NULL,NULL),(131,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-23 21:44:00',NULL,NULL),(132,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-23 21:45:51',NULL,NULL),(133,'BORROW_BOOK',3,2,NULL,'2025-01-23 00:00:00','2025-01-24 00:00:00',NULL),(134,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-23 21:47:44',NULL,NULL),(135,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 21:49:57',NULL,NULL),(136,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 21:54:30',NULL,NULL),(137,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 21:57:03',NULL,NULL),(138,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-23 21:58:43',NULL,NULL),(139,'ORDER_BOOK',3,NULL,4,'2025-01-23 21:58:51',NULL,NULL),(140,'RETURN_BOOK',1,6,NULL,'2025-01-23 21:59:03',NULL,NULL),(141,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-23 22:00:34',NULL,NULL),(142,'BORROW_BOOK',3,6,NULL,'2025-01-23 00:00:00','2025-02-06 00:00:00',NULL),(143,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 22:03:50',NULL,NULL),(144,'ORDER_BOOK',1,NULL,4,'2025-01-23 22:03:53',NULL,NULL),(145,'RETURN_BOOK',3,6,NULL,'2025-01-23 22:04:00',NULL,NULL),(146,'BORROW_BOOK',1,6,NULL,'2025-01-23 00:00:00','2025-02-06 00:00:00',NULL),(147,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-23 22:04:57',NULL,NULL),(148,'ORDER_BOOK',3,NULL,4,'2025-01-23 22:05:04',NULL,NULL),(149,'RETURN_BOOK',1,6,NULL,'2025-01-23 22:05:19',NULL,NULL),(150,'BORROW_BOOK',3,6,NULL,'2025-01-23 00:00:00','2025-02-06 00:00:00',NULL),(151,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 22:06:02',NULL,NULL),(152,'ORDER_BOOK',1,NULL,4,'2025-01-23 22:06:04',NULL,NULL),(153,'RETURN_BOOK',3,6,NULL,'2025-01-23 22:06:31',NULL,NULL),(154,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 22:07:02',NULL,NULL),(155,'BORROW_BOOK',1,6,NULL,'2025-01-23 00:00:00','2025-02-06 00:00:00',NULL),(156,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-23 22:07:31',NULL,NULL),(157,'ORDER_BOOK',3,NULL,4,'2025-01-23 22:07:34',NULL,NULL),(158,'RETURN_BOOK',1,6,NULL,'2025-01-23 22:07:48',NULL,NULL),(159,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 22:09:03',NULL,NULL),(160,'BORROW_BOOK',3,6,NULL,'2025-01-23 00:00:00','2025-02-06 00:00:00',NULL),(161,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 22:14:34',NULL,NULL),(162,'ORDER_BOOK',1,NULL,4,'2025-01-23 22:14:36',NULL,NULL),(163,'RETURN_BOOK',3,6,NULL,'2025-01-23 22:14:45',NULL,NULL),(164,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 22:15:42',NULL,NULL),(165,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-23 22:15:49',NULL,NULL),(166,'BORROW_BOOK',1,6,NULL,'2025-01-23 00:00:00','2025-02-06 00:00:00',NULL),(167,'ORDER_BOOK',3,NULL,4,'2025-01-23 22:16:13',NULL,NULL),(168,'RETURN_BOOK',1,6,NULL,'2025-01-23 22:16:15',NULL,NULL),(169,'BORROW_BOOK',3,6,NULL,'2025-01-23 00:00:00','2025-02-06 00:00:00',NULL),(170,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 22:18:50',NULL,NULL),(171,'ORDER_BOOK',1,NULL,4,'2025-01-23 22:18:53',NULL,NULL),(172,'RETURN_BOOK',3,6,NULL,'2025-01-23 22:18:57',NULL,NULL),(173,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-23 22:20:28',NULL,NULL),(174,'BORROW_BOOK',1,6,NULL,'2025-01-23 00:00:00','2025-02-06 00:00:00',NULL),(175,'ORDER_BOOK',3,NULL,4,'2025-01-23 22:21:03',NULL,NULL),(176,'RETURN_BOOK',1,6,NULL,'2025-01-23 22:21:08',NULL,NULL),(177,'LATE_RETURN',3,2,NULL,'2025-01-25 00:53:01',NULL,NULL),(178,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 00:53:12',NULL,NULL),(179,'ORDER_BOOK',1,NULL,3,'2025-01-25 00:53:25',NULL,NULL),(180,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 01:56:33',NULL,NULL),(181,'ORDER_BOOK',1,NULL,3,'2025-01-25 01:56:45',NULL,NULL),(182,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 02:01:00',NULL,NULL),(183,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 02:05:10',NULL,NULL),(184,'RETURN_BOOK',3,2,NULL,'2025-01-25 02:06:28',NULL,NULL),(185,'BORROW_BOOK',1,2,NULL,'2025-01-25 00:00:00','2025-02-08 00:00:00',NULL),(186,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 02:07:06',NULL,NULL),(187,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 12:08:11',NULL,NULL),(188,'RETURN_BOOK',1,5,NULL,'2025-01-25 12:08:31',NULL,NULL),(189,'BORROW_BOOK',3,1,NULL,'2025-01-25 12:11:38','2025-02-08 12:11:38',NULL),(190,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 12:11:47',NULL,NULL),(191,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-25 12:11:50',NULL,NULL),(192,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 12:23:48',NULL,NULL),(193,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 12:28:43',NULL,NULL),(194,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 12:28:45',NULL,NULL),(195,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 12:29:05',NULL,NULL),(196,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 12:29:25',NULL,NULL),(197,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 12:30:51',NULL,NULL),(198,'RETURN_BOOK',1,2,NULL,'2025-01-25 12:31:17',NULL,NULL),(199,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 12:33:02',NULL,NULL),(200,'BORROW_BOOK',1,3,NULL,'2025-01-25 12:33:17','2025-02-08 12:33:17',NULL),(201,'RETURN_BOOK',1,3,NULL,'2025-01-25 12:34:51',NULL,NULL),(202,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 12:36:54',NULL,NULL),(203,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 13:04:12',NULL,NULL);
/*!40000 ALTER TABLE `subscriber_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriber_status_report`
--

DROP TABLE IF EXISTS `subscriber_status_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscriber_status_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `freeze_date` date DEFAULT NULL,
  `freeze_end_date` date DEFAULT NULL,
  `report_date` date DEFAULT NULL,
  `subscriber_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `subscriber_id_idx` (`subscriber_id`),
  CONSTRAINT `subscriber_id` FOREIGN KEY (`subscriber_id`) REFERENCES `subscriber` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriber_status_report`
--

LOCK TABLES `subscriber_status_report` WRITE;
/*!40000 ALTER TABLE `subscriber_status_report` DISABLE KEYS */;
INSERT INTO `subscriber_status_report` VALUES (11,'2024-12-03','2025-01-03','2024-12-01',1),(12,'2024-12-03','2025-01-03','2024-12-01',1);
/*!40000 ALTER TABLE `subscriber_status_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role` enum('librarian','subscriber') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Daniel','Student','123','subscriber'),(2,'Safranit','Lol','321','librarian'),(3,'Luffy','D.Monkey','123','subscriber'),(4,'bery','hi','123','subscriber'),(5,'moti','lochim','123','subscriber'),(6,'Elias','Yes','elias','subscriber'),(7,'Helal','Hammoud','123','subscriber');
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

-- Dump completed on 2025-01-25 13:47:59
