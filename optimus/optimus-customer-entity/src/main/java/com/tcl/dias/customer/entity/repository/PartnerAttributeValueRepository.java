package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.PartnerAttributeValue;

/**
 * Partner Attribute Values Entity Repository Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerAttributeValueRepository extends JpaRepository<PartnerAttributeValue, Integer> {

    /**
     * Find By Partner Id
     *
     * @param partnerId
     * @return
     */
    List<PartnerAttributeValue> findByPartnerId(Integer partnerId);

    PartnerAttributeValue findByPartnerIdAndMstCustomerSpAttributeId(Integer partnerId, Integer id);
}
