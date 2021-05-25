package com.tcl.dias.batch.odr.izopc;

import org.springframework.stereotype.Service;

import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;

/**
 * This class is used to define the Izopc related Order flat table
 *
 *
 * @author Keerthana S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class IzopcMacdOdrService extends OrderService {

    protected String getReferenceName() {
        return OdrConstants.IZO_PC_SITES;
    }
}
