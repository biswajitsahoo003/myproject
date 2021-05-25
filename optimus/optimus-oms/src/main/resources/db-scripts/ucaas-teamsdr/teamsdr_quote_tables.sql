use oms;

--for storing teams dr quote data
create TABLE `quote_teamsdr` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_to_le_id` int(11) DEFAULT NULL,
  `product_solution_id` int(11) DEFAULT NULL,
  `profile_name` varchar(45) DEFAULT NULL,
  `no_of_users` int(8) DEFAULT NULL,
  `service_name` varchar(45) DEFAULT NULL,
  `is_config` tinyint(1) DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `quote_version` int(3) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_QUOTE_LE_KEY_DR_UCAAS_idx` (`quote_to_le_id`),
  KEY `FK_PROD_SLN_KEY_DR_UCAAS_idx` (`product_solution_id`),
  CONSTRAINT `FK_PROD_SLN_KEY_DR_UCAAS` FOREIGN KEY (`product_solution_id`) REFERENCES `product_solutions` (`id`) ON delete NO ACTION ON update NO ACTION,
  CONSTRAINT `FK_QUOTE_LE_KEY_DR_UCAAS` FOREIGN KEY (`quote_to_le_id`) REFERENCES `quote_to_le` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=21634 DEFAULT CHARSET=latin1;

--for storing teams dr quote details
create TABLE `quote_teamsdr_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_teamsdr_id` int(11) DEFAULT NULL,
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
  KEY `FK_TEAMSDR_KEY_idx` (`quote_teamsdr_id`),
  CONSTRAINT `FK_TEAMSDR_KEY` FOREIGN KEY (`quote_teamsdr_id`) REFERENCES `quote_teamsdr` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2660 DEFAULT CHARSET=latin1;

--for storing teams dr license data
create TABLE `quote_teams_license` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_teamsdr_id` int(11) DEFAULT NULL,
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
  `quote_version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_QUOTETEAMSDR_KEY1` (`quote_teamsdr_id`),
  CONSTRAINT `FK_QUOTETEAMSDR_KEY1` FOREIGN KEY (`quote_teamsdr_id`) REFERENCES `quote_teamsdr` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3683 DEFAULT CHARSET=latin1;

--for storing teams dr config-country data
create TABLE `quote_dr` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_teamsdr_id` int(11) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  `quote_version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_QUOTETEAMSDR_KEY` (`quote_teamsdr_id`),
  CONSTRAINT `FK_QUOTETEAMSDR_KEY` FOREIGN KEY (`quote_teamsdr_id`) REFERENCES `quote_teamsdr` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2043 DEFAULT CHARSET=latin1;


--for storing teams dr config-city data
create TABLE `quote_dr_city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_dr_id` int(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `media_gateway_type` varchar(45) DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  `quote_version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_quote_teamsdr_details_idx1` (`quote_dr_id`),
  CONSTRAINT `fk_quote_teamsdr_details2` FOREIGN KEY (`quote_dr_id`) REFERENCES `quote_dr` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8718 DEFAULT CHARSET=utf8;

--for storing teams dr media gateway data
create TABLE `quote_dr_mediagateways` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_dr_cityid` int(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `mrc` double DEFAULT NULL,
  `nrc` double DEFAULT NULL,
  `arc` double DEFAULT NULL,
  `tcv` double DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  `quote_version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_teamsdr_city_details` (`quote_dr_cityid`)
) ENGINE=InnoDB AUTO_INCREMENT=47441 DEFAULT CHARSET=utf8;

--adding sub_stage column for saving sub_stage information for multi le scenario
alter table `quote_to_le`
ADD COLUMN `sub_stage` VARCHAR(45) NULL DEFAULT NULL AFTER `stage`;

ALTER TABLE `oms_uat`.`quote_to_le`
ADD COLUMN `quote_le_code` VARCHAR(10) NULL DEFAULT NULL AFTER `id`;

-- Adding reference type for teamsdr
ALTER TABLE `oms_uat`.`additional_service_params`
CHANGE COLUMN `reference_type` `reference_type` ENUM('Site', 'Link', 'GVPN', 'GSC-ITFS', 'TEAMSDR') NULL DEFAULT NULL ;

ALTER TABLE `oms_uat`.`cof_details`
ADD COLUMN `reference_id` VARCHAR(45) NULL DEFAULT NULL AFTER `order_uuid`,
ADD COLUMN `reference_type` VARCHAR(45) NULL DEFAULT NULL AFTER `reference_id`;

ALTER TABLE `quote_teams_license`
ADD COLUMN `sfdc_product_name` VARCHAR(100) NULL DEFAULT NULL AFTER `no_of_licenses`;

ALTER TABLE `oms_uat`.`mst_product_component`
CHANGE COLUMN `name` `name` VARCHAR(75) NULL DEFAULT NULL ;

ALTER TABLE `oms_uat`.`quote_price_audit`
ADD COLUMN `currency_code` VARCHAR(45) NULL DEFAULT NULL AFTER `to_effective_usage_price`;

-----------------

CREATE TABLE `teamsdr_pricing_engine` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_code` varchar(100) NOT NULL,
  `price_mode` enum('manual','system') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `pricing_type` varchar(100) DEFAULT NULL,
  `request_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`)
) ;

