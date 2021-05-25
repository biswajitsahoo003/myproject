package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.PartnerTempCustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for Temp Customer for Partner
 *
 * @author Diksha Sharma
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerTempCustomerDetailsRepository extends JpaRepository<PartnerTempCustomerDetails, Integer> {

    @Query(value = "SELECT * FROM partner_temp_customer_details WHERE country =:countryName and erf_partner_id=:partnerId and customer_name like :entityName%", nativeQuery = true)
    List<PartnerTempCustomerDetails> findTempCustomerDetails(@Param("countryName") String countryName, @Param("entityName") String entityName, @Param("partnerId") String partnerId);

    PartnerTempCustomerDetails findByCustomerName(String customerName);

    PartnerTempCustomerDetails findByThirdPartyServiceJobReferenceId(String referenceId);

    List<PartnerTempCustomerDetails> findByErfPartnerId(String erfPartnerId);
}
