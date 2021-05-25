package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.serviceinventory.entity.entities.SdwanInventoryAudit;
import org.springframework.data.repository.query.Param;

public interface SdwanInventoryAuditRepository extends JpaRepository<SdwanInventoryAudit, Integer> {

	List<SdwanInventoryAudit> findByUserId(String userId);

	SdwanInventoryAudit findFirstByComponentNameAndComponentValueAndOperationOrderByIdDesc(
			@Param("componentName") String componentName, @Param("componentValue") String componentValue,
			@Param("operation") String operation);

    List<SdwanInventoryAudit> findByTaskIdIn(List<Integer> taskIds);

    List<SdwanInventoryAudit> findTop2000ByOrderByIdDesc();
    
    List<SdwanInventoryAudit> findTop2000ByCustomerIdOrderByCustomerIdDesc(Integer customerId);

}
