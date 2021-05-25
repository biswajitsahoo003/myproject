package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.Appointment;
import com.tcl.dias.servicefulfillment.entity.entities.AppointmentDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
