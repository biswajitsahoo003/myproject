package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.MstCountry;
/**
 * 
 * This file is the repository class for MstCountry.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface MstCountryRepository extends JpaRepository<MstCountry, Integer> { 
	
	List<MstCountry> findByName(String name);
	
	/**
	 * 
	 * Get all non entity countries
	 * @author AnandhiV
	 * @return
	 */
	@Query(value="SELECT name FROM mst_countries where id not in(select country_id from sp_le_country)",nativeQuery = true)
	public List<String> getNonEntityCountries();
	
	Optional<MstCountry> findById(Integer countryId);

}
