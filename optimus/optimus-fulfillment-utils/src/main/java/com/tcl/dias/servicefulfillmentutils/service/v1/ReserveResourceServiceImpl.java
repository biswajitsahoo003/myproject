package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.tcl.dias.servicefulfillment.entity.entities.MasterTclDistributionCenter;
import com.tcl.dias.servicefulfillment.entity.repository.MstTclDistributionCenterRepository;
import com.tcl.dias.servicefulfillmentutils.beans.DataCenterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.sap.CpeInventoryRequestBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.CpeInventoryResponseBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * SAP Integration service.
 *
 * @author VishAwas
 */
@Service
public class ReserveResourceServiceImpl implements ReserveResourceService {

    private static final Logger log = LoggerFactory.getLogger(TaskAttributeMasterSingleton.class);


    @Value("${sap.integration.url}")
    String sapUrl;

    @Value("${sap.integration.username}")
    String username;

    @Value("${sap.integration.password}")
    String password;

    @Autowired
    RestClientService restClientService;

    @Autowired
    MstTclDistributionCenterRepository mstTclDistributionCenterRepository;

    /**
     * Get CPE Inventory details.
     *
     * @param cpeInventoryRequestBean
     * @return CpeInventoryResponseBean
     * @throws TclCommonException
     */
    @Override
    public CpeInventoryResponseBean getCPEInventoryDetails(CpeInventoryRequestBean cpeInventoryRequestBean) throws TclCommonException {
        log.info("CpeInventoryRequestBean -> {]", cpeInventoryRequestBean);
        RestResponse restResponse = restClientService.post(sapUrl, Utils.convertObjectToJson(cpeInventoryRequestBean), username, password, new HashMap<>());
        log.info("Inventory response - > {}", restResponse.toString());
        CpeInventoryResponseBean cpeInventoryResponseBean = new CpeInventoryResponseBean();
        if (restResponse.getStatus() == Status.SUCCESS) {
            cpeInventoryResponseBean = Utils.convertJsonToObject(restResponse.getData(), CpeInventoryResponseBean.class);
        }
        return cpeInventoryResponseBean;
    }

    public List<DataCenterBean> getTclDataCenterDetails(){
        List<DataCenterBean> dataCenters=new ArrayList<>();
        List<MasterTclDistributionCenter> masterTclDistributionCenters=mstTclDistributionCenterRepository.findAll();
        if(Objects.nonNull(masterTclDistributionCenters) && !masterTclDistributionCenters.isEmpty()){
            masterTclDistributionCenters.stream().forEach(masterTclDistributionCenter -> {
                DataCenterBean dataCenter=new DataCenterBean();
                dataCenter.setDistributionCenterName(masterTclDistributionCenter.getDistributionCenterName());
                dataCenter.setDistributionCenterState(masterTclDistributionCenter.getDistributionCenterState());
                dataCenter.setPlant(masterTclDistributionCenter.getPlant());
                dataCenter.setSapStorageLocation(masterTclDistributionCenter.getSapStorageLocation());
                dataCenter.setDistributionCenterAddress(masterTclDistributionCenter.getDistributionCenterAddress());
                dataCenters.add(dataCenter);
            });
        }
        return  dataCenters;
    }

}
