package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.l2oworkflow.entity.entities.MfVendorData;

public interface MfVendorDataRepository extends JpaRepository<MfVendorData, Integer> {
	
	/*   @Query(value = "SELECT distinct vendor_id as vendorId,vendor_name as vendorName,"
	   		+ "created_time as createdTime,updated_time as updatedTime, created_by as createdBy,updated_by as updatedBy,tcl_entity_name as tclEntityName,region,city,address,"
	   		+ "sfdc_provider_name as sfdcProviderName from mf_vendor_data "
	   		+ " where vendor_name LIKE %:searchValue%", nativeQuery = true)
	   List<Map<String,Object>> findByVendor(@Param("searchValue") String searchValue);
	   
	   @Query(value = "SELECT distinct vendor_id as vendorId,vendor_name as vendorName,"
		   		+ "created_time as createdTime,updated_time as updatedTime, created_by as createdBy,updated_by as updatedBy,tcl_entity_name as tclEntityName,region,city,address,"
		   		+ "sfdc_provider_name as sfdcProviderName from mf_vendor_data "
		   		+ " where sfdc_provider_name LIKE %:searchValue%", nativeQuery = true)
		   List<Map<String,Object>> findBysfdcProvider(@Param("searchValue") String searchValue);
	   
	   
	   @Query(value="SELECT distinct vendor_id as vendorId,vendor_name as vendorName," + 
	   		   		"created_time as createdTime,updated_time as updatedTime, created_by as createdBy,updated_by as updatedBy,"
	   		   		+ "tcl_entity_name as tclEntityName,region,city,address," + 
	   		"sfdc_provider_name as sfdcProviderName from mf_vendor_data  ORDER BY vendor_name ASC Limit 0, 500", nativeQuery=true)
	   List<Map<String,Object>> findVendorByLimit(); */
	
	  @Query(value = "SELECT * from mf_vendor_data "
		   		+ " where vendor_name LIKE %:searchValue%", nativeQuery = true)
		   List<MfVendorData> findByVendor(@Param("searchValue") String searchValue);
		   
		   @Query(value = "SELECT * from mf_vendor_data "
			   		+ " where sfdc_provider_name LIKE %:searchValue%", nativeQuery = true)
			   List<MfVendorData> findBysfdcProvider(@Param("searchValue") String searchValue);
		   
		   
		   @Query(value="SELECT * from mf_vendor_data  ORDER BY vendor_name ASC Limit 0, 500", nativeQuery=true)
		   List<MfVendorData> findVendorByLimit();

}


