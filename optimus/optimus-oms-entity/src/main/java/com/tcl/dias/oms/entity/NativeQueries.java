package com.tcl.dias.oms.entity;

/**
 * 
 * This file contains the NativeQueries.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NativeQueries {

	private NativeQueries() {

	}

	public static final String GET_CUSTOMERS_USG = "select distinct usgtocusle.erf_cus_customer_id from user_group_to_customer_le usgtocusle where usgtocusle.user_group_id=:userGroupId";

	public static final String GET_USG_BY_CUSTOMER = "SELECT distinct usgtocusle.user_group_id FROM user_group_to_customer_le usgtocusle where usgtocusle.erf_cus_customer_id=:customerId";

	public static final String GET_ORDER_GSC_IDS_BY_DOWNSTREAM_ORDER_ID = "select ogsc.id " + "from order_gsc ogsc, "
			+ "     order_product_component opc, " + "     order_product_components_attribute_values attr, "
			+ "     product_attribute_master pam " + "where pam.name = 'GSC_DOWNSTREAM_ORDER_ID' "
			+ "  and attr.attribute_id = pam.Id " + "  and attr.order_product_component_id = opc.id "
			+ "  and opc.type = 'ORDER_GSC' " + "  and ogsc.id = opc.reference_id "
			+ "  and ogsc.product_name = :productName " + "  and ogsc.access_type = :accessType "
			+ "and attr.attribute_values = :downstreamOrderId";

	public static final String GET_LATEST_MACD_TYPE_FOR_ORDER = "select order_category from order_to_le\n"
			+ "where erf_service_inventory_parent_order_id = :downstreamOrderId and stage <> 'ORDER_PROVISIONED'\n"
			+ "order by id desc limit 1";
	public static final String GET_ENGAGEMENT_DETAILS = "select a.erf_cus_customer_le_id as customerLeId, b.erf_prod_catalog_product_family_id as productFamilyId, b.name as productFamilyName\r\n" + 
			"from engagement a\r\n" + 
			"inner join mst_product_family b on a.product_family_id = b.id\r\n" + 
			"where a.erf_cus_customer_le_id in (:customerLeIds) and a.status = :status\r\n" + 
			"group by a.erf_cus_customer_le_id, a.product_family_id, b.name\r\n" + 
			"order by  b.name ASC ";
	
	public static final String GET_ENGAGEMENT_PROD_DETAILS = "select erf_prod_catalog_product_family_id as id , name from mst_product_family where name in (select distinct(b.name)\r\n" + 
			"from engagement a\r\n" + 
			"inner join mst_product_family b on a.product_family_id = b.id\r\n" + 
			"where a.erf_cus_customer_le_id in (:customerLeIds) and a.status = :status\r\n" + 
			"group by a.erf_cus_customer_le_id, a.product_family_id, b.name\r\n" + 
			"order by  b.name ASC )";

	public static final String GET_PARTNER_ENGAGEMENT_DETAILS = "select a.erf_cus_partner_le_id as partner_le_id, a.product_family_id as product_family_id, b.name as product_family_name\n" +
			"from engagement a\n" +
			"inner join mst_product_family b on a.product_family_id = b.id\n" +
			"where a.erf_cus_partner_le_id in (:partnerLeIds) and a.status = :status\n" +
			"group by a.erf_cus_partner_le_id, a.product_family_id, b.name\n" +
			"order by  b.name ASC;";
	
//	public static final String GET_PARTNER_USER_ENGAGEMENT = "select a.product_family_id as productId, b.name as productName, b.product_category as productCategory\n" +
//			"from engagement a\n" +
//			"inner join mst_product_family b on a.product_family_id = b.id\n" +
//			"where a.erf_cus_partner_le_id in (:partnerLeIds) and a.status = :status\n" +
//			"group by a.erf_cus_partner_le_id, a.product_family_id, b.name\n" +
//			"order by  b.name ASC;";
	
	public static final String GET_PARTNER_USER_ENGAGEMENTS = "select erf_prod_catalog_product_family_id as productId , product_category as productCategory, name as productName from mst_product_family where name in (select distinct(b.name)  \r\n" + 
			"from engagement a \r\n" + 
			"inner join mst_product_family b on a.product_family_id = b.id \r\n" + 
			"where a.erf_cus_partner_le_id in (:partnerLeIds) and a.status =:status \r\n" + 
			"group by a.erf_cus_partner_le_id, a.product_family_id, b.name \r\n" + 
			"order by  b.name ASC )";
	
	public static final String GET_USER_ENGAGEMENTS = "select erf_prod_catalog_product_family_id as productId , product_category as productCategory, name as productName from mst_product_family where name in (select distinct(b.name) \r\n" + 
			"from engagement a\r\n" + 
			"inner join mst_product_family b on a.product_family_id = b.id\r\n" + 
			"where a.erf_cus_customer_le_id in (:customerLeIds) and a.status = :status\r\n" + 
			"group by a.erf_cus_customer_le_id, a.product_family_id, b.name\r\n" + 
			"order by  b.name ASC )";
}
