package com.tcl.dias.productcatelog.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.WebexEndpointHsnCodeView;

/**
 * Repository for Webex endpoint HSN code view
 * 
 * @author Srinivasa Raghavan
 */
@Repository
public interface WebexEndpointHsnCodeViewRepository extends JpaRepository<WebexEndpointHsnCodeView, String> {

	/**
	 * Find HSN code for given SKU
	 *
	 * @param request
	 * @return
	 */
	WebexEndpointHsnCodeView findBySku(String request);
}
