package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstDiscountDelegation;

@Repository
public interface MstDiscountDelegationRepository extends JpaRepository<MstDiscountDelegation, Integer> {

	List<MstDiscountDelegation> findByProductNameAndAttributeName(String productName,String attribute);
	List<MstDiscountDelegation> findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(String productName,String attribute,String type,Integer countryToRegId);
}
