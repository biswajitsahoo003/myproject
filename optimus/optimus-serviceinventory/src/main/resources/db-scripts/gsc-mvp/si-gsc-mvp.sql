-- Need to execute this file in Service Inventory

update si_service_detail set source_country_code_repc=source_country_code,
destination_country_code_repc=destination_country_code
where gsc_order_sequence_id is not null;

update si_service_detail t1
inner join product_catalog_uat.location t2
on t2.tiger_srs_ctry_cd = t1.source_country_code_repc and t2.is_active_ind='Y'
set t1
.source_country_code = t2.iso_3_cd
where gsc_order_sequence_id is not null;

update si_service_detail t1
inner join product_catalog_uat.location t2
on t2.tiger_srs_ctry_cd = t1.destination_country_code_repc
and t2.is_active_ind='Y'
set t1
.destination_country_code = t2.iso_3_cd
where gsc_order_sequence_id is not null;

update si_service_detail set erf_prd_catalog_product_name = 'GSIP' where erf_prd_catalog_product_name = 'GSC';
update si_service_detail set access_type = 'Public IP' where access_type = 'PUBLIC IP';
update si_service_detail set erf_prd_catalog_offering_name = 'ACDTFS on Public IP' where erf_prd_catalog_offering_name = 'ACDTFS on PublicIP';
update si_product_reference set access_type = 'Public IP' where access_type = 'PUBLIC IP';
update si_service_detail set destination_country = NULL, destination_country_code = NULL, destination_country_code_repc = NULL where access_type = 'Public IP' or access_type = 'GVPN';


update si_order set erf_cust_sp_le_id = '5', erf_cust_sp_le_name = 'Tata Communications (America) Inc.'
where erf_cust_le_name = 'Cognizant Technology Solutions U.S. Corporation' and erf_cust_le_id = '1124';



update si_service_detail set product_reference_id = '1' where erf_prd_catalog_offering_name = 'ITFS on Public IP';
update si_service_detail set product_reference_id = '2' where erf_prd_catalog_offering_name = 'ITFS on MPLS';
update si_service_detail set product_reference_id = '3' where erf_prd_catalog_offering_name = 'ITFS on PSTN';
update si_service_detail set product_reference_id = '4' where erf_prd_catalog_offering_name = 'LNS on Public IP';
update si_service_detail set product_reference_id = '5' where erf_prd_catalog_offering_name = 'LNS on MPLS';
update si_service_detail set product_reference_id = '6' where erf_prd_catalog_offering_name = 'LNS on PSTN';
update si_service_detail set product_reference_id = '7' where erf_prd_catalog_offering_name = 'UIFN on Public IP';
update si_service_detail set product_reference_id = '8' where erf_prd_catalog_offering_name = 'UIFN on MPLS';
update si_service_detail set product_reference_id = '9' where erf_prd_catalog_offering_name = 'UIFN on PSTN';
update si_service_detail set product_reference_id = '10' where erf_prd_catalog_offering_name = 'ACDTFS on Public IP';
update si_service_detail set product_reference_id = '11' where erf_prd_catalog_offering_name = 'ACDTFS on MPLS';
update si_service_detail set product_reference_id = '12' where erf_prd_catalog_offering_name = 'ACDTFS on PSTN';
update si_service_detail set product_reference_id = '13' where erf_prd_catalog_offering_name = 'ACANS on Public IP';
update si_service_detail set product_reference_id = '14' where erf_prd_catalog_offering_name = 'ACANS on MPLS';
update si_service_detail set product_reference_id = '15' where erf_prd_catalog_offering_name = 'ACANS on PSTN';




update si_order set erf_cust_le_id = 1124, erf_cust_le_name = 'Cognizant Technology Solutions U.S. Corporation'
where si_order.id in (select si_order_id
from si_service_detail
where erf_prd_catalog_offering_name = 'ITFS on Public IP');
update si_order set erf_cust_le_id = 1124, erf_cust_le_name = 'Cognizant Technology Solutions U.S. Corporation'
where si_order.id in (select si_order_id
from si_service_detail
where erf_prd_catalog_offering_name = 'LNS on Public IP');
update si_order set erf_cust_le_id = 1124, erf_cust_le_name = 'Cognizant Technology Solutions U.S. Corporation'
where si_order.id in (select si_order_id
from si_service_detail
where erf_prd_catalog_offering_name = 'UIFN on Public IP');
update si_order set erf_cust_le_id = 1124, erf_cust_le_name = 'Cognizant Technology Solutions U.S. Corporation'
where si_order.id in (select si_order_id
from si_service_detail
where erf_prd_catalog_offering_name = 'ITFS on PSTN');
update si_order set erf_cust_le_id = 1124, erf_cust_le_name = 'Cognizant Technology Solutions U.S. Corporation'
where si_order.id in (select si_order_id
from si_service_detail
where erf_prd_catalog_offering_name = 'LNS on PSTN');
update si_order set erf_cust_le_id = 1124, erf_cust_le_name = 'Cognizant Technology Solutions U.S. Corporation'
where si_order.id in (select si_order_id
from si_service_detail
where erf_prd_catalog_offering_name = 'UIFN on PPSTN');



update si_order set tps_secs_id = 7023 where erf_cust_le_id = 1124;
update si_order set tps_secs_id = 24071 where erf_cust_le_id = 536;