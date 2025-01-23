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
  `id` int NOT NULL,
  `title` varchar(45) DEFAULT NULL,
  `authors` varchar(45) DEFAULT NULL,
  `genre` varchar(45) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'Superman',' John Byrne','Superhero',' Born in England and raised in Canada, John Byrne discovered superheroes through The Adventures of Superman on television. After studying at the Alberta College of Art and Design, he broke into comics first with Skywald and then at Charlton, where he created the character Rog-2000. Following his tenure at Charlton, Byrne moved to Marvel, where his acclaimed runs on The Uncanny X-Men and The Fantastic Four soon made him one of the most popular artists in the industry. In 1986 he came to DC to revamp Superman from the ground up, and since then he has gone on to draw and/or write every major character at both DC and Marvel. ','superman.jpg','A'),(2,'Batman Vol. 1','Bob Kane, John Broome','Superhero','cc1','batman.jpg','B'),(3,'One Piece Vol. 1','Eichiro Oda','Shounen','The One Piece is Real','luffy.jpg','C'),(4,'City of Orange','David Yoon','Sci-Fi','aa1','pic1.jpg','D'),(5,'The Let Them Theory','Mel Robbins','Motivation','A Life-Changing Tool Millions Of People Can\'t Stop Talking About','TheLetThemTheoryBookCover.jpg','E'),(6,'The Women','Kristin Hannah','Fiction','From the celebrated author of The Nightingale and The Four Winds comes Kristin Hannah\'s The Women—at once an intimate portrait of coming of age in a dangerous time and an epic tale of a nation divided','TheWomenBookCover.jpg','F'),(7,'The God of the Woods','Liz Moore','Fiction','When a teenager vanishes from her Adirondack summer camp, two worlds collide','TheGodOfTheWoodsBookCover.jpg','G'),(8,'The Unseen World','Liz Moore','Fiction','The moving story of a daughter’s quest to discover the truth about her beloved father’s hidden past.','TheUnseenWorldBookCover.jpg','H');
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
  PRIMARY KEY (`id`),
  KEY `bookId_idx` (`book_id`),
  KEY `subscriber_id_idx` (`borrow_subscriber_id`),
  CONSTRAINT `borrow_subscriber_id` FOREIGN KEY (`borrow_subscriber_id`) REFERENCES `subscriber` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_copy`
--

LOCK TABLES `book_copy` WRITE;
/*!40000 ALTER TABLE `book_copy` DISABLE KEYS */;
INSERT INTO `book_copy` VALUES (1,1,NULL,NULL,NULL,0,0),(2,3,NULL,NULL,NULL,0,0),(3,1,NULL,NULL,NULL,0,0),(4,2,NULL,NULL,NULL,0,0),(5,2,'2025-01-04 00:00:00','2025-02-07 00:00:00',1,0,1),(6,4,'2023-03-16 00:00:00','2025-03-30 00:00:00',1,0,0),(7,5,NULL,NULL,NULL,0,0),(8,6,NULL,NULL,NULL,0,0),(9,7,NULL,NULL,NULL,0,0),(10,8,NULL,NULL,NULL,0,0);
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
  `date` date NOT NULL DEFAULT (now()),
  `ordered_until` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `subscriber_id_idx` (`subscriber_id`),
  KEY `book_id_idx` (`book_id`),
  CONSTRAINT `book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  CONSTRAINT `subscriber` FOREIGN KEY (`subscriber_id`) REFERENCES `subscriber` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_order`
--

LOCK TABLES `book_order` WRITE;
/*!40000 ALTER TABLE `book_order` DISABLE KEYS */;
INSERT INTO `book_order` VALUES (1,1,1,'2025-01-21',NULL),(4,3,1,'2025-01-21',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (11,'2025-01-22 02:34:24','generate-reports'),(12,'2025-01-23 06:25:56','check-borrows');
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
INSERT INTO `subscriber` VALUES (1,1,'0501234567','hi@gmail.com','2025-01-10'),(2,5,'0521479856','shalom@gmail.com',NULL),(3,3,'0548975642','bye@walla.com',NULL),(4,4,'0508797841','ma@gmail.com','2025-01-10'),(5,6,'0508797111','elias@elias.elias',NULL);
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
  `date` datetime NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `librarian_user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriber_history`
--

LOCK TABLES `subscriber_history` WRITE;
/*!40000 ALTER TABLE `subscriber_history` DISABLE KEYS */;
INSERT INTO `subscriber_history` VALUES (1,'BORROW_BOOK',1,3,NULL,'2024-12-04 00:00:00','2024-12-18 00:00:00',NULL),(2,'LATE_RETURN',1,3,NULL,'2024-12-18 00:00:00',NULL,NULL),(4,'LOST_BOOK',1,4,NULL,'2025-01-21 00:00:00',NULL,NULL),(5,'FREEZE_SUBSCRIBER',1,NULL,NULL,'2024-12-03 00:00:00','2025-01-03 00:00:00',NULL),(6,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 05:05:28',NULL,NULL),(7,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 05:09:38',NULL,NULL),(8,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 05:13:19',NULL,NULL),(9,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 05:14:00',NULL,NULL),(10,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 22:56:10',NULL,NULL),(11,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 23:00:45',NULL,NULL),(12,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 23:02:07',NULL,NULL),(13,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 23:02:23',NULL,NULL),(14,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 23:03:19',NULL,NULL),(15,'EXTEND_BORROW_LIBRARIAN',1,5,NULL,'2025-01-22 23:13:10','2025-01-18 00:00:00',NULL),(16,'EXTEND_BORROW_LIBRARIAN',1,5,NULL,'2025-01-22 23:14:16','2025-01-20 00:00:00',NULL),(17,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 23:14:40',NULL,NULL),(18,'EXTEND_BORROW_LIBRARIAN',1,5,NULL,'2025-01-22 23:17:54','2025-01-21 00:00:00',NULL),(19,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 23:17:59',NULL,NULL),(20,'EXTEND_BORROW_LIBRARIAN',1,5,NULL,'2025-01-22 23:20:18','2025-01-23 00:00:00',2),(21,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-22 23:21:05',NULL,NULL),(56,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 05:45:46',NULL,NULL),(57,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 05:45:47',NULL,NULL),(58,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 05:46:14',NULL,NULL),(59,'EXTEND_BORROW_SUBSCRIBER',1,5,NULL,'2025-01-23 05:48:17','2025-02-06 00:00:00',NULL),(60,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:06:40',NULL,NULL),(61,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:07:54',NULL,NULL),(62,'EXTEND_BORROW_SUBSCRIBER',1,5,NULL,'2025-01-23 06:09:24','2025-02-09 00:00:00',NULL),(63,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:11:39',NULL,NULL),(64,'EXTEND_BORROW_SUBSCRIBER',1,5,NULL,'2025-01-23 06:12:57','2025-02-06 00:00:00',NULL),(65,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:14:41',NULL,NULL),(66,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:15:00',NULL,NULL),(67,'EXTEND_BORROW_SUBSCRIBER',1,5,NULL,'2025-01-23 06:15:01','2025-02-07 00:00:00',NULL),(68,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:15:45',NULL,NULL),(69,'EXTEND_BORROW_SUBSCRIBER',1,5,NULL,'2025-01-23 06:15:46','2025-02-07 00:00:00',NULL),(70,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:26:04',NULL,NULL),(71,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:49:49',NULL,NULL),(72,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:50:36',NULL,NULL),(73,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:51:14',NULL,NULL),(74,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:51:47',NULL,NULL),(75,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:52:05',NULL,NULL),(76,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-23 06:52:28',NULL,NULL);
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
INSERT INTO `user` VALUES (1,'Daniel','Student','123','subscriber'),(2,'Safranit','Lol','321','librarian'),(3,'yeled','haha','123','subscriber'),(4,'bery','hi','123','subscriber'),(5,'moti','lochim','123','subscriber'),(6,'Elias','Yes','elias','subscriber'),(7,'Helal','Hammoud','123','subscriber');
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

-- Dump completed on 2025-01-23  6:54:46
