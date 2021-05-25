package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tcl.dias.oms.entity.entities.ImpersonationUserMapping;
import com.tcl.dias.oms.entity.entities.User;

/**
 * Repository class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface ImpersonationUserMappingRepository
		extends JpaRepository<ImpersonationUserMapping, Integer>, JpaSpecificationExecutor<ImpersonationUserMapping> {

	List<ImpersonationUserMapping> findBySourceUserId(User user);

}
