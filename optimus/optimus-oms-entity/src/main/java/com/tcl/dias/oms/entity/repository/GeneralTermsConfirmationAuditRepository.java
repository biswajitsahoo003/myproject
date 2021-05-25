package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.GeneralTermsConfirmationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for GeneralTermsConfirmationAudit
 *
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface GeneralTermsConfirmationAuditRepository extends JpaRepository<GeneralTermsConfirmationAudit, Integer> {

    GeneralTermsConfirmationAudit findByName(String username);
}
