-- use product_catalog_ipc_dev;
use product_catalog_uat;

CREATE TABLE IF NOT EXISTS `pricing_ipc_crossborder_wh_tax` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `customer_le_country` varchar(50) NOT NULL,
  `dc_location_country` varchar(50) NOT NULL,
  `tax_percentage` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pricing_ipc_crossborder_wh_tax_unique_constraint` (`customer_le_country`,`dc_location_country`)
) ENGINE=InnoDB;

INSERT INTO `pricing_ipc_crossborder_wh_tax` (`id`, `customer_le_country`, `dc_location_country`, `tax_percentage`) VALUES
	(1, 'INDIA', 'UK', 0),
	(2, 'INDIA', 'HONGKONG', 10),
	(3, 'INDIA', 'SINGAPORE', 0),
	(4, 'INDIA', 'DUBAI', 0),
	(5, 'INDIA', 'MALAYSIA', 10),
	(6, 'INDIA', 'GERMANY', 10),
	(7, 'UK', 'INDIA', 0),
	(8, 'HONGKONG', 'INDIA', 0),
	(9, 'SINGAPORE', 'INDIA', 0),
	(10, 'DUBAI', 'INDIA', 0),
	(11, 'MALAYSIA', 'INDIA', 10),
	(12, 'GERMANY', 'INDIA', 0),
	(13, 'UK', 'HONGKONG', 0),
	(14, 'UK', 'SINGAPORE', 0),
	(15, 'UK', 'DUBAI', 0),
	(16, 'UK', 'MALAYSIA', 0),
	(17, 'UK', 'GERMANY', 0),
	(18, 'HONGKONG', 'UK', 0),
	(19, 'HONGKONG', 'SINGAPORE', 0),
	(20, 'HONGKONG', 'DUBAI', 0),
	(21, 'HONGKONG', 'MALAYSIA', 0),
	(22, 'HONGKONG', 'GERMANY', 0),
	(23, 'SINGAPORE', 'UK', 0),
	(24, 'SINGAPORE', 'HONGKONG', 17),
	(25, 'SINGAPORE', 'DUBAI', 0),
	(26, 'SINGAPORE', 'MALAYSIA', 5),
	(27, 'SINGAPORE', 'GERMANY', 0),
	(28, 'DUBAI', 'UK', 0),
	(29, 'DUBAI', 'HONGKONG', 0),
	(30, 'DUBAI', 'SINGAPORE', 0),
	(31, 'DUBAI', 'MALAYSIA', 0),
	(32, 'DUBAI', 'GERMANY', 0),
	(33, 'MALAYSIA', 'UK', 8),
	(34, 'MALAYSIA', 'HONGKONG', 5),
	(35, 'MALAYSIA', 'SINGAPORE', 5),
	(36, 'MALAYSIA', 'DUBAI', 0),
	(37, 'MALAYSIA', 'GERMANY', 7),
	(38, 'GERMANY', 'UK', 0),
	(39, 'GERMANY', 'HONGKONG', 0),
	(40, 'GERMANY', 'SINGAPORE', 0),
	(41, 'GERMANY', 'DUBAI', 0),
	(42, 'GERMANY', 'MALAYSIA', 0);

CREATE TABLE IF NOT EXISTS `pricing_ipc_customer_net_margin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_code` varchar(50) NOT NULL,
  `country_code` varchar(50) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `final_discount_percentage` double NOT NULL DEFAULT 0,
  `net_margin_percentage` double NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_catalog_uat_pricing_ipc_customer_net_margin_unique` (`city_code`,`country_code`,`customer_id`)
) ENGINE=InnoDB;

