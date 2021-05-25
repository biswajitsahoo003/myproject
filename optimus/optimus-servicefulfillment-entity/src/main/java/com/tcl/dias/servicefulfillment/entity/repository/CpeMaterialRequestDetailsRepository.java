package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.CpeMaterialRequestDetails;

@Repository
public interface CpeMaterialRequestDetailsRepository extends JpaRepository<CpeMaterialRequestDetails, Integer>, PagingAndSortingRepository<CpeMaterialRequestDetails, Integer>,JpaSpecificationExecutor<CpeMaterialRequestDetails> {

	 List<CpeMaterialRequestDetails> findByScServiceDetailIdAndServiceCodeAndAvailable(Integer scServiceDetailId,
			String serviceCode, String available);
	 
	 List<CpeMaterialRequestDetails> findByScServiceDetailIdAndServiceCodeAndAvailableAndCatagory(Integer scServiceDetailId,
				String serviceCode, String available, String catagory);
	 
	 List<CpeMaterialRequestDetails> findByScServiceDetailIdAndServiceCodeAndAvailableAndCatagoryNot(Integer scServiceDetailId,
				String serviceCode, String available, String catagory);
	
	 List<CpeMaterialRequestDetails> findByScServiceDetailIdAndMaterialCodeIn(Integer scServiceDetailId,List<String> materialCode);
	
	
}
