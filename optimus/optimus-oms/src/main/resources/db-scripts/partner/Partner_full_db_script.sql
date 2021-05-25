use customer_uat;

create table IF NOT EXISTS callidus_data
(
    id                                   int auto_increment
        primary key,
    partner_name                         varchar(30) null,
    partner_ID                           varchar(20) null,
    partner_profile                      varchar(10) null,
    partner_region                       varchar(10) null,
    segment                              varchar(10) null,
    oppty_ID                             varchar(10) null,
    circuit_ID                           varchar(20) null,
    COPFID                               varchar(20) null,
    CRNID                                varchar(20) null,
    SECSID                               varchar(20) null,
    billing_start_date                   varchar(20) null,
    billing_end_date                     varchar(20) null,
    sub_agent                            varchar(20) null,
    invoice_number                       varchar(20) null,
    opportunity_term                     int         null,
    OB_date                              varchar(20) null,
    commissioned_date                    varchar(20) null,
    contract_expiry_date                 varchar(20) null,
    commission_type                      varchar(30) null,
    base_commission_level_percentage     varchar(10) null,
    discount_level_commission_percentage varchar(10) null,
    multi_year_commission_percentage     varchar(10) null,
    deal_reg_percentage                  varchar(10) null,
    comp_date                            varchar(20) null,
    customer_name                        varchar(20) null,
    service_name                         varchar(10) null,
    tran_currency                        varchar(10) null,
    ACV_in_tran_currency                 double      null,
    billed_value_in_tran_currency        double      null,
    exchange_rate                        double      null,
    partner_base_currency                varchar(10) null,
    billed_value_in_base_currency        double      null,
    incentive_value_in_base_currency     double      null,
    remarks                              varchar(40) null,
    commissioned_percentage              varchar(20) null
);

create table IF NOT EXISTS sap_data
(
    id               int auto_increment
        primary key,
    partner_Id       int         not null,
    partner_name     varchar(40) null,
    month            varchar(5)  null,
    year             varchar(4)  null,
    commissions_paid double      null,
    currency         varchar(10) null
);

