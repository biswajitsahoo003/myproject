package com.tcl.dias.location.entity.repository;

import com.tcl.dias.location.entity.entities.MstLocationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This file contains the MstLocationDataRepository.java class.
 *
 * @author Prakash G
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface MstLocationDataRepository extends JpaRepository<MstLocationData, Integer> {

    Optional<List<MstLocationData>> findByCountryName(String country);
}
