package com.tcl.dias.networkaugmentation.service;

import com.tcl.dias.networkaugment.entity.entities.NwaEorEquipDetails;
import com.tcl.dias.networkaugment.entity.repository.NwaEorEquipDetailsRepository;
import com.tcl.dias.networkaugmentation.beans.NwaEorEquipDetailsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NwaEorEquipDetailsService {
    @Autowired
    private NwaEorEquipDetailsRepository nwaEorEquipDetailsRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(NwaMstMasterDataService.class);

    public NwaEorEquipDetailsBean saveNwaEorEquipDetails(NwaEorEquipDetailsBean nwaEorEquipDetailsBean){
        NwaEorEquipDetails nwaEorEquipDetails= new NwaEorEquipDetails();
        nwaEorEquipDetails.setOrderCode(nwaEorEquipDetailsBean.getOrderCode());
        nwaEorEquipDetails.setaEndMuxName(nwaEorEquipDetailsBean.getaEndMuxName());
        nwaEorEquipDetails.setaEndMuxPort(nwaEorEquipDetailsBean.getaEndMuxPort());
        nwaEorEquipDetails.setaEndMuxPort2(nwaEorEquipDetailsBean.getaEndMuxPort2());
        nwaEorEquipDetails.setAggrParentRouterIp(nwaEorEquipDetailsBean.getAggrParentRouterIp());
        nwaEorEquipDetails.setArea(nwaEorEquipDetailsBean.getArea());
        nwaEorEquipDetails.setAreaOther(nwaEorEquipDetailsBean.getAreaOther());
        nwaEorEquipDetails.setBldg(nwaEorEquipDetailsBean.getBldg());
        nwaEorEquipDetails.setBldgOther(nwaEorEquipDetailsBean.getBldgOther());
        nwaEorEquipDetails.setCity(nwaEorEquipDetailsBean.getCity());
        nwaEorEquipDetails.setCityOther(nwaEorEquipDetailsBean.getCityOther());
        nwaEorEquipDetails.setCountry(nwaEorEquipDetailsBean.getCountry());
        nwaEorEquipDetails.setCoverage(nwaEorEquipDetailsBean.getCoverage());
        nwaEorEquipDetails.setDcnReq(nwaEorEquipDetailsBean.getDcnReq());
        nwaEorEquipDetails.setDeviceSubType(nwaEorEquipDetailsBean.getDeviceSubType());
        nwaEorEquipDetails.setRingType(nwaEorEquipDetailsBean.getRingType());
        nwaEorEquipDetails.setEndMuxAddress(nwaEorEquipDetailsBean.getEndMuxAddress());
        nwaEorEquipDetails.setEorEquipStatus(nwaEorEquipDetailsBean.getEorEquipStatus());
        nwaEorEquipDetails.setEorWimaxOrderSite(nwaEorEquipDetailsBean.getEorWimaxOrderSite());
        nwaEorEquipDetails.setEqpmntIp(nwaEorEquipDetailsBean.getEqpmntIp());
        nwaEorEquipDetails.setEqpmntName(nwaEorEquipDetailsBean.getEqpmntName());
        nwaEorEquipDetails.setEqpmntPort(nwaEorEquipDetailsBean.getEqpmntPort());
        nwaEorEquipDetails.setEqpmntType(nwaEorEquipDetailsBean.getEqpmntType());
        nwaEorEquipDetails.setFloor(nwaEorEquipDetailsBean.getFloor());
        nwaEorEquipDetails.setInterfaceToParent(nwaEorEquipDetailsBean.getInterfaceToParent());
        nwaEorEquipDetails.setInterfaceType(nwaEorEquipDetailsBean.getInterfaceType());
        nwaEorEquipDetails.setInterFibreReq(nwaEorEquipDetailsBean.getInterFibreReq());
        nwaEorEquipDetails.setIsManEquipment(nwaEorEquipDetailsBean.getIsManEquipment());
        nwaEorEquipDetails.setIsSwitchIpRequird(nwaEorEquipDetailsBean.getIsSwitchIpRequird());
        nwaEorEquipDetails.setHostName(nwaEorEquipDetailsBean.getHostName());
        nwaEorEquipDetails.setMajorPop(nwaEorEquipDetailsBean.getMajorPop());
        nwaEorEquipDetails.setMicroPop(nwaEorEquipDetailsBean.getMicroPop());
        nwaEorEquipDetails.setNocTeam(nwaEorEquipDetailsBean.getNocTeam());
        nwaEorEquipDetails.setNodeOwner(nwaEorEquipDetailsBean.getNodeOwner());
        nwaEorEquipDetails.setNodeOwnerOther(nwaEorEquipDetails.getNodeOwnerOther());
       // nwaEorEquipDetails.setNwaOrderDetails(nwaEorEquipDetails.getNwaOrderDetails());
        //nwaEorEquipDetails.setOrderId(nwaEorEquipDetailsBean.getOrderId());
        nwaEorEquipDetails.setPoolSize(nwaEorEquipDetailsBean.getPoolSize());
        nwaEorEquipDetails.setPopName(nwaEorEquipDetailsBean.getPopName());
        nwaEorEquipDetails.setPopOwner(nwaEorEquipDetailsBean.getPopOwner());
        nwaEorEquipDetails.setPopType(nwaEorEquipDetailsBean.getPopType());
        nwaEorEquipDetails.setVlan(nwaEorEquipDetailsBean.getVlan());
        nwaEorEquipDetails.setSiteAddress(nwaEorEquipDetailsBean.getSiteAddress());
        nwaEorEquipDetails.setSiteEnd(nwaEorEquipDetailsBean.getSiteEnd());
        nwaEorEquipDetails.setSiteName(nwaEorEquipDetailsBean.getSiteName());
        nwaEorEquipDetails.setSpareScrap(nwaEorEquipDetailsBean.getSpareScrap());
        nwaEorEquipDetails.setState(nwaEorEquipDetailsBean.getState());
        nwaEorEquipDetails.setPowerRating(nwaEorEquipDetailsBean.getPowerRating());
        nwaEorEquipDetails.setPowerType(nwaEorEquipDetailsBean.getPowerType());
        nwaEorEquipDetails.setSpareScrap(nwaEorEquipDetailsBean.getSpareScrap());
         nwaEorEquipDetailsRepository.save(nwaEorEquipDetails);
         return  nwaEorEquipDetailsBean;
    }

    public NwaEorEquipDetailsBean getNwaEorEquipDetail(String orderCode){
        NwaEorEquipDetails nwaEorEquipDetails1= nwaEorEquipDetailsRepository.findByOrderCode(orderCode);
        NwaEorEquipDetailsBean nwaEorEquipDetailsBean= new NwaEorEquipDetailsBean();
        nwaEorEquipDetailsBean.setaEndMuxPort(nwaEorEquipDetails1.getaEndMuxPort());
        nwaEorEquipDetailsBean.setaEndMuxPort2(nwaEorEquipDetails1.getaEndMuxPort2());
        nwaEorEquipDetailsBean.setAggrParentRouterIp(nwaEorEquipDetails1.getAggrParentRouterIp());
        nwaEorEquipDetailsBean.setArea(nwaEorEquipDetails1.getArea());
        nwaEorEquipDetailsBean.setAreaOther(nwaEorEquipDetails1.getAreaOther());
        nwaEorEquipDetailsBean.setBldg(nwaEorEquipDetails1.getBldg());
        nwaEorEquipDetailsBean.setBldgOther(nwaEorEquipDetails1.getBldgOther());
        nwaEorEquipDetailsBean.setCity(nwaEorEquipDetails1.getCity());
        nwaEorEquipDetailsBean.setCityOther(nwaEorEquipDetails1.getCityOther());
        nwaEorEquipDetailsBean.setCountry(nwaEorEquipDetails1.getCountry());
        nwaEorEquipDetailsBean.setCoverage(nwaEorEquipDetails1.getCoverage());
        nwaEorEquipDetailsBean.setDcnReq(nwaEorEquipDetails1.getDcnReq());
        nwaEorEquipDetailsBean.setDeviceSubType(nwaEorEquipDetails1.getDeviceSubType());
        nwaEorEquipDetailsBean.setRingType(nwaEorEquipDetails1.getRingType());
        nwaEorEquipDetailsBean.setEndMuxAddress(nwaEorEquipDetails1.getEndMuxAddress());
        nwaEorEquipDetailsBean.setEorEquipStatus(nwaEorEquipDetails1.getEorEquipStatus());
        nwaEorEquipDetailsBean.setEorWimaxOrderSite(nwaEorEquipDetails1.getEorWimaxOrderSite());
        nwaEorEquipDetailsBean.setEqpmntIp(nwaEorEquipDetails1.getEqpmntIp());
        nwaEorEquipDetailsBean.setEqpmntName(nwaEorEquipDetails1.getEqpmntName());
        nwaEorEquipDetailsBean.setEqpmntPort(nwaEorEquipDetails1.getEqpmntPort());
        nwaEorEquipDetailsBean.setEqpmntType(nwaEorEquipDetails1.getEqpmntType());
        nwaEorEquipDetailsBean.setFloor(nwaEorEquipDetails1.getFloor());
        nwaEorEquipDetailsBean.setInterfaceToParent(nwaEorEquipDetails1.getInterfaceToParent());
        nwaEorEquipDetailsBean.setInterfaceType(nwaEorEquipDetails1.getInterfaceType());
        nwaEorEquipDetailsBean.setInterFibreReq(nwaEorEquipDetails1.getInterFibreReq());
        nwaEorEquipDetailsBean.setIsManEquipment(nwaEorEquipDetails1.getIsManEquipment());
        nwaEorEquipDetailsBean.setIsSwitchIpRequird(nwaEorEquipDetails1.getIsSwitchIpRequird());
        nwaEorEquipDetailsBean.setHostName(nwaEorEquipDetails1.getHostName());
        nwaEorEquipDetailsBean.setMajorPop(nwaEorEquipDetails1.getMajorPop());
        nwaEorEquipDetailsBean.setMicroPop(nwaEorEquipDetails1.getMicroPop());
        nwaEorEquipDetailsBean.setNocTeam(nwaEorEquipDetails1.getNocTeam());
        nwaEorEquipDetailsBean.setNodeOwner(nwaEorEquipDetails1.getNodeOwner());
        nwaEorEquipDetailsBean.setNodeOwnerOther(nwaEorEquipDetails1.getNodeOwnerOther());
        //  updatenwaEorEquipDetails.setNwaOrderDetails(nwaEorEquipDetails.getNwaOrderDetails());
        //nwaEorEquipDetailsBean.setOrderId(nwaEorEquipDetails1.getOrderId());
        nwaEorEquipDetailsBean.setPoolSize(nwaEorEquipDetails1.getPoolSize());
        nwaEorEquipDetailsBean.setPopName(nwaEorEquipDetails1.getPopName());
        nwaEorEquipDetailsBean.setPopOwner(nwaEorEquipDetails1.getPopOwner());
        nwaEorEquipDetailsBean.setPopType(nwaEorEquipDetails1.getPopType());
        nwaEorEquipDetailsBean.setVlan(nwaEorEquipDetails1.getVlan());
        nwaEorEquipDetailsBean.setSiteAddress(nwaEorEquipDetails1.getSiteAddress());
        nwaEorEquipDetailsBean.setSiteEnd(nwaEorEquipDetails1.getSiteEnd());
        nwaEorEquipDetailsBean.setSiteName(nwaEorEquipDetails1.getSiteName());
        nwaEorEquipDetailsBean.setSpareScrap(nwaEorEquipDetails1.getSpareScrap());
        nwaEorEquipDetailsBean.setState(nwaEorEquipDetails1.getState());
        nwaEorEquipDetailsBean.setPowerRating(nwaEorEquipDetails1.getPowerRating());
        nwaEorEquipDetailsBean.setPowerType(nwaEorEquipDetails1.getPowerType());
        nwaEorEquipDetailsBean.setSpareScrap(nwaEorEquipDetails1.getSpareScrap());

        //nwaEorEquipDetailsBean.setaEndMuxName(nwaEorEquipDetails1.getaEndMuxName());
        nwaEorEquipDetailsBean.setaEndMuxPort(nwaEorEquipDetails1.getaEndMuxPort());
        return  nwaEorEquipDetailsBean;
    }
}
