package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.ProductCatalog;
import com.tcl.dias.productcatelog.entity.queries.NativeQueries;
/**
 * 
 * @author Manojkumar R
 *
 */
@Repository
public interface ProductFamilyRepository extends JpaRepository<ProductCatalog, Long> {

	@Query(nativeQuery = true, value = NativeQueries.GET_PRODUCT_LOCATIONS)
	public List<String> getProductLocations(@Param("product_name") String prodId);

	public Optional<ProductCatalog> findById(Integer prodFamilyId); 
	
	public Optional<ProductCatalog> findByName(String prodFamilyName);

	/**
	 * Get Product Locations for given product based and country
	 *
	 * @param prodId
	 * @param country
	 * @return
	 */
	@Query(value = "SELECT vpca.location_name FROM vw_product_country_availability vpca where vpca.product_name=:product_name and vpca.location_name=:country",
			nativeQuery = true)
	List<String> getProductLocationsForGivenCountry(@Param("product_name") String prodId, @Param("country") String country);

	/**
	 * Get Product Locations for given product based and exclude country
	 *
	 * @param prodId
	 * @param country
	 * @return
	 */
	@Query(value = "SELECT vpca.location_name FROM vw_product_country_availability vpca where vpca.product_name=:product_name and vpca.location_name !=:country",
			nativeQuery = true)
	List<String> getProductLocationsAndExcludeGivenCountry(@Param("product_name") String prodId, @Param("country") String country);
}
