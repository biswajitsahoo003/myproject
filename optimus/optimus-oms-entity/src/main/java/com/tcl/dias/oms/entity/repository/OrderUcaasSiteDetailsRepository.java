package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.OrderUcaasSiteDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface OrderUcaasSiteDetailsRepository extends JpaRepository<OrderUcaasSiteDetails, Integer> {
    List<OrderUcaasSiteDetails> findByOrderProductSolutionId(Integer orderProductSolutionId);
    Optional<OrderUcaasSiteDetails> findByOrderProductSolutionIdAndEndpointLocationId(Integer orderProductSolutionId,
                                                                                      Integer endpointLocationId);
}