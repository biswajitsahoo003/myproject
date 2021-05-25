package com.tcl.dias.networkaugmentation.service;

import com.tcl.dias.networkaugment.entity.entities.NwaOrderDetailsExtnd;
import com.tcl.dias.networkaugment.entity.repository.NwaOrderDetailsExtndRepository;
import com.tcl.dias.networkaugmentation.beans.NwaOrderDetailsExtndBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NwaOrderDetailsExtndService {
    @Autowired
    NwaOrderDetailsExtndRepository nwaOrderDetailsExtndRepository;

    public NwaOrderDetailsExtndBean saveNwaOrderDetailsExtend(NwaOrderDetailsExtndBean nwaOrderDetailsExtndBean){
        NwaOrderDetailsExtnd nwaOrderDetailsExtnd= new NwaOrderDetailsExtnd();
        nwaOrderDetailsExtnd.setEthernetAccess(nwaOrderDetailsExtndBean.getEthernetAccess());
        nwaOrderDetailsExtnd.setEthernetAccessEorType(nwaOrderDetailsExtndBean.getEthernetAccessEorType());
        nwaOrderDetailsExtnd.setFiberFeasibility(nwaOrderDetailsExtndBean.getFiberFeasibility());
        nwaOrderDetailsExtnd.setFieldOpsTeam(nwaOrderDetailsExtndBean.getFieldOpsTeam());
        nwaOrderDetailsExtnd.setGspiBranch(nwaOrderDetailsExtndBean.getGspiBranch());
        nwaOrderDetailsExtnd.setInfraFeasibility(nwaOrderDetailsExtndBean.getInfraFeasibility());
        nwaOrderDetailsExtnd.setIpPool(nwaOrderDetailsExtndBean.getIpPool());
        nwaOrderDetailsExtnd.setIsMuxDetailsChanged(nwaOrderDetailsExtndBean.getIsMuxDetailsChanged());
        nwaOrderDetailsExtnd.setIsTejasMumbaiPuneEci(nwaOrderDetailsExtndBean.getIsTejasMumbaiPuneEci());
        nwaOrderDetailsExtnd.setIsTermExistingLinkReqd(nwaOrderDetailsExtndBean.getIsTermExistingLinkReqd());
        nwaOrderDetailsExtnd.setLoopBackIp0(nwaOrderDetailsExtndBean.getLoopBackIp0());
        nwaOrderDetailsExtnd.setLoopBackIp1(nwaOrderDetailsExtndBean.getLoopBackIp1());
        nwaOrderDetailsExtnd.setNetworkType(nwaOrderDetailsExtndBean.getNetworkType());
        nwaOrderDetailsExtnd.setOrderCode(nwaOrderDetailsExtndBean.getOrderCode());
        nwaOrderDetailsExtnd.setPoFrnNo(nwaOrderDetailsExtndBean.getPoFrnNo());
        nwaOrderDetailsExtnd.setPortFeasibility(nwaOrderDetailsExtndBean.getPortFeasibility());
        nwaOrderDetailsExtnd.setPowerFeasibility(nwaOrderDetailsExtndBean.getPowerFeasibility());
        nwaOrderDetailsExtnd.setSerialNo(nwaOrderDetailsExtndBean.getSerialNo());
        nwaOrderDetailsExtnd.setSoftwareVersion(nwaOrderDetailsExtndBean.getSoftwareVersion());
        nwaOrderDetailsExtnd.setWarehouseLocation(nwaOrderDetailsExtndBean.getWarehouseLocation());
        nwaOrderDetailsExtnd.setWebReportAttached(nwaOrderDetailsExtndBean.getWebReportAttached());

        return nwaOrderDetailsExtndBean;
    }
    public NwaOrderDetailsExtndBean getNwaOrderDetailsExtend(String orderCode){
        NwaOrderDetailsExtnd nwaOrderDetailsExtnd=nwaOrderDetailsExtndRepository.findByOrderCode(orderCode);
        NwaOrderDetailsExtndBean nwaOrderDetailsExtndBean= new NwaOrderDetailsExtndBean();
        nwaOrderDetailsExtndBean.setEthernetAccess(nwaOrderDetailsExtnd.getEthernetAccess());
        nwaOrderDetailsExtndBean.setEthernetAccessEorType(nwaOrderDetailsExtnd.getEthernetAccessEorType());
        nwaOrderDetailsExtndBean.setFiberFeasibility(nwaOrderDetailsExtnd.getFiberFeasibility());
        nwaOrderDetailsExtndBean.setFieldOpsTeam(nwaOrderDetailsExtnd.getFieldOpsTeam());
        nwaOrderDetailsExtndBean.setGspiBranch(nwaOrderDetailsExtnd.getGspiBranch());
        nwaOrderDetailsExtndBean.setInfraFeasibility(nwaOrderDetailsExtnd.getInfraFeasibility());
        nwaOrderDetailsExtndBean.setIpPool(nwaOrderDetailsExtnd.getIpPool());
        nwaOrderDetailsExtndBean.setIsMuxDetailsChanged(nwaOrderDetailsExtnd.getIsMuxDetailsChanged());
        nwaOrderDetailsExtndBean.setIsTejasMumbaiPuneEci(nwaOrderDetailsExtnd.getIsTejasMumbaiPuneEci());
        nwaOrderDetailsExtndBean.setIsTermExistingLinkReqd(nwaOrderDetailsExtnd.getIsTermExistingLinkReqd());
        nwaOrderDetailsExtndBean.setLoopBackIp0(nwaOrderDetailsExtnd.getLoopBackIp0());
        nwaOrderDetailsExtndBean.setLoopBackIp1(nwaOrderDetailsExtnd.getLoopBackIp1());
        nwaOrderDetailsExtndBean.setNetworkType(nwaOrderDetailsExtnd.getNetworkType());
        nwaOrderDetailsExtndBean.setOrderCode(nwaOrderDetailsExtnd.getOrderCode());
        nwaOrderDetailsExtndBean.setPoFrnNo(nwaOrderDetailsExtnd.getPoFrnNo());
        nwaOrderDetailsExtndBean.setPortFeasibility(nwaOrderDetailsExtnd.getPortFeasibility());
        nwaOrderDetailsExtndBean.setPowerFeasibility(nwaOrderDetailsExtnd.getPowerFeasibility());
        nwaOrderDetailsExtndBean.setSerialNo(nwaOrderDetailsExtnd.getSerialNo());
        nwaOrderDetailsExtndBean.setSoftwareVersion(nwaOrderDetailsExtnd.getSoftwareVersion());
        nwaOrderDetailsExtndBean.setWarehouseLocation(nwaOrderDetailsExtnd.getWarehouseLocation());
        nwaOrderDetailsExtndBean.setWebReportAttached(nwaOrderDetailsExtnd.getWebReportAttached());

        return  nwaOrderDetailsExtndBean;
    }


}
