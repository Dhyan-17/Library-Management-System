-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 24, 2025 at 06:42 PM
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
-- Database: `book_rental`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_admin` (IN `p_admin_id` INT, IN `p_name` VARCHAR(50), IN `p_password` VARCHAR(100), IN `p_email` VARCHAR(100), OUT `result` INT)   BEGIN
    INSERT INTO administrator(administrator_id, administrator_name, password, administrator_email)
    VALUES(p_admin_id, p_name, p_password, p_email);

    SET result = ROW_COUNT();
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `add_book` (IN `p_book_no` INT, IN `p_name` VARCHAR(100), IN `p_rent_price` DECIMAL(10,2), IN `p_rating` DOUBLE, IN `p_category` VARCHAR(50), OUT `result` INT)   BEGIN
    INSERT INTO book_list(book_isbn_no, book_name, rent_price, rating, category)
    VALUES(p_book_no, p_name, p_rent_price, p_rating, p_category);

    SET result = ROW_COUNT();
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `add_user` (IN `p_username` VARCHAR(50), IN `p_password` VARCHAR(100), IN `p_email` VARCHAR(100), IN `p_phone` VARCHAR(15), OUT `result` INT)   BEGIN
    INSERT INTO users(user_name,password,email,phone_no)
    VALUES(p_username, p_password, p_email, p_phone);

    SET result = ROW_COUNT();
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `login_admin` (IN `p_admin_id` INT, IN `p_password` VARCHAR(100), OUT `login_status` INT)   BEGIN
    DECLARE cnt INT;
    SELECT COUNT(*) INTO cnt 
    FROM administrator 
    WHERE administrator_id = p_admin_id AND password = p_password;

    IF cnt > 0 THEN
        SET login_status = 1;
    ELSE
        SET login_status = 0;
    END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `login_user` (IN `p_username` VARCHAR(50), IN `p_password` VARCHAR(100), OUT `login_status` INT)   BEGIN
    DECLARE cnt INT;
    SELECT COUNT(*) INTO cnt 
    FROM users 
    WHERE user_name = p_username AND password = p_password;

    IF cnt > 0 THEN
        SET login_status = 1;
    ELSE
        SET login_status = 0;
    END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `rent_book` (IN `p_username` VARCHAR(50), IN `p_book_no` INT, IN `p_rent_date` DATE, IN `p_return_date` DATE, OUT `result` INT)   BEGIN
    INSERT INTO book_rentals(user_name, book_isbn_no, rent_date, return_date)
    VALUES(p_username, p_book_no, p_rent_date, p_return_date);

    SET result = ROW_COUNT();
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `search_book_by_category` (IN `p_category` VARCHAR(50))   BEGIN
    SELECT * 
    FROM book_list 
    WHERE category LIKE CONCAT('%', p_category, '%');
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `search_book_by_isbn` (IN `isbn` INT)   BEGIN
    SELECT * FROM book_list
    WHERE book_isbn_no = isbn;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `search_book_by_name` (IN `p_book_name` VARCHAR(100))   BEGIN
    SELECT * 
    FROM book_list 
    WHERE book_name LIKE CONCAT('%', p_book_name, '%');
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `administrator`
--

CREATE TABLE `administrator` (
  `administrator_id` int(11) NOT NULL,
  `administrator_name` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `mobileNo` varchar(10) NOT NULL,
  `administrator_email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `administrator`
--

INSERT INTO `administrator` (`administrator_id`, `administrator_name`, `password`, `mobileNo`, `administrator_email`) VALUES
(1488, 'Dhyan', 'Dhyan148', '9106682710', 'daivik@gmail.com');

--
-- Triggers `administrator`
--
DELIMITER $$
CREATE TRIGGER `admin_delete` AFTER DELETE ON `administrator` FOR EACH ROW BEGIN
    INSERT INTO user_logs (user_id, user_name, action)
    VALUES (OLD.administrator_id, OLD.administrator_name, 'Admin Deleted');
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `book_list`
--

