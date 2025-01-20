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
  `lend_date` date DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `borrow_subscriber_id` int DEFAULT NULL,
  `order_subscriber_id` int DEFAULT NULL,
  `is_lost` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `bookId_idx` (`book_id`),
  KEY `subscriber_id_idx` (`borrow_subscriber_id`),
  KEY `order_subscriber_id_idx` (`order_subscriber_id`),
  CONSTRAINT `borrow_subscriber_id` FOREIGN KEY (`borrow_subscriber_id`) REFERENCES `subscriber` (`id`),
  CONSTRAINT `order_subscriber_id` FOREIGN KEY (`order_subscriber_id`) REFERENCES `subscriber` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_copy`
--

LOCK TABLES `book_copy` WRITE;
/*!40000 ALTER TABLE `book_copy` DISABLE KEYS */;
INSERT INTO `book_copy` VALUES (1,1,'2025-01-15','2025-01-29',1,NULL,0),(2,3,NULL,NULL,NULL,NULL,0),(3,1,'2025-01-02','2025-01-16',1,NULL,0),(4,2,'2025-01-03','2025-01-17',1,3,0),(5,2,'2025-01-04','2025-01-16',1,1,0),(6,4,'2023-03-16','2025-03-30',1,NULL,0),(7,5,NULL,NULL,NULL,NULL,0),(8,6,NULL,NULL,NULL,NULL,0),(9,7,NULL,NULL,NULL,NULL,0),(10,8,NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `book_copy` ENABLE KEYS */;
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
  `report_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `book_id_idx` (`book_id`),
  CONSTRAINT `book_id` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow_report`
--

LOCK TABLES `borrow_report` WRITE;
/*!40000 ALTER TABLE `borrow_report` DISABLE KEYS */;
INSERT INTO `borrow_report` VALUES (1,1,3,'2024-12-16','2024-12-20','2024-12-22','2024-12-01'),(2,1,3,'2024-12-23','2024-12-26',NULL,'2024-12-01'),(3,1,4,'2024-12-22','2024-12-25',NULL,'2024-12-01'),(4,2,5,'2024-12-20','2025-01-02','2025-01-04','2024-12-01');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (1,'2025-01-10 16:54:32','generate-reports'),(2,'2025-01-18 15:10:31','check-borrows');
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
  `subscriber_name` varchar(100) NOT NULL,
  `message` varchar(150) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `isNew` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
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
INSERT INTO `subscriber` VALUES (1,1,'0501234567','hi@gmail.com','2025-02-16'),(2,5,'0521479856','shalom@gmail.com',NULL),(3,3,'0548975642','bye@walla.com',NULL),(4,4,'0508797841','ma@gmail.com','2025-01-10'),(5,6,'0508797111','elias@elias.elias',NULL);
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
  `date` date NOT NULL,
  `end_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriber_history`
--

LOCK TABLES `subscriber_history` WRITE;
/*!40000 ALTER TABLE `subscriber_history` DISABLE KEYS */;
INSERT INTO `subscriber_history` VALUES (1,'borrow',1,3,'2024-12-16','2024-12-20'),(2,'late',1,3,'2024-12-22',NULL);
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriber_status_report`
--

LOCK TABLES `subscriber_status_report` WRITE;
/*!40000 ALTER TABLE `subscriber_status_report` DISABLE KEYS */;
INSERT INTO `subscriber_status_report` VALUES (1,'2024-11-01','2024-12-01','2024-11-01'),(2,'2024-11-04','2024-12-15','2024-11-01'),(3,'2024-12-01','2025-01-01','2024-12-01'),(4,'2024-12-03','2024-12-12','2024-12-01'),(5,'2024-12-03','2024-12-10','2024-12-01'),(6,'2024-12-08','2024-12-20','2024-12-01');
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
INSERT INTO `user` VALUES (1,'Daniel','Student','123','subscriber'),(2,'Safranit','Lol','321','librarian'),(3,'yeled','haha','852','subscriber'),(4,'bery','hi','963','subscriber'),(5,'moti','lochim','258','subscriber'),(6,'Elias','Yes','elias','subscriber'),(7,'Helal','Hammoud','123','subscriber');
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

-- Dump completed on 2025-01-18 16:09:56
