use `oms_uat`;

DROP TABLE IF EXISTS `quote_cloud`;

CREATE TABLE `quote_cloud` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(50) DEFAULT NULL,
  `cloud_code` varchar(100) DEFAULT NULL,
  `parent_cloud_code` varchar(100) DEFAULT NULL,
  `quote_to_le_id` int(11) DEFAULT NULL,
  `quote_id` int(11) DEFAULT NULL,
  `product_solutions_id` int(11) DEFAULT NULL,
  `dc_location_id` varchar(50) DEFAULT NULL,
  `resource_display_name` varchar(50) DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `effective_date` timestamp NULL DEFAULT NULL,
  `fp_status` enum('P','MP') DEFAULT 'P',
  `is_active` char(1) DEFAULT 'Y',
  `status` varchar(45) DEFAULT NULL,
  `is_task_triggered` tinyint(4) NOT NULL DEFAULT 0,
  `created_time` timestamp NULL DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `updated_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_quote_cloud_quote_to_le` (`quote_to_le_id`),
  KEY `FK_quote_cloud_quote` (`quote_id`),
  KEY `FK_quote_cloud_product_solutions` (`product_solutions_id`),
  CONSTRAINT `FK_quote_cloud_product_solutions` FOREIGN KEY (`product_solutions_id`) REFERENCES `product_solutions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_quote_cloud_quote` FOREIGN KEY (`quote_id`) REFERENCES `quote` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_quote_cloud_quote_to_le` FOREIGN KEY (`quote_to_le_id`) REFERENCES `quote_to_le` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;