INSERT INTO `pricing_ipc_customer_net_margin` (`id`, `city_code`, `country_code`, `customer_id`, `final_discount_percentage`, `net_margin_percentage`) VALUES
	(1, 'EP_V2_BL', 'INDIA', NULL, 10, 10),
	(2, 'EP_V2_DEL', 'INDIA', NULL, 10, 10),
	(3, 'EP_V2_MUM', 'INDIA', NULL, 10, 10),
	(4, 'EP_DUBAI', 'DUBAI', NULL, 10, 10),
	(5, 'EP_V2_GER', 'GERMANY', NULL, 10, 10),
	(6, 'EP_V2_HK', 'HONGKONG', NULL, 10, 10),
	(7, 'EP_V2_MAL', 'MALAYSIA', NULL, 10, 10),
	(8, 'EP_V2_SG03', 'SINGAPORE', NULL, 10, 10),
	(9, 'EP_V2_SG_TCX', 'SINGAPORE', NULL, 10, 10),
	(10, 'EP_V2_UKCX', 'UK', NULL, 10, 10),
	(11, 'EP_V2_UKHB', 'UK', NULL, 10, 10),
	(12, 'EP_V2_BL', 'INDIA', 904, 60, 10.94),
	(13, 'EP_V2_DEL', 'INDIA', 904, 60, 10.94),
	(14, 'EP_V2_MUM', 'INDIA', 904, 60, 10.94),
	(15, 'EP_DUBAI', 'DUBAI', 904, 20, 30),
	(16, 'EP_V2_GER', 'GERMANY', 904, 20, 30),
	(17, 'EP_V2_HK', 'HONGKONG', 904, 20, 30),
	(18, 'EP_V2_MAL', 'MALAYSIA', 904, 20, 30),
	(19, 'EP_V2_SG03', 'SINGAPORE', 904, 20, 30),
	(20, 'EP_V2_SG_TCX', 'SINGAPORE', 904, 20, 30),
	(21, 'EP_V2_UKCX', 'UK', 904, 20, 30),
	(22, 'EP_V2_UKHB', 'UK', 904, 20, 30),
	(23, 'EP_V2_UKCX', 'UK', 721, 10, 10),
	(24, 'EP_V2_UKHB', 'UK', 721, 10, 10);

