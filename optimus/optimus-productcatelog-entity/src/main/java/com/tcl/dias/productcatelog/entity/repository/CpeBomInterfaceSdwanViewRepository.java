package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.CpeBomInterfaceSdwanView;

/**
 * @author mpalanis
 *
 */

@Repository
public interface CpeBomInterfaceSdwanViewRepository extends JpaRepository<CpeBomInterfaceSdwanView,Integer>{

	@Query(value="SELECT distinct bom_name_cd FROM vw_bom_phy_intf_sdwan",nativeQuery = true)
	public List<String> getDistinctCpeBomName();
	
	
}
