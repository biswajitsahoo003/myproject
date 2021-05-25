package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.IasSLAView;
import com.tcl.dias.productcatelog.entity.entities.IasSlaViewId;

/**
 * Spring data JPA Repository file for IasSLAView
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface IasSLAViewRepository extends JpaRepository<IasSLAView, IasSlaViewId> {

	public List<IasSLAView> findBySlaIdAndFactorValueId(Integer slaId, Integer factorVludeId);

	public List<IasSLAView> findBySlaIdAndFactorValue(Integer slaId, String factorValue);

	public List<IasSLAView> findBySlaId(Integer slaId);
	
	public List<IasSLAView> findBySlaIdAndSltVariant(Integer slaId, String sltVariant);

}
