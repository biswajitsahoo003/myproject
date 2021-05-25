package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.custom.IScTeamsDRServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ScTeamsDRServiceCommercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file contains the ScTeamsDRServiceCommercialRepository.java class.
 *
 * @author SyedAli
 */

@Repository
public interface ScTeamsDRServiceCommercialRepository extends JpaRepository<ScTeamsDRServiceCommercial, Integer> {
	List<ScTeamsDRServiceCommercial> findByScServiceDetailId(Integer serviceId);

	List<ScTeamsDRServiceCommercial> findByScServiceDetailIdAndComponentTypeIn(Integer serviceId,
			List<String> componentTypes);

	List<ScTeamsDRServiceCommercial> findByScServiceDetailIdAndComponentNameAndComponentTypeIn(Integer serviceId,
			String componentName,List<String> componentTypes);

	@Query(value = "SELECT wsc.component_name AS componentName,wsc.charge_item AS componentDesc,wsc.component_type AS componentType, wsc.quantity AS quantity, wsc.mrc AS mrc, wsc.nrc AS nrc, wsc.arc AS arc, wsc.effective_usage AS effectiveUsage, wsc.effective_overage AS effectiveOverage, wsc.hsn_code AS hsnCode,mcc.vendor_name AS vendorName, mcc.sale_material_code AS saleMaterialCode, mcc.rental_material_code AS rentalMaterialCode, mcc.service_number AS serviceNumber,  wsc.contract_type AS contractType FROM sc_teamsdr_service_commercial wsc LEFT JOIN mst_cost_catalogue mcc ON mcc.bundled_bom=wsc.component_name WHERE wsc.sc_service_detail_id=:serviceId AND wsc.component_type IN (:componentType)", nativeQuery = true)
	List<IScTeamsDRServiceCommercial> getByScServiceDetailIdAndComponentType(@Param("serviceId") Integer serviceId,
			@Param("componentType") List<String> componentType);
}
