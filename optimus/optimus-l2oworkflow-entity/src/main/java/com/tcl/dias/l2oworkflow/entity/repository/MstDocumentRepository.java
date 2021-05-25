package com.tcl.dias.l2oworkflow.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.MstAppointmentDocuments;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used for  repository calls related to MstAppointmentDocuments
 */
@Repository
public interface MstDocumentRepository extends JpaRepository<MstAppointmentDocuments, Integer> {

}
