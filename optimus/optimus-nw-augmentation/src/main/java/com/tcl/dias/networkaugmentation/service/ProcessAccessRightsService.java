package com.tcl.dias.networkaugmentation.service;

import com.tcl.dias.networkaugment.entity.entities.ProcessAccessRights;
import com.tcl.dias.networkaugment.entity.entities.ProcessNameMaster;
import com.tcl.dias.networkaugment.entity.repository.ProcessAccessRightsRepository;
import com.tcl.dias.networkaugment.entity.repository.ProcessNameMasterRepository;
import com.tcl.dias.networkaugmentation.beans.ProcessAccessRightsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessAccessRightsService {

    @Autowired
    ProcessAccessRightsRepository processAccessRightsRepository;

    @Autowired
    ProcessNameMasterRepository processNameMasterRepository;

    public ProcessAccessRightsBean getAccessRights(String groupName){

        ProcessAccessRights processAccessRights = processAccessRightsRepository.findByGroupName(groupName);
        ProcessNameMaster processNameMaster = processAccessRights.getProcessNameMaster();


        ProcessAccessRightsBean processAccessRightsBean = new ProcessAccessRightsBean();

        processAccessRightsBean.setProcessId(processNameMaster.getId());
        processAccessRightsBean.setProcess_name(processNameMaster.getProcess_name());
        processAccessRightsBean.setMenuHeaderName(processNameMaster.getMenuHeaderName());
        processAccessRightsBean.setMenuDisplayName(processNameMaster.getMenuDisplayName());
        processAccessRightsBean.setGroupName(processAccessRights.getGroupName());
        processAccessRightsBean.setAccessRight(processAccessRights.getAccessRight());

        return processAccessRightsBean;


    }

}
