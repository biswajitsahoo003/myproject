package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

	Optional<Location> findById(Integer id);
	
	@Query(value = "SELECT * FROM location  where nm like :countryName and is_active_ind='Y'",nativeQuery = true)
	List<Location> getLocationInfoByCountryName(String countryName);

}
