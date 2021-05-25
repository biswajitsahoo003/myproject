package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.BomPhysicalResourceAssoc;
/**
 * 
 * This file contains the BomPhysicalResourceAssocRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface BomPhysicalResourceAssocRepository extends JpaRepository<BomPhysicalResourceAssoc, Integer>{

	@Query(value="SELECT * FROM bom_physical_resource_assoc where bom_name_cd=:bomName order by relation like 'Main%' desc",nativeQuery = true)
	List<BomPhysicalResourceAssoc> getPhysicalResourcesMapped(String bomName);
}
