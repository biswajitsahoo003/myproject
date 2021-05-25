package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.Attachment;
import com.tcl.dias.serviceinventory.entity.entities.SIAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 *
 * This file contains the repository class for SIAttachment entity
 * 
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
	
}
