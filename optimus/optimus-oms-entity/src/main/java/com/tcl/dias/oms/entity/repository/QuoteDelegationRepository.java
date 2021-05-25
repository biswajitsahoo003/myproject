package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * This file contains the QuoteDelegationRepository.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteDelegationRepository extends JpaRepository<QuoteDelegation, Integer> {

	Optional<QuoteDelegation> findByAssignToAndStatus(Integer userId, String open);

	Optional<QuoteDelegation> findByQuoteToLeAndAssignToAndStatus(QuoteToLe quoteToLe, Integer userId, String open);

	List<QuoteDelegation> findByQuoteToLe(QuoteToLe quoteToLe);

}
