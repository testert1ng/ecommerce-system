--
-- Create schema ecommerce
--

CREATE DATABASE IF NOT EXISTS ecommerce;
USE ecommerce;

--
-- Definition of table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(15) NOT NULL primary key,
  `password` varchar(15) NOT NULL,
  `role` varchar(15) NOT NULL
);

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `status` varchar(15) NOT NULL,
  `customer` varchar(20) NOT NULL,
  `address` varchar(15) NOT NULL,
  `totalprice` decimal(10,2) NOT NULL,
  `shippingcost` decimal(10,2) NOT NULL,
  `finalcost` decimal(10,2) NOT NULL,
  `cart` varchar(50000) NOT NULL,
  PRIMARY KEY (`id`)
);

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`,`password`,`role`) VALUES 
 ('admin','admin','admin'),
 ('user0','user0','regular'),
 ('user1','user1','regular');
