package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.Appointment;
import com.tcl.dias.l2oworkflow.entity.entities.AppointmentDocuments;

/**
 * Repository interface for AppointmentDocuments.class.
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface AppointmentDocumentsRepository extends JpaRepository<AppointmentDocuments, Integer> {
    List<AppointmentDocuments> findByAppointment(Appointment appointment);
}
