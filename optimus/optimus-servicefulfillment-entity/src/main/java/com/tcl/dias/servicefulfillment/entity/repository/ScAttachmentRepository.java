package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * This file contains the ScAttachmentRepository.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface ScAttachmentRepository extends JpaRepository<ScAttachment, Integer> {

    List<ScAttachment> findAllByScServiceDetail_Id(Integer serviceId);
    
    ScAttachment findFirstByScServiceDetail_IdAndAttachment_categoryAndSiteTypeOrderByIdDesc(Integer serviceId,String category, String siteType);
    
    ScAttachment findFirstByScServiceDetail_IdAndAttachment_categoryOrderByIdDesc(Integer serviceId,String category);
    
    List<ScAttachment> findAllByScServiceDetail_IdAndAttachment_categoryIn(Integer serviceId,List<String> categoryList);
    
    List<ScAttachment> findAllByScServiceDetail_IdAndAttachment_categoryNotIn(Integer serviceId,List<String> categoryList);
    
    List<ScAttachment> findAllByScServiceDetail_IdAndAttachment(Integer serviceId,Attachment attachment);
    
	List<ScAttachment> findAllByScServiceDetail_IdAndAttachment_category(Integer serviceId, String category);
    
    List<ScAttachment> findAllByScServiceDetail_IdAndAttachment_Id(Integer serviceId, Integer attachmentId);
    
	@Query(value="SELECT oa.id as sc_attachment_id,a.id as attachment_id FROM sc_attachment oa,attachment a where oa.attachment_id=a.id and a.type=:type and oa.service_code=:serviceCode",nativeQuery=true)
	Map<String,Object> findByServiceCodeAndType(@Param("serviceCode") String serviceCode,@Param("type") String type);

	@Query(value="SELECT oa.id as sc_attachment_id,a.id as attachment_id FROM sc_attachment oa,attachment a where oa.attachment_id=a.id and a.type=:type and oa.service_id=:serviceId",nativeQuery=true)
	Map<String,Object> findByServiceIdAndType(@Param("serviceId") Integer serviceId,@Param("type") String type);
	
	
	@Query(value="SELECT oa.id as sc_attachment_id,a.id as attachment_id FROM sc_attachment oa,attachment a where oa.attachment_id=a.id and a.type=:type and oa.service_code=:erfOdrServiceId",nativeQuery=true)
	Map<String,Object> findByErfOdrServiceIdAndType(@Param("erfOdrServiceId") Integer serviceCode,@Param("type") String type);

	List<ScAttachment> findAllByScServiceDetail_IdAndSiteIdAndAttachment(Integer serviceId,Integer siteId,Attachment attachment);
}
