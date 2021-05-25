package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.custom.IScWebexServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ScWebexServiceCommercial;

/**
 * 
 * This file contains the ScWebexServiceCommercialRepository.java class.
 * 
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Repository
public interface ScWebexServiceCommercialRepository extends JpaRepository<ScWebexServiceCommercial, Integer> {

	 List<ScWebexServiceCommercial> findByScServiceDetailIdAndComponentType(Integer serviceId, String componentType);
	 
	 ScWebexServiceCommercial findFirstByScServiceDetailIdAndComponentType(Integer serviceId, String componentType);
	 
	 List<ScWebexServiceCommercial> findByScServiceDetailIdAndComponentTypeIn(Integer serviceId, List<String> componentType);
	 
	 List<ScWebexServiceCommercial> findByScServiceDetailId(Integer serviceId);
	 
	 @Query(value = "SELECT wsc.component_name AS componentName,wsc.component_desc AS componentDesc,wsc.component_type AS componentType, wsc.quantity AS quantity, wsc.cisco_unit_list_price AS ciscoUnitListPrice, wsc.cisco_discnt_prct AS ciscoDiscntPrct, wsc.cisco_unit_net_price AS ciscoUnitNetPrice, wsc.hsn_code AS hsnCode, mcc.sale_material_code AS saleMaterialCode, mcc.rental_material_code AS rentalMaterialCode, mcc.service_number AS serviceNumbe, if(mcc.short_text is null, wsc.short_text, mcc.short_text) AS shortText, wsc.contract_type AS contractType, wsc.endpoint_management_type AS supportType FROM sc_webex_service_commercial wsc LEFT JOIN mst_cost_catalogue mcc ON mcc.bundled_bom=wsc.component_name WHERE wsc.sc_service_detail_id=:serviceId AND wsc.component_type IN (:componentType)", nativeQuery = true)
	 List<IScWebexServiceCommercial> getByScServiceDetailIdAndComponentType(@Param("serviceId") Integer serviceId, @Param("componentType") List<String> componentType);
}
