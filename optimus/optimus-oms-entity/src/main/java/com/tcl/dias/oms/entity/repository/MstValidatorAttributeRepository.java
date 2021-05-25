package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstValidatorAttribute;

/***
 * 
 * Repository Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstValidatorAttributeRepository extends JpaRepository<MstValidatorAttribute, Integer> {
	
	@Query(value = "SELECT a.node_name,a.type,a.x_path,b.name FROM mst_validator_attributes a,mst_product_family b where b.id=a.product_id", nativeQuery = true)
	public List<Map<String, Object>> findAllValidatorAttributes();

}
