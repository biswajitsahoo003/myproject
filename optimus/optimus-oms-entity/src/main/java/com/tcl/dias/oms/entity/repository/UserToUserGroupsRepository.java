package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstUserGroups;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.entities.UserToUserGroups;

/**
 * 
 *entity for user to user groups related queries
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface UserToUserGroupsRepository  extends JpaRepository<UserToUserGroups, Integer>{
	
	public List<UserToUserGroups> findByUser(User user);

	public List<UserToUserGroups> findByMstUserGroups(MstUserGroups mstUserGroups);
	
	public List<UserToUserGroups> findByUsername(String username);
	
	public List<UserToUserGroups> findByUserAndMstUserGroups(User user, MstUserGroups mstUserGroups);
	
	@Query(value="SELECT a.* FROM user_to_user_groups a,mst_user_groups b WHERE a.user_group_id = b.id AND b.group_name = :groupName",nativeQuery = true)
	public List<UserToUserGroups> findByGroupName(@Param("groupName") String groupName);
}