CREATE TABLE IF NOT EXISTS `pricing_ipc_datatransfer` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `location_code` varchar(250) NOT NULL,
  `start_limit` int(10) NOT NULL,
  `end_limit` int(10) NOT NULL,
  `unit` varchar(25) NOT NULL,
  `price` double NOT NULL,
  `bandwidth` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `pricing_ipc_datatransfer` (`id`, `location_code`, `start_limit`, `end_limit`, `unit`, `price`, `bandwidth`) VALUES
	(1, 'INDIA', 0, 500, 'GB', 0.18, 15),
	(2, 'INDIA', 501, 2000, 'GB', 0.19, 15),
	(3, 'INDIA', 2001, 5000, 'GB', 0.19, 30),
	(4, 'INDIA', 5001, 10000, 'GB', 0.16, 50),
	(5, 'INDIA', 10001, 20000, 'GB', 0.16, 75),
	(6, 'INDIA', 20001, 50000, 'GB', 0.14, 125),
	(7, 'INDIA', 50001, 150000, 'GB', 0.12, 300),
	(8, 'INDIA', 150001, 500000, 'GB', 0.1, 300),
	(81, 'UK', 0, 500, 'GB', 0.12, 15),
	(82, 'UK', 501, 2000, 'GB', 0.13, 15),
	(83, 'UK', 2001, 5000, 'GB', 0.13, 30),
	(84, 'UK', 5001, 10000, 'GB', 0.11, 50),
	(85, 'UK', 10001, 20000, 'GB', 0.11, 75),
	(86, 'UK', 20001, 50000, 'GB', 0.09, 125),
	(87, 'UK', 50001, 150000, 'GB', 0.08, 300),
	(88, 'UK', 150001, 500000, 'GB', 0.07, 300),
	(89, 'HONGKONG', 0, 500, 'GB', 0.24, 15),
	(90, 'HONGKONG', 501, 2000, 'GB', 0.26, 15),
	(91, 'HONGKONG', 2001, 5000, 'GB', 0.26, 30),
	(92, 'HONGKONG', 5001, 10000, 'GB', 0.22, 50),
	(93, 'HONGKONG', 10001, 20000, 'GB', 0.22, 75),
	(94, 'HONGKONG', 20001, 50000, 'GB', 0.18, 125),
	(95, 'HONGKONG', 50001, 150000, 'GB', 0.16, 300),
	(96, 'HONGKONG', 150001, 500000, 'GB', 0.13, 300),
	(97, 'SINGAPORE', 0, 500, 'GB', 0.18, 15),
	(98, 'SINGAPORE', 501, 2000, 'GB', 0.19, 15),
	(99, 'SINGAPORE', 2001, 5000, 'GB', 0.19, 30),
	(100, 'SINGAPORE', 5001, 10000, 'GB', 0.16, 50),
	(101, 'SINGAPORE', 10001, 20000, 'GB', 0.16, 75),
	(102, 'SINGAPORE', 20001, 50000, 'GB', 0.14, 125),
	(103, 'SINGAPORE', 50001, 150000, 'GB', 0.12, 300),
	(104, 'SINGAPORE', 150001, 500000, 'GB', 0.1, 300),
	(105, 'GERMANY', 0, 500, 'GB', 0.14, 15),
	(106, 'GERMANY', 501, 2000, 'GB', 0.15, 15),
	(107, 'GERMANY', 2001, 5000, 'GB', 0.15, 30),
	(108, 'GERMANY', 5001, 10000, 'GB', 0.13, 50),
	(109, 'GERMANY', 10001, 20000, 'GB', 0.13, 75),
	(110, 'GERMANY', 20001, 50000, 'GB', 0.11, 125),
	(111, 'GERMANY', 50001, 150000, 'GB', 0.09, 300),
	(112, 'GERMANY', 150001, 500000, 'GB', 0.08, 300),
	(113, 'DUBAI', 0, 10240, 'GB', 1.4, 10),
	(114, 'DUBAI', 10241, 12000, 'GB', 1.4, 10);
