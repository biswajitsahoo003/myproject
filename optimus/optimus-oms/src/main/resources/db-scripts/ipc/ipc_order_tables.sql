use `oms_uat`;

CREATE TABLE `order_cloud` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(100) DEFAULT NULL,
  `cloud_code` varchar(100) DEFAULT NULL,
  `parent_cloud_code` varchar(100) DEFAULT NULL,
  `order_to_le_id` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `order_product_solutions_id` int(11) DEFAULT NULL,
  `resource_display_name` varchar(50) DEFAULT NULL,
  `dc_location_id` varchar(50) DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `effective_date` timestamp NULL DEFAULT NULL,
  `fp_status` enum('P','MP') DEFAULT NULL,
  `stage` varchar(50) DEFAULT NULL,
  `is_active` char(1) DEFAULT 'Y',
  `status` varchar(45) DEFAULT NULL,
  `is_task_triggered` tinyint(4) NOT NULL DEFAULT 0,
  `created_time` timestamp NULL DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `updated_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_order_cloud_orders` (`order_to_le_id`),
  KEY `FK_order_cloud_orders_2` (`order_id`),
  KEY `FK_order_cloud_product_solutions` (`order_product_solutions_id`),
  CONSTRAINT `FK_order_cloud_orders` FOREIGN KEY (`order_to_le_id`) REFERENCES `order_to_le` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_order_cloud_orders_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_order_cloud_product_solutions` FOREIGN KEY (`order_product_solutions_id`) REFERENCES `order_product_solutions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `odr_product_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `odr_service_detail_id` int(11) NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `solution_name` varchar(50) DEFAULT NULL,
  `mrc` double DEFAULT 0,
  `nrc` double DEFAULT 0,
  `arc` double DEFAULT 0,
  `is_active` char(1) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT current_timestamp(),
  `updated_by` varchar(45) DEFAULT NULL,
  `updated_date` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_odr_product_detail_odr_service_detail` (`odr_service_detail_id`),
  CONSTRAINT `FK_odr_product_detail_odr_service_detail` FOREIGN KEY (`odr_service_detail_id`) REFERENCES `odr_service_detail` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `odr_product_detail_attributes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `odr_product_detail_id` int(11) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `attribute_name` varchar(50) DEFAULT NULL,
  `attribute_value` varchar(255) DEFAULT NULL,
  `is_active` char(1) DEFAULT NULL,
  `created_by` varchar(50) DEFAULT '',
  `created_date` timestamp NULL DEFAULT current_timestamp(),
  `updated_by` varchar(50) DEFAULT '',
  `updated_date` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_odr_product_detail_attributes_odr_product_detail` (`odr_product_detail_id`),
  CONSTRAINT `FK_odr_product_detail_attributes_odr_product_detail` FOREIGN KEY (`odr_product_detail_id`) REFERENCES `odr_product_detail` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `odr_service_commercial_component` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_item` int(11) DEFAULT NULL,
  `odr_product_detail_id` int(11) NOT NULL,
  `item_type` varchar(50) NOT NULL,
  `item` varchar(250) NOT NULL,
  `nrc` double NOT NULL,
  `mrc` double NOT NULL,
  `arc` double NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_odr_service_commercial_component_odr_product_detail` (`odr_product_detail_id`),
  CONSTRAINT `FK_odr_service_commercial_component_odr_product_detail` FOREIGN KEY (`odr_product_detail_id`) REFERENCES `odr_product_detail` (`id`)
) ENGINE=InnoDB;
