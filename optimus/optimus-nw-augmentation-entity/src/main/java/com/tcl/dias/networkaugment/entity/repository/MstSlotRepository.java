package com.tcl.dias.networkaugment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.MstAppointmentSlots;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 * used for the master related slot details
 *
 */
@Repository
public interface MstSlotRepository extends JpaRepository<MstAppointmentSlots, Integer> {

}
