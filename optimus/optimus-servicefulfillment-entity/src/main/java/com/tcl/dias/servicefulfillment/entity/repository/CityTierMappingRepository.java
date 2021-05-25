package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.CityTierMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityTierMappingRepository  extends JpaRepository<CityTierMapping, Integer> {
    CityTierMapping findByCityName(String cityName);
}
