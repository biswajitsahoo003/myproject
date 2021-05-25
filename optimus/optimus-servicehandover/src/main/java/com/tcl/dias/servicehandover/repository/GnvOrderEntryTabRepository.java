package com.tcl.dias.servicehandover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.servicehandover.entity.GnvOrderEntryTab;

/**
 * 
 * Repository class for GenevaIpcOrderEntry - for entries like Account,Order 
 * 
 *
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface GnvOrderEntryTabRepository extends JpaRepository<GnvOrderEntryTab, Integer>{

	@Query(value="SELECT DISTINCT I_CUSTOMER_REF from gnv_order_entry_tab where i_service_id=:serviceCode and REQUEST_TYPE='PRODUCT' AND STATUS='SUCCESS' AND ROWNUM=1 ",nativeQuery = true)
	String findFirstByIServiceIdAndRequestTypeAndStatus(@Param("serviceCode") String serviceCode);
	
	@Query(value="select distinct CUSTOMER_REF from VSNL_CORP.vsnl_corp_custdata where circuit_id=:serviceCode and REQUEST_STATUS='C' AND CUSTOMER_REF not like '%\\_%' ESCAPE '\\' order by status_dtm desc fetch first row only",nativeQuery = true)
	String findCustomerRef(@Param("serviceCode") String serviceCode);
	
	@Query(value="select source_product_seq from gnv_generic_order_entry where account_number=:account_number and product_name=:prod_name and status='SUCCESS' AND action_type='CREATE' order by 1 desc fetch first row only ",nativeQuery = true)
	String findSourceProdSeq(@Param("account_number") String accountNumber,@Param("prod_name") String productName);
	
	@Query(value="select ACCOUNT_NUMBER from gnv_generic_order_entry where input_group_id=:input_group_id and status='SUCCESS' order by 1 desc ",nativeQuery = true)
	String findByInputGroupId(@Param("input_group_id") String inputGroupId);
	
	@Query(value="select MAX(COMMISSION_DATE) from VSNL_CORP.vsnl_corp_custdata where circuit_id=:serviceCode and REQUEST_STATUS='C' AND CUSTOMER_REF not like '%\\_%' ESCAPE '\\' ",nativeQuery = true)
	String findNewOrderCommDate(@Param("serviceCode") String serviceCode);

	@Query(value="select account_number from gnv_generic_order_entry where input_group_id like :inpGroupIdRegex and request_type='ACCOUNT' and status='SUCCESS' order by input_row_id desc fetch first row only",nativeQuery = true)
	String findAccountNumberByInputGroupIdRegex(String inpGroupIdRegex);
	
	@Query(value="select status from gnv_generic_order_entry where service_id=:serviceCode and source_product_seq=:sourceProdSeq and action_type=:actionType",nativeQuery = true)
	String findStatusByServiceCodeAndSourceProdSeqAndActionType(String serviceCode, Integer sourceProdSeq, String actionType);
	
	@Query(value="select distinct event_source from GENEVA_ADMIN.custeventsource where event_source like '%'||NVL(:serviceCode,'$')||'%' and end_dtm is null fetch first row only",nativeQuery = true)
	String getEventSource(@Param("serviceCode") String serviceCode);
	
	@Query(value = "select CUSTOMER_REF from GENEVA_ADMIN.CUSTOMER where ROOT_CUSTOMER_REF=:secs and CUSTOMER_REF like '%'||UPPER(:tataEntity)||'%' ",nativeQuery = true)
	List<String> getIntlCustomerRef(@Param("secs") String secs, @Param("tataEntity") String tataEntity);
	
	@Query(value = "SELECT ACCOUNT_NUM from GENEVA_ADMIN.ACCOUNT where CUSTOMER_REF=:customerRef and BILLING_STATUS='OK' ",nativeQuery = true)
	List<String> getIntlAccountList(@Param("customerRef") String customerRef);
	
	@Query(value = "SELECT CPS_NAME FROM Contractedpointofsupply where CPS_NAME like '%'||initcap(:countryName)||'%' ",nativeQuery = true)
	List<String> getCpsList(@Param("countryName") String countryName);
	
	//@Query(value = "select TAX_SET_NAME from taxset where tax_set_name like '%'||initcap(:countryName)||'%' ",nativeQuery = true)
	@Query(value = "select TAX_SET_DESC from taxset where tax_set_name like '%'||NVL(:countryName,'$')||'%' and rownum=1",nativeQuery = true)
	List<String> getCpsListNew(@Param("countryName") String countryName);
	
	@Query(value="SELECT COMPANY_CODE from vsnl_corp.vsnl_corp_tcli_provider_matrix where ACTUAL_PROVIDER_NAME=:spLeName and BILLING_CURRENCY=:currency fetch first row only",nativeQuery = true)
	String getEntity(@Param("spLeName") String spLeName,@Param("currency") String currency);
	
	@Query(value="Select distinct input_group_id from gnv_generic_order_entry where COPF_ID=:orderCode order by INPUT_ROW_ID desc fetch first row only",nativeQuery = true)
	String getDevEnv(@Param("orderCode") String orderCode);
	
	@Query(value="SELECT count(*) as record_entry from gnv_order_entry_tab where i_service_id=:serviceCode and INPUT_GROUP_ID LIKE 'COMM_OPT%' and STATUS='SUCCESS'",nativeQuery = true)
	String checkRecord(@Param("serviceCode") String serviceCode);
	
	@Query(value = "select TAX_SET_DESC from taxset where tax_set_name like '%'||(:countryName)||'%' and rownum=1",nativeQuery = true)
	List<String> getCpsListForIpc(@Param("countryName") String countryName);
	
	@Query(value="SELECT COMPANY_CODE from vsnl_corp.vsnl_corp_tcli_provider_matrix where UPPER(ACTUAL_PROVIDER_NAME)=:spLeName and BILLING_CURRENCY=:currency fetch first row only",nativeQuery = true)
	String getEntityForIpcIntl(@Param("spLeName") String spLeName,@Param("currency") String currency);

	@Query(value="select count(*) from gnv_generic_order_entry where service_id=:serviceCode and copf_id=:orderCode AND action_type ='CREATE' and (status is null OR status <> 'FAILURE')",nativeQuery = true)
	String checkForDuplicateProducts(@Param("serviceCode") String serviceCode,@Param("orderCode") String orderCode);
	
	@Query(value="select status from gnv_generic_order_entry where service_id=:serviceCode and copf_id=:orderCode and service_type=:serviceType and action_type ='CREATE' order by input_row_id desc fetch first :prodSize row only",nativeQuery = true)
	List<String> checkForDuplicateProducts(@Param("serviceCode") String serviceCode,@Param("orderCode") String orderCode , @Param("serviceType") String serviceType, @Param("prodSize") String prodSize);
	
}
