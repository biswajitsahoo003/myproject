package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;

/**
 * 
 * Repository class for ScChargeLineitem - for Auditing entries like Account,charge lineitem for billing
 * 
 *
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ScChargeLineitemRepository extends JpaRepository<ScChargeLineitem, Integer>{

	List<ScChargeLineitem> findByServiceIdAndServiceType(String serviceId,String serviceType);
	
	@Query(value = "select * from sc_charge_lineitems where service_id=:serviceId and service_type=:serviceType order by id desc limit 1",nativeQuery=true)
	ScChargeLineitem findByServiceIdAndServiceTypeforInvoice(String serviceId,String serviceType);
	
	List<ScChargeLineitem> findByServiceId(String serviceId);
	
	List<ScChargeLineitem> findByServiceCodeAndServiceType(String serviceCode,String serviceType);
	
	ScChargeLineitem findFirstByChargeLineitemAndServiceCode(String productName,String serviceCode);
	
	ScChargeLineitem findFirstByCloudCode(String productName);
	
	@Query(value = "select distinct account_number as count from sc_charge_lineitems where service_id=:serviceId and service_type=:serviceType ",nativeQuery=true)
	Set<String> findByTotalAccountNumbers(String serviceId,String serviceType);
	
	List<ScChargeLineitem> findByServiceIdAndServiceTypeAndAccountNumber(String serviceId,String serviceType,String accountNo);
	
	@Modifying
	@Query(value = "update sc_charge_lineitems set account_number=:accountNumber , billing_type=:billingType  where service_id=:serviceId and service_type=:serviceType and site_type=:siteType ", nativeQuery = true)
	@Transactional
	void updateAccountNumberNPL(@Param("accountNumber") String accountNumber,@Param("billingType") String billingType, @Param("serviceId") String serviceId,
			@Param("serviceType") String serviceType, @Param("siteType") String siteType);
	
	@Modifying
	@Query(value = "update sc_charge_lineitems set account_number=:accountNumber where service_id=:serviceId and service_type=:serviceType ", nativeQuery = true)
	@Transactional
	void updateAccountNumber(@Param("accountNumber") String accountNumber, @Param("serviceId") String serviceId,
			@Param("serviceType") String serviceType);
	
	@Modifying
	@Query(value = "update sc_charge_lineitems set account_number=:accountNumber where service_id=:serviceId and service_type=:serviceType and site_type=:siteType", nativeQuery = true)
	@Transactional
	void updateAccountNumberCpe(@Param("accountNumber") String accountNumber, @Param("serviceId") String serviceId,
			@Param("serviceType") String serviceType, @Param("siteType") String siteType);
	
	@Modifying
	@Query(value = "delete from sc_charge_lineitems where service_id=:serviceId", nativeQuery = true)
	@Transactional
	void deleteLineItems(@Param("serviceId") String serviceId);
	
	@Query(value = "select * from sc_charge_lineitems where service_id=:serviceId and service_type=:serviceType and account_number=:accountNumber and (commissioning_flag is null or commissioning_flag='N') and (site_type is null or site_type=:siteType) ",nativeQuery=true)
	List<ScChargeLineitem> findByServiceIdAndServiceTypeAndCommissioningFlag(@Param("serviceId") String serviceId,@Param("serviceType") String serviceType,@Param("accountNumber") String accountNumber,@Param("siteType") String siteType);

	
	@Query(value = "select * from sc_charge_lineitems where service_id=:serviceId and service_type=:serviceType and account_number=:accountNumber and (commissioning_flag is null or commissioning_flag='N')",nativeQuery=true)
    List<ScChargeLineitem> findByServiceIdAndServiceTypeAndCommissioningFlagNPL(@Param("serviceId") String serviceId,@Param("serviceType") String serviceType,@Param("accountNumber") String accountNumber);
	
	@Query(value = "select * from sc_charge_lineitems where service_type=:serviceType",nativeQuery=true)
	List<ScChargeLineitem> findByServiceType(@Param("serviceType") String serviceType);
	
	ScChargeLineitem findFirstBySourceProdSequence(String sequence);
	
	@Query(value = "select * from sc_charge_lineitems where service_id=:serviceId and service_type=:serviceType and service_termination_flag='N'",nativeQuery=true)
	ScChargeLineitem findFirstByServiceTermination(@Param("serviceId") String serviceId, @Param("serviceType") String serviceType);
	
	@Modifying
	@Query(value = "update sc_charge_lineitems set account_number=:accountNumber  where service_id=:serviceId and service_type=:serviceType and billing_type=:billingType", nativeQuery = true)
	@Transactional
	void updateAccountNumberIzoSdWan(@Param("accountNumber") String accountNumber, @Param("serviceId") String serviceId,
			@Param("serviceType") String serviceType, @Param("billingType") String billingType);
	
	ScChargeLineitem findFirstByInputGroupId(String inputGroupId);
	
	List<ScChargeLineitem> findByServiceIdAndServiceTypeAndSiteType(String serviceId,String serviceType,String siteType);
	
	@Transactional
	@Modifying(clearAutomatically=true, flushAutomatically=true)
	@Query(value = "update sc_charge_lineitems set input_group_id=:inputGroupId where service_id=:serviceId and service_type=:serviceType and (site_type is null or site_type=:siteType) ", nativeQuery = true)
	void updateInputGroupId(@Param("inputGroupId") String inputGroupId, @Param("serviceId") String serviceId,@Param("serviceType") String serviceType,@Param("siteType") String siteType);
	
	@Modifying(clearAutomatically=true, flushAutomatically=true)
	@Query(value = "update sc_charge_lineitems set account_number=:accountNumber where input_group_id=:inputGroupId", nativeQuery = true)
	void updateAccountNumberForGroupId(@Param("accountNumber") String accountNumber, @Param("inputGroupId") String inputGroupId);

	@Query(value = "select * from sc_charge_lineitems where service_id=:serviceId and service_type=:serviceType and charge_lineitem='Cancellation Charges' order by id desc limit 1",nativeQuery=true)
    ScChargeLineitem findFirstByServiceCancellation(@Param("serviceId") String serviceId, @Param("serviceType") String serviceType);
	
	@Query(value = "select * from sc_charge_lineitems where service_id=:serviceId and service_type=:serviceType and charge_lineitem='Cancellation Charges' and (commissioning_flag is null or commissioning_flag='N') order by id desc",nativeQuery=true)
    List<ScChargeLineitem> findAllForServiceCancellationProductCommissioning(@Param("serviceId") String serviceId, @Param("serviceType") String serviceType);
	
	@Query(value = "select * from sc_charge_lineitems where service_id=:serviceId and service_type=:serviceType and charge_lineitem='Cancellation Charges' and service_termination_flag='N' and status='SUCCESS' order by id desc",nativeQuery=true)
    List<ScChargeLineitem> findAllForServiceCancellationProductTermination(@Param("serviceId") String serviceId, @Param("serviceType") String serviceType);
	
	List<ScChargeLineitem> findByInputGroupId(String inputGroupId);
	
	@Query(value = "select count(*) from sc_charge_lineitems where service_id=:serviceId and service_type=:serviceType and commissioning_flag='Y' ",nativeQuery=true)
	String findByServiceIdAndCommissioningFlag(@Param("serviceId") String serviceId,@Param("serviceType") String serviceType);

}
