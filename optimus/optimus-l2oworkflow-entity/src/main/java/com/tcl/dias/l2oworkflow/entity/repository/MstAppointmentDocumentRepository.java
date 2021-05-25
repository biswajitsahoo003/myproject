package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.MstAppointmentDocuments;

/**
 * Repository interface for MstAppointmentDocuments.class.
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstAppointmentDocumentRepository extends JpaRepository<MstAppointmentDocuments, Integer> {
    List<MstAppointmentDocuments> findByIdIn(List<Integer> mstDocIds);
}
