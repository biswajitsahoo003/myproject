package com.tcl.dias.customer.entity.repository;

import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;
import com.tcl.dias.customer.entity.entities.LeCcaAddress;
import com.tcl.dias.customer.entity.entities.LeStateGst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * Repo Class
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface LeCcaAddressRepository  extends JpaRepository<LeCcaAddress, Integer> {

    List<LeCcaAddress> findByCustomerLegalEntity(CustomerLegalEntity customerLegalEntity);
    List<LeCcaAddress> findByMdmAddressId(String mdmAddressId);

}
