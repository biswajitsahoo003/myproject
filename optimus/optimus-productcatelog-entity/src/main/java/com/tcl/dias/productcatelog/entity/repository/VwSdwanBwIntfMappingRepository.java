package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.VwSdwanBwIntfMapping;
import java.lang.String;

/**
 * Repository for VwSdwanBwIntfMappingRepository
 * @author mpalanis
 *
 */
@Repository
public interface VwSdwanBwIntfMappingRepository extends JpaRepository<VwSdwanBwIntfMapping, Integer>{
	
	@Query(value = "SELECT distinct(interface) FROM vw_bw_intf_mapping",nativeQuery=true)
	public List<String> getInterface();
	
	List<VwSdwanBwIntfMapping> findByVendor(String vendor);

}
