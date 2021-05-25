package com.tcl.dias.networkaugment.entity.repository;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.Vendors;

/**
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface VendorsRepository extends JpaRepository<Vendors, Integer> {

	@Query(value = "select v.id,v.phone_number,v.name,v.service_id,v.email,v.task_id,v.mst_vendor_id,mv.name as vendorName,mv.type as vendorType, mv.city as vendorCity,mv.country as vendorCountry,mv.status as vendorStatus,mv.phone_number as mvPhone ,mv.email as mvEmail from vendors v,mst_vendors mv where v.mst_vendor_id=mv.id and v.service_id=:serviceId and mv.type=:vendorType and mv.status=5 order By v.id DESC limit 1", nativeQuery = true)
	Map<String, Object> findByServiceId(@Param("serviceId") Integer serviceId, @Param("vendorType") String vendorType);
}
