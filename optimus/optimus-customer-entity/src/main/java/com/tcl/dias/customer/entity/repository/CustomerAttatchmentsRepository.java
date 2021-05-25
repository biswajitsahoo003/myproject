package com.tcl.dias.customer.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.customer.entity.entities.CustomerAttachment;
/**
 * 
 * This file contains the CustomerAttatchmentsRepository.java class.
 * 
 *
 * @author Kishore Nagarajan
 */
public interface CustomerAttatchmentsRepository extends JpaRepository<CustomerAttachment, Integer>{
	
	CustomerAttachment findByAttachmentName(String attachmentName);
	

}
