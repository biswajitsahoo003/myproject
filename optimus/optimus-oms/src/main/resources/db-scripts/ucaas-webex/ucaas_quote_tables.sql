use oms;

create TABLE IF NOT EXISTS `quote_ucaas`
(
    `id`                  int(11)   NOT NULL AUTO_INCREMENT,
    `quote_to_le_id`      int(11)            DEFAULT NULL,
    `product_solution_id` int(11)            DEFAULT NULL,
    `deal_id`             varchar(50)        DEFAULT NULL,
    `is_config`           tinyint(1)         DEFAULT NULL,
    `response`            blob               DEFAULT NULL,
    `name`                varchar(100)       DEFAULT NULL,
    `description`         varchar(100)       DEFAULT NULL,
    `quantity`            int(11)            DEFAULT NULL,
    `mrc`                 double             DEFAULT NULL,
    `nrc`                 double             DEFAULT NULL,
    `arc`                 double             DEFAULT NULL,
    `tcv`                 double             DEFAULT NULL,
    `deal_status`         varchar(20)        DEFAULT NULL,
    `deal_message`        varchar(200)       DEFAULT NULL,
    `license_provider`    varchar(50)        DEFAULT NULL,
    `primary_region`      varchar(20)        DEFAULT NULL,
    `cug_required`        tinyint(1)         DEFAULT NULL,
    `audio_model`         varchar(50)        DEFAULT NULL,
    `payment_model`       varchar(50)        DEFAULT NULL,
    `audio_type`          varchar(100)       DEFAULT NULL,
    `gvpn_mode`           varchar(20)        DEFAULT NULL,
    `dial_in`             tinyint(1)         DEFAULT NULL,
    `dial_out`            tinyint(1)         DEFAULT NULL,
    `dial_back`           tinyint(1)         DEFAULT NULL,
    `is_lns`              tinyint(1)         DEFAULT NULL,
    `is_itfs`             tinyint(1)         DEFAULT NULL,
    `is_outbound`         tinyint(1)         DEFAULT NULL,
    `status`              tinyint(1)         DEFAULT NULL,
    `created_by`          varchar(100)       DEFAULT NULL,
    `created_time`        timestamp NOT NULL DEFAULT current_timestamp,
    `quote_version`       int(11)            DEFAULT NULL,
    PRIMARY KEY (id),
    KEY `FK_PROD_SLN_KEY_idx` (`product_solution_id`),
    KEY `FK_QUOTE_LE_KEY_idx` (`quote_to_le_id`),
    CONSTRAINT `FK_PROD_SLN_KEY_UCAAS` FOREIGN KEY (`product_solution_id`) REFERENCES `product_solutions` (`id`) ON delete NO ACTION ON update NO ACTION,
    CONSTRAINT `FK_QUOTE_LE_KEY_UCAAS` FOREIGN KEY (`quote_to_le_id`) REFERENCES `quote_to_le` (`id`) ON delete NO ACTION ON update NO ACTION
);

ALTER TABLE `quote_ucaas`
ADD COLUMN `deal_attributes` BLOB NULL DEFAULT NULL AFTER `deal_message`;

ALTER TABLE `quote_ucaas`
ADD COLUMN `cisco_unit_list_price` DOUBLE NULL DEFAULT NULL AFTER `tcv`,
ADD COLUMN `cisco_discnt_prct` DOUBLE NULL DEFAULT NULL AFTER `cisco_unit_list_price`,
ADD COLUMN `cisco_unit_net_price` DOUBLE NULL DEFAULT NULL AFTER `cisco_discnt_prct`;

ALTER TABLE `quote_ucaas`
DROP COLUMN `response`;

CREATE TABLE `quote_ucaas_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quote_ucaas_id` int(11) DEFAULT NULL,
  `response` mediumtext DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_UCAAS_KEY_idx` (`quote_ucaas_id`),
  CONSTRAINT `FK_UCAAS_KEY` FOREIGN KEY (`quote_ucaas_id`) REFERENCES `quote_ucaas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

ALTER TABLE `quote_to_le`
ADD COLUMN `erf_service_inventory_tps_service_id` VARCHAR(45) NULL DEFAULT NULL;

ALTER TABLE `quote_ucaas`
ADD COLUMN `hsn_code` VARCHAR(45) NULL DEFAULT NULL AFTER `deal_attributes`;

ALTER TABLE `quote_ucaas`
ADD COLUMN `endpoint_location_id` INT(11) NULL DEFAULT NULL ;

CREATE TABLE `quote_ucaas_site_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_code` varchar(45) DEFAULT NULL,
  `product_solution_id` int(11) DEFAULT NULL,
  `endpoint_location_id` int(11) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_PS_KEY_idx` (`product_solution_id`),
  CONSTRAINT `FK_PS_KEY` FOREIGN KEY (`product_solution_id`) REFERENCES `product_solutions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

ALTER TABLE `quote_ucaas_site_details`
ADD COLUMN `gst_number` varchar(45) NULL DEFAULT NULL AFTER `endpoint_location_id`;

ALTER TABLE `quote_ucaas`
ADD COLUMN `contract_type` VARCHAR(45) NULL DEFAULT NULL AFTER `endpoint_location_id`,
ADD COLUMN `endpoint_management_type` VARCHAR(45) NULL AFTER `contract_type`;

ALTER TABLE `oms_uat`.`quote_ucaas`
ADD COLUMN `is_updated` TINYINT(1) NULL DEFAULT NULL AFTER `endpoint_management_type`;

ALTER TABLE `additional_service_params`
CHANGE COLUMN `reference_type` `reference_type` ENUM('Site', 'Link', 'GVPN', 'GSC-ITFS', 'WEBEX') NULL DEFAULT NULL ;

ALTER TABLE `quote_ucaas`
ADD COLUMN `unit_mrc` double NULL DEFAULT NULL AFTER `quantity`,
ADD COLUMN `unit_nrc` double NULL DEFAULT NULL AFTER `mrc`,
ADD COLUMN `uom` varchar(25) NULL DEFAULT NULL AFTER `quote_version`;

use location;

ALTER TABLE `mst_address`
CHANGE COLUMN `source` `source` ENUM('manual', 'system', 'api', 'cisco_webex') CHARACTER SET 'utf8' NULL DEFAULT NULL ;

ALTER TABLE `mst_city`
CHANGE COLUMN `source` `source` ENUM('manual', 'system', 'api', 'cisco_webex') NULL DEFAULT NULL ;

ALTER TABLE `mst_state`
CHANGE COLUMN `source` `source` ENUM('manual', 'system', 'api', 'cisco_webex') NULL DEFAULT NULL ;

ALTER TABLE `mst_country`
CHANGE COLUMN `source` `source` ENUM('manual', 'system', 'api', 'cisco_webex') NULL DEFAULT NULL ;

ALTER TABLE `mst_pincode`
CHANGE COLUMN `source` `source` ENUM('manual', 'system', 'api', 'cisco_webex') NULL DEFAULT NULL ;

ALTER TABLE `mst_locality`
CHANGE COLUMN `source` `source` ENUM('manual', 'system', 'api', 'cisco_webex') NULL DEFAULT NULL ;


