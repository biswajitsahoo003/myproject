package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.IpDummyDetail;

/**
 * This file contains the IpDummyDetailRepository.java Repository class.
 *
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface IpDummyDetailRepository extends JpaRepository<IpDummyDetail, Integer> {

	IpDummyDetail findFirstByServiceDetail_IdOrderByIdDesc(Integer serviceId);

}
