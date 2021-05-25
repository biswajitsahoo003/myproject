use oms;

-- Master product_family data
insert into mst_product_family (name, status)
values ('UCAAS', 1);

-- Master product_offerings data
insert into mst_product_offerings (product_family_id, product_name, product_description, status, created_by,
                                   created_time)
values (8, 'LNS on PSTN', 'LNS on PSTN', 1, 'ucaas', current_timestamp());
insert into mst_product_offerings (product_family_id, product_name, product_description, status, created_by,
                                   created_time)
values (8, 'ITFS on PSTN', 'ITFS on PSTN', 1, 'ucaas', current_timestamp());
insert into mst_product_offerings (product_family_id, product_name, product_description, status, created_by,
                                   created_time)
values (8, 'Global Outbound on PSTN', 'Global Outbound on PSTN', 1, 'ucaas', current_timestamp());
insert into mst_product_offerings (product_family_id, product_name, product_description, status, created_by,
                                   created_time)
values (8, 'ITFS on MPLS', 'ITFS on MPLS', 1, 'ucaas', current_timestamp());
insert into mst_product_offerings (product_family_id, product_name, product_description, status, created_by,
                                   created_time)
values (8, 'LNS on MPLS', 'LNS on MPLS', 1, 'ucaas', current_timestamp());
insert into mst_product_offerings (product_family_id, product_name, product_description, status, created_by,
                                   created_time)
values (8, 'Global Outbound on MPLS', 'Global Outbound on MPLS', 1, 'ucaas', current_timestamp());
insert into mst_product_offerings (product_family_id, product_name, product_description, status, created_by,
                                   created_time)
values (8, 'WEBEX', 'WEBEX', 1, 'ucaas', current_timestamp());

