package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.oms.entity.entities.MstUserGroups;
import com.tcl.dias.oms.entity.entities.UserToCustomerLe;
import com.tcl.dias.oms.entity.entities.UserToUserGroup;
import com.tcl.dias.oms.entity.entities.UsergroupToPartnerLe;

/***
 * 
 * Repository Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface UserToUserGroupRepository extends JpaRepository<UserToUserGroup, Integer> {
	
	public List<UserToUserGroup> findByUsername(String username);
	
	public List<UserToUserGroup> findByUsernameAndMstUserGroup(String username,MstUserGroups mstUserGroup);
	
	@Query(value = "SELECT a.* FROM user_to_user_groups a,mst_user_groups b,mst_group_type c WHERE a.user_id = :userId AND b.id = a.user_group_id AND c.id = b.group_type AND c.group_type = :groupType",nativeQuery = true)
	public List<UserToUserGroup> findByUserIdAndMstUserGroup(@Param("userId") Integer userId,@Param("groupType") String groupType);
	
	@Query(value = "SELECT a.* FROM user_to_user_groups a,mst_user_groups b,mst_group_type c WHERE a.user_id = :userId AND b.id = a.user_group_id AND c.id = b.group_type AND c.group_type = :groupType and b.group_name=:groupName",nativeQuery = true)
	public List<UserToUserGroup> findByUserIdAndMstUserGroupAndGroupName(@Param("userId") Integer userId,@Param("groupType") String groupType,@Param("groupName") String groupName);
	
	public List<UserToUserGroup> findByMstUserGroup_GroupName(String groupName);
	
	@Query(value = "select a.* from user_to_user_groups a,mst_user_groups b,mst_group_type c where a.user_group_id=b.id and b.group_type=c.id and group_type_code=:grouptype and a.username=:username",nativeQuery = true)
	public List<UserToUserGroup> findByUsernameAndMstUserGroupMstGroupType_GroupTypeCode(@Param("username") String username,@Param("grouptype") String grouptype);
	
	public List<UserToUserGroup> findByMstUserGroup(MstUserGroups mstUserGroups);
	
	public List<UserToUserGroup> findByIdNotInAndMstUserGroup(List<Integer> mappingIds,MstUserGroups mstUserGroup);
	
	public List<UserToUserGroup> findByIdIn(List<Integer> mappingIds);
	
	Page<UserToUserGroup> findAll(Specification<UserToUserGroup> specification, Pageable pageable);
	
	@Query(value="SELECT count(id) FROM user_to_user_groups where user_group_id=:mstGroupId",nativeQuery=true)
	public Integer countByMstGroupId(@Param("mstGroupId") Integer mstGroupId);
	
	/**
	 * 
	 * Delete all user group mappings by user group
	 * @param userGroupId
	 * @return
	 */
	@Query(value = "delete from user_to_user_groups where user_group_id=:userGroupId", nativeQuery = true)
	@Modifying
	@Transactional
	public int deleteUserGroupMappings(Integer userGroupId);
	public List<UserToUserGroup> findByMstUserGroup_GroupNameIn(List<String> groupName);
}
