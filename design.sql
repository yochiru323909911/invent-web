-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: יוני 29, 2023 בזמן 09:48 AM
-- גרסת שרת: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ex5`
--

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `design`
--

CREATE TABLE `design` (
  `creation_date` date DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `img_design_id` bigint(20) DEFAULT NULL,
  `free_text` varchar(255) DEFAULT NULL,
  `owner` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- הוצאת מידע עבור טבלה `design`
--

INSERT INTO `design` (`creation_date`, `id`, `img_design_id`, `free_text`, `owner`) VALUES
('2023-06-29', 2, 1, 'jmht\r\nght\r\nhgty\r\nhgyt\r\nhyt\r\n', 'user1'),
('2023-06-29', 3, 3, 'h5\r\nhg5\r\nh\r\nt\r\nht\r\nthtyt', 'user1'),
('2023-06-29', 4, 4, 'h5\r\nhg5\r\nh\r\nt\r\nht\r\nthtyt', 'user1'),
('2023-06-29', 5, 6, 'hr\r\nhtr\r\nty\r\nhty\r\nhty\r\nht', 'user2'),
('2023-06-28', 6, 15, 'hr\r\nhtr\r\nty\r\nhty\r\nhty\r\nht', 'user2'),
('2023-06-28', 7, 12, 'hr\r\nhtr\r\nty\r\nhty\r\ngrtgrtg\r\nht', 'user2'),
('2023-06-28', 8, 3, 'hr\r\nhtr\r\nrtrtgrt\r\nhty\r\ngrtgrtg\r\nht', 'user2'),
('2023-06-27', 9, 13, 'hr\r\nhtr\r\nrtrtgrt\r\nhty\r\ngrtgrtg\r\nht', 'user3'),
('2023-06-27', 10, 5, 'hr\r\nhtr\r\nrtrtgrt\r\nhty\r\ngrtgrtg\r\nht', 'user3'),
('2023-06-27', 11, 6, 'hr\r\nhtr\r\nrtrtgrt\r\nhty\r\ngrtgrtg\r\nht', 'user3'),
('2023-06-27', 12, 14, 'hr\r\nhtr\r\nrtrtgrt\r\nhty\r\ngrtgrtg\r\nht', 'user3');

--
-- Indexes for dumped tables
--

--
-- אינדקסים לטבלה `design`
--
ALTER TABLE `design`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKgrs54we6g0e3447ted655j7fa` (`img_design_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `design`
--
ALTER TABLE `design`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- הגבלות לטבלאות שהוצאו
--

--
-- הגבלות לטבלה `design`
--
ALTER TABLE `design`
  ADD CONSTRAINT `FKgrs54we6g0e3447ted655j7fa` FOREIGN KEY (`img_design_id`) REFERENCES `image` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
