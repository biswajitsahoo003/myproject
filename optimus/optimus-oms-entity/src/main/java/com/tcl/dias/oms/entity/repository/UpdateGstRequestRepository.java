package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.UpdateGstRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UpdateGstRequestRepository extends JpaRepository<UpdateGstRequest, Integer>{

	List<UpdateGstRequest> findByIsGstUpdated(String isupdated);
	}
