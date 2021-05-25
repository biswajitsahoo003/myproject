package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
