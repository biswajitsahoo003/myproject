package com.tcl.dias.productcatelog.entity.repository;

import com.tcl.dias.productcatelog.entity.entities.VwCpeBomPowerCableGsc;
import com.tcl.dias.productcatelog.entity.entities.VwCpeBomPowerCableGscViewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**Repository for power cable cpe gsc
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface VwCpeBomPowerCableGscRepository extends JpaRepository<VwCpeBomPowerCableGsc, VwCpeBomPowerCableGscViewId> {

	List<VwCpeBomPowerCableGsc> findByCountryName(String country);

	List<VwCpeBomPowerCableGsc> findByProductCode(String powerCable);
}
