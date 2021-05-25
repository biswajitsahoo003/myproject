package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.Stg0SapPoDtlOptimus;

import java.util.List;
import java.util.Map;

/**
 * 
 * This file contains the Stg0SapPoDtlOptimusRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface Stg0SapPoDtlOptimusRepository extends JpaRepository<Stg0SapPoDtlOptimus, String>{
	@Query(value = "SELECT po.* FROM STG0_SAP_PO_DTL_OPTIMUS po where po.TCL_SERVICE_ID = :serviceCode AND (po.PRODUCT_COMPONENT='A_END_LM' OR po.PO_NUMBER like 'GS%') AND po.TERMINATION_TYPE IS NULL AND po.VENDOR_REF_ID_ORDER_ID IS NOT NULL ORDER BY CONCAT_WS('-', SUBSTRING(`PO_CREATION_DATE`, 7, 4), SUBSTRING(`PO_CREATION_DATE`, 4, 2), SUBSTRING(`PO_CREATION_DATE`, 1, 2)) DESC limit 1",nativeQuery = true)
	public Stg0SapPoDtlOptimus findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(@Param("serviceCode") String serviceCode);

	@Query(value = "SELECT po.* FROM STG0_SAP_PO_DTL_OPTIMUS po where po.TCL_SERVICE_ID = :serviceCode AND (po.PRODUCT_COMPONENT='B_END_LM' OR po.PO_NUMBER like 'GS%') AND po.TERMINATION_TYPE IS NULL AND po.VENDOR_REF_ID_ORDER_ID IS NOT NULL ORDER BY CONCAT_WS('-', SUBSTRING(`PO_CREATION_DATE`, 7, 4), SUBSTRING(`PO_CREATION_DATE`, 4, 2), SUBSTRING(`PO_CREATION_DATE`, 1, 2)) DESC limit 1",nativeQuery = true)
	public Stg0SapPoDtlOptimus getDataByServiceIdSiteB(@Param("serviceCode") String serviceCode);

	
	@Query(value="SELECT po.PO_NUMBER as oldOffnetPoNumber,po.CHILD_PO_NUMBER as childPoNumber,po.VENDOR_REF_ID_ORDER_ID as oldBsoCircuitId,po.TCL_SERVICE_ID as serviceId,po.PRODUCT_COMPONENT as productComponent,po.PO_CREATION_DATE as poCreationDate,po.VENDOR_NO as vendorId,v.name as vendorName, v.SFDC_PROVIDER_NAME__C as sfdcProviderName  FROM STG0_SAP_PO_DTL_OPTIMUS po left join STG0_SFDC_VENDOR__C v on po.VENDOR_NO=v.VENDOR_ID__C where po.TCL_SERVICE_ID = :tclServiceId AND (po.PRODUCT_COMPONENT='A_END_LM' OR po.PO_NUMBER like 'GS%') AND po.TERMINATION_TYPE IS NULL AND po.VENDOR_REF_ID_ORDER_ID IS NOT NULL ORDER BY CONCAT_WS('-', SUBSTRING(`PO_CREATION_DATE`, 7, 4), SUBSTRING(`PO_CREATION_DATE`, 4, 2), SUBSTRING(`PO_CREATION_DATE`, 1, 2)) DESC", nativeQuery=true)
	public List<Map<String,Object>> getDataByServiceId(@Param("tclServiceId") String tclServiceId);
	
	@Query(value = "SELECT * FROM STG0_SAP_PO_DTL_OPTIMUS WHERE VENDOR_REF_ID_ORDER_ID =:vendorRefIdOrderId AND TCL_SERVICE_ID =:tclServiceId AND TERMINATION_TYPE IS NULL AND VENDOR_REF_ID_ORDER_ID IS NOT NULL", nativeQuery=true)
	public List<Stg0SapPoDtlOptimus> findByVendorRefIdOrderIdAndTclServiceIdAndTerminationTypeIsNull(String vendorRefIdOrderId, String tclServiceId);
}
