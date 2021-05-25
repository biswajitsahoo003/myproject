package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file contains the AttachmentRepository.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {

    List<Attachment> findAllByIdInOrderByIdDesc(List<Integer> attachmentIds);
    
    @Query(value="SELECT a.* FROM sc_attachment sa JOIN attachment a ON sa.attachment_id=a.id where sa.service_id=:serviceId and a.category=:category",nativeQuery=true)
    List<Attachment> findAllAttachmentsByCategory(Integer serviceId,String category);
}
