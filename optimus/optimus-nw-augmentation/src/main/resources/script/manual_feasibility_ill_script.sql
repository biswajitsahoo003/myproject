/*Script forr manual feasibility workbench start*/

use service_fulfillment_uat;
-- mf_detail
CREATE TABLE `mf_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` int(11) DEFAULT NULL,
  `site_code` varchar(45) DEFAULT NULL,
  `quote_id` int(11) DEFAULT NULL,
  `quote_code` varchar(45) DEFAULT NULL,
  `quote_le_id` int(11) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `is_active` int(11) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `updated_by` varchar(45) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `mf_details` text DEFAULT NULL,
  `assigned_to` varchar(45) DEFAULT NULL,
  `region` varchar(45) DEFAULT NULL,
  `site_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=825 DEFAULT CHARSET=utf8;

/*Task*/
ALTER TABLE  task
  ADD COLUMN  `mf_detail_id` int(11) DEFAULT NULL,
  ADD FOREIGN KEY  `fk_mf_detail_id_idx_idx`(`mf_detail_id`) REFERENCES  `mf_detail` (`id`)  ON DELETE NO ACTION ON UPDATE NO ACTION;
--
alter table task add column feasibility_id varchar(20) DEFAULT NULL after mf_detail_id;

-- Stage
ALTER TABLE  stage
ADD COLUMN  `mf_detail_id` int(11) DEFAULT NULL,
ADD FOREIGN KEY  `fk_mf_detail_id_idx_idx`(`mf_detail_id`) REFERENCES  `mf_detail` (`id`)  ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Lcon-Remarks
insert into oms_uat.product_attribute_master (name,description,status,category)
values
(
'LCON_REMARKS',
'LCON Remarks',
1,
'NEW'
);             

-- Site_feasibility
alter table oms_uat.site_feasibility add column site_document blob default null AFTER created_time;

alter table oms_uat.site_feasibility add column site_document_name varchar(100) default null after site_document;

-- mf_status
alter table quote_ill_sites add mf_status varchar(45) null;

-- mf_task_detail
CREATE TABLE `mf_task_detail` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `subject` varchar(100) DEFAULT NULL,
  `requestor_comments` text DEFAULT NULL,
  `assigned_to` varchar(100) DEFAULT NULL,
  `task_id` int(11) unsigned DEFAULT NULL,
  `assigned_from` varchar(100) DEFAULT NULL,
  `responder_comments` text DEFAULT NULL,
  `site_id` int(11) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `reason` text DEFAULT NULL,
  `quote_id` int(11) DEFAULT NULL,
  `requestor_task_id` int(11) DEFAULT NULL,
  `prv_comments` text DEFAULT NULL,
  `prv_status` varchar(45) DEFAULT NULL,
  `assigned_group` varchar(45) DEFAULT NULL,
  `task_data` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_task_id_idx_idx` (`task_id`),
  CONSTRAINT `fk_task_id_idx_idx` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1113 DEFAULT CHARSET=utf8;

-- mf_task_detail_audit
CREATE TABLE `mf_task_detail_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL,
  `feasibility_id` varchar(45) NOT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `requestor_comments` text DEFAULT NULL,
  `responder_comments` text DEFAULT NULL,
  `reason` text DEFAULT NULL,
  `requestor_task_id` int(11) DEFAULT NULL,
  `prv_comments` text DEFAULT NULL,
  `prv_status` varchar(45) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `created_by` varchar(100) DEFAULT NULL,
  `mf_task_detail_id` int(11) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `assigned_from` varchar(100) DEFAULT NULL,
  `assigned_to` varchar(100) DEFAULT NULL,
  `task_data` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=781 DEFAULT CHARSET=utf8;

-- mf_support_group
CREATE TABLE `mf_support_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(45) DEFAULT NULL,
  `dependant_team` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- mf_response_detail
CREATE TABLE `mf_response_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL,
  `site_id` int(11) DEFAULT NULL,
  `provider` varchar(100) DEFAULT NULL,
  `create_response_json` mediumtext DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `created_time` datetime DEFAULT current_timestamp(),
  `updated_by` varchar(100) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `feasibility_mode` varchar(255) DEFAULT NULL,
  `mf_rank` int(11) DEFAULT 0,
  `is_selected` int(11) NOT NULL DEFAULT 0,
  `feasibility_status` varchar(50) DEFAULT NULL,
  `feasibility_check` varchar(50) DEFAULT NULL,
  `feasibility_type` varchar(50) DEFAULT NULL,
  `quote_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=609 DEFAULT CHARSET=utf8;

CREATE TABLE `mf_response_detail_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mf_response_detail_id` int(11) NOT NULL,
  `mf_response_json` mediumtext DEFAULT NULL,
  `is_updated` tinyint(1) DEFAULT NULL,
  `created_by` varchar(200) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `FX_MF_RESP_ID` (`mf_response_detail_id`),
  CONSTRAINT `FX_MF_RESP_ID` FOREIGN KEY (`mf_response_detail_id`) REFERENCES `mf_response_detail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=461 DEFAULT CHARSET=utf8;

-- mf_provider_data
CREATE TABLE `mf_provider_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provider_name` varchar(500) DEFAULT NULL,
  `created_time` datetime DEFAULT current_timestamp(),
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=400 DEFAULT CHARSET=utf8;

-- mf_pop_data

CREATE TABLE `mf_pop_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `network_location_id` varchar(255) DEFAULT NULL,
  `site_address` text DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `updated_date` datetime DEFAULT current_timestamp(),
  `country` varchar(255) DEFAULT NULL,
  `network_location_type` varchar(100) DEFAULT NULL,
  `pop_service_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3194 DEFAULT CHARSET=utf8;
-- mf_hh_data
CREATE TABLE `mf_hh_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num_hh` varchar(500) DEFAULT NULL,
  `hh_state` varchar(100) DEFAULT NULL,
  `hh_lat` varchar(100) DEFAULT NULL,
  `hh_long` varchar(100) DEFAULT NULL,
  `created_time` datetime DEFAULT current_timestamp(),
  `updated_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- mf_dependant_team
