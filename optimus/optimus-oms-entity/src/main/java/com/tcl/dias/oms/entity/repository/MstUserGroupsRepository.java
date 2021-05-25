package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstGroupType;
import com.tcl.dias.oms.entity.entities.MstUserGroups;

/**
 * 
 *entity for user group master related queries
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface MstUserGroupsRepository extends JpaRepository<MstUserGroups, Integer>{
	
	public MstUserGroups  findByMstGroupTypeAndStatus(MstGroupType groupType, Byte status);
	
	public MstUserGroups findByGroupName(String groupName);
	
	public MstUserGroups findByGroupNameAndStatus(String groupName, Byte status);
	
	public List<MstUserGroups> findByGroupNameContainingIgnoreCaseAndStatus(String groupName, Byte status);

	Page<MstUserGroups> findAll(Specification<MstUserGroups> specification, Pageable pageable);

	@Query (value ="SELECT * from mst_user_groups where group_name like %:search% ",nativeQuery =true)
	public List<MstUserGroups> searchByNameOfTheGroup(@Param("search") String search);

}