CREATE TABLE `book_list` (
  `book_isbn_no` int(11) NOT NULL,
  `book_name` varchar(100) NOT NULL,
  `rent_price` decimal(10,2) DEFAULT 100.00,
  `rating` double DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `author_name` varchar(100) DEFAULT NULL,
  `copies` int(11) DEFAULT 5
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `book_list`
--

INSERT INTO `book_list` (`book_isbn_no`, `book_name`, `rent_price`, `rating`, `category`, `author_name`, `copies`) VALUES
(1, 'Atomic Habits', 10.00, 10, NULL, 'james Clear', 5),
(2, 'Think And Grow Rich', 15.00, 8, NULL, 'Napoleon Hill', 5),
(3, 'The Power Of Now', 20.00, 9, NULL, 'Eckhart Tolle', 5),
(4, 'Rich Dad Poor Dad', 25.00, 7, NULL, 'Robert T.Kiyosaki', 5),
(5, 'Meditation', 30.00, 8, NULL, 'Marcus Aurelius', 5),
(6, 'Deep Work', 30.00, 9, NULL, 'Cal Hurt Me', 5),
(7, 'Grit', 35.00, 9, NULL, 'Angela Duckworth', 5),
(8, 'Flow', 40.00, 10, NULL, 'Mihaly', 5),
(9, 'Start With Why', 45.00, 7, NULL, 'Simon Sinek', 5),
(101010, 'Can\'t Hurt Me ', 50.00, 10, NULL, 'David Goggins', 2),
(231245, 'sw', 20.00, 7.8, NULL, 'de', 5);

-- --------------------------------------------------------

--
-- Table structure for table `book_rentals`
--

CREATE TABLE `book_rentals` (
  `rental_id` int(11) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `book_isbn_no` int(11) DEFAULT NULL,
  `rent_date` date DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `total_amount` varchar(50) NOT NULL,
  `return_status` enum('NOT_RETURNED','RETURNED') NOT NULL DEFAULT 'NOT_RETURNED',
  `actual_return_date` date DEFAULT NULL,
  `fine` int(11) NOT NULL DEFAULT 0,
  `copies` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `book_rentals`
--

INSERT INTO `book_rentals` (`rental_id`, `user_name`, `book_isbn_no`, `rent_date`, `return_date`, `total_amount`, `return_status`, `actual_return_date`, `fine`, `copies`) VALUES
(13, 'Daivik', 231245, '2025-08-24', '2025-08-27', '180.0', 'RETURNED', '2025-08-24', 0, 3),
(14, 'Daivik', 101010, '2025-08-24', '2025-09-05', '2400.0', 'RETURNED', '2025-08-24', 0, 4),
(15, 'Daivik', 231245, '2025-08-24', '2025-08-26', '200.0', 'RETURNED', '2025-08-24', 0, 5),
(16, 'Daivik', 101010, '2025-08-24', '2025-08-27', '300.0', 'RETURNED', '2025-08-24', 0, 2),
(17, 'Daivik', 231245, '2025-08-24', '2025-09-03', '1400.0', 'RETURNED', '2025-08-24', 0, 7),
(18, 'Daivik', 231245, '2025-08-24', '2025-09-05', '720.0', 'NOT_RETURNED', NULL, 0, 3);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(50) NOT NULL DEFAULT 'Not Null',
  `password` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `MobileNo` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `user_name`, `password`, `email`, `MobileNo`) VALUES
(1488, 'Daivik', 'Dhyan148', 'dhyan@gmail.com', '9106682710'),
(1490, 'Daivik', 'Daivik147', 'daivik123@gmail.com', '6767676767');

--
-- Triggers `users`
--
DELIMITER $$
CREATE TRIGGER `user_delete` AFTER DELETE ON `users` FOR EACH ROW BEGIN
    INSERT INTO user_logs (user_id, user_name, action)
    VALUES (OLD.user_id, OLD.user_name, 'User Deleted');
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `user_logs`
--

CREATE TABLE `user_logs` (
  `log_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  `action` varchar(255) DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `administrator`
--
ALTER TABLE `administrator`
  ADD PRIMARY KEY (`administrator_id`),
  ADD UNIQUE KEY `mobileNo` (`mobileNo`),
  ADD UNIQUE KEY `administrator_email` (`administrator_email`);

--
-- Indexes for table `book_list`
--
ALTER TABLE `book_list`
  ADD PRIMARY KEY (`book_isbn_no`);

--
-- Indexes for table `book_rentals`
--
ALTER TABLE `book_rentals`
  ADD PRIMARY KEY (`rental_id`),
  ADD KEY `user_name` (`user_name`),
  ADD KEY `book_isbn_no` (`book_isbn_no`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `user_name` (`user_name`);

--
-- Indexes for table `user_logs`
--
ALTER TABLE `user_logs`
  ADD PRIMARY KEY (`log_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `book_rentals`
--
ALTER TABLE `book_rentals`
  MODIFY `rental_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1491;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book_rentals`
--
ALTER TABLE `book_rentals`
  ADD CONSTRAINT `book_rentals_ibfk_1` FOREIGN KEY (`user_name`) REFERENCES `users` (`user_name`) ON DELETE CASCADE,
  ADD CONSTRAINT `book_rentals_ibfk_2` FOREIGN KEY (`book_isbn_no`) REFERENCES `book_list` (`book_isbn_no`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
