package com.tcl.dias.servicefulfillmentutils.service.v1;

import com.tcl.dias.servicefulfillmentutils.beans.DataCenterBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.CpeInventoryRequestBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.CpeInventoryResponseBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import java.util.List;

public interface ReserveResourceService {

    CpeInventoryResponseBean getCPEInventoryDetails(CpeInventoryRequestBean cpeInventoryRequestBean) throws TclCommonException;
    List<DataCenterBean> getTclDataCenterDetails();

}
