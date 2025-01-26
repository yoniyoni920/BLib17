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
  `title` varchar(100) DEFAULT NULL,
  `authors` varchar(45) DEFAULT NULL,
  `genre` varchar(45) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'Superman',' John Byrne','Superhero','Born in England and raised in Canada, John Byrne discovered superheroes through The Adventures of Superman on television. After studying at the Alberta College of Art and Design, he broke into comics first with Skywald and then at Charlton, where he created the character Rog-2000. Following his tenure at Charlton, Byrne moved to Marvel, where his acclaimed runs on The Uncanny X-Men and The Fantastic Four soon made him one of the most popular artists in the industry. In 1986 he came to DC to revamp Superman from the ground up, and since then he has gone on to draw and/or write every major character at both DC and Marvel. ','superman.jpg','A'),(2,'Batman Vol. 1','Bob Kane, John Broome','Superhero','The Caped Crusader has never been stopped. Not by the Joker. Not by Two-Face. Not even by the entire Justice League. But now, Batman must face his most challenging foe ever - a hero who wants to save Gotham.. from the Batman. ','batman.jpg','B'),(3,'One Piece Vol. 1','Eichiro Oda','Shounen Jump','The One Piece is Real','luffy.jpg','C'),(4,'City of Orange','David Yoon','Sci-Fi','A man who can not remember his own name wakes up in an apocalyptic landscape, injured and alone. He has vague memories of life before, but he can\'t see it clearly and can\'t grasp how his current situation came to be. He must learn to survive by finding sources of water and foraging for food. Then he encounters a boy--and he realizes nothing is what he thought it was, neither the past nor the present. ','pic1.jpg','D'),(5,'The Let Them Theory','Mel Robbins','Motivation','A Life-Changing Tool Millions Of People Can\'t Stop Talking About','TheLetThemTheoryBookCover.jpg','E'),(6,'The Women','Kristin Hannah','Fiction','From the celebrated author of The Nightingale and The Four Winds comes Kristin Hannah\'s The Women—at once an intimate portrait of coming of age in a dangerous time and an epic tale of a nation divided','TheWomenBookCover.jpg','F'),(7,'The God of the Woods','Liz Moore','Fiction','When a teenager vanishes from her Adirondack summer camp, two worlds collide','TheGodOfTheWoodsBookCover.jpg','G'),(8,'The Unseen World','Liz Moore','Fiction','The moving story of a daughter’s quest to discover the truth about her beloved father’s hidden past.','TheUnseenWorldBookCover.jpg','H'),(9,'Chainsaw Man Vol. 1','Tatsuki Fujimoto','Shounen Jump','Denji\'s a poor young man who\'ll do anything for money, even hunting down Devils with his pet devil-dog Pochita. He\'s a simple man with simple dreams, drowning under a mountain of debt. But his sad life gets turned upside down when he\'s betrayed by someone he trusts. Now with the power of a Devil inside him, Denji\'s become a whole new man-Chainsaw Man! ','csm1.jpg','C'),(10,'The Amazing Spider-Man','Bob Harras','Superhero','Stan Lee breaks the fourth wall by ripping through the pages of a Spider-Man comic. He tells the reading audience that the Marvel Bullpen has asked him to narrate this month to tell stories about the past of the Marvel Universe. He then presents a story about Peter Parker\'s past, promising that this tale will reveal secrets that were known to himself and very few others before this moment... ','spiderman.jpg','A'),(11,'That Time I Got Reincarnated as a Slime, Vol. 1','Taiki Kawakami, Fuse, Mitz Vah','Isekai','As players of Monster Hunter and Dungeons & Dragons know, the slime is not exactly the king of the fantasy monsters. So when a 37-year-old Tokyo salaryman dies and wakes up in a world of dragons and magic, he\'s a little disappointed to find he\'s become a blind, boneless slime monster. ','slime.jpg','I'),(12,'The Understory','Saneh Sangsuk','Fiction','The lovable, yarnspinning monk Luang Paw Tien, now in his nineties, is the last person in his village to bear witness to the power and plenitude of the jungle before agrarian and then capitalist life took over his community. Nightly, he entertains the children of his village with tales from his younger years: his long pilgrimage to India, his mother’s dreams of a more stable life through agriculture, his proud huntsman father who resisted those dreams, and his love, who led him to pursue those dreams all over again. Sangsuk’s novel is a celebration of the oral tradition of storytelling and, above all else, a testament to the power of stories to entertain.','understory.jpg','F');
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_copy`
--

LOCK TABLES `book_copy` WRITE;
/*!40000 ALTER TABLE `book_copy` DISABLE KEYS */;
INSERT INTO `book_copy` VALUES (14,1,NULL,NULL,NULL,0,0,0),(15,1,NULL,NULL,NULL,0,0,0),(16,2,NULL,NULL,NULL,0,0,0),(17,2,NULL,NULL,NULL,0,0,0),(18,3,'2025-01-18 00:40:55','2025-02-01 00:40:55',3,0,0,0),(19,3,NULL,NULL,NULL,0,0,0),(20,4,NULL,NULL,NULL,0,0,0),(21,4,NULL,NULL,NULL,0,0,0),(22,5,NULL,NULL,NULL,0,0,0),(23,5,NULL,NULL,NULL,0,0,0),(24,6,NULL,NULL,NULL,0,0,0),(25,6,NULL,NULL,NULL,0,0,0),(26,7,NULL,NULL,NULL,0,0,0),(27,7,NULL,NULL,NULL,0,0,0),(28,8,NULL,NULL,NULL,0,0,0),(29,8,NULL,NULL,NULL,0,0,0),(30,9,'2025-01-27 00:48:01','2025-02-10 00:48:01',1,0,0,0),(31,9,NULL,NULL,NULL,0,0,0),(32,10,NULL,NULL,NULL,0,0,0),(33,10,NULL,NULL,NULL,0,0,0),(34,10,'2025-01-04 00:40:29','2025-01-18 00:40:29',2,0,1,0),(35,11,'2025-01-15 00:45:29','2025-01-29 00:45:29',2,0,0,0),(36,12,'2025-01-16 00:43:22','2025-01-30 00:43:22',3,0,0,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_order`
--

