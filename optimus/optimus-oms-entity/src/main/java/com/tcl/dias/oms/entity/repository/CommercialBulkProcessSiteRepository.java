package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.CommercialBulkProcessSites;



/**
 * Repository class
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CommercialBulkProcessSiteRepository extends JpaRepository<CommercialBulkProcessSites, Integer> {

	List<CommercialBulkProcessSites> findByStatus(String string);
	
	List<CommercialBulkProcessSites> findByQuoteIdOrderByIdDesc(Integer quoteid);
	
	CommercialBulkProcessSites findBytaskId(Integer taskId);
	
}
