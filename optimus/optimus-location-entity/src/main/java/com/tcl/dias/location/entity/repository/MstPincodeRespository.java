package com.tcl.dias.location.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.MstCity;
import com.tcl.dias.location.entity.entities.MstPincode;
import com.tcl.dias.location.entity.queries.NativeQueries;

/**
 * This file contains the MstPincodeRespository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstPincodeRespository extends JpaRepository<MstPincode, Integer> {

	@Query(value = NativeQueries.GET_PINCODE_DETAILS_V1, nativeQuery = true)
	public List<Map<String, Object>> findByPincodeAndSource(@Param("pincode") String pincode,
			@Param("country") String country, @Param("source") String source);

	@Query(value = NativeQueries.GET_PINCODE_DETAILS, nativeQuery = true)
	public List<Map<String, Object>> findByPincode(@Param("pincode") String pincode, @Param("country") String country);
	
	@Query(value = NativeQueries.GET_PINCODE_DETAILS_CITY_CHANGE, nativeQuery = true)
	public List<Map<String, Object>> findByPincodeV1(@Param("pincode") String pincode, @Param("country") String country);

	@Query(value = NativeQueries.GET_COUNTRY_DETAILS, nativeQuery = true)
	public List<Map<String, Object>> findByCountry(@Param("country") String country);

	public List<MstPincode> findByCode(String name);
	
	@Query(value = NativeQueries.GET_LOCALITY_DETAILS_BY_PINCODE_AND_CITY, nativeQuery = true)
	public List<Map<String, Object>> findByPincodeAndCity(@Param("pincode") String pincode,@Param("city") String city);
	
	@Query(value = NativeQueries.GET_CITY_DETAILS_BY_PINCODE, nativeQuery = true)
	public List<Map<String, Object>> findCityDetailsByPincode(@Param("code") String pincode);
	
	public List<MstPincode> findByCodeAndMstCity(String code,MstCity mstCity);

}
