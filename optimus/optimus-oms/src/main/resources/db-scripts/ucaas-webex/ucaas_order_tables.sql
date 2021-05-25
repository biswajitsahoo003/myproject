use oms;

CREATE TABLE IF NOT EXISTS `order_ucaas`
(
    `id`                  int(11)   NOT NULL AUTO_INCREMENT,
    `order_to_le_id`      int(11)            DEFAULT NULL,
    `order_product_solution_id` int(11)      DEFAULT NULL,
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
    KEY `FK_PROD_SLN_KEY_idx` (`order_product_solution_id`),
    KEY `FK_ORDER_LE_KEY_idx` (`order_to_le_id`),
    CONSTRAINT `FK_ORDER_PROD_SLN_KEY_UCAAS` FOREIGN KEY (`order_product_solution_id`) REFERENCES `order_product_solutions` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT `FK_ORDER_LE_KEY_UCAAS` FOREIGN KEY (`order_to_le_id`) REFERENCES `order_to_le` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

ALTER TABLE `order_to_le`
ADD COLUMN `erf_service_inventory_tps_service_id` VARCHAR(45) NULL DEFAULT NULL AFTER `erf_service_inventory_parent_order_id`;

ALTER TABLE `order_ucaas`
ADD COLUMN `deal_attributes` BLOB NULL DEFAULT NULL AFTER `deal_message`;


ALTER TABLE `order_ucaas`
ADD COLUMN `cisco_unit_list_price` DOUBLE NULL DEFAULT NULL AFTER `tcv`,
ADD COLUMN `cisco_discnt_prct` DOUBLE NULL DEFAULT NULL AFTER `cisco_unit_list_price`,
ADD COLUMN `cisco_unit_net_price` DOUBLE NULL DEFAULT NULL AFTER `cisco_discnt_prct`;

ALTER TABLE `order_ucaas`
DROP COLUMN `response`;

CREATE TABLE `order_ucaas_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_ucaas_id` int(11) DEFAULT NULL,
  `response` mediumtext DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_ORDUCAAS_KEY_idx` (`order_ucaas_id`),
  CONSTRAINT `FK_ORDUCAAS_KEY` FOREIGN KEY (`order_ucaas_id`) REFERENCES `order_ucaas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

ALTER TABLE `order_ucaas`
ADD COLUMN `hsn_code` VARCHAR(45) NULL DEFAULT NULL AFTER `deal_attributes`;

ALTER TABLE `order_ucaas`
ADD COLUMN `endpoint_location_id` INT(11) NULL DEFAULT NULL;

CREATE TABLE `order_ucaas_site_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_code` varchar(45) DEFAULT NULL,
  `order_product_solution_id` int(11) DEFAULT NULL,
  `endpoint_location_id` int(11) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_OPS_KEY_idx` (`order_product_solution_id`),
  CONSTRAINT `FK_OPS_KEY` FOREIGN KEY (`order_product_solution_id`) REFERENCES `order_product_solutions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

ALTER TABLE `order_ucaas`
ADD COLUMN `contract_type` VARCHAR(45) NULL DEFAULT NULL AFTER `endpoint_location_id`,
ADD COLUMN `endpoint_management_type` VARCHAR(45) NULL DEFAULT NULL AFTER `contract_type`;

ALTER TABLE `order_ucaas`
ADD COLUMN `is_updated` TINYINT(1) NULL DEFAULT NULL AFTER `endpoint_management_type`;

ALTER TABLE `order_ucaas`
ADD COLUMN `unit_mrc` double NULL DEFAULT NULL AFTER `quantity`,
ADD COLUMN `unit_nrc` double NULL DEFAULT NULL AFTER `mrc`,
ADD COLUMN `uom` varchar(25) NULL DEFAULT NULL AFTER `quote_version`;

ALTER TABLE `order_ucaas_site_details`
ADD COLUMN `gst_number` varchar(45) NULL DEFAULT NULL AFTER `endpoint_location_id`;

