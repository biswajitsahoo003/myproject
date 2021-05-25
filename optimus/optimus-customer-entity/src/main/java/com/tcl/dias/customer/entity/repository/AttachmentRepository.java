package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.Attachment;

/**
 * This class contains repository to store the attachment information
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Lim
 */
@Repository

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {

	List<Attachment> findByIdIn(List<Integer> attachmentIds);

}
