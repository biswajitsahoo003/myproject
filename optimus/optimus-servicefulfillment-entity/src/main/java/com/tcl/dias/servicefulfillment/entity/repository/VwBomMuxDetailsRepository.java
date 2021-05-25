package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.BomViewId;
import com.tcl.dias.servicefulfillment.entity.entities.VwMuxBomMaterialDetail;

@Repository
public interface VwBomMuxDetailsRepository extends JpaRepository<VwMuxBomMaterialDetail, BomViewId> {

	@Query(value = "SELECT * FROM vw_mux_bom_material_detail where max_ll_bw_in_mbps >=:bandwidth and min_ll_bw_in_mbps <=:bandwidth", nativeQuery = true)
	List<VwMuxBomMaterialDetail> findByBandwidthLessThan(@Param("bandwidth") double bandwidth);

	@Query(value = "SELECT * FROM vw_mux_bom_material_detail where  max_ll_bw_in_mbps >=:bandwidth and min_ll_bw_in_mbps <=:bandwidth and make=:make", nativeQuery = true)
	List<VwMuxBomMaterialDetail> findByMakeBandwidthLessThan(@Param("bandwidth") double bandwidth,
			@Param("make") String make);
	
	List<VwMuxBomMaterialDetail> findByMake(String make);
	
	List<VwMuxBomMaterialDetail> findByBomCode(String bomCode);
	
	List<VwMuxBomMaterialDetail> findByBomName(String bomCode);



}
