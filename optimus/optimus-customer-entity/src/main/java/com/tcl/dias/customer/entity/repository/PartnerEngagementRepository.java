package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;
import com.tcl.dias.customer.entity.entities.PartnerEngagement;

/**
 * Partner Engagement Entity Repository Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerEngagementRepository extends JpaRepository<PartnerEngagement, Integer> {
    List<PartnerEngagement> findByCustomerLegalEntity(CustomerLegalEntity customerLegalEntity);
}
