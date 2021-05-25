package com.tcl.dias.customer.entity.repository;

import com.tcl.dias.customer.entity.entities.SpLeCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This file is the repository for ServiceProvider LegalEntity Currency Repository
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface ServiceProviderLegalEntityCurrencyRepository extends JpaRepository<SpLeCurrency, Integer> {

    /**
     * FInd Supplier Legal Country By Id
     *
     * @param supplierLegalId
     * @return {@link SpLeCurrency}
     */
    SpLeCurrency findByServiceProviderLegalEntity_IdAndIsDefault(Integer supplierLegalId, Byte isDefault);

}
