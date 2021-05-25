package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.Appointment;
import com.tcl.dias.networkaugment.entity.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

/**
 * AppointmentRepository - Used to save customer appointment details
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Optional<Appointment> findByTask(Task task);

    /**
     * used to fetch appointment by given service id and type in desc order.
     *
     * @param serviceId
     * @param type
     * @return
     */
    @Query(value = "SELECT a.*,mst.slots FROM appointment a,mst_appointment_slots mst where a.slot_id=mst.id and a.service_id=:serviceId and a.type=:type order By a.id DESC limit 1", nativeQuery = true)
    Map<String, Object> findByServiceId(@Param("serviceId") Integer serviceId, @Param("type") String type);

    Optional<Appointment> findFirstByServiceIdAndAppointmentTypeOrderByIdDesc(Integer serviceId, String type);


}
