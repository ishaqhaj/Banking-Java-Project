-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 11, 2024 at 03:51 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `banking-java-app-users-db`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `account_number` varchar(35) NOT NULL,
  `account_type` varchar(20) DEFAULT NULL,
  `bank_id` int(11) NOT NULL,
  `user_id` varchar(35) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`account_number`, `account_type`, `bank_id`, `user_id`) VALUES
('AL35202111090000000001234567', NULL, 1, '4GLWBO'),
('AT483200000012345864', NULL, 1, 'N3IUR6'),
('FR7630004000031234567890143', NULL, 1, 'VG4UWW'),
('MA64 2308 2544 5074 9211 0052 0004', NULL, 1, '0GMZQX'),
('MA64011519000001205000534921', NULL, 1, 'XL0BJN'),
('SA4420000001234567891234', NULL, 1, 'NL9WFP');

-- --------------------------------------------------------

--
-- Table structure for table `bank`
--

CREATE TABLE `bank` (
  `bank_id` int(11) NOT NULL,
  `name` varchar(140) NOT NULL,
  `bic` char(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bank`
--

INSERT INTO `bank` (`bank_id`, `name`, `bic`) VALUES
(1, 'CIH', 'CIHMMAMC');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` varchar(35) NOT NULL,
  `name` varchar(140) NOT NULL,
  `password` varchar(256) NOT NULL,
  `email` varchar(256) NOT NULL,
  `cin_or_passport` varchar(35) NOT NULL,
  `address` varchar(140) DEFAULT NULL,
  `city` varchar(35) DEFAULT NULL,
  `postal_code` varchar(16) DEFAULT NULL,
  `country` char(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `name`, `password`, `email`, `cin_or_passport`, `address`, `city`, `postal_code`, `country`) VALUES
('0GMZQX', 'Malak Youssofi', 'UUUU', 'ouidadmochariq1@gmail.com', 'fghhh', '', '', '', ''),
('4GLWBO', 'Mochariq Ouidad', 'TTTTT', 'ouidadmochariq1@gmail.com', 'qqqqq', '', '', '', ''),
('N3IUR6', 'Alice Albert', '1111', 'ouidadmochariq1@gmail.com', 'youp', '', '', '', ''),
('NL9WFP', 'Manal Said', '7878', 'ouidadmochariq1@gmail.com', 'raj590', '', '', '', ''),
('VG4UWW', 'Amal Nassiri', 'oooo', 'ouidadmochariq1@gmail.com', 'ymmm', '', '', '', ''),

-- --------------------------------------------------------



--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`account_number`),
  ADD KEY `bank_id` (`bank_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `bank`
--
ALTER TABLE `bank`
  ADD PRIMARY KEY (`bank_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);



--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bank`
--
ALTER TABLE `bank`
  MODIFY `bank_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accounts`
--
ALTER TABLE `accounts`
  ADD CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`bank_id`) REFERENCES `bank` (`bank_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `accounts_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;



/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
