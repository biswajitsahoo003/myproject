package com.tcl.dias.location.entity.repository;

import com.tcl.dias.location.entity.entities.CCLocalITContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CCLocalITContactRepository extends JpaRepository<CCLocalITContact, Integer> {
}
