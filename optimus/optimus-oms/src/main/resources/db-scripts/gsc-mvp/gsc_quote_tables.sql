use oms_uat;
CREATE TABLE `quote_gsc` (
  `id`                  int(11)   NOT NULL AUTO_INCREMENT,
  `name`                varchar(150)       DEFAULT NULL,
  `product_name`        varchar(150)       DEFAULT NULL,
  `access_type`         varchar(100)       DEFAULT NULL,
  `image_url`           varchar(100)       DEFAULT NULL,
  `product_solution_id` int(11)            DEFAULT NULL,
  `quote_to_le_id`      int(11)            DEFAULT NULL,
  `mrc`                 double             DEFAULT NULL,
  `nrc`                 double             DEFAULT NULL,
  `arc`                 double             DEFAULT NULL,
  `tcv`                 double             DEFAULT NULL,
  `quote_version`       int(11)            DEFAULT NULL,
  `status`              tinyint(1)         DEFAULT NULL,
  `created_by`          varchar(100)       DEFAULT NULL,
  `created_time`        timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FK_PROD_SLN_KEY_idx` (`product_solution_id`),
  KEY `FK_QUOTE_LE_KEY_idx` (`quote_to_le_id`),
  CONSTRAINT `FK_PROD_SLN_KEY` FOREIGN KEY (`product_solution_id`) REFERENCES `product_solutions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_QUOTE_LE_KEY` FOREIGN KEY (`quote_to_le_id`) REFERENCES `quote_to_le` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


CREATE TABLE `quote_gsc_sla` (
  `id`              int(11) NOT NULL AUTO_INCREMENT,
  `quote_gsc_id`    int(11)          DEFAULT NULL,
  `sla_master_id`   int(11)          DEFAULT NULL,
  `attribute_name`  varchar(200)     DEFAULT NULL,
  `attribute_value` varchar(200)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_GSC_ID_KEY_idx` (`quote_gsc_id`),
  KEY `FK_SLA_KEY_idx` (`sla_master_id`),
  CONSTRAINT `FK_GSC_ID_KEY` FOREIGN KEY (`quote_gsc_id`) REFERENCES `quote_gsc` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_SLA_KEY` FOREIGN KEY (`sla_master_id`) REFERENCES `sla_master` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


CREATE TABLE `quote_gsc_details` (
  `id`           int(11)   NOT NULL AUTO_INCREMENT,
  `quote_gsc_id` int(11)            DEFAULT NULL,
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
  KEY `FK_GSC_KEY_idx` (`quote_gsc_id`),
  CONSTRAINT `FK_GSC_KEY` FOREIGN KEY (`quote_gsc_id`) REFERENCES `quote_gsc` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);