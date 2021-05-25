use oms_uat;

CREATE TABLE `order_gsc` (
  `id`                        int(11)   NOT NULL AUTO_INCREMENT,
  `name`                      varchar(150)       DEFAULT NULL,
  `product_name`              varchar(150)       DEFAULT NULL,
  `access_type`               varchar(100)       DEFAULT NULL,
  `image_url`                 varchar(100)       DEFAULT NULL,
  `order_product_solution_id` int(11)            DEFAULT NULL,
  `order_to_le_id`            int(11)            DEFAULT NULL,
  `mrc`                       double             DEFAULT NULL,
  `nrc`                       double             DEFAULT NULL,
  `arc`                       double             DEFAULT NULL,
  `tcv`                       double             DEFAULT NULL,
  `order_version`             int(11)            DEFAULT NULL,
  `status`                    tinyint(1)         DEFAULT NULL,
  `created_by`                varchar(100)       DEFAULT NULL,
  `created_time`              timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_ORD_PROD_SLN_KEY_idx` (`order_product_solution_id`),
  KEY `FK_ORDER_LE_KEY_idx` (`order_to_le_id`),
  CONSTRAINT `FK_ORD_PROD_SLN_KEY` FOREIGN KEY (`order_product_solution_id`) REFERENCES `order_product_solutions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ORDER_LE_KEY` FOREIGN KEY (`order_to_le_id`) REFERENCES `order_to_le` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


CREATE TABLE `order_gsc_sla` (
  `id`              int(11) NOT NULL AUTO_INCREMENT,
  `order_gsc_id`    int(11)          DEFAULT NULL,
  `sla_master_id`   int(11)          DEFAULT NULL,
  `attribute_name`  varchar(200)     DEFAULT NULL,
  `attribute_value` varchar(200)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ORD_GSC_ID_KEY_idx` (`order_gsc_id`),
  KEY `FK_SLA1_KEY_idx` (`sla_master_id`),
  CONSTRAINT `FK_ORD_GSC_ID_KEY` FOREIGN KEY (`order_gsc_id`) REFERENCES `order_gsc` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SLA1_KEY` FOREIGN KEY (`sla_master_id`) REFERENCES `sla_master` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


CREATE TABLE `order_gsc_details` (
  `id`           int(11)   NOT NULL AUTO_INCREMENT,
  `order_gsc_id` int(11)            DEFAULT NULL,
  `src`          varchar(100)       DEFAULT NULL,
  `src_type`     varchar(45)        DEFAULT NULL,
  `dest`         varchar(100)       DEFAULT NULL,
  `dest_type`    varchar(45)        DEFAULT NULL,
  `mrc`          double             DEFAULT NULL,
  `nrc`          double             DEFAULT NULL,
  `arc`          double             DEFAULT NULL,
  `created_by`   varchar(45)        DEFAULT NULL,
  `created_time` timestamp NULL     DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_ORD_GSC_KEY_idx` (`order_gsc_id`),
  CONSTRAINT `FK_ORD_GSC_KEY` FOREIGN KEY (`order_gsc_id`) REFERENCES `order_gsc` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE `order_gsc_tfn` (
  `id`           int(11)   NOT NULL AUTO_INCREMENT,
  `order_gsc_id` int(11)            DEFAULT NULL,
  `tfn_number`   varchar(200)       DEFAULT NULL,
  `is_ported`    tinyint(1)         DEFAULT NULL,
  `status`       tinyint(1)         DEFAULT 1,
  `ported_from`  varchar(150)       DEFAULT NULL,
  `created_by`   varchar(45)        DEFAULT NULL,
  `created_time` timestamp NULL     DEFAULT current_timestamp(),
  `updated_by`   varchar(45)        DEFAULT NULL,
  `updated_time` timestamp NULL     DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ORD_GSC_ID1_KEY_idx` (`order_gsc_id`),
  CONSTRAINT `FK_ORD_GSC_ID1_KEY` FOREIGN KEY (`order_gsc_id`) REFERENCES `order_gsc` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE `gsc_attachments` (
  `id`                int(11)   NOT NULL AUTO_INCREMENT,
  `oms_attachment_id` int(11)   DEFAULT NULL,
  `document_name`      varchar(100)        DEFAULT NULL,
  `document_uid`      varchar(50)        DEFAULT NULL,
  `document_category` varchar(100)       DEFAULT NULL,
  `template_name`     varchar(100)       DEFAULT NULL,
  `document_type`     varchar(10)        DEFAULT NULL,
  `status`            varchar(10)        DEFAULT NULL,
  `uploaded_time`     timestamp NOT NULL DEFAULT current_timestamp(),
  `uploaded_by`       varchar(100),
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_OMS_ATTACHMENT_KEY` FOREIGN KEY (`oms_attachment_id`) REFERENCES `oms_attachments` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


CREATE TABLE `global_outbound_pricing_engine_response` (
  `id`                        int(11)   NOT NULL AUTO_INCREMENT,
  `quote_code`                varchar(100) DEFAULT NULL,
  `is_negotiated_prices`      tinyint(1)   DEFAULT NULL,
  `price_mode`                varchar(30)  DEFAULT NULL,
  `pricing_type`              varchar(30)  DEFAULT NULL,
  `request_data`              text         DEFAULT NULL,
  `response_data`             longtext     DEFAULT NULL,
  `date_time`                 timestamp    DEFAULT current_timestamp()
  PRIMARY KEY (`id`)
);