LOCK TABLES `book_order` WRITE;
/*!40000 ALTER TABLE `book_order` DISABLE KEYS */;
INSERT INTO `book_order` VALUES (23,1,12,'2025-01-26 01:04:15',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow_report`
--

LOCK TABLES `borrow_report` WRITE;
/*!40000 ALTER TABLE `borrow_report` DISABLE KEYS */;
INSERT INTO `borrow_report` VALUES (23,1,14,'2024-11-29','2024-12-13',NULL,0,'2024-12-01'),(24,1,15,'2024-11-30','2024-12-14','2024-12-22',1,'2024-12-01'),(25,1,14,'2024-12-14','2024-12-28',NULL,0,'2024-12-01'),(26,1,15,'2024-12-22','2025-01-05',NULL,0,'2024-12-01');
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
INSERT INTO `job` VALUES (11,'2025-01-25 23:31:53','generate-reports'),(12,'2025-01-27 00:32:28','check-borrows'),(17,'2025-01-26 00:38:50','send-reminders'),(18,'2025-01-27 00:32:28','cancel-orders');
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriber`
--

LOCK TABLES `subscriber` WRITE;
/*!40000 ALTER TABLE `subscriber` DISABLE KEYS */;
INSERT INTO `subscriber` VALUES (1,2,'0535201682','luffydafloffi@gmail.com',NULL),(2,3,'0521479856','daniel.rozentsvaig@e.braude.ac.il','2025-02-25'),(3,4,'0535201682','luffydafloffi@gmail.com',NULL),(4,5,'0508797841','danielandro11@gmail.com',NULL),(5,6,'0545699889','eliasfarah776@gmail.com',NULL),(6,7,'0535201682','luffydafloffi@gmail.com',NULL),(7,8,'0535201682','eliasfarah776@gmail.com',NULL);
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
  `action` enum('LOST_BOOK','BORROW_BOOK','LATE_RETURN','FREEZE_SUBSCRIBER','RETURN_BOOK','LOGIN_SUBSCRIBER','EXTEND_BORROW_SUBSCRIBER','EXTEND_BORROW_LIBRARIAN','ORDER_BOOK') NOT NULL,
  `subscriber_id` int NOT NULL,
  `book_copy_id` int DEFAULT NULL,
  `book_id` int DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_date` datetime DEFAULT NULL,
  `librarian_user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=294 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriber_history`
--

LOCK TABLES `subscriber_history` WRITE;
/*!40000 ALTER TABLE `subscriber_history` DISABLE KEYS */;
INSERT INTO `subscriber_history` VALUES (230,'BORROW_BOOK',1,14,NULL,'2024-11-29 15:30:03','2024-12-13 15:30:03',NULL),(231,'BORROW_BOOK',2,15,NULL,'2024-11-30 16:10:03','2024-12-14 16:10:03',NULL),(232,'LATE_RETURN',2,15,NULL,'2024-12-14 16:10:03','2024-12-22 16:00:03',NULL),(233,'LOGIN_SUBSCRIBER',2,NULL,NULL,'2024-11-29 15:10:03',NULL,NULL),(234,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2024-11-30 15:15:03',NULL,NULL),(237,'FREEZE_SUBSCRIBER',2,NULL,NULL,'2024-12-21 16:10:03','2024-01-20 16:10:03',NULL),(238,'ORDER_BOOK',3,NULL,1,'2024-12-12 12:30:03',NULL,NULL),(239,'BORROW_BOOK',3,14,NULL,'2024-12-14 15:20:00','2024-12-28 15:20:00',NULL),(240,'BORROW_BOOK',1,15,NULL,'2024-12-22 17:00:00','2025-01-05 17:00:00',NULL),(241,'RETURN_BOOK',1,14,NULL,'2024-12-13 11:30:03',NULL,NULL),(242,'RETURN_BOOK',2,15,NULL,'2024-12-22 16:00:03',NULL,NULL),(243,'RETURN_BOOK',3,14,NULL,'2024-12-27 15:20:00',NULL,NULL),(244,'RETURN_BOOK',1,15,NULL,'2025-01-05 13:00:00',NULL,NULL),(245,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-25 23:26:47',NULL,NULL),(248,'BORROW_BOOK',3,18,NULL,'2025-01-18 00:40:55','2025-02-01 00:40:55',NULL),(249,'BORROW_BOOK',3,36,NULL,'2025-01-16 00:43:22','2025-01-30 00:43:22',NULL),(250,'LOGIN_SUBSCRIBER',2,NULL,NULL,'2025-01-26 00:43:33',NULL,NULL),(251,'BORROW_BOOK',2,35,NULL,'2025-01-15 00:45:29','2025-01-29 00:45:29',NULL),(252,'LATE_RETURN',2,34,NULL,'2025-01-26 00:48:37',NULL,NULL),(253,'FREEZE_SUBSCRIBER',2,NULL,NULL,'2025-01-26 00:50:10','2025-02-25 00:50:10',NULL),(254,'LOGIN_SUBSCRIBER',2,NULL,NULL,'2025-01-26 00:50:19',NULL,NULL),(267,'ORDER_BOOK',1,NULL,12,'2025-01-26 01:04:16',NULL,NULL),(284,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-26 01:27:17',NULL,NULL),(285,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-27 00:32:37',NULL,NULL),(286,'LOGIN_SUBSCRIBER',2,NULL,NULL,'2025-01-27 00:35:18',NULL,NULL),(287,'LOGIN_SUBSCRIBER',4,NULL,NULL,'2025-01-27 00:35:29',NULL,NULL),(288,'LOGIN_SUBSCRIBER',3,NULL,NULL,'2025-01-27 00:37:09',NULL,NULL),(289,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-27 00:40:33',NULL,NULL),(290,'LOGIN_SUBSCRIBER',2,NULL,NULL,'2025-01-27 00:41:28',NULL,NULL),(291,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-27 00:41:31',NULL,NULL),(292,'BORROW_BOOK',1,30,NULL,'2025-01-27 00:48:01','2025-02-10 00:48:01',NULL),(293,'LOGIN_SUBSCRIBER',1,NULL,NULL,'2025-01-27 00:56:57',NULL,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriber_status_report`
--

LOCK TABLES `subscriber_status_report` WRITE;
/*!40000 ALTER TABLE `subscriber_status_report` DISABLE KEYS */;
INSERT INTO `subscriber_status_report` VALUES (16,'2024-12-21','2024-01-20','2024-12-01',2);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Nico','Robin','321','librarian'),(2,'Daniel','Student','123','subscriber'),(3,'Moti',' Lochim','123','subscriber'),(4,'Luffy','D. Monkey','123','subscriber'),(5,'Berry','Tzakala','123','subscriber'),(6,'Elias','Saile','elias','subscriber'),(7,'Helal','Hammoud','123','subscriber'),(8,'Chopper','Tony tony','human','subscriber');
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

-- Dump completed on 2025-01-27  1:03:58
