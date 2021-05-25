package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.CommercialQuoteAudit;
import com.tcl.dias.oms.entity.entities.Customer;

/**
 * Repository class
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CommercialQuoteAuditRepository extends JpaRepository<CommercialQuoteAudit, Integer> {
	public List<CommercialQuoteAudit> findByQuoteId(Integer quoteid);
}