CREATE TABLE `mf_dependant_team` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_name` varchar(50) DEFAULT NULL,
  `team_region` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;


CREATE TABLE `mf_bts_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` varchar(500) DEFAULT NULL,
  `site_name` varchar(200) DEFAULT NULL,
  `longitude` varchar(100) DEFAULT NULL,
  `latitude` varchar(100) DEFAULT NULL,
  `site_address` varchar(300) DEFAULT NULL,
  `created_time` datetime DEFAULT current_timestamp(),
  `updated_time` datetime DEFAULT current_timestamp(),
  `ipaddress` varchar(255) DEFAULT NULL,
  `sector_id` text DEFAULT NULL,
  `sector_name` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2323 DEFAULT CHARSET=utf8;

CREATE TABLE `mf_provider_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provider_name` varchar(500) DEFAULT NULL,
  `created_time` datetime DEFAULT current_timestamp(),
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=400 DEFAULT CHARSET=utf8;

CREATE TABLE `mf_vendor_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vendor_name` varchar(500) DEFAULT NULL,
  `created_time` datetime DEFAULT current_timestamp(),
  `updated_time` datetime DEFAULT current_timestamp(),
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `provider_name` varchar(255) DEFAULT NULL,
  `vendor_id` varchar(255) DEFAULT NULL,
  `tcl_entity_name` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `sfdc_provider_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38364 DEFAULT CHARSET=utf8;


CREATE TABLE `feasibility_category_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `f_mode` varchar(50) NOT NULL,
  `f_status` varchar(50) NOT NULL,
  `f_category` varchar(50) NOT NULL,
  `is_active` char(1) DEFAULT 'Y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8;

CREATE TABLE oms_uat.mst_mf_prefeasible_bw (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(100) NOT NULL,
  `product` varchar(10) NOT NULL,
  `pre_feasible_bw_new` int(10) DEFAULT NULL,
  `pre_feasible_bw_macd` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=latin1;

CREATE TABLE location_uat.mst_region (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `region_code` varchar(45) NOT NULL,
  `country` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `is_active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=latin1;

/*Insert script start*/

/*mst_process_def*/
INSERT INTO `service_fulfillment_uat`.`mst_process_def` (`key`, `name`, `stage_def_key`, `code`, `sequence`, `customer_view`) VALUES ('manual_feasibility_process', 'Manual Feasibility Process', 'quote_stage', 'MANFEAS', '0', 'N');

INSERT INTO `service_fulfillment_uat`.`mst_activity_def` (`key`, `name`, `process_def_key`, `customer_view`, `sequence`) VALUES ('manual_feasibility_activity', 'Manual Feasibility Activity', 'manual_feasibility_process', 'N', '0');

/*mst_status*/
INSERT INTO `service_fulfillment_uat`.`mst_status` (`code`, `active`) VALUES ('DELETED', 'Y');
INSERT INTO `service_fulfillment_uat`.`mst_status` (`code`, `active`) VALUES ('RETURNED', 'Y');

/*mst_task_def*/

INSERT INTO `service_fulfillment_uat`.`mst_task_def` (`key`, `name`, `activity_def_key`, `tat`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `is_dependent_task`, `dynamic_assignment`) VALUES ('manual_feasibility_osp', 'Manual Feasibility Task For OSP', 'manual_feasibility_activity', '240', 'OSP', 'OSP', 'N', 'Y', 'Manual Feasibility', 'Manual Feasibility task for OSP team', 'N', 'N');
INSERT INTO `service_fulfillment_uat`.`mst_task_def` (`key`, `name`, `activity_def_key`, `tat`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `is_dependent_task`, `dynamic_assignment`) VALUES ('manual_feasibility_man', 'Manual Feasibility Task For MAN', 'manual_feasibility_activity', '240', 'MAN', 'MAN', 'N', 'Y', 'Manual Feasibility', 'Manual Feasibility task for MAN team', 'N', 'N');
INSERT INTO `service_fulfillment_uat`.`mst_task_def` (`key`, `name`, `activity_def_key`, `tat`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `is_dependent_task`, `dynamic_assignment`) VALUES ('manual_feasibility_afm', 'Manual Feasibility Task For AFM', 'manual_feasibility_activity', '240', 'AFM', 'AFM', 'N', 'Y', 'Manual Feasibility', 'Manual Feasibility task for AFM team', 'N', 'N');
INSERT INTO `service_fulfillment_uat`.`mst_task_def` (`key`, `name`, `activity_def_key`, `tat`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `is_dependent_task`, `dynamic_assignment`) VALUES ('manual_feasibility_gimec', 'Manual Feasibility Task For GIMEC', 'manual_feasibility_activity', '240', 'GIMEC', 'GIMEC', 'N', 'Y', 'Manual Feasibility', 'Manual Feasibility task for GIMEC team', 'N', 'N');
INSERT INTO `service_fulfillment_uat`.`mst_task_def` (`key`, `name`, `activity_def_key`, `tat`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `is_dependent_task`, `dynamic_assignment`) VALUES ('manual_feasibility_prv', 'Manual Feasibility Task For PRV', 'manual_feasibility_activity', '240', 'PRV', 'PRV', 'N', 'Y', 'Manual Feasibility', 'Manual Feasibility task for PRV team', 'N', 'N');
INSERT INTO `service_fulfillment_uat`.`mst_task_def` (`key`, `name`, `activity_def_key`, `tat`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `is_dependent_task`, `dynamic_assignment`) VALUES ('manual_feasibility_prvtx', 'Manual Feasibility Task For PRV TX ', 'manual_feasibility_activity', '480', 'PRVTX', 'PRVTX', 'N', 'Y', 'Manual Feasibility', 'Manual Feasibility task for PRV TX team', 'N', 'N');
INSERT INTO `service_fulfillment_uat`.`mst_task_def` (`key`, `name`, `activity_def_key`, `tat`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `is_dependent_task`, `dynamic_assignment`) VALUES ('manual_feasibility_txengg', 'Manual Feasibility Task For TX Engg', 'manual_feasibility_activity', '480', 'TXENGG', 'TXENGG', 'N', 'Y', 'Manual Feasibility', 'Manual Feasibility task for Tx Engg team', 'N', 'N');
INSERT INTO `service_fulfillment_uat`.`mst_task_def` (`key`, `name`, `activity_def_key`, `tat`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `is_dependent_task`, `dynamic_assignment`) VALUES ('manual_feasibility_asp', 'Manual Feasibility Task For ASP', 'manual_feasibility_activity', '480', 'ASP', 'ASP', 'N', 'Y', 'Manual Feasibility', 'Manual Feasibility task for ASP team', 'N', 'N');
INSERT INTO `service_fulfillment_uat`.`mst_task_def` (`key`, `name`, `activity_def_key`, `tat`, `owner_group`, `assigned_group`, `is_customer_task`, `is_manual_task`, `title`, `description`, `is_dependent_task`, `dynamic_assignment`, `admin_view`) VALUES ('manual_feasibility_rff', 'Manual Feasibility task for RFF team', 'manual_feasibility_activity', '480', 'RFF', 'RFF', 'N', 'Y', 'Manual Feasibility', 'Manual Feasibility task for RFF Team', 'N', 'N', 'Y');
/*Support group start*/

