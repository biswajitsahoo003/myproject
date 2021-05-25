package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.CpeDeviceNameDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;

import java.util.List;

/**
 * 
 * This file contains the CpeDeviceNameDetailRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CpeDeviceNameDetailRepository extends JpaRepository<CpeDeviceNameDetail, Integer>{
	
	CpeDeviceNameDetail findFirstByCountryCodeAndCityAndErfCustCustomerIdOrderByIdDesc(String countryCode,String city,Integer customerId);
	
	CpeDeviceNameDetail findByScServiceDetailAndScComponent(ScServiceDetail scServiceDetail,ScComponent scComponent);

}
