package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;

/**
 * 
 * Repository class for ScIpcChargeLineitem - for Auditing entries like Account,charge lineitem for billing
 * 
 *
 * @author ram
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface IpcChargeLineitemRepository extends JpaRepository<IpcChargeLineitem, Integer>{

	List<IpcChargeLineitem> findByServiceIdAndServiceType(String serviceId,String serviceType);
	
	List<IpcChargeLineitem> findByServiceCodeAndServiceType(String serviceCode,String serviceType);
	
	List<IpcChargeLineitem> findByServiceCodeAndVersionAndActionType(String serviceCode, Integer version, String actionType);
	
	@Query("select coalesce(max(ipc.version),0) from IpcChargeLineitem ipc where ipc.serviceCode = :serviceCode and ipc.actionType = :actionType")
	Integer findLatestVersionByServiceCodeAndActionType(String serviceCode, String actionType);

	@Modifying
	@Query(value = "update ipc_charge_lineitems set account_number=:accountNumber where service_id=:serviceId and service_type=:serviceType", nativeQuery = true)
	@Transactional
	void updateAccountNumber(@Param("accountNumber") String accountNumber, @Param("serviceId") String serviceId,@Param("serviceType") String serviceType);
	
	List<IpcChargeLineitem> findByServiceTypeAndSourceProductSequence(String serviceType, Integer sequence);
	
	List<IpcChargeLineitem> findByServiceCodeAndActionTypeAndStatus(String serviceCode, String actionType, String status);

	List<IpcChargeLineitem> findByServiceId(String serviceId);
	
	List<IpcChargeLineitem> findByServiceCodeAndServiceTypeAndActionTypeAndStatusAndCommissionedFlag(String serviceCode,String serviceType, String actionType, String status, String commissionedFlag);
	
	List<IpcChargeLineitem> findByServiceCodeAndServiceTypeAndActionTypeAndStatusAndTerminatedFlag(String serviceCode,String serviceType, String actionType, String status, String terminatedFlag);

	List<IpcChargeLineitem> findByServiceIdAndStatus(String serviceId, String status);
	
}
