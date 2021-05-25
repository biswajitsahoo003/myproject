package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderDirectRoutingCity;

/**
 * Repository class for order_dr_city table
 * 
 * @author Srinivasa Raghavan
 */
@Repository
public interface OrderDirectRoutingCityRepository extends JpaRepository<OrderDirectRoutingCity, Integer> {

	List<OrderDirectRoutingCity> findByOrderDirectRoutingId(Integer id);
}
