package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.Attachment;

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
