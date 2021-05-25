package com.tcl.dias.networkaugmentation.service;

import com.tcl.dias.networkaugment.entity.entities.NwaAccessEorDetails;
import com.tcl.dias.networkaugment.entity.entities.NwaEorEquipDetails;
import com.tcl.dias.networkaugment.entity.repository.NwaAccessEorDetailsRepository;
import com.tcl.dias.networkaugment.entity.repository.NwaOrderDetailsRepository;
import com.tcl.dias.networkaugmentation.beans.NwaAccessEorDetailsBean;
import com.tcl.dias.networkaugmentation.beans.NwaEorEquipDetailsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NwaAccessEorDetailsServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(NwaAccessEorDetailsServices.class);
    @Autowired
    private NwaAccessEorDetailsRepository nwaAccessEorDetailsRepository;


    public NwaAccessEorDetailsBean saveNwaAccessEorDetails(NwaAccessEorDetailsBean nwaAccessEorDetailsBean) {
        NwaAccessEorDetails nwaAccessEorDetails= new NwaAccessEorDetails();
        nwaAccessEorDetails.setbEndEorId(nwaAccessEorDetailsBean.getbEndEorId());
        nwaAccessEorDetails.setOrderCode(nwaAccessEorDetailsBean.getOrderCode());
        nwaAccessEorDetails.setbEndNeighbourPortDetails(nwaAccessEorDetailsBean.getbEndNeighbourPortDetails());
        nwaAccessEorDetails.setCfmMipLevel(nwaAccessEorDetailsBean.getCfmMipLevel());
        nwaAccessEorDetails.setCityName(nwaAccessEorDetailsBean.getCityName());
        nwaAccessEorDetails.setCmfMdLevel(nwaAccessEorDetailsBean.getCmfMdLevel());
        nwaAccessEorDetails.setConfigureAEndUplinkPort(nwaAccessEorDetailsBean.getConfigureAEndUplinkPort());
        nwaAccessEorDetails.setControlVlanId(nwaAccessEorDetailsBean.getControlVlanId());
        nwaAccessEorDetails.setDescAEndNeighbour(nwaAccessEorDetailsBean.getDescAEndNeighbour());
        nwaAccessEorDetails.setDescNewDeviceAEndUplinkPort(nwaAccessEorDetailsBean.getDescNewDeviceAEndUplinkPort());
        nwaAccessEorDetails.setErpsRingId(nwaAccessEorDetailsBean.getErpsRingId());
        nwaAccessEorDetails.setaEndNeighbourHostName(nwaAccessEorDetailsBean.getaEndNeighbourHostman());
        nwaAccessEorDetails.setaEndEorID(nwaAccessEorDetailsBean.getbEndEorId());
        nwaAccessEorDetails.setaEndNeighbourPortDetails(nwaAccessEorDetailsBean.getaEndNeighbourPortDetails());
        nwaAccessEorDetails.setGateWayIpAddressNtp(nwaAccessEorDetailsBean.getGateWayIpAddressNtp());
        nwaAccessEorDetails.setHostName(nwaAccessEorDetailsBean.getHostName());
        nwaAccessEorDetails.setInitTemplate(nwaAccessEorDetailsBean.getInitTemplate());
        nwaAccessEorDetails.setManagementVlanDescription(nwaAccessEorDetailsBean.getManagementVlanDescription());
        nwaAccessEorDetails.setProtectedInstanceId(nwaAccessEorDetailsBean.getProtectedInstanceId());
        nwaAccessEorDetails.setSubring(nwaAccessEorDetailsBean.getSubring());
        nwaAccessEorDetails.setSwitchLocation(nwaAccessEorDetailsBean.getSwitchLocation());
        nwaAccessEorDetails.setUplinkAPort(nwaAccessEorDetailsBean.getUplinkAPort());
        nwaAccessEorDetails.setUploadNodeFlag(nwaAccessEorDetailsBean.getUploadNodeFlag());

        return  nwaAccessEorDetailsBean;


    }
    public NwaAccessEorDetailsBean getNwaEorEquipDetail(String orderCode){

      /*  NwaEorEquipDetails nwaEorEquipDetails1= nwaEorEquipDetailsRepository.findByOrderCode(orderCode);
        NwaEorEquipDetailsBean nwaEorEquipDetailsBean= new NwaEorEquipDetailsBean();
        nwaEorEquipDetailsBean.setaEndMuxPort(nwaEorEquipDetails1.getaEndMuxPort());*/

        NwaAccessEorDetails nwaAccessEorDetails=nwaAccessEorDetailsRepository.findByOrderCode(orderCode);
        NwaAccessEorDetailsBean nwaAccessEorDetailsBean= new NwaAccessEorDetailsBean();
        nwaAccessEorDetailsBean.setbEndEorId(nwaAccessEorDetails.getbEndEorId());
        nwaAccessEorDetailsBean.setOrderCode(nwaAccessEorDetails.getOrderCode());
        nwaAccessEorDetailsBean.setbEndNeighbourPortDetails(nwaAccessEorDetails.getbEndNeighbourPortDetails());
        nwaAccessEorDetailsBean.setCfmMipLevel(nwaAccessEorDetails.getCfmMipLevel());
        nwaAccessEorDetailsBean.setCityName(nwaAccessEorDetails.getCityName());
        nwaAccessEorDetailsBean.setCmfMdLevel(nwaAccessEorDetails.getCmfMdLevel());
        nwaAccessEorDetailsBean.setConfigureAEndUplinkPort(nwaAccessEorDetails.getConfigureAEndUplinkPort());
        nwaAccessEorDetailsBean.setControlVlanId(nwaAccessEorDetails.getControlVlanId());
        nwaAccessEorDetailsBean.setDescAEndNeighbour(nwaAccessEorDetails.getDescAEndNeighbour());
        nwaAccessEorDetailsBean.setDescNewDeviceAEndUplinkPort(nwaAccessEorDetails.getDescNewDeviceAEndUplinkPort());
        nwaAccessEorDetailsBean.setErpsRingId(nwaAccessEorDetails.getErpsRingId());
        nwaAccessEorDetailsBean.setaEndNeighbourHostman(nwaAccessEorDetails.getaEndNeighbourHostName());
        nwaAccessEorDetailsBean.setaEndEorID(nwaAccessEorDetails.getbEndEorId());
        nwaAccessEorDetailsBean.setaEndNeighbourPortDetails(nwaAccessEorDetails.getaEndNeighbourPortDetails());
        nwaAccessEorDetailsBean.setGateWayIpAddressNtp(nwaAccessEorDetails.getGateWayIpAddressNtp());
        nwaAccessEorDetailsBean.setHostName(nwaAccessEorDetails.getHostName());
        nwaAccessEorDetailsBean.setInitTemplate(nwaAccessEorDetails.getInitTemplate());
        nwaAccessEorDetailsBean.setManagementVlanDescription(nwaAccessEorDetails.getManagementVlanDescription());
        nwaAccessEorDetailsBean.setProtectedInstanceId(nwaAccessEorDetails.getProtectedInstanceId());
        nwaAccessEorDetailsBean.setSubring(nwaAccessEorDetails.getSubring());
        nwaAccessEorDetailsBean.setSwitchLocation(nwaAccessEorDetails.getSwitchLocation());
        nwaAccessEorDetailsBean.setUplinkAPort(nwaAccessEorDetails.getUplinkAPort());
        nwaAccessEorDetailsBean.setUploadNodeFlag(nwaAccessEorDetails.getUploadNodeFlag());


        return  nwaAccessEorDetailsBean;
    }


}
