package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.MstLeAttribute;
import com.tcl.dias.customer.entity.entities.ServiceProviderLegalEntity;
import com.tcl.dias.customer.entity.entities.SpLeAttributeValue;

/**
 * This file contains the SpLeAttribute Values repository
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SpLeAttributeValueRepository extends JpaRepository<SpLeAttributeValue, Integer> {
	Optional<SpLeAttributeValue> findByServiceProviderLegalEntityAndMstLeAttribute(ServiceProviderLegalEntity spLegalEntity,
			MstLeAttribute mstLeAttribute);
	SpLeAttributeValue findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(Integer mstLeAttributeId, Integer spLeId);

	List<SpLeAttributeValue> findByServiceProviderLegalEntity(ServiceProviderLegalEntity sples);

}

