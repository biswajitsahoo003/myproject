package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.IpAddressDetail;

/**
 * This file contains the IpAddressDetailRepository.java Repository class.
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface IpAddressDetailRepository extends JpaRepository<IpAddressDetail, Integer> {

	IpAddressDetail findByServiceDetailIdAndEndDateIsNull(Integer serviceId);

	IpAddressDetail findByServiceDetailId(Integer serviceId);
}
