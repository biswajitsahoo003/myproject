package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Set;

import com.tcl.dias.oms.entity.entities.OdrOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;

/**
 * 
 * This file contains repository class of OdrServiceDetail entity
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrServiceDetailRepository extends JpaRepository<OdrServiceDetail, Integer> {

	OdrServiceDetail findByServiceRefIdAndPrimarySecondary(String serviceRefId, String type);
	
	Set<OdrServiceDetail> findByServiceRefId(String serviceRefId);

	Set<OdrServiceDetail> findByodrOrder(OdrOrder odrOrder);
	
	OdrServiceDetail findTopByOrderByIdDesc();

	@Query(value = "select * from odr_service_detail where uuid=:uuid order by id desc", nativeQuery = true)
	List<OdrServiceDetail> findByUuid(String uuid);
	
	List<OdrServiceDetail> findByOdrOrderAndErfPrdCatalogProductName(OdrOrder odrOrder,String productName);
	
	List<OdrServiceDetail> findByOdrOrderAndErfPrdCatalogProductNameIn(OdrOrder odrOrder,List<String> productNameList);

	List<OdrServiceDetail> findByUuidInAndServiceStatusNotIn(List<String> serviceIds, String serviceStatus);

	OdrServiceDetail findByOdrOrderUuidAndPopSiteCode(String orderCode, String popSiteCode);

}
