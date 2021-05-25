package com.tcl.dias.networkaugment.entity.repository;

import com.tcl.dias.networkaugment.entity.entities.NwaLinkedOrderDetails;
import com.tcl.dias.networkaugment.entity.entities.NwaSuspensionReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NwaSuspensionReasonRepository extends JpaRepository<NwaSuspensionReason, Integer> {

    public NwaSuspensionReason findByOrderCode(String orderCode);

}
