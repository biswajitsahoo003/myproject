package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstAppointmentDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
