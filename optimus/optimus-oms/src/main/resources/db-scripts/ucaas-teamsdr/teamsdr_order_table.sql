use oms;

--for storing teams dr order data
create TABLE `order_teamsdr` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_to_le_id` int(11) DEFAULT NULL,
  `order_product_solution_id` int(11) DEFAULT NULL,
  `profile_name` varchar(45) DEFAULT NULL,
  `no_of_users` int(8) DEFAULT NULL,
  `service_name` varchar(45) DEFAULT NULL,
  `is_config` tinyint(1) DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `order_version` int(3) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ORDER_LE_KEY_DR_UCAAS_idx` (`order_to_le_id`),
  KEY `FK_ODR_PROD_SLN_KEY_DR_UCAAS_idx` (`order_product_solution_id`),
  CONSTRAINT `FK_ODR_PROD_SLN_KEY_DR_UCAAS` FOREIGN KEY (`order_product_solution_id`) REFERENCES `order_product_solutions` (`id`) ON delete NO ACTION ON update NO ACTION,
  CONSTRAINT `FK_ORDER_LE_KEY_DR_UCAAS` FOREIGN KEY (`order_to_le_id`) REFERENCES `order_to_le` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21634 DEFAULT CHARSET=latin1;

create TABLE `order_teamsdr_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_teamsdr_id` int(11) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `no_of_named_users` int(11) DEFAULT 0,
  `no_of_common_area_devices` int(11) DEFAULT 0,
  `total_users` int(11) DEFAULT 0,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_ORDER_TEAMSDR_KEY_idx` (`order_teamsdr_id`),
  CONSTRAINT `FK_ORDER_TEAMSDR_KEY_idx` FOREIGN KEY (`order_teamsdr_id`) REFERENCES `order_teamsdr` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2660 DEFAULT CHARSET=latin1;


create TABLE `order_teams_license` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_teamsdr_id` int(11) DEFAULT NULL,
  `license_name` varchar(100) DEFAULT NULL,
  `agreement_type` varchar(45) DEFAULT NULL,
  `provider` varchar(45) DEFAULT NULL,
  `contract_period` varchar(45) DEFAULT NULL,
  `no_of_licenses` int(11) DEFAULT 0,
  `mrc` double DEFAULT 0,
  `nrc` double DEFAULT 0,
  `arc` double DEFAULT 0,
  `tcv` double DEFAULT 0,
  `created_time` timestamp NULL DEFAULT NULL,
  `order_version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ORDER_TEAMSDR_LICENSE_KEY` (`order_teamsdr_id`),
  CONSTRAINT `FK_ORDER_TEAMSDR_LICENSE_KEY` FOREIGN KEY (`order_teamsdr_id`) REFERENCES `order_teamsdr` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3683 DEFAULT CHARSET=latin1;

create TABLE `order_dr` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_teamsdr_id` int(11) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  `order_version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ORDER_TEAMSDR_DR_KEY` (`order_teamsdr_id`),
  CONSTRAINT `FK_ORDER_TEAMSDR_DR_KEY` FOREIGN KEY (`order_teamsdr_id`) REFERENCES `order_teamsdr` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2043 DEFAULT CHARSET=latin1;


create TABLE `order_dr_city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_dr_id` int(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `media_gateway_type` varchar(45) DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  `order_version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_teamsdr_details_idx1` (`order_dr_id`),
  CONSTRAINT `fk_order_teamsdr_details` FOREIGN KEY (`order_dr_id`) REFERENCES `order_dr` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8718 DEFAULT CHARSET=utf8;

create TABLE `order_dr_mediagateways` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_dr_city_id` int(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  `order_version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_teamsdr_city_details` (`order_dr_city_id`),
  CONSTRAINT `fk_teamsdr_city_details` FOREIGN KEY (`order_dr_city_id`) REFERENCES `order_dr_city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47441 DEFAULT CHARSET=utf8;

ALTER TABLE `oms_uat`.`order_to_le`
ADD COLUMN `order_le_code` VARCHAR(10) NULL DEFAULT NULL AFTER `id`;

ALTER TABLE `oms_uat`.`order_to_le`
ADD COLUMN `sub_stage` VARCHAR(45) NULL DEFAULT NULL AFTER `stage`;

ALTER TABLE `order_teams_license`
ADD COLUMN `sfdc_product_name` VARCHAR(100) NULL DEFAULT NULL AFTER `no_of_licenses`;