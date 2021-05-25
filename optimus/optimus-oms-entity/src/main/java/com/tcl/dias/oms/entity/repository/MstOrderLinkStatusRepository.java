package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcl.dias.oms.entity.entities.MstOrderLinkStatus;

/** 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 */

@Repository
public interface MstOrderLinkStatusRepository extends JpaRepository<MstOrderLinkStatus, Integer>{
	public MstOrderLinkStatus findByName(String name);
}
