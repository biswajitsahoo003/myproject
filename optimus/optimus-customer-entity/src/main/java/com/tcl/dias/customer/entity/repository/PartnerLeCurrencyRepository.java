package com.tcl.dias.customer.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.PartnerLeCurrency;

import java.util.List;

/**
 * Partner Le Currency Entity Repository Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerLeCurrencyRepository extends JpaRepository<PartnerLeCurrency, Integer> {

    @Query(value = "select cm.short_name from partner_le_currency pc left join currency_master cm on cm.id = pc.currency_id where pc.partner_le_id = :partnerLeId", nativeQuery = true)
    List<String> getPartnerLeCurrency(@Param("partnerLeId") Integer partnerLeId);
}
