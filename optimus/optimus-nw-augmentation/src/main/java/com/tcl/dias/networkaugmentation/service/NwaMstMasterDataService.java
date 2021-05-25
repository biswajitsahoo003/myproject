package com.tcl.dias.networkaugmentation.service;

import com.tcl.dias.networkaugmentation.beans.NwaMstMasterDataBean;
import com.tcl.dias.networkaugment.entity.entities.NwaMstMasterData;
import com.tcl.dias.networkaugment.entity.repository.NwaMstMasterDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = false,isolation= Isolation.READ_COMMITTED)
public class NwaMstMasterDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NwaMstMasterDataService.class);

    @Autowired
    NwaMstMasterDataRepository nwaMstMasterDataRepository;

    public List<NwaMstMasterDataBean> getOptionValues() {
        List<NwaMstMasterDataBean> nwaMstMasterDataBeanList = new ArrayList<NwaMstMasterDataBean>();
        List<NwaMstMasterData> nwaMstMasterDataList = nwaMstMasterDataRepository.findAll();
        Map<String, List<NwaMstMasterData>> groupMap = nwaMstMasterDataList.stream()
                .collect(Collectors.groupingBy(NwaMstMasterData::getAttribute_key, Collectors.toList()));
        for (String key : groupMap.keySet()) {
            NwaMstMasterDataBean nwaMstMasterDataBean = new NwaMstMasterDataBean();
            nwaMstMasterDataBean.setAttribute_key(key);
            nwaMstMasterDataBean.setAttribute_option_value(new ArrayList<>(groupMap.get(key)
                    .stream()
                    .map(NwaMstMasterData::getAttribute_option_value).collect(Collectors.toList())));
            nwaMstMasterDataBeanList.add(nwaMstMasterDataBean);
        }

         /*NwaMstMasterDataBean nwaMstMasterDataBean=null;
    String key = "";
    for(NwaMstMasterData nwaMstMasterData : nwaMstMasterDataList){
        if(!key.equals(nwaMstMasterData.getAttribute_key())){
            if(!key.equals("")){
                nwaMstMasterDataBeanList.add(nwaMstMasterDataBean);
            }
            nwaMstMasterDataBean = new NwaMstMasterDataBean();
            key = nwaMstMasterData.getAttribute_key();
        }
        nwaMstMasterDataBean.setAttribute_key(key);
        nwaMstMasterDataBean.getAttribute_option_value().add(nwaMstMasterData.getAttribute_option_value());
    }
    nwaMstMasterDataBeanList.add(nwaMstMasterDataBean);*/

        return nwaMstMasterDataBeanList;
    }
}