INSERT INTO `service_fulfillment_uat`.`mf_support_group` (`group_name`,`dependant_team`) VALUES( "AFM","OSP,MAN,PRVTX,ASP,RFF");
INSERT INTO `service_fulfillment_uat`.`mf_support_group` (`group_name`,`dependant_team`) VALUES( "PRV","GIMEC");
INSERT INTO `service_fulfillment_uat`.`mf_support_group` (`group_name`,`dependant_team`) VALUES( "MAN","TXENGG,OSP,PRVTX");
INSERT INTO `service_fulfillment_uat`.`mf_support_group` (`group_name`,`dependant_team`) VALUES( "PRVTX","TXENGG,OSP,MAN");
INSERT INTO `service_fulfillment_uat`.`mf_support_group` (`group_name`,`dependant_team`) VALUES( "OSP","TXENGG,PRVTX,MAN");
INSERT INTO `service_fulfillment_uat`.`mf_support_group` (`group_name`,`dependant_team`) VALUES( "TXENGG","OSP,PRVTX,MAN");
INSERT INTO `service_fulfillment_uat`.`mf_support_group` (`group_name`,`dependant_team`) VALUES( "RFF","WE");
INSERT INTO `service_fulfillment_uat`.`mf_support_group` (`group_name`,`dependant_team`) VALUES ("ASP", "AFM");

/*Support group end*/

/*mf_dependant_team insert start*/
-- OSP
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','UP');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','PNE');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','PJB');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','ND');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','MMB');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','KOL');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','HYD');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','ERK');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','CHN');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','BGL');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','AHM');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('OSP','OSP');

-- MAN
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','PNE');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','NOI');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','ND');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','MUM');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','KOL');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','HYD');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','GGN');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','ERK');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','CHN');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','BGL');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','AHM');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('MAN','MAN');
-- AFM
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('AFM','RON');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('AFM','PUN');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('AFM','ND');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('AFM','MUM');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('AFM','KOL');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('AFM','HYD');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('AFM','ERK');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('AFM','CHN');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('AFM','BGL');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('AFM','AHM');

insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('ASP','INDIA');
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('PRVTX',null);
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('PRV',null);
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('GIMEC',null);
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('TXENGG',null); 
insert into service_fulfillment_uat.mf_dependant_team(team_name,team_region) values('WE',null); 

-- mf_provider_data

insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( '012 Smile', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( '013 Netvision', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( '1&1 Versatel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( '1Asia Alliance', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'A1 Telekom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'AAPT', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'AboveNet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Access Kenya', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'ADNSL', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Agarik', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'AIMS', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'AIRCEL 4G', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Aircel Road Warrier', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Aircel Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Aircel(Wimax)', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Aircle LTE', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'AirSpeedCom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'AirTel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Airtel P2P', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Airtel(Wimax)', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Akton', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Angola Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Arteria', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Asianet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'AT&T', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'ATM', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'ATS Network', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Axione', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Azeronline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Batelco', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Beeline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Belgacom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Bell Canada', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Bell Teleservices', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Bezeq International', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Bharti Airtel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Bharti Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'BICS', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Bite', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Biznet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Blue Wireless', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'BlueTel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Bouygues Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'BringCom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Brodynt', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'BSNL Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'BT', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'BTC', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Bulgarian Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'C&W', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Cableman', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CASPEL', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CAT', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Caucasus Online', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CBCCom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CEB', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Century Link', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CGC SAS', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Chief', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CHT', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CIT', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Citictel CPC', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Citycom Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CJSC Synterra', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Claranet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CM Intl', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CMC', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Cogeco', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CogentCom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'COLO LM', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Colt', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Columbus Networks', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Comcast', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Completel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Congo', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Consolidated', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Cox', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CT Global', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'CU Global', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Cyprus Telecom Authority', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'DBN', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Development Logic', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Development Logics', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Dhiraagu', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Dial', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Digicel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Djibouti Data Center', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'DNA', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Docomo Pacific', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'DTAG', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'EasyNet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Efar', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Elisa', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Enet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Epsilon', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Equinix', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Ericom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Esto Internet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Etisalat', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'EuNetworks', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Eurofiber', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'EuroproNET', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Euskaltel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'EvoSwitch', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Exatel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Expereo', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Exponential-E', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'FastWeb', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Fiberail', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'FiberRing', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'FibreNoire', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'FirstLight', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Flex Networks', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'FPL', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'FPT Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'FTLD', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Gail Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'GAX', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'GCX', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Global Switch', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Global Telecom Services', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Globe', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Globenet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Gold Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'GTA', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'GTS CE', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'GTS Energies', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'GTT', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'HFCL Wireless', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'HFCL Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'HGC', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Hibernia', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'HKBN', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'hSo', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Hub Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'HUGHES', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Hughes 3G', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'ICN', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Icosnet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'IDEA Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'IG Networks', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Indosat', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Infracom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Intelcom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Interactive Data', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Internet Solutions', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Interoute', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Interxion', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Invitech Solutions', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'INWI', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Itconic', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'ITENOS', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Jastel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Jazz Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'JT', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Kazakhtelecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'KazTransCom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'KCOM', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'KDDI', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'KINX', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Kordia', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'KPN', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Lanka Bell', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Lankacom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Level 3', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Lightower', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Lightpath', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Linx Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Liquid Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'M-net', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Magnet Networks', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Main One', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'MAN', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'MAN Near Net', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Matrix Networks', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Mauritius Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Maxis', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'MegaFon', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Megatelecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'MEO', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Metro Optic', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Mobily', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Mobitel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Monaco Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Moratel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Movitel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'MTI Teleport', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'MTN', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'MTNL Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'MTS', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Mukand Systems', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Multinet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Navega', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'NCIC', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'NEECO S.R.O.', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'NeoTel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'NetCom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Neterra', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Netia', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Neutrona', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'NewTelco', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'NextGen', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'NJFX', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'NOS', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Not Required', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'NourNet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Novocom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'NTT', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Nucleus Connect', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Ooredoo', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Optimal Telemedia Pvt Ltd', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Optus', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Orange', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Orixcom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Others', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Pantel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Paratus', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'PCCW', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'PGCIL Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Pipe Networks', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'PLDT', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Plus', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Polkomtel Plus', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Produban', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'PT NAP Info', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'PT Prime', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'PT XL', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'PTCL', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'QSC', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin from GAIL Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin from TCL POP', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with Aircell Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with Airtel (Bharti) Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with CityCom Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with HFCL Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with IDEA Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with PGCIL Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with RailTel Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with Reliance Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with ShyamTel Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with TCL WiMax Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with TCL Wimax Backhaul(Offnet)', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with TCL Wimax Backhaul(Onnet)', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with TTML Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with TTSL Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Radwin with Vodafone Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Railtel Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'RapidCom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'redIT', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Reliance RW', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Reliance Wireless', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Reliance Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Relined', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Retelit', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'RETN', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'RK Infra', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'RK Infra Wireless', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'RK Infra Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Rogers', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Roke Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'RomTelecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'ROS Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Rozvytok', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'RP World', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'RTCom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Rukku', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Safaricom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Sampark', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Samsung', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Sanghvi Infotech', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Sarenet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Seacom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Sejong', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Semsfor77', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SFDC', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SFR', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Shaw', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Shyamtel Wireless', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Shyamtel Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Sify Wireless', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Sify Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SIG', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Signum', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SimbaNET', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SingTel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Sipartech', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Six Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SixNet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SKB', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Skynet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SME OHF', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SNTPL', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Softbank', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SOL', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Spark NZ', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SPTel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Sri Lanka Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SSC', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'SSE Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Starhub', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Startel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'STC', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Subisu', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Summit Communications', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Sunrise', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Syswall Telecom Pvt Ltd', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'T-Mobile', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'T-Systems', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TalkTalk', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tashicell', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tata Teleservices', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TATANET', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tawasul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW from Customer Location (TCL POP)', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW from TCL POP', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW with Aircel Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW with Airtel (Bharti) Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW with IDEA Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW with Reliance Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW with ShyamTel Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW with TCL WiMax Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW with TTML Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW with TTSL Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL MW with Vodafone Backhaul', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL OHF', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL UBR PMP', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TCL WiMax', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TDC', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TE Data', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tejays', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tele2', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telecity', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telecom Egypt', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telecom Italia', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telecom Namibia', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TelecomNet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telefonica', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telehouse', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telekom Malaysia', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telekom Romania', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telespazio', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Teliasonera', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TelNet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telstra', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Telus', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TEXES CONNECT PRIVATE LIMITED WIRELESS', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TFN', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tikona', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tikona LTE', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TIME', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Time Warner Cable', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tiscali', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Transgrid', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TraxComm', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TRUE', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TSA', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TTL / TTSL', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TTML Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TTSL RW', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TTSL Wireless', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TTSL Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tulip Wireless', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tulip Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Tunisia Telecom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Turk Telekom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'TWA', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Uclix-Tejays', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Ucom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Ufinet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Unidata', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Unitel Telecom Ltd', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'UTL', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'VADS', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Vainavi', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Vancis', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Veermax', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'VENUS', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Verizon', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Videocon', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'VIDI', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Viettel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'VimpelCom', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Vipnet', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Virgin Media', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Virtual1', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Vocus', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Vodafone', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Vodafone Wireless', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Vodafone Wireline', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'WAN Aggregation', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Wave Broadband', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Wharf', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Wilcon', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Windstream', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Xfiber', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'XO', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'YashTel', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Zain', now(), 'admin', 'admin', now());
insert into service_fulfillment_uat.mf_provider_data ( provider_name, created_time, created_by, updated_by, updated_time) values ( 'Zayo', now(), 'admin', 'admin', now());

/*feasibility_category_map*/

insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Pending',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Pending',	'Pending',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Feasible',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Feasible',	'No Dependancy',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Feasible',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Feasible',	'Backhaul not Avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Feasible',	'No LOS',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Feasible',	'No Onnet BTS under Coverage area',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Feasible',	'BTS Infrastructure sharing not avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Pending',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Feasible',	'BTS low Uptime',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Feasible',	'Limited Space for Mast at CE towards BTS ',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Pending',	'Pending',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Feasible',	'P2P Customer Count Issue at BTS',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Feasible',	'Hanging Cable Or Length Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Feasible',	'HT Electrical Lines',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Feasible',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Feasible',	'No Dependancy',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Feasible with Resources',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Feasible with Resources',	'site dependancy',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Feasible with Resources',	'Backhaul Augmenction Requied',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Feasible',	'L1 Feasible on Tool',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Feasible with Resources',	'Backhaul Not Checked',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Checked',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'Backhaul not available',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Checked',	'Survey Permission not avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Checked',	'site not ready for L2 survey',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'No LOS',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Checked',	'Contact details Indequate',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Checked',	'Terrace Access Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'Sector BW Not Available',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Pending with ASP',	'NA',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Pending with RFF',	'NA',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Feasible with Resources',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Pending',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Feasible with Resources',	'Site Dependancy',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Pending',	'Pending',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Feasible',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Feasible',	'No Dependancy',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Feasible with Resources',	'Backhaul Augmenction Requied',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Feasible',	'Budgetary Quote',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Feasible',	'Pre Feasible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Feasible',	'Feasible on Tool',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Checked',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Feasible',	'L2 Feasible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Checked',	'Survey Permission not avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Feasible',	'L1 Feasible ',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Checked',	'site not ready for L2 survey',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Feasible',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Checked',	'Contact details Indequate',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Feasible',	'Backhaul not Avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Checked',	'Terrace Access Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Feasible',	'BW not Avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Feasible',	'No LOS',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Feasible',	'NO Network',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Feasible',	'Sector BW Not Avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Feasible',	'NNI  Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Pending with RFF',	'NA',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Feasible',	'BTS Infrastructure sharing not avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Checked',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Checked',	'Survey Permission not avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Checked',	'site not ready for L2 survey',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Checked',	'Contact details Indequate',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Checked',	'Terrace Access Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Checked',	'Wrongly Created Response',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'No Onnet BTS under Coverage area',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Checked',	'Address Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'BTS low Uptime',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Checked',	'Terrace Fully Covered/ Not RCC & No Space for GBT',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'Limited Space for Mast at CE towards BTS ',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'Not Checked',	'No Response from Customer',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'Sector Choke-Customer count issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'Hanging Cable Or Length Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'HT Electrical Lines',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Feasible',	'L1 Feasible on Tool',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Feasible',	'No UBR Coverage',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Checked',	'Wrongly Created Response',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Checked',	'Address Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Not Checked',	'Terrace Fully Covered/ Not RCC & No Space for GBT',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Pending with ASP',	'Check Backhaul BW',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'Pending with ASP',	'Check Colo Feasibility',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Checked',	'Wrongly Created Response',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Checked',	'Address Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'Not Checked',	'Terrace Fully Covered/ Not RCC & No Space for GBT',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Pending',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Pending',	'Pending',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible',	'MAN on tool',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible',	'No Dependancy',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible',	'Budgetary Quote',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'MAN on tool',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Pending',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'BW not Avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Pending',	'Pending',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'Infrastructure sharing not avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'local Drop not Avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Feasible',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'NO Network',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Feasible',	'Budgetary Quote',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'NO OSP Route',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'ROW Permission Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Feasible',	'Provider price list',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'PROW Permission Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'Access Ring BW and Fiber Choke',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Feasible',	'High Capex',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Not Feasible',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Not Feasible',	'BW not Avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Not Feasible',	'NO Network',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible with Capex',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Not Feasible',	'NO OSP Route',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible with Capex',	'OSP  <  500 m',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible with Capex',	'OSP  > 500 m',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Not Checked',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible with Resources',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Not Checked',	'Survey Permission not avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible with Resources',	'Equipment Upgradaction requied',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible with Resources',	'New Ring to be created',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Not Checked',	'Contact details Indequate',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible with Resources',	'Ring Splitting requied',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible with Resources',	'site dependancy',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Feasible with Resources',	'Linear Path Avalible',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Not Checked',	'Wrongly Created Response',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Not Checked',	'Address Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'Not Checked',	'No Response from Customer',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Checked',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Checked',	'Contact details Indequate',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Checked',	'Wrongly Created Response',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'Not Checked',	'Address Issue',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireline - MAN/VBL',	'None',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - RADWIN',	'None',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Wireless - UBR PMP / WiMax',	'None',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireless',	'None',	'None',	'Y');
insert into service_fulfillment_uat.feasibility_category_map (f_mode, f_status, f_category, is_active) values ('Offnet - Wireline',	'None',	'None',	'Y');

/*mst_region*/

insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('ND',	'INDIA',	'UTTAR PRADESH',	'Noida',	1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('ND',	'INDIA',	'UTTAR PRADESH',	'Ghaziabad',	1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('RON',	'INDIA',	'UTTAR PRADESH',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('RON',	'INDIA',	'HARYANA',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('ND',	'INDIA',	'HARYANA',	'Faridabad',	1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('ND',	'INDIA',	'HARYANA',	'Gurgaon',	1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('PUN',	'INDIA',	'MAHARASHTRA',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('MUM',	'INDIA',	'MAHARASHTRA',	'Mumbai',	1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('MUM',	'INDIA',	'MAHARASHTRA',	'Thane',	1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('MUM',	'INDIA',	'MAHARASHTRA',	'Navi Mumbai',	1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('MUM',	'INDIA',	'MAHARASHTRA',	'Roha',	1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('MUM',	'INDIA',	'MAHARASHTRA',	'Raigad',	1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'ARUNACHAL PRADESH',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'ASSAM',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'BIHAR',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'CHATTISGARH',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'MANIPUR',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'MEGHALAYA',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'MIZORAM',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'NAGALAND',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'ORISSA',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'SIKKIM',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'TRIPURA',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'WEST BENGAL',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'ANDAMAN AND NICOBAR ISLANDS',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('KOL',	'INDIA',	'JHARKHAND',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('RON',	'INDIA',	'HIMACHAL PRADESH',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('RON',	'INDIA',	'JAMMU AND KASHMIR',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('RON',	'INDIA',	'PUNJAB',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('RON',	'INDIA',	'RAJASTHAN',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('RON',	'INDIA',	'UTTARAKHAND',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('RON',	'INDIA',	'UTTARANCHAL',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('RON',	'INDIA',	'CHANDIGARH',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('HYD',	'INDIA',	'ANDHRA PRADESH',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('HYD',	'INDIA',	'TELANGANA',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('PUN',	'INDIA',	'GOA',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('AHM',	'INDIA',	'GUJARAT',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('AHM',	'INDIA',	'DADRA AND NAGAR HAVELI',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('AHM',	'INDIA',	'DAMAN AND DIU',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('AHM',	'INDIA',	'MADHYA PRADESH',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('BGL',	'INDIA',	'KARNATAKA',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('ERK',	'INDIA',	'KERALA',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('ERK',	'INDIA',	'LAKSHADWEEP',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('CHN',	'INDIA',	'TAMILNADU',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('CHN',	'INDIA',	'PONDICHERY',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('ND',	'INDIA',	'DELHI',	null, 1);
insert into location_uat.mst_region (region_code, country, state, city, is_active) values ('CHN',	'INDIA',	'TAMIL NADU',	null, 1);

/*mf_prefeasible_bw*/

insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('AHMEDABAD',	'IAS',	3000	,	1500);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('BANGALORE',	'IAS',	3000	,	1500);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('CHENNAI',	'IAS',	3000	,	1500);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('MUMBAI',	'IAS',	3000	,	1500);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('DELHI',	'IAS',	3000	,	1500);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('ERNAKULAM',	'IAS',	3000	,	1500);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('HYDERABAD',	'IAS',	3000	,	1500);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('KOLKATA',	'IAS',	3000	,	1500);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('PUNE',	'IAS',	3000	,	1500);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Baroda (NLD)',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Bhopal',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Bhubaneshwar (Puri)',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Coimbatore',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Gurgaon',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Indore',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Jaipur',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Jalandhar',	'IAS',	90	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Jamshedpur',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Lucknow',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Nagpur',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Surat(Shopper Stop)',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Trivandrum',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Vizag',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Mohali',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Mumbai_BKC',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Delhi_GK1',	'IAS',	2000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Nasik',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('panjim',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('GPX',	'IAS',	3000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Patna',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Guwahati',	'IAS',	50	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Meerut',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Vijayawada',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Adoni',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Agra New POP',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Ambatur',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Ahmednagar',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Ajmer',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Allahabad',	'IAS',	20	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Allepy/Alpuzha',	'IAS',	5	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Alwar',	'IAS',	20	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Ambala',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Aurangabad',	'IAS',	90	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Amritsar',	'IAS',	20	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Anand (Nadiad)',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Asansol',	'IAS',	5	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Bareilly',	'IAS',	5	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Belgaum',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Bharuch',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Bhilai',	'IAS',	5	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Bhilwara',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Calicut(Kozikode)',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Cannore',	'IAS',	5	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Cuttack',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Dehradun',	'IAS',	20	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Durgapur',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Erode',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Gandhinagar',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Ghaziabad',	'IAS',	20	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Guntur',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Gwalior',	'IAS',	5	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Hassan',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('himatnagar',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Hissar',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Hossur',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Hubli',	'IAS',	20	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Jabalpur (NLD)',	'IAS',	20	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Jammu',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Jodhpur',	'IAS',	90	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Kanpur',	'IAS',	90	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Karnal',	'IAS',	20	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Kolhapur',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Kollam',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Kota',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Kottyam',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Ludhiana',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Madurai',	'IAS',	20	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Mangalore',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('mehsana',	'IAS',	5	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Mysore',	'IAS',	90	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Nellore',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Patiala',	'IAS',	5	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Pondicherry',	'IAS',	90	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Raipur',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Rajkot',	'IAS',	150	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Rajmundary',	'IAS',	5	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Ranchi',	'IAS',	50	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Roorkee',	'IAS',	5	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Rourkela',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Salem',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Sambalpur',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Sangli',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Satara',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Shimla',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Silliguri',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Solapur',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Sonepat',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Thirussur',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Tirupathy',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Tirupur',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Trichy',	'IAS',	50	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Tumkur',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Udaipur',	'IAS',	1000	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('valsad',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('VASHI',	'IAS',	150	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Vellore',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Warangal',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Faridabad',	'IAS',	10	,	null);
insert into oms_uat.mst_mf_prefeasible_bw ( location, product, pre_feasible_bw_new, pre_feasible_bw_macd) values ('Varanasi',	'IAS',	50	,	null);

/*oms user*/

INSERT INTO `oms_uat`.`mst_group_type` (`group_type_code`, `group_type`, `status`) VALUES ('MANUAL_FEASIBILITY', 'MANUAL_FEASIBILITY', '1');


INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('29', 'AFM_RON', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('30', 'AFM_PUN', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('31', 'AFM_ND', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('32', 'AFM_KOL', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('33', 'AFM_HYD', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('34', 'AFM_ERK', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('35', 'AFM_CHN', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('36', 'AFM_BGL', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('23', 'AFM_AHM', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('24', 'AFM_MUM', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('25', 'MAN_MUM', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('26', 'MAN_UP', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('27', 'OSP_MUM', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('28', 'OSP_UP', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('37', 'ASP', '3', '1', '0');

INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('38', 'MAN_PNE', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('39', 'MAN_NOI', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('40', 'MAN_ND', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('41', 'MAN_KOL', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('42', 'MAN_HYD', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('43', 'MAN_GGN', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('44', 'MAN_ERK', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('45', 'MAN_CHN', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('46', 'MAN_BGL', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('47', 'MAN_AHM', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('48', 'MAN_MP', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('49', 'OSP_PNE', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('50', 'OSP_PJB', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('51', 'OSP_ND', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('52', 'OSP_MMB', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('53', 'OSP_KOL', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('54', 'OSP_HYD', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('55', 'OSP_ERK', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('56', 'OSP_CHN', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('57', 'OSP_BGL', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('58', 'OSP_AHM', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('59', 'OSP_MP', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('PRVTX', '3', '1', '0');
INSERT INTO `oms_uat`.`mst_user_groups` (`group_name`, `group_type`, `status`, `is_region_required`) VALUES ('TXENGG', '3', '1', '0');


-- user table 

INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`, `force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 'afm.ron@legomail.com', 'afm.ron@legomail.com', 'afm', 'ron', '8989898989', '1', '0', '0', '0');

INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'afm.pun@legomail.com', 'afm.pun@legomail.com', 'afm', 'pun', '9909090909', '1', '0', '0', '0');

  INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'afm.nd@legomail.com', 'afm.nd@legomail.com', 'afm', 'nd', '9909090909', '1', '0', '0', '0');
 
   INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'afm.mum@legomail.com', 'afm.mum@legomail.com', 'afm', 'mum', '9909090909', '1', '0', '0', '0');

    INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'afm.kol@legomail.com', 'afm.kol@legomail.com', 'afm', 'kol', '9909090909', '1', '0', '0', '0');
  
    INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'afm.hyd@legomail.com', 'afm.hyd@legomail.com', 'afm', 'hyd', '9909090909', '1', '0', '0', '0');
 
     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'afm.erk@legomail.com', 'afm.erk@legomail.com', 'afm', 'erk', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'afm.chn@legomail.com', 'afm.chn@legomail.com', 'afm', 'chn', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'afm.bgl@legomail.com', 'afm.bgl@legomail.com', 'afm', 'bgl', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'afm.ahm@legomail.com', 'afm.ahm@legomail.com', 'afm', 'ahm', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'raja.vr@legomail.com', 'raja.vr@legomail.com', 'asp', 'india', '9909090909', '1', '0', '0', '0');
 
 INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'man-feasibility.pne@legomail.com', 'man-feasibility.pne@legomail.com', 'man', 'pun', '9909090909', '1', '0', '0', '0');

  INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'man-feasibility.noi@legomail.com', 'man-feasibility.noi@legomail.com', 'man', 'noi', '9909090909', '1', '0', '0', '0');
 
   INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'navneet.khurana@legomail.com', 'navneet.khurana@legomail.com', 'navneet', 'khurana', '9909090909', '1', '0', '0', '0');

    INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'man-feasibility.mum@legomail.com', 'man-feasibility.mum@legomail.com', 'man', 'mum', '9909090909', '1', '0', '0', '0');
  
    INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'man-feasibility.kol@legomail.com', 'man-feasibility.kol@legomail.com', 'man', 'kol', '9909090909', '1', '0', '0', '0');
 
     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'mahesh.tuniki@legomail.com', 'mahesh.tuniki@legomail.com', 'mahesh', 'tuniki', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'man-feasibility.ggn@legomail.com', 'man-feasibility.ggn@legomail.com', 'man', 'ggn', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'man-feasibility.erk@legomail.com', 'man-feasibility.erk@legomail.com', 'man', 'erk', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'jebit.loorthaiah@legomail.com', 'jebit.loorthaiah@legomail.com', 'jebit', 'loorthaiah', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'man-feasibility.bgl@legomail.com', 'man-feasibility.bgl@legomail.com', 'man', 'bgl', '9909090909', '1', '0', '0', '0');
 
 INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'nirmalgiri.goswami@legomail.com', 'nirmalgiri.goswami@legomail.com', 'nirmalgiri', 'goswami', '9909090909', '1', '0', '0', '0');

  INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'nirdosh.bopche@legomail.com', 'nirdosh.bopche@legomail.com', 'nirdosh', 'bopche', '9909090909', '1', '0', '0', '0');
 
   INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'bheem.singh1@legomail.com', 'bheem.singh1@legomail.com', 'bheem', 'singh', '9909090909', '1', '0', '0', '0');

    INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'gis.pune@legomail.com', 'gis.pune@legomail.com', 'gis', 'pune', '9909090909', '1', '0', '0', '0');
  
    INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'osp-feasibility.nd@legomail.com', 'osp-feasibility.nd@legomail.com', 'osp', 'nd', '9909090909', '1', '0', '0', '0');
 
     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'osp-feasibility.mmb@legomail.com', 'osp-feasibility.mmb@legomail.com', 'osp', 'mmb', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'osp-feasibility.kol@legomail.com', 'osp-feasibility.kol@legomail.com', 'osp', 'kol', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'osp-feasibility.hyd@legomail.com', 'osp-feasibility.hyd@legomail.com', 'osp', 'hyd', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'osp-feasibility.erk@legomail.com', 'osp-feasibility.erk@legomail.com', 'osp', 'erk', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'gajendrababu.dr@legomail.com', 'gajendrababu.dr@legomail.com', 'gaj', 'babu', '9909090909', '1', '0', '0', '0');
 
 INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'osp-feasibility.bgl@legomail.com', 'osp-feasibility.bgl@legomail.com', 'osp', 'bgl', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'bhuwan.joshi@legomail.com', 'bhuwan.joshi@legomail.com', 'bhuwan', 'joshi', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'harish.rathi@legomail.com', 'harish.rathi@legomail.com', 'harish', 'rathi', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'prov-ip.feasibility@legomail.com', 'prov-ip.feasibility@legomail.com', 'prv', 'data', '9909090909', '1', '0', '0', '0');

     INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`,
`force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 
 'prov-tx.feasibility@legomail.com', 'prov-tx.feasibility@legomail.com', 'prvtx', 'tx', '9909090909', '1', '0', '0', '0');
 
 INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`, `force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 'prv@legomail.com', 'prv@legomail.com', 'prv', 'prv', '8989989098', '1', '1', '0', '0');
 
INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`, `force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 'asp@legomail.com', 'asp@legomail.com', 'asp', 'asp', '8989989098', '1', '1', '0', '0');

INSERT INTO `oms_uat`.`user` (`user_type`, `username`, `email_id`, `first_name`, `last_name`, `contact_no`, `status`, `force_change_password`, `is_otp_enabled`, `is_gen_terms_approved`) VALUES ('sales', 'gimec@legomail.com', 'gimec@legomail.com', 'gimec', 'gimec', '8989989098', '1', '1', '0', '0');

/*user to group*/

INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('29', 'afm.ron@legomail.com', '1023');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('30', 'afm.pun@legomail.com', '988');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('31', 'afm.nd@legomail.com', '989');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('24', 'afm.mum@legomail.com', '990');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('32', 'afm.kol@legomail.com', '991');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('33', 'afm.hyd@legomail.com', '992');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('34', 'afm.erk@legomail.com', '993');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('35', 'afm.chn@legomail.com', '994');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('36', 'afm.bgl@legomail.com', '995');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('23', 'afm.ahm@legomail.com', '996');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('37', 'raja.vr@legomail.com', '997');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('38', 'man-feasibility.pne@legomail.com', '998');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('39', 'man-feasibility.noi@legomail.com', '999');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('40', 'navneet.khurana@legomail.com', '1000');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('25', 'man-feasibility.mum@legomail.com', '1001');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('41', 'man-feasibility.kol@legomail.com', '1002');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('42', 'mahesh.tuniki@legomail.com', '1003');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('43', 'man-feasibility.ggn@legomail.com', '1004');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('44', 'man-feasibility.erk@legomail.com', '1005');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('45', 'jebit.loorthaiah@legomail.com', '1006');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('46', 'man-feasibility.bgl@legomail.com', '1007');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('47', 'nirmalgiri.goswami@legomail.com', '1008');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('48', 'nirdosh.bopche@legomail.com', '1009');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('28', 'bheem.singh1@legomail.com', '1010');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('49', 'gis.pune@legomail.com', '1011');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('50', 'bheem.singh1@legomail.com', '1010');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('51', 'osp-feasibility.nd@legomail.com', '1012');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('52', 'osp-feasibility.mmb@legomail.com', '1013');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('53', 'osp-feasibility.kol@legomail.com', '1014');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('54', 'osp-feasibility.hyd@legomail.com', '1015');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('55', 'osp-feasibility.erk@legomail.com', '1016');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('56', 'gajendrababu.dr@legomail.com', '1017');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('57', 'osp-feasibility.bgl@legomail.com', '1018');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('58', 'bhuwan.joshi@legomail.com', '1019');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('59', 'harish.rathi@legomail.com', '1020');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('15', 'prov-ip.feasibility@legomail.com', '1021');
INSERT INTO `oms_uat`.`user_to_user_groups` ( `user_group_id`, `username`, `user_id`) VALUES ('21', 'prov-tx.feasibility@legomail.com', '1022');

-- notification nfw 

INSERT INTO `notification_nfw`.`notification_template` (`name`, `code`, `template_reference_name`, `notification_type`, `is_active`) VALUES ('mf_task_notify_close_template', 'MFNCT', 'mf_task_notify_close_template', 'Email', '1');

INSERT INTO `notification_nfw`.`notification_template` (`name`, `code`, `template_reference_name`, `notification_type`, `is_active`) VALUES ('mf_task_notify_closeAsFeasibile_prvToAfm_template', 'MFNCFT', 'mf_task_notify_closeAsFeasibile_prvToAfm_template', 'Email', '1');

INSERT INTO `notification_nfw`.`notification_template` (`name`, `code`, `template_reference_name`, `notification_type`, `is_active`) VALUES ('mf_task_notify_notFeasible_By_AFMOrPRV_template', 'MFNNFT', 'mf_task_notify_notFeasible_By_AFMOrPRV_template', 'Email', '1');

INSERT INTO `notification_nfw`.`notification_template` (`name`, `code`, `template_reference_name`, `notification_type`, `is_active`) VALUES ('mf_task_notify_return_template', 'MFNRT', 'mf_task_notify_return_template', 'Email', '1');


INSERT INTO `notification_nfw`.`notification_template` (`name`, `code`, `template_reference_name`, `notification_type`, `is_active`) VALUES (' mf_task_notify_assignee_template', 'MFNAT', ' mf_task_notify_assignee_template', 'Email', '1');

/*bts,pop,hh,vvendor

for mf_bts_data, use below query to export data from pricing data base.

select 
b.site_id, b.site_name, b.longitude, b.latitude, b.site_address, b.ipaddress, 
o.sector_id, o.sector_name
from optimus_abstract_uat2.bts_data b 
inner join Onnet_RF_Sector_Check_Rolled_Up o on o.ip_address = b.ipaddress;

 For mf_pop_data & mf_hh_data export required column from pop_data  and hh_data respectively.
 
 For mf_vendor_data, export excel data to table.
 
 mf_vendor_data

*/

/*Insert end*/

/*Script forr manual feasibility workbench start*/





