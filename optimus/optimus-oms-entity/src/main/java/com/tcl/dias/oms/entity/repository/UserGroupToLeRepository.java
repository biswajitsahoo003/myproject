package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.oms.entity.entities.MstUserGroups;
import com.tcl.dias.oms.entity.entities.UserGroupToLe;

/**
 * 
 *entity for user groups to le related queries
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface UserGroupToLeRepository extends JpaRepository<UserGroupToLe, Integer> {
	
	public List<UserGroupToLe> findByMstUserGroups(MstUserGroups mstUserGroups);
	
	public List<UserGroupToLe> findByMstUserGroupsAndErfCusCustomerLeId(MstUserGroups mstUserGroups, Integer erfCusCustomerLeId);
	
	public List<UserGroupToLe> findByErfCusCustomerLeId(Integer erfCusCustomerLeId);
		
	public List<UserGroupToLe> findByIdNotInAndMstUserGroups(List<Integer> mappingIds,MstUserGroups mstUserGroups);
	
	Page<UserGroupToLe> findAll(Specification<UserGroupToLe> specification, Pageable pageable);
	
	/**
	 * 
	 * Delete all user group mappings by user group
	 * @param userGroupId
	 * @return
	 */
	@Query(value = "delete from user_group_to_le where user_group_id=:userGroupId", nativeQuery = true)
	@Modifying
	@Transactional
	public int deleteUserGroupMappings(Integer userGroupId);
}
