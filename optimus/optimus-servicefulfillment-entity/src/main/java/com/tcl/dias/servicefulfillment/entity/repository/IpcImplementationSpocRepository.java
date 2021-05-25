package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.IpcImplementationSpoc;

@Repository
public interface IpcImplementationSpocRepository extends JpaRepository<IpcImplementationSpoc, Integer> {

	List<IpcImplementationSpoc> findAll();
}
