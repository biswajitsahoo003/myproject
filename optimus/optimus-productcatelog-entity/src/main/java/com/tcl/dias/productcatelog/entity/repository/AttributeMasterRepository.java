package com.tcl.dias.productcatelog.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.AttributeMaster;
/**
 * 
 * @author Biswajit Sahoo
 *
 */
@Repository
public interface AttributeMasterRepository extends JpaRepository<AttributeMaster, Integer> {

	public Optional<AttributeMaster> findByCdAndIsActiveIsNullOrCdAndIsActive(String attributeCd, String attributeCd1,
			String isActiveInd);

}
