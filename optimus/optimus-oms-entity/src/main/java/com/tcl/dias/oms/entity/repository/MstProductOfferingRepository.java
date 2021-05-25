package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;

/**
 * This file contains the MstProductOfferingRepository.java class.
 * Repository class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstProductOfferingRepository  extends JpaRepository<MstProductOffering, Integer> {

	public MstProductOffering findByMstProductFamilyAndProductNameAndStatus(MstProductFamily mstProductFamily,String productName,byte status);
	
	public MstProductOffering findByMstProductFamilyAndProductNameAndStatusAndVendorCd(MstProductFamily mstProductFamily,String productName,byte status,String vendor);

	MstProductOffering findByProductNameAndStatus(String offeringName,byte status);



}
