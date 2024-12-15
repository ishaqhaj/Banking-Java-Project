-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 15, 2024 at 12:47 PM
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
('AZ96AZEJ00000000001234567890', NULL, 1, 'HQVV2R'),
('BE71096123456769', NULL, 1, 'XJA1NC'),
('BH02CITI00001077181611', NULL, 1, '4JF8AL'),
('BR1500000000000010932840814P2', NULL, 1, '4JF8AL'),
('CR23015108410026012345', NULL, 1, '4JF8AL'),
('CY21002001950000357001234567', NULL, 1, 'EZIHTY'),
('FR7630004000031234567890143', NULL, 1, 'VG4UWW'),
('MA64 2308 2544 5074 9211 0052 0004', NULL, 1, '0GMZQX'),
('MA64011519000001205000534921', NULL, 1, 'XL0BJN'),
('SA4420000001234567891234', NULL, 1, 'NL9WFP');
-- --------------------------------------------------------

--
-- Table structure for table `account_beneficiaries`
--

CREATE TABLE `account_beneficiaries` (
  `user_account` varchar(34) NOT NULL,
  `beneficiary_account` varchar(34) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `account_beneficiaries`
--

INSERT INTO `account_beneficiaries` (`user_account`, `beneficiary_account`) VALUES
('AL35202111090000000001234567', 'AT483200000012345864'),
('AL35202111090000000001234567', 'BH02CITI00001077181611'),
('AL35202111090000000001234567', 'FR7630004000031234567890143'),
('AT483200000012345864', 'FR7630004000031234567890143'),
('BR1500000000000010932840814P2', 'CY21002001950000357001234567');

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
('4JF8AL', 'Fouad Samid', 'pppp', 'ouidadmochariq1@gmail.com', 'AS213456', '', '', '', ''),
('EZIHTY', 'Malak Mortaai', 'yyyy', 'ouidadmochariq1@gmail.com', 'rtyuui22', '', '', '', ''),
('HQVV2R', 'Nouha Adnani', 'ouioui', 'ouidadmochariq1@gmail.com', 'trh786', '', '', '', ''),
('N3IUR6', 'Alice Albert', '1111', 'ouidadmochariq1@gmail.com', 'youp', '', '', '', ''),
('NL9WFP', 'Manal Said', '7878', 'ouidadmochariq1@gmail.com', 'raj590', '', '', '', ''),
('VG4UWW', 'Amal Nassiri', 'oooo', 'ouidadmochariq1@gmail.com', 'ymmm', '', '', '', ''),
('XJA1NC', 'Malak Roudani', 'yyyy', 'ouidadmochariq1@gmail.com', 'trtr', '', '', '', ''),
('XL0BJN', 'Mohammed Ali', 'uuuu', 'ouidadmochaqri1@gmail.com', 'pop123', '', '', '', '');


-- --------------------------------------------------------

--
-- Table structure for table `virement`
--

CREATE TABLE `virement` (
  `end_to_end_id` varchar(35) NOT NULL,
  `debtor_account_number` varchar(34) NOT NULL,
  `creditor_account_number` varchar(34) NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `currency` char(3) NOT NULL,
  `timestamp` varchar(100) NOT NULL,
  `motif` text DEFAULT NULL,
  `type` varchar(50) NOT NULL,
  `methode_paiement` varchar(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`account_number`),
  ADD KEY `bank_id` (`bank_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `account_beneficiaries`
--
ALTER TABLE `account_beneficiaries`
  ADD PRIMARY KEY (`user_account`,`beneficiary_account`),
  ADD KEY `beneficiary_account` (`beneficiary_account`);

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
-- Indexes for table `virement`
--
ALTER TABLE `virement`
  ADD PRIMARY KEY (`end_to_end_id`),
  ADD KEY `fk_debtor_account` (`debtor_account_number`),
  ADD KEY `fk_creditor_account` (`creditor_account_number`);

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

--
-- Constraints for table `account_beneficiaries`
--
ALTER TABLE `account_beneficiaries`
  ADD CONSTRAINT `account_beneficiaries_ibfk_1` FOREIGN KEY (`user_account`) REFERENCES `accounts` (`account_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `account_beneficiaries_ibfk_2` FOREIGN KEY (`beneficiary_account`) REFERENCES `accounts` (`account_number`) ON DELETE CASCADE ON UPDATE CASCADE;



--
-- Constraints for table `virement`
--
ALTER TABLE `virement`
  ADD CONSTRAINT `fk_creditor_account` FOREIGN KEY (`creditor_account_number`) REFERENCES `accounts` (`account_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_debtor_account` FOREIGN KEY (`debtor_account_number`) REFERENCES `accounts` (`account_number`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
