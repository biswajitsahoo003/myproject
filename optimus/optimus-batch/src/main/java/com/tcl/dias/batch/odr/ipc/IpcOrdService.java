package com.tcl.dias.batch.odr.ipc;

import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;
import org.springframework.stereotype.Service;

/**
 * This class is used to define the IPC related Order flat table
 *
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class IpcOrdService extends OrderService {

    protected String getReferenceName() {
        return OdrConstants.IPC_CLOUD;
    }
}
