package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.CustomFilterBean;
import com.tcl.dias.servicefulfillmentutils.beans.MstTaskDefBean;

public interface CustomFilterService {

    CustomFilterBean getCustomFilterDetails(String userName,String group, String type);

    CustomFilterBean saveCustomFilterDetails(CustomFilterBean customFilterBean);

    List<MstTaskDefBean> getMstTasksByAssignedGroup(String[] assignedGroup);

    void deleteCustomFilter(String filterName, String filterType, String groupName);
}