/*!40000 ALTER TABLE `pricing_ipc_datatransfer` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `pricing_ipc_location` (
  `id` int(5) NOT NULL,
  `city_code` varchar(50) NOT NULL,
  `city` varchar(50) NOT NULL,
  `country_code` varchar(50) NOT NULL,
  `location_id` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `pricing_ipc_location` (`id`, `city_code`, `city`, `country_code`, `location_id`) VALUES
	(1, 'EP_V2_BL', 'Bangalore', 'INDIA', 153),
	(2, 'EP_V2_DEL', 'Delhi', 'INDIA', 1225),
	(3, 'EP_V2_MUM', 'Mumbai', 'INDIA', 632),
	(4, 'EP_DUBAI', 'Dubai', 'DUBAI', 7),
	(5, 'EP_V2_GER', 'Germany', 'GERMANY', 6771),
	(6, 'EP_V2_HK', 'Hongkong', 'HONGKONG', 2388),
	(7, 'EP_V2_MAL', 'Malaysia', 'MALAYSIA', 11974),
	(8, 'EP_V2_SG03', 'Singapore ', 'SINGAPORE', 2382),
	(9, 'EP_V2_SG_TCX', 'TCX', 'SINGAPORE', 2382),
	(10, 'EP_V2_UKCX', 'Cressex ', 'UK', 188759),
	(11, 'EP_V2_UKHB', 'Highbridge ', 'UK', 188759);
/*!40000 ALTER TABLE `pricing_ipc_location` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `pricing_ipc_publicip` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `ip_count` int(3) NOT NULL,
  `location_code` varchar(75) NOT NULL,
  `pricing` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `pricing_ipc_publicip` (`id`, `ip_count`, `location_code`, `pricing`) VALUES
	(1, 1, 'INDIA', 22),
	(2, 1, 'SINGAPORE', 20),
	(3, 1, 'DUBAI', 20),
	(4, 1, 'GERMANY', 20),
	(5, 1, 'HONGKONG', 20),
	(6, 1, 'MALAYSIA', 20),
	(7, 1, 'UK', 20);

CREATE TABLE IF NOT EXISTS `pricing_ipc_storage` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `country_code` varchar(75) NOT NULL,
  `start_limit` int(10) NOT NULL,
  `end_limit` int(10) NOT NULL,
  `unit` varchar(25) NOT NULL,
  `price_6` double NOT NULL,
  `price_12` double NOT NULL,
  `price_18` double NOT NULL,
  `price_24` double NOT NULL,
  `price_36` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `pricing_ipc_storage` (`id`, `country_code`, `start_limit`, `end_limit`, `unit`, `price_6`, `price_12`, `price_18`, `price_24`, `price_36`) VALUES
	(1, 'INDIA', 0, 250, 'TB', 200.44, 113.16, 84.5, 70.52, 57.24),
	(2, 'INDIA', 251, 500, 'TB', 172.8, 97.55, 72.85, 60.79, 49.35),
	(3, 'INDIA', 501, 2000, 'TB', 155.52, 87.79, 65.56, 54.71, 44.41),
	(4, 'UK', 1, 250, 'TB', 200.44, 113.16, 84.5, 70.52, 57.24),
	(5, 'UK', 251, 500, 'TB', 172.8, 97.55, 72.85, 60.79, 49.35),
	(6, 'UK', 501, 2000, 'TB', 155.52, 87.79, 65.56, 54.71, 44.41),
	(7, 'HONGKONG', 1, 250, 'TB', 200.44, 113.16, 84.5, 70.52, 57.24),
	(8, 'HONGKONG', 251, 500, 'TB', 172.8, 97.55, 72.85, 60.79, 49.35),
	(9, 'HONGKONG', 501, 2000, 'TB', 155.52, 87.79, 65.56, 54.71, 44.41),
	(10, 'SINGAPORE', 1, 250, 'TB', 200.44, 113.16, 84.5, 70.52, 57.24),
	(11, 'SINGAPORE', 251, 500, 'TB', 172.8, 97.55, 72.85, 60.79, 49.35),
	(12, 'SINGAPORE', 501, 2000, 'TB', 155.52, 87.79, 65.56, 54.71, 44.41),
	(13, 'MALAYSIA', 1, 250, 'TB', 200.44, 113.16, 84.5, 70.52, 57.24),
	(14, 'MALAYSIA', 251, 500, 'TB', 172.8, 97.55, 72.85, 60.79, 49.35),
	(15, 'MALAYSIA', 501, 2000, 'TB', 155.52, 87.79, 65.56, 54.71, 44.41),
	(16, 'GERMANY', 1, 250, 'TB', 200.44, 113.16, 84.5, 70.52, 57.24),
	(17, 'GERMANY', 251, 500, 'TB', 172.8, 97.55, 72.85, 60.79, 49.35),
	(18, 'GERMANY', 501, 2000, 'TB', 155.52, 87.79, 65.56, 54.71, 44.41),
	(19, 'DUBAI', 1, 250, 'TB', 200.44, 113.16, 84.5, 70.52, 57.24),
	(20, 'DUBAI', 251, 500, 'TB', 172.8, 97.55, 72.85, 60.79, 49.35),
	(21, 'DUBAI', 501, 2000, 'TB', 155.52, 87.79, 65.56, 54.71, 44.41);

CREATE TABLE IF NOT EXISTS `pricing_ipc_storage_policy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `storage_policy` varchar(45) NOT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `pricing_ipc_storage_policy` (`id`, `storage_policy`, `price`) VALUES
	(1, 'value_based', 0.016),
	(2, 'geo_resilient', 0.032);
