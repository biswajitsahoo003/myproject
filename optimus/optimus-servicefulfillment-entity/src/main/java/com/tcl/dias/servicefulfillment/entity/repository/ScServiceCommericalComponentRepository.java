package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommericalComponent;


@Repository
public interface ScServiceCommericalComponentRepository extends JpaRepository<ScServiceCommericalComponent,Integer>{
	
	ScServiceCommericalComponent findByScProductDetailIdAndItem(Integer scProductDetailId,String item);

}
