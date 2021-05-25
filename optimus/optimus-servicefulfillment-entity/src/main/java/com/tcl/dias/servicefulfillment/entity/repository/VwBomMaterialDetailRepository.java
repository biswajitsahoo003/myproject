package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.BomViewId;
import com.tcl.dias.servicefulfillment.entity.entities.VwBomMaterialDetail;

@Repository
public interface VwBomMaterialDetailRepository extends JpaRepository<VwBomMaterialDetail, BomViewId> {

	@Query(value = "SELECT * FROM vw_bom_material_detail where bom_type=:bomType and ll_bw_in_mbps >=:bandwidth", nativeQuery = true)
	List<VwBomMaterialDetail> findByBandwidthLessThan(@Param("bomType") String bomType,
			@Param("bandwidth") Double bandwidth);
	
	List<VwBomMaterialDetail> findByMake(String make);
	

	
	List<VwBomMaterialDetail> findByBomCode(String bomCode);
	
	List<VwBomMaterialDetail> findByBomType(String type);

	
	


}