create TABLE IF NOT EXISTS `partner`
(
  `id`                  int(11)   NOT NULL AUTO_INCREMENT,
  `name`                varchar(100)   DEFAULT NULL,
  `tps_sfdc_account_id` varchar(45)    DEFAULT NULL,
  `created_by`          varchar(100)   DEFAULT NULL,
  `created_time`        timestamp NULL DEFAULT NULL,
  `status`              tinyint(4)     DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


create TABLE IF NOT EXISTS `partner_legal_entity`
(
  `id`            int(11)   NOT NULL AUTO_INCREMENT,
  `entity_name`   varchar(100)   DEFAULT NULL,
  `partner_id`    int(11)        DEFAULT NULL,
  `tps_sfdc_cuid` varchar(45)    DEFAULT NULL,
  `agreement_id`  varchar(100)   DEFAULT NULL,
  `created_time`  timestamp NULL DEFAULT NULL,
  `status`        tinyint(4)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_partner_legal_entity_service_provider1_idx` (`partner_id`),
  CONSTRAINT `fk_partner_legal_entity_service_provider1` FOREIGN KEY (`partner_id`) REFERENCES `partner` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


create TABLE IF NOT EXISTS `partner_attribute_values`
(
  `id`                           int(11) NOT NULL AUTO_INCREMENT,
  `partner_id`                   int(11)      DEFAULT NULL,
  `mst_customer_sp_attribute_id` int(11)      DEFAULT NULL,
  `attribute_values`             varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_partner_attribute_values_partner_idx` (`partner_id`),
  KEY `fk_partner_attribute_values_mst_customer_sp_attributes1_idx` (`mst_customer_sp_attribute_id`),
  CONSTRAINT `fk_partner_attribute_values_mst_customer_sp_attributes1` FOREIGN KEY (`mst_customer_sp_attribute_id`) REFERENCES `mst_customer_sp_attributes` (`id`) ON delete NO ACTION ON update NO ACTION,
  CONSTRAINT `fk_partner_attribute_values_service_provider1` FOREIGN KEY (`partner_id`) REFERENCES `partner` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


create TABLE IF NOT EXISTS `partner_le_attribute_values`
(
  `id`                   int(11) NOT NULL AUTO_INCREMENT,
  `partner_le_id`        int(11)      DEFAULT NULL,
  `mst_le_attributes_id` int(11)      DEFAULT NULL,
  `attribute_values`     varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_partner_le_attribute_values_partner_legal_entity1_idx` (`partner_le_id`),
  KEY `fk_partner_le_attribute_values_mst_le_attributes1_idx` (`mst_le_attributes_id`),
  CONSTRAINT `fk_partner_le_attribute_values_mst_le_attributes1` FOREIGN KEY (`mst_le_attributes_id`) REFERENCES `mst_le_attributes` (`id`) ON delete NO ACTION ON update NO ACTION,
  CONSTRAINT `fk_partner_le_attribute_values_partner_legal_entity1` FOREIGN KEY (`partner_le_id`) REFERENCES `partner_legal_entity` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


create TABLE IF NOT EXISTS `partner_le_country`
(
  `id`            int(11) NOT NULL,
  `partner_le_id` int(11)    DEFAULT NULL,
  `country_id`    int(11)    DEFAULT NULL,
  `is_default`    tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_partner_le_country_partner_legal_entity1_idx` (`partner_le_id`),
  KEY `fk_partner_le_country_mst_countries1_idx` (`country_id`),
  CONSTRAINT `fk_partner_le_country_mst_countries1` FOREIGN KEY (`country_id`) REFERENCES `mst_countries` (`id`) ON delete NO ACTION ON update NO ACTION,
  CONSTRAINT `fk_partner_le_country_partner_legal_entity1` FOREIGN KEY (`partner_le_id`) REFERENCES `partner_legal_entity` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


create TABLE IF NOT EXISTS `partner_le_currency`
(
  `id`            int(11) NOT NULL AUTO_INCREMENT,
  `partner_le_id` int(11)    DEFAULT NULL,
  `currency_id`   int(11)    DEFAULT NULL,
  `is_default`    tinyint(4) DEFAULT NULL,
  `status`        tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_partner_le_currency_partner_legal_entity1_idx` (`partner_le_id`),
  KEY `fk_partner_le_currency_currency_master1_idx` (`currency_id`),
  CONSTRAINT `fk_partner_le_currency_currency_master1` FOREIGN KEY (`currency_id`) REFERENCES `currency_master` (`id`) ON delete NO ACTION ON update NO ACTION,
  CONSTRAINT `fk_partner_le_currency_partner_legal_entity1` FOREIGN KEY (`partner_le_id`) REFERENCES `partner_legal_entity` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


alter table `customer_legal_entity`
  ADD COLUMN `partner_le_id` INT(11) NULL DEFAULT NULL AFTER `customer_id`;


alter table `customer_legal_entity`
  ADD CONSTRAINT `FK2_customer_le_partner_le` FOREIGN KEY (`partner_le_id`)
    REFERENCES `partner_legal_entity` (`id`) ON
      update NO ACTION
    ON
      delete NO ACTION;

create TABLE IF NOT EXISTS `partner_engagements`
(
  `id`                  INT     NOT NULL AUTO_INCREMENT,
  `le_id`               INT     NOT NULL,
  `partner_template_id` INT     NOT NULL,
  `is_active`           CHAR(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`)
);


alter table `partner_engagements`
  ADD CONSTRAINT `FK1_partner_engagements_to_le` FOREIGN KEY (`le_id`) REFERENCES `customer_legal_entity` (`id`) ON
    update NO ACTION
    ON
      delete NO ACTION,
  ADD CONSTRAINT `FK2_partner_engagements_to_partner_template` FOREIGN KEY (`partner_template_id`) REFERENCES `partner_template` (`id`)
    ON
      update NO ACTION
    ON
      delete NO ACTION;


insert into mst_le_attributes(name,description,type,status) values('Sell With NDA','NDA or Engagement Agreement for Sell With order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell With CommonTC','Common T&C - Cover and Core for Sell With New order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell With selling mode','Documents Specific to selling mode for Sell With order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell With product','Documents Specific to product for Sell With order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell With due','Due Diligence for Sell With order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell With MSA','MSA for Sell With order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell With Addendum','Addendum for Sell With order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell With Compensation','Compensation documents for Sell With order','Partner',1);

insert into mst_le_attributes(name,description,type,status) values('Sell Through NDA','NDA or Engagement Agreement for Sell Through order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell Through CommonTC','Common T&C - Cover and Core for Sell Through New order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell Through selling mode','Documents Specific to selling mode for Sell Through order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell Through product','Documents Specific to product for Sell Through order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell Through due','Due Diligence for Sell Through order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell Through MSA','MSA for Sell Through order','Partner',1);
insert into mst_le_attributes(name,description,type,status) values('Sell Through Addendum','Addendum for Sell Through order','Partner',1);

insert into mst_le_attributes(name,description,type,status) values('Partner type','Type of partner either new or existing','Partner',1);

alter table partner_le_attribute_values add product_name VARCHAR(50);


alter table customer_legal_entity drop foreign key FK2_customer_le_partner_le;

drop index FK2_customer_le_partner_le on customer_legal_entity;

create index FK2_customer_le_partner_le
	on customer_legal_entity (partner_le_id);

alter table customer_legal_entity drop column partner_le_id;

alter table customer_legal_entity
	add constraint FK2_customer_le_partner_le
		foreign key (partner_le_id) references partner_legal_entity (id);



alter table `customer_uat`.`partner`
ADD COLUMN `account_id_18` VARCHAR(20) NOT NULL AFTER `status`;








create table IF NOT EXISTS `partner_le_billing_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `partner_id` int(11) DEFAULT NULL,
  `partner_le_id` int(11) DEFAULT NULL,
  `bill_acc_no` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `title` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `fname` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `lname` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `email_id` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `phone_number` varchar(25) CHARACTER SET utf8 DEFAULT NULL,
  `address_seq` int(11) DEFAULT NULL,
  `isactive` enum('Yes','No') CHARACTER SET utf8 DEFAULT 'Yes',
  `bill_addr` text CHARACTER SET utf8,
  `mobile_number` varchar(25) CHARACTER SET utf8 DEFAULT NULL,
  `country` varchar(25) CHARACTER SET utf8 DEFAULT NULL,
  `contact_type` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `bill_contact_seq` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `contact_id` varchar(50) DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  `erf_loc_location_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `partner_id_idx` (`partner_id`),
  KEY `partner_le_id_idx` (`partner_le_id`),
  KEY `contact_id_idx` (`contact_id`),
  KEY `bill_acc_no_idx` (`bill_acc_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

create table IF NOT EXISTS `partner_le_contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `partner_id` int(11) DEFAULT NULL,
  `partner_le_id` int(11) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `title` varchar(250) DEFAULT NULL,
  `address` text,
  `mobile_Phone` varchar(200) DEFAULT NULL,
  `Home_Phone` varchar(200) DEFAULT NULL,
  `Other_Phone` varchar(200) DEFAULT NULL,
  `Assistant_Phone` varchar(200) DEFAULT NULL,
  `fax_no` varchar(200) DEFAULT NULL,
  `email_id` varchar(200) DEFAULT NULL,
  `contact_id` varchar(50) DEFAULT NULL,
  `erf_loc_location_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_partner_idx` (`partner_id`),
  CONSTRAINT `fk_partner_idx` FOREIGN KEY (`partner_id`) REFERENCES `partner` (`id`) ON delete NO ACTION ON update NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

create table IF NOT EXISTS `partner_legal_entity_sap_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `partner_le_id` int(11) DEFAULT NULL,
  `code_value` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `code_type` enum('SECS Code','SAP Code','Not Available') CHARACTER SET utf8 DEFAULT 'Not Available',
  `is_active` enum('Yes','No') CHARACTER SET utf8 DEFAULT 'No',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



create table IF NOT EXISTS partner_profile
(
    id           int auto_increment
        primary key,
    name         varchar(100)                            not null,
    code         varchar(20)                             not null,
    is_active    char      default 'Y'                   not null,
    created_by   varchar(50)                             null,
    created_time timestamp default current_timestamp()   not null on update current_timestamp(),
    updated_by   varchar(50)                             null,
    updated_time timestamp default '0000-00-00 00:00:00' not null
);

alter table partner
add COLUMN partner_profile_id INT NULL DEFAULT NULL AFTER tps_sfdc_account_id;

alter table partner_legal_entity drop column partner_le_category_id;

drop table partner_le_category;


INSERT INTO partner_profile (name,code,is_active) VALUES ('Authorised Solution Partner','Profile 1','Y');
INSERT INTO partner_profile (name,code,is_active) VALUES ('Affiliate Solution Partner','Profile 2','Y');
INSERT INTO partner_profile (name,code,is_active) VALUES ('Master Agent','Profile 3','Y');
INSERT INTO partner_profile (name,code,is_active) VALUES ('Premier Solution Partner','Profile 4','Y');
INSERT INTO partner_profile (name,code,is_active) VALUES ('Focus Partner','Profile 5','Y');
INSERT INTO partner_profile (name,code,is_active) VALUES ('Service Provider Partner','Profile 6','Y');
INSERT INTO partner_profile (name,code,is_active) VALUES ('Alliance Partner','Profile 7','Y');
INSERT INTO partner_profile (name,code,is_active) VALUES ('Distributor','Profile 8','Y');
INSERT INTO partner_profile (name,code,is_active) VALUES ('Media Vertical Partner','Profile 9','Y');

create table IF NOT EXISTS partner_legal_entity_company_code
(
    id int auto_increment primary key,
    partner_le_sap_code_id int                                          null,
    code_value             varchar(50) charset utf8                     null,
    is_active              enum ('Yes', 'No') charset utf8 default 'No' null,
    constraint FK_partner_le_company_code_partner_le_sap_code
        foreign key (partner_le_sap_code_id) references partner_legal_entity_sap_code (id)
);

use oms_uat;

drop table callidus_data;
drop table sap_data;

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

alter table `quote_to_le`
	ADD COLUMN `classification` VARCHAR(20) NULL DEFAULT NULL AFTER `erf_service_inventory_tps_service_id`;

alter table `order_to_le`
	ADD COLUMN `classification` VARCHAR(20) NULL DEFAULT NULL AFTER `currency_code`;

alter table oms_attachments modify attachment_type enum('Tax', 'COF', 'Others', 'LOU', 'Solution', 'Partner') null;

create table IF NOT EXISTS general_terms_confirmation_audit
(
    id                int auto_increment
        primary key,
    username              varchar(255)                   null,
    public_ip         varchar(45)                        null,
    audit_type        enum ('PARTNER') default 'PARTNER' null,
    created_time      timestamp                          null,
    user_agent        varchar(255)                       null,
    created_time_unix timestamp                          null
);

INSERT INTO product_attribute_master (name, description, status, created_by, created_time)
SELECT *
FROM (SELECT 'Selected Termination Number (Working Outpulse)',
             'Selected Termination Number (Working Outpulse) ',
             1,
             'gsc',
             current_timestamp()) AS tmp
WHERE NOT EXISTS(
            SELECT name FROM product_attribute_master WHERE name = 'Selected Termination Number (Working Outpulse)'
    )
LIMIT 1;