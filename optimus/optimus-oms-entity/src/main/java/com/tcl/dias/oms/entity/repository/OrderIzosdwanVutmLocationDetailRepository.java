package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.OrderIzosdwanVutmLocationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 *
 * This file contains the OrderIzosdwanVutmLocationDetailRepository.java class.
 *
 *
 * @author Anway Bhutkar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderIzosdwanVutmLocationDetailRepository extends JpaRepository<OrderIzosdwanVutmLocationDetail, Integer> {
    List<OrderIzosdwanVutmLocationDetail> findByReferenceId(Integer referenceId);

    List<OrderIzosdwanVutmLocationDetail> findByReferenceIdAndBreakupLocation(Integer referenceId, String locationName);

    List<OrderIzosdwanVutmLocationDetail> findByReferenceIdAndBreakupLocationAndLocationIdNotIn(Integer referenceId,
                                                                                                String locationName, List<Integer> locations);

    List<OrderIzosdwanVutmLocationDetail> findByReferenceIdAndLocationIdNotIn(Integer referenceId, List<Integer> locations);
}
