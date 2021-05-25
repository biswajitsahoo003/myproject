package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstGroupType;

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
public interface MstGroupTypeRepository extends JpaRepository<MstGroupType, Integer> {
	
	public MstGroupType findByGroupTypeAndStatus(String groupType, Byte status);
	
	public MstGroupType findByGroupTypeCode(String groupTypeCode);
	
	public MstGroupType findByGroupTypeCodeAndStatus(String groupTypeCode, Byte status);
	
	/**
	 * 
	 * Search group type by name
	 * @param search
	 * @return
	 */
	@Query (value ="SELECT * from mst_group_type where group_type like %:search% ",nativeQuery =true)
	public List<MstGroupType> searchByTypeOfTheGroup(@Param("search") String search);

}
