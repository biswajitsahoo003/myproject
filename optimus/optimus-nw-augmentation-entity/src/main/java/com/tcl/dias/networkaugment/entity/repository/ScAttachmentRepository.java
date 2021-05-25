package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.ScAttachment;
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
    
    List<ScAttachment> findAllByScServiceDetail_IdAndAttachment_categoryIn(Integer serviceId,List<String> categoryList);
    
	@Query(value="SELECT oa.id as sc_attachment_id,a.id as attachment_id FROM sc_attachment oa,attachment a where oa.attachment_id=a.id and a.type=:type and oa.service_code=:serviceCode",nativeQuery=true)
	Map<String,Object> findByServiceCodeAndType(@Param("serviceCode") String serviceCode,@Param("type") String type);

	@Query(value="SELECT oa.id as sc_attachment_id,a.id as attachment_id FROM sc_attachment oa,attachment a where oa.attachment_id=a.id and a.type=:type and oa.service_code=:erfOdrServiceId",nativeQuery=true)
	Map<String,Object> findByErfOdrServiceIdAndType(@Param("erfOdrServiceId") Integer serviceCode,@Param("type") String type);

    @Query(value="DELETE FROM sc_attachment sca,attachment a where sca.attachment_id=a.id and a.id=:attachmentId",nativeQuery=true)
    void deleteByAttachmentId(@Param("attachmentId") Integer  attachmentId);

    @Query(value= "select * from sc_attachment where attachment_id=:attachmentId",nativeQuery = true)
    List<ScAttachment> findByAtachmentId(@Param("attachmentId") Integer attachmentId);
}
