package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class for Partner
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
	
	Partner findByIdAndStatus(Integer partnerId, Byte status);

    /**
     * Find Partner By Erf_Cus_Partner
     *
     * @param erfCustomerId
     * @param status
     * @return {@link Partner}
     */
    Partner findByErfCusPartnerIdAndStatus(Integer erfCustomerId, Byte status);

    /**
     * Find Partner By Erf Partner ID
     *
     * @param partnerId
     * @return {@link Partner}
     */
    Optional<Partner> findByErfCusPartnerId(Integer partnerId);

    /**
     * Find Partner by name
     *
     * @param partnerName
     * @return {@link Partner}
     */
    Optional<Partner> findByPartnerName(String partnerName);
}
