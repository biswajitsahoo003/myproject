package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.PartnerProductConfiguration;

/**
 * Repository class Partner Product Configuration Entity
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerProductConfigurationRepository extends JpaRepository<PartnerProductConfiguration, Integer> {

    /**
     * Get Partner Product Configuration by Partner Id
     *
     * @param partnerId
     * @return {@link List< PartnerProductConfiguration >}
     */
    @Query("SELECT p from PartnerProductConfiguration p where p.partnerId=:partnerId and isActive=1")
    List<PartnerProductConfiguration> findByPartnerId(@Param("partnerId") Integer partnerId);

}
