package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.CustomFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomFilterRepository extends JpaRepository<CustomFilter, Integer> {

	List<CustomFilter> findAllByUserNameAndGroupName(String userName, String group);

	Optional<CustomFilter> findByUserNameAndGroupNameAndTypeAndIsDefault(String userName, String group, String type,
			Byte isDefault);

	Optional<CustomFilter> findFirstByUserNameAndFilterNameAndTypeAndGroupName(String userName, String filterName,
			String type, String groupName);

	List<CustomFilter> findAllByUserName(String userName);

	List<CustomFilter> findAllByGroupName(String group);

	List<CustomFilter> findByUserNameAndGroupNameAndType(String userName, String groupName, String type);

	List<CustomFilter> findByUserNameAndType(String userName, String type);

	List<CustomFilter> findByGroupNameAndType(String groupName, String type);

}
