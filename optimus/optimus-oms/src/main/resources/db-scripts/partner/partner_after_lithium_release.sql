use customer_uat;
create table sap_data
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


create TABLE `partner_le_billing_info` (
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

create TABLE `partner_le_contacts` (
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

create TABLE `partner_legal_entity_sap_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `partner_le_id` int(11) DEFAULT NULL,
  `code_value` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `code_type` enum('SECS Code','SAP Code','Not Available') CHARACTER SET utf8 DEFAULT 'Not Available',
  `is_active` enum('Yes','No') CHARACTER SET utf8 DEFAULT 'No',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



create table partner_profile
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


insert into partner_profile (name,code,is_active) values ('Authorised Solution Partner','Profile 1','Y');
insert into partner_profile (name,code,is_active) values ('Affiliate Solution Partner','Profile 2','Y');
insert into partner_profile (name,code,is_active) values ('Master Agent','Profile 3','Y');
insert into partner_profile (name,code,is_active) values ('Premier Solution Partner','Profile 4','Y');
insert into partner_profile (name,code,is_active) values ('Focus Partner','Profile 5','Y');
insert into partner_profile (name,code,is_active) values ('Service Provider Partner','Profile 6','Y');
insert into partner_profile (name,code,is_active) values ('Alliance Partner','Profile 7','Y');
insert into partner_profile (name,code,is_active) values ('Distributor','Profile 8','Y');
insert into partner_profile (name,code,is_active) values ('Media Vertical Partner','Profile 9','Y');


create table partner_legal_entity_company_code
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

alter table opportunity
    add column customer_le_name varchar(75) NULL after opty_classification;

alter table oms_attachments modify attachment_type enum('Tax', 'COF', 'Others', 'LOU', 'Solution', 'Partner') null;

create table general_terms_confirmation_audit
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
