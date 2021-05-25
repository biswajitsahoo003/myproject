use oms_uat;

-- auto-generated definition
create TABLE IF NOT EXISTS opportunity
(
  id                  int auto_increment
    primary key,
  uuid                varchar(45) null,
  tps_opty_id         varchar(45) null,
  tps_opty_system     varchar(45) null,
  opty_classification varchar(45) null,
  expected_arc        double      null,
  expected_nrc        double      null,
  expected_currency   varchar(3)  null,
  currency_iso_code   varchar(5)  null,
  campaign_name       varchar(75) null,
  opty_summary        varchar(75) null,
  created_by          int         null,
  created_date        timestamp   null,
  updated_by          int         null,
  updated_date        timestamp   null,
  status              varchar(45) null,
  is_active           char default 'Y' null
);


create TABLE IF NOT EXISTS `oms_uat`.`opportunity_to_attachment`
(
  `id`              INT         NOT NULL AUTO_INCREMENT,
  `opty_id`         INT         NULL,
  `attachment_id`   INT         NULL,
  `attachment_type` VARCHAR(45) NULL,
  `is_active`       CHAR(1)     NULL DEFAULT 'Y',
  `created_by`      INT         NULL,
  `created_date`    TIMESTAMP   NULL,
  PRIMARY KEY (`id`)
);


create TABLE IF NOT EXISTS `oms_uat`.`engagement_to_opportunity`
(
  `id`            INT       NOT NULL AUTO_INCREMENT,
  `engagement_id` INT       NULL,
  `opty_id`       INT       NULL,
  `is_active`     CHAR(1)   NULL DEFAULT 'Y',
  `created_by`    INT       NULL,
  `created_date`  TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_engagement_to_opportunity_opportunity`
    FOREIGN KEY (`opty_id`)
      REFERENCES `oms_uat`.`opportunity` (`id`),
  CONSTRAINT `fk_engagement_to_opportunity_engagement1`
    FOREIGN KEY (`engagement_id`)
      REFERENCES `oms_uat`.`engagement` (`id`)
);


create TABLE IF NOT EXISTS `oms_uat`.`engagement_opportunity_to_product`
(
  `id`                    INT       NOT NULL AUTO_INCREMENT,
  `engagement_to_opty_id` INT       NULL,
  `mst_product_id`        INT       NULL,
  `is_active`             CHAR(1)   NULL DEFAULT 'Y',
  `created_by`            INT       NULL,
  `created_date`          TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_engagement_opportunity_to_product_engagement_to_opportunity1`
    FOREIGN KEY (`engagement_to_opty_id`)
      REFERENCES `oms_uat`.`engagement_to_opportunity` (`id`)
);


alter table  engagement
  add erf_cus_partner_le_id int(11);


alter table partner
  add erf_cus_partner_id int(11);

alter table orders
  add column engagement_to_opportunity_id int(11) after order_code;

alter table quote
  add column engagement_to_opportunity_id int(11) after quote_code;

alter table quote
  add FOREIGN KEY (engagement_to_opportunity_id) REFERENCES engagement_to_opportunity (id);

alter table orders
  add FOREIGN KEY (engagement_to_opportunity_id) REFERENCES engagement_to_opportunity (id);

alter table opportunity
    add column customer_le_name varchar(75) NULL after opty_classification;

ALTER TABLE `opportunity`
    ADD COLUMN `campaign_id` VARCHAR(75) NULL AFTER `campaign_name`;
