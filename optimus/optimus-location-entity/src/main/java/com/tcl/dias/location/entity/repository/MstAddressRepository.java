package com.tcl.dias.location.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.MstAddress;

/**
 * This file contains the MstAddressRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstAddressRepository extends JpaRepository<MstAddress, Integer> {

	List<MstAddress> findByIdIn(List<Integer> addressIds);

	List<MstAddress> findByAddressLineOneAndCityAndCountryAndPincodeAndStateAndLocality(String addressLineOne,
			String city, String country, String pincode, String state, String locality);
	
	List<MstAddress> findByAddressLineOneAndCityAndCountry(String addressLineOne,String city, String country);

	List<MstAddress> findById(List<Integer> addressIds);
	
	
	//Optional<MstAddress> find
	@Query(value="select * from mst_address  where id=:id  and address_line_one like %:addressLineOne% ", nativeQuery=true)
	Optional<MstAddress> findByIdAndAddressLineOne(Integer id,String addressLineOne);
	
	@Query(value="select * from mst_address  where id=:id  and (address_line_one like %:textToSearch% or address_line_two like %:textToSearch% or pincode like  %:textToSearch%  or city like %:textToSearch%  or state like %:textToSearch% or locality  like %:textToSearch% or country like %:textToSearch%)", nativeQuery=true)
	MstAddress selectBasedOnSearchAndId(Integer id,String textToSearch);
	
}
