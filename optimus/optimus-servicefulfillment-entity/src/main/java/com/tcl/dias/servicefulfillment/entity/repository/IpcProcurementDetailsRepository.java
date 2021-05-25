package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.IpcProcurementDetails;

/**
 * @author Savanya
 *
 */
@Repository
public interface IpcProcurementDetailsRepository extends JpaRepository<IpcProcurementDetails, Integer>{

	/**
	 * @param scOrderId
	 * @return
	 */
	List<IpcProcurementDetails> findByScOrderId(Integer scOrderId);
}
