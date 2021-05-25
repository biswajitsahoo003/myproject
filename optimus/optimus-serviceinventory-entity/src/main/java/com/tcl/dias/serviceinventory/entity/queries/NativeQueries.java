package com.tcl.dias.serviceinventory.entity.queries;

/**
 * This file contains the NativeQueries
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NativeQueries {

	private NativeQueries() {
	}

	public static final String FETCH_PRODUCT_FAMILYWISE_ASSET_COUNT_BY_TYPE = "select prodref.sub_variant sub_variant, count(*) as asset_count\n"
			+ "from si_order ord\n" + "       INNER JOIN si_service_detail sd ON sd.si_order_id = ord.id\n"
			+ "       INNER JOIN si_asset_to_service sda on sda.SI_service_detail_id = sd.id\n"
			+ "       INNER JOIN si_asset asst on asst.id = sda.SI_Asset_ID\n"
			+ "       INNER JOIN si_product_reference prodref on prodref.id = sd.product_reference_id\n"
			+ "where ord.erf_cust_le_id IN (:customerLeIds)\n"
			+ "  and sd.erf_prd_catalog_product_name = :productFamily\n"
			+ "  and asst.type = :assetType\n" + "  and sda.is_active = 'Y'\n" + "  and sd.is_active = 'Y'\n"
			+ "  and ord.is_active = 'Y'\n" + "  and prodref.is_active = 'Y'\n"
			+ "  and prodref.variant = :productFamily\n" + "group by prodref.sub_variant";

	public static final String FETCH_ASSET_BY_TYPE_AND_SERVICE_DETAIL_AND_RELATIONSHIP_TYPE = "select asst.id as asset_id\n"
			+ "from si_asset asst\n" + "       join si_asset_to_service atos on atos.SI_Asset_ID = asst.id\n"
			+ "       join si_service_detail sd on atos.SI_service_detail_id = sd.id\n"
			+ "where asst.type IN (:assetTypes)\n" + "  and sd.is_active = 'Y'\n" + "  and atos.is_active = 'Y'\n"
			+ "  and sd.id = :serviceDetailId\n" + "  and coalesce(asst.asset_status, 'Active') != 'Terminated'\n"
			+ "union\n" + "select rel.SI_Related_Asset_ID\n" + "from si_asset asst\n"
			+ "       join si_asset_to_service atos on atos.SI_Asset_ID = asst.id\n"
			+ "       join si_service_detail sd on atos.SI_service_detail_id = sd.id\n"
			+ "       join si_asset_relation rel on rel.SI_Asset_ID = asst.id\n" + "where asst.type IN (:assetTypes)\n"
			+ "  and sd.is_active = 'Y'\n" + "  and atos.is_active = 'Y'\n" + "  and rel.is_active = 'Y'\n"
			+ "  and sd.id = :serviceDetailId\n" + "  and coalesce(asst.asset_status, 'Active') != 'Terminated'\n"
			+ "  and rel.relation_type IN (:relationTypes);";

	public static final String FETCH_GVPN_SERVICE_DATA_QUERY = "select si_order_id                           orderId,\n"
			+ "       vpn_name                              vpnName,\n"
			+ "       site_address                          siteAddress,\n"
			+ "       concat(bw_portspeed, ' ', bw_unit) as portBandwidth\n" + "from si_service_detail sd\n"
			+ "       inner join si_order ord on ord.id = sd.si_order_id\n"
			+ "       inner join si_product_reference pref on pref.id = sd.product_reference_id\n"
			+ "where sd.is_active = 'Y'\n" + "  and ord.is_active = 'Y'\n" + "  and pref.is_active = 'Y'\n"
			+ "  and pref.access_type = 'GVPN'\n" + "  and pref.sub_variant = :productName order by sd.si_order_id";

	public static final String FETCH_GVPN_SERVICE_DATA_COUNT_QUERY = "select count(1)\n"
			+ "from si_service_detail sd\n" + "       inner join si_order ord on ord.id = sd.si_order_id\n"
			+ "       inner join si_product_reference pref on pref.id = sd.product_reference_id\n"
			+ "where sd.is_active = 'Y'\n" + "  and ord.is_active = 'Y'\n" + "  and pref.is_active = 'Y'\n"
			+ "  and pref.access_type = 'GVPN'\n" + "  and pref.sub_variant = :productName";

}
