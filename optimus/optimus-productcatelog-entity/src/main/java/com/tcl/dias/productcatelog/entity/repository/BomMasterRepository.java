package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.BomMaster;


/**
 * Repository class for database operations related to bom_master
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface BomMasterRepository extends JpaRepository<BomMaster, Integer>{
	
	BomMaster findByBomName(String bomName);
	

	@Query(value = "select bm.* from bom_master bm \r\n" +
			"inner join bom_physical_resource_assoc pra on pra.bom_id = bm.id \r\n" +
			"inner join physical_resource pr on pr.id = pra.physical_resource_id and pr.is_applicable_ntw_product = 'Y' where bm.bom_name =:cpe_name", nativeQuery = true)
	BomMaster findByBomNameAndIsApplicalbeNtwProduct(@Param("cpe_name")  String cpeName);
}
