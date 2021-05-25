package com.tcl.dias.customer.entity.repository;

import com.tcl.dias.customer.entity.entities.PartnerEndCustomerMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public interface PartnerEndCustomerMappingRepository extends JpaRepository<PartnerEndCustomerMapping, Integer> {

    List<PartnerEndCustomerMapping> findByPartnerId(Integer partnerId);

    PartnerEndCustomerMapping findByEndCustomerCuid(String endCustomerCuid);
}
