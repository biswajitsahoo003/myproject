package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstCostCatalogue;

/**
 * 
 * Repository Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface MstCostCatalogueRepository extends JpaRepository<MstCostCatalogue, Integer> {

	List<MstCostCatalogue> findByRentalMaterialCodeAndBundledBom(String rentalMaterialCode,String bundledBom);
	
	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom and sale_material_code=:sale_material_code order by id asc", nativeQuery = true)
	List<MstCostCatalogue> findBySaleMaterialCodeAndBundledBom(@Param("sale_material_code") String saleMaterialCode,@Param("bundledBom") String bundledBom);

	List<MstCostCatalogue> findByProductCodeAndCategory(String productCode, String category);

	@Query(value = "SELECT * FROM mst_cost_catalogue where service_number=:service_number and bundled_bom=:bundledBom and product_code=:productCode order by id asc", nativeQuery = true)
	List<MstCostCatalogue> findByServiceNumberAndBundledBomAndProductCode(@Param("service_number") String serviceNumber,@Param("bundledBom") String bundledBom,@Param("productCode") String productCode);

	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom order by id asc", nativeQuery = true)
	List<MstCostCatalogue> findByBundledBom(String bundledBom);
	


	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom  group by rental_material_code,sale_material_code order by id asc", nativeQuery = true)
	List<MstCostCatalogue> findByDistinctBundledBom(@Param("bundledBom") String bundledBom);
	
	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom and category NOT IN('Cable') group by rental_material_code,sale_material_code order by id asc", nativeQuery = true)
	List<MstCostCatalogue> findByDistinctBundledBomForRental(@Param("bundledBom") String bundledBom);
	
	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom and service_number is not null group by service_number,product_code order by id asc", nativeQuery = true)
	List<MstCostCatalogue> findByDistinctBundledBomService(@Param("bundledBom") String bundledBom);
	
	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom  and category IN('Cable') group by rental_material_code,sale_material_code order by id asc" , nativeQuery = true)
	List<MstCostCatalogue> findByDistinctBundledBomForRentalWithCable(@Param("bundledBom") String bundledBom);
	
	List<MstCostCatalogue> findByBundledBomAndProductCodeIn(String bundledBom,List<String> productCodeList);
	
	MstCostCatalogue findFirstByBundledBomAndCategory(String bundledBom,String category);

	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom and product_code=:productCode and category=:category and rental_material_code is not null" , nativeQuery = true)
	List<MstCostCatalogue> findByBundledBomAndProductCodeAndCategoryForRental(@Param("bundledBom") String bundledBom,@Param("productCode") String productCode,@Param("category")String category);

	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom and product_code=:productCode  and category=:category and sale_material_code is not null" , nativeQuery = true)
	List<MstCostCatalogue> findByBundledBomAndProductCodeAndCategoryForOutright(@Param("bundledBom") String bundledBom,@Param("productCode") String productCode,@Param("category")String category);

	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom and category=:category and rental_material_code is not null" , nativeQuery = true)
	List<MstCostCatalogue> findByBundledBomAndCategoryForRental(@Param("bundledBom") String bundledBom,@Param("category")String category);

	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom and category=:category and sale_material_code is not null" , nativeQuery = true)
	List<MstCostCatalogue> findByBundledBomAndCategoryForOutright(@Param("bundledBom") String bundledBom,@Param("category")String category);

	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom and sale_material_code is not null" , nativeQuery = true)
	List<MstCostCatalogue> findByBundledBomForOutright(@Param("bundledBom") String bundledBom);

	@Query(value = "SELECT * FROM mst_cost_catalogue where bundled_bom=:bundledBom and rental_material_code is not null" , nativeQuery = true)
	List<MstCostCatalogue> findByBundledBomForRental(@Param("bundledBom") String bundledBom);

	List<MstCostCatalogue> findByOem(String oem);

}
