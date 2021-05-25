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
import com.tcl.dias.oms.entity.entities.UsergroupToPartnerLe;
/**
 * 
 * This is the repository class for UsergroupToPartnerLe
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface UserGroupToPartnerLeRepository extends JpaRepository<UsergroupToPartnerLe, Integer>{
	
	public List<UsergroupToPartnerLe> findByIdNotInAndMstUserGroup(List<Integer> mappingIds,MstUserGroups mstUserGroups);
	
	public List<UsergroupToPartnerLe> findByMstUserGroup(MstUserGroups mstUserGroups);
	
	public List<UsergroupToPartnerLe> findByMstUserGroupAndErfCusPartnerLeId(MstUserGroups mstUserGroups,Integer erfCusPartnerLeId);
	
	Page<UsergroupToPartnerLe> findAll(Specification<UsergroupToPartnerLe> specification, Pageable pageable);
	
	/**
	 * 
	 * Delete all user group mappings by user group
	 * @param userGroupId
	 * @return
	 */
	@Query(value = "delete from usergroup_to_partner_le where user_group_id=:userGroupId", nativeQuery = true)
	@Modifying
	@Transactional
	public int deleteUserGroupMappings(Integer userGroupId);
	
}
