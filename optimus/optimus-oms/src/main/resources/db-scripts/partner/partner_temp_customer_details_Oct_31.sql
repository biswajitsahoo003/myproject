use oms_uat;

CREATE TABLE `partner_temp_customer_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(200) DEFAULT NULL,
  `industry` varchar(100) DEFAULT NULL,
  `sub_industry` varchar(100) DEFAULT NULL,
  `industry_subtype` varchar(100) DEFAULT NULL,
  `customer_website` varchar(100) DEFAULT NULL,
  `registration_no` varchar(50) DEFAULT NULL,
  `business_type` varchar(100) DEFAULT NULL,
  `erf_partner_id` varchar(10) DEFAULT NULL,
  `secs_id` varchar(10) DEFAULT NULL,
  `org_id` varchar(10) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



alter table opportunity change expected_arc expected_mrc double null;


alter table partner_temp_customer_details
	add customer_contact_number varchar(10) null;

alter table partner_temp_customer_details
	add third_party_service_job_reference_id varchar(10) null;

alter table customer_legal_entity_id
	add customer_contact_number varchar(10) null;


