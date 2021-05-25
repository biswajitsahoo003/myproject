package com.tcl.dias.servicefulfillmentutils.service.v1;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.networkaugment.entity.entities.NetworkInventory;
import com.tcl.dias.networkaugment.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.servicefulfillmentutils.beans.NetworkInventoryBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class NetworkInventoryService {

    @Autowired
    NetworkInventoryRepository networkInventoryRepository;

    public void saveInNetworkInventory(NetworkInventoryBean networkInventoryBean) throws TclCommonException {
        NetworkInventory networkInventory = new NetworkInventory();

        networkInventory.setOrderCode(networkInventoryBean.getOrderCode());
        networkInventory.setRequestId(networkInventoryBean.getRequestId());
        networkInventory.setRequest(networkInventoryBean.getRequest());
        networkInventory.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        networkInventory.setResponse(Utils.convertObjectToJson(networkInventoryBean));
        networkInventory.setType(networkInventoryBean.getType());
        networkInventoryRepository.save(networkInventory);
    }
}
