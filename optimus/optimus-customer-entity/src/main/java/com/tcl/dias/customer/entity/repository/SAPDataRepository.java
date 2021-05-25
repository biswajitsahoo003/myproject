package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.SAPData;
/**
 * Repository class for SAP Data
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface SAPDataRepository extends JpaRepository<SAPData, Integer> {

    List<SAPData> findAllByPartnerIdIn(List<String> partnerLeIds);
}
