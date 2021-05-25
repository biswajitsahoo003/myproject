-- Need to be executed in service inventory

/*IZOSDWAN scripts */
/*Executed in PROD on 07-07-2020 start */
-- SDWANCP-66 Audit and transaction
 CREATE TABLE `sdwan_inventory_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(100) DEFAULT NULL,
  `mdc_token` varchar(45) DEFAULT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `customer_le_id` varchar(255) DEFAULT NULL,
  `request_method` varchar(10) DEFAULT NULL,
  `request_payload` longtext DEFAULT NULL,
  `response` longtext DEFAULT NULL,
  `response_code` int(11) DEFAULT NULL,
  `service_id` varchar(50) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `operation` varchar(255) DEFAULT NULL,
  `updated_time` timestamp NULL DEFAULT current_timestamp(),
  `instance_region` varchar(100) DEFAULT NULL,
  `display_text` longtext DEFAULT NULL,
  `request_time` timestamp NULL DEFAULT current_timestamp(),
  `response_time` timestamp NULL DEFAULT current_timestamp(),
  `organization_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)

-- SDWANCP-211 Versa diretor mapping
CREATE TABLE `sdwan_endpoints` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vendor_type` varchar(100) NOT NULL,
  `server_code` varchar(50) NOT NULL,
  `server_locaion` varchar(100) NOT NULL,
  `server_ip` varchar(50) NOT NULL,
  `server_port` varchar(10) NOT NULL,
  `server_username` varchar(50) NOT NULL,
  `server_password` varchar(50) NOT NULL,
  `op_param` varchar(50) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*SDWAN enpoints data*/
INSERT INTO `sdwan_endpoints` (`vendor_type`, `server_code`, `server_locaion`, `server_ip`, `server_port`, `server_username`, `server_password`, `status`) VALUES ('versa', 'APAC', 'APAC', 'https://115.114.85.153', '9182', 'ssp_user', 'Tata@1234', '1');
INSERT INTO `sdwan_endpoints` (`vendor_type`, `server_code`, `server_locaion`, `server_ip`, `server_port`, `server_username`, `server_password`, `status`) VALUES ('versa', 'US', 'US', 'https://115.114.85.154', '9182', 'ssp_user', 'Tata@1234', '1');
INSERT INTO `sdwan_endpoints` (`vendor_type`, `server_code`, `server_locaion`, `server_ip`, `server_port`, `server_username`, `server_password`, `status`) VALUES ('versa', 'UK', 'UK', 'https://115.114.85.155', '9182', 'ssp_user', 'Tata@1234', '1');

/*Executed in PROD on 07-07-2020 end */

alter table sdwan_inventory_audit add column template_name varchar(255) default null;

alter table sdwan_inventory_audit add column component_name varchar(100) default null;

alter table sdwan_inventory_audit add column component_value varchar(255) default null;

alter table sdwan_inventory_audit add column task_id int default null;

Alter table sdwan_endpoints add column is_director tinyint(4) default null;

INSERT INTO `service_inventory_uat_v4`.`sdwan_endpoints` (`vendor_type`, `server_code`, `server_locaion`, `server_ip`, `server_port`, `server_username`, `server_password`, `status`, `is_director`) VALUES ('versa', 'Versa Analytics Server', 'Mumbai', 'https://219.65.51.13', '443', 'system', 'Tata@123', '1', '0');
UPDATE `service_inventory_uat_v4`.`sdwan_endpoints` SET `is_director` = '1' WHERE (`server_code` != 'Versa Analytics Server');

/*IZOSDWAN scripts end */


/* PI3-Sptint 1, 2 Start*/   

Use customer;

CREATE TABLE `customer_attachment` (

`id` INT NOT NULL AUTO_INCREMENT,

`attachment_id` VARCHAR(40) NOT NULL,

`attachment_name` VARCHAR(100) NOT NULL,

`status` INT NOT NULL,

`created_by` VARCHAR(40) ,,

`created_time` TIMESTAMP,

PRIMARY KEY (`id`));


ALTER TABLE service_assurance.product_to_service_catalog
Modify COLUMN erf_prd_catalog_product_name varchar(250);


-- SR Generic
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Fiber Channel'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Fiber Channel'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Managed Security Services'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Managed Security Services'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Telepresence'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Telepresence'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Managed Hosting'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Managed Hosting'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='IZO Managed Cloud for Azure'), (SELECT nm from product_catalog_uat.product_catalog where nm ='IZO Managed Cloud for Azure'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Cisco Webex Conferencing - Cloud Connected Audio'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Cisco Webex Conferencing - Cloud Connected Audio'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Content Service - Live Classroom'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Content Service - Live Classroom'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Broadcasting Solutions - TV Uplink'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Broadcasting Solutions - TV Uplink'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='IZO Managed Cloud for AWS'), (SELECT nm from product_catalog_uat.product_catalog where nm ='IZO Managed Cloud for AWS'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Audio Conferencing Service'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Audio Conferencing Service'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Tata Communications with Direct Routing'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Tata Communications with Direct Routing'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Tata Communications with Skype for Business'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Tata Communications with Skype for Business'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Insta CC'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Insta CC'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Content Delivery Network'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Content Delivery Network'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Managed Messaging Services'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Managed Messaging Services'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Global Dedicated Ethernet'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Global Dedicated Ethernet'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='IP Transit'), (SELECT nm from product_catalog_uat.product_catalog where nm ='IP Transit'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='International Private Line'), (SELECT nm from product_catalog_uat.product_catalog where nm ='International Private Line'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');

-- SR RFO

INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Fiber Channel'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Fiber Channel'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Managed Security Services'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Managed Security Services'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Telepresence'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Telepresence'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Managed Hosting'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Managed Hosting'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='IZO Managed Cloud for Azure'), (SELECT nm from product_catalog_uat.product_catalog where nm ='IZO Managed Cloud for Azure'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Cisco Webex Conferencing - Cloud Connected Audio'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Cisco Webex Conferencing - Cloud Connected Audio'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Content Service - Live Classroom'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Content Service - Live Classroom'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Broadcasting Solutions - TV Uplink'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Broadcasting Solutions - TV Uplink'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='IZO Managed Cloud for AWS'), (SELECT nm from product_catalog_uat.product_catalog where nm ='IZO Managed Cloud for AWS'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Audio Conferencing Service'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Audio Conferencing Service'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Tata Communications with Direct Routing'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Tata Communications with Direct Routing'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Tata Communications with Skype for Business'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Tata Communications with Skype for Business'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Insta CC'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Insta CC'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Content Delivery Network'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Content Delivery Network'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Managed Messaging Services'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Managed Messaging Services'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Global Dedicated Ethernet'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Global Dedicated Ethernet'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='IP Transit'), (SELECT nm from product_catalog_uat.product_catalog where nm ='IP Transit'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='International Private Line'), (SELECT nm from product_catalog_uat.product_catalog where nm ='International Private Line'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');

/* Should executed in prod only after verifying each CR in DEV*/

-- Global Dedicate Ethernet -GDE
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='Global Dedicated Ethernet'), (SELECT nm from product_catalog_uat.product_catalog where nm ='Global Dedicated Ethernet'), (select id from service_assurance.mst_service_catalog where name ='Access List Changes'),'Y');

-- IP Transit
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='IP Transit'), (SELECT nm from product_catalog_uat.product_catalog where nm ='IP Transit'), (select id from service_assurance.mst_service_catalog where name ='Access List Changes'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='IP Transit'), (SELECT nm from product_catalog_uat.product_catalog where nm ='IP Transit'), (select id from service_assurance.mst_service_catalog where name ='Routing Changes'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='IP Transit'), (SELECT nm from product_catalog_uat.product_catalog where nm ='IP Transit'), (select id from service_assurance.mst_service_catalog where name ='Opening Prefix/filter list for Customer Public IP Pools'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='IP Transit'), (SELECT nm from product_catalog_uat.product_catalog where nm ='IP Transit'), (select id from service_assurance.mst_service_catalog where name ='Ebgp creation for Customer Owned AS'),'Y');



-- IPL 
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='International Private Line'), (SELECT nm from product_catalog_uat.product_catalog where nm ='International Private Line'), (select id from service_assurance.mst_service_catalog where name ='QOS Marking Changes'),'Y');

-- NDE
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='Access List Changes'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='Add/Change The Interface On The Managed CE'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='Bandwidth Dividation on Managed CE'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='Debug Tools (Netflow, Accounting, etc)'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='Extended LAN'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='Implementing Routing Protocol'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='IPV6 Enablement'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='NAT Configuration'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='Opening Prefix/filter list for Customer Public IP Pools'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='QOS Marking Changes'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='Routing Changes'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='SNMP Configuration'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='VLAN Membership'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='NDE'), (SELECT nm from product_catalog_uat.product_catalog where nm ='NDE'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');

-- DIA

INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='Access List Changes'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='Add/Change The Interface On The Managed CE'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='Ebgp creation for Customer Owned AS'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='Extended LAN'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='GRE/IPSec Configuration'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='Implementing Routing Protocol'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='IPV6 Enablement'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='NAT Configuration'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='Opening Prefix/filter list for Customer Public IP Pools'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='QOS Marking Changes'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='Route-Map,RTBH,Community/Policy based routing'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='SNMP Configuration'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='VLAN Membership'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='IP Related Change'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='Generic Request'),'Y');
INSERT INTO service_assurance.product_to_service_catalog (erf_prd_catalog_product_catalog_id,erf_prd_catalog_product_name,service_catalog_id, is_active) values ((SELECT id from product_catalog_uat.product_catalog where nm='DIA'), (SELECT nm from product_catalog_uat.product_catalog where nm ='DIA'), (select id from service_assurance.mst_service_catalog where name ='RFO Request'),'Y');


/* PI3-Sptint 1, 2 End*/  



