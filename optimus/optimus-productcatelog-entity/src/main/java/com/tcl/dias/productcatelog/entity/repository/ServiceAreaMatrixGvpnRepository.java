package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGVPN;

/**
 * Repository class for to retrieve service area matrix details for GVPN
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface ServiceAreaMatrixGvpnRepository extends JpaRepository<ServiceAreaMatrixGVPN, Integer> {

	Optional<ServiceAreaMatrixGVPN> findByLocationId(String locationId);

	List<ServiceAreaMatrixGVPN> findByCityNmContainingIgnoreCase(String cityName);
}
