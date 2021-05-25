package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteAccessPermission;

/**
 * 
 * Repository class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteAccessPermissionRepository extends JpaRepository<QuoteAccessPermission, Integer> {

	List<QuoteAccessPermission> findByProductFamilyIdAndTypeAndRefId(Integer productFamilyId, String type,
			String refId);

}
