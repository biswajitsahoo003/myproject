use `service_fulfillment_uat`;

CREATE TABLE `sc_product_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sc_service_detail_id` int(11) NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `solution_name` varchar(50) DEFAULT NULL,
  `mrc` double DEFAULT 0,
  `nrc` double DEFAULT 0,
  `arc` double DEFAULT 0,
  `is_active` char(1) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT '',
  `created_date` timestamp NULL DEFAULT current_timestamp(),
  `updated_by` varchar(45) DEFAULT '',
  `updated_date` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_sc_product_detail_sc_service_detail` (`sc_service_detail_id`),
  CONSTRAINT `FK_sc_product_detail_sc_service_detail` FOREIGN KEY (`sc_service_detail_id`) REFERENCES `sc_service_detail` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `sc_product_detail_attributes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sc_product_detail_id` int(11) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  `attribute_name` varchar(50) DEFAULT NULL,
  `attribute_value` varchar(255) DEFAULT NULL,
  `is_active` char(1) DEFAULT NULL,
  `created_by` varchar(50) DEFAULT '',
  `created_date` timestamp NULL DEFAULT current_timestamp(),
  `updated_by` varchar(50) DEFAULT '',
  `updated_date` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_sc_product_detail_attributes_sc_product_detail` (`sc_product_detail_id`),
  CONSTRAINT `FK_sc_product_detail_attributes_sc_product_detail` FOREIGN KEY (`sc_product_detail_id`) REFERENCES `sc_product_detail` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `service_handover_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `opportunity_id` varchar(100) DEFAULT NULL,
  `crn_id` varchar(45) DEFAULT NULL,
  `legal_entity_name` varchar(100) DEFAULT NULL,
  `order_id` varchar(100) DEFAULT NULL,
  `geneva_group_id` varchar(45) DEFAULT NULL,
  `service_id` varchar(45) DEFAULT NULL,
  `request_type` varchar(45) DEFAULT NULL,
  `customer_type` varchar(45) DEFAULT NULL,
  `provider_segment` varchar(45) DEFAULT NULL,
  `account_number` varchar(45) DEFAULT NULL,
  `invoice_no` varchar(100) DEFAULT NULL,
  `request` blob DEFAULT NULL,
  `response` blob DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `error_msg` varchar(45) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  `process_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `sc_service_commerical_component` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`sc_product_detail_id` INT(11) NOT NULL,
	`item_type` VARCHAR(50) NOT NULL,
	`parent_item` INT(11) NULL DEFAULT NULL,
	`item` VARCHAR(250) NOT NULL,
	`nrc` DOUBLE NOT NULL,
	`mrc` DOUBLE NOT NULL,
	`arc` DOUBLE NOT NULL,
	`created_by` VARCHAR(45) NULL DEFAULT NULL,
	`created_date` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `FK_sc_service_commerical_component_sc_service_detail` (`sc_product_detail_id`),
	CONSTRAINT `FK_sc_service_commerical_component_sc_service_detail` FOREIGN KEY (`sc_product_detail_id`) REFERENCES `sc_product_detail` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
) ENGINE=InnoDB;
