package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrAttachment;
/**
 * 
 * This file contains repository class of OdrAttachment entity
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrAttachmentRepository extends JpaRepository<OdrAttachment, Integer>{

	List<OdrAttachment> findByOdrServiceDetailId(Integer odrServiceId);

	@Query(value = "SELECT oa.id as odr_attachment_id,a.id as attachment_id  FROM odr_attachment oa,attachment a where oa.attachment_id=a.id and a.type=:type and oa.erf_odr_service_id=:erfOdrServiceId", nativeQuery = true)
	Map<String, Object> findByErfOdrServiceIdAndType(@Param("erfOdrServiceId") Integer erfOdrServiceId,
			@Param("type") String type);
	
	List<OdrAttachment> findByOrderIdAndAttachmentType(Integer orderId, String attachmentType);

	OdrAttachment findByServiceCodeAndProductNameOrderByIdDesc(String serviceCode, String productName);
}
