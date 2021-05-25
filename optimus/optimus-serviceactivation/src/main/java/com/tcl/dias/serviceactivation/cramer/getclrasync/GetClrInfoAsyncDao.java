package com.tcl.dias.serviceactivation.cramer.getclrasync;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.GetCLRInfoResponse;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.NodeToBeConfigured;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.OrderInfo;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.TransmissionService;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.Worker;
import com.tcl.dias.serviceactivation.entity.entities.NodeToConfigure;
import com.tcl.dias.serviceactivation.entity.entities.TxConfiguration;
import com.tcl.dias.serviceactivation.entity.repository.NodeToConfigureRepository;
import com.tcl.dias.serviceactivation.entity.repository.TxConfigurationRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.JAXBException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This file contains the GetClrInfoAsyncDao.java class.
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Transactional
@Component
public class GetClrInfoAsyncDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetClrInfoAsyncDao.class);


    @Autowired
    NodeToConfigureRepository nodeToConfigureRepository;

    @Autowired
    TxConfigurationRepository txConfigurationRepository;

    @Autowired
    ScComponentAttributesRepository scComponentAttributesRepository;

    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;

    /**
     * saves Tx,worker,protection and nodeToConfigure Details.
     *
     * @param getCLRInfoResponse
     */
    @Transactional
    public void saveTxConfigurationDetails(GetCLRInfoResponse getCLRInfoResponse, String serviceContents) throws TclCommonException, JAXBException {
        OrderInfo orderInfo = getCLRInfoResponse.getResponse();
        TransmissionService transmissionService = orderInfo.getService();
        TxConfiguration txConfiguration = saveTxConfiguration(getCLRInfoResponse, serviceContents);
        if (Objects.nonNull(txConfiguration)) {
            saveNodeToConfigureDetails(transmissionService.getNodeToConfigure(), txConfiguration);
        }
    }

    private TxConfiguration saveTxConfiguration(GetCLRInfoResponse getCLRInfoResponse, String serviceContents) throws JAXBException {
    	LOGGER.info("saveTxConfiguration method invoked for serviceId::{}",getCLRInfoResponse.getResponse().getServiceId());
        TxConfiguration previousConfigIfPresent = cancelPreviousConfigIfPresent(getCLRInfoResponse.getResponse().getServiceId());
        TxConfiguration txConfiguration = new TxConfiguration();
        //if (Objects.nonNull(getCLRInfoResponse.getResponse()))
        txConfiguration.setServiceId(getCLRInfoResponse.getResponse().getServiceId());
        txConfiguration.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
        txConfiguration.setServiceContents(serviceContents);
        txConfiguration.setStatus("ISSUED");
        if (Objects.nonNull(previousConfigIfPresent))
            txConfiguration.setVersion(previousConfigIfPresent.getVersion() + 1);
        else
            txConfiguration.setVersion(1);
        txConfiguration.setClrResponse(Utils.convertObjectToXmlString(getCLRInfoResponse, GetCLRInfoResponse.class));
        return txConfigurationRepository.saveAndFlush(txConfiguration);
    }

    private TxConfiguration cancelPreviousConfigIfPresent(String serviceId) {
    	LOGGER.info("cancelPreviousConfigIfPresent method invoked for serviceId::{}",serviceId);
        return txConfigurationRepository.findByServiceIdAndStatus(serviceId, "ISSUED")
                .map(txConfiguration -> {
                    txConfiguration.setStatus("CANCEL");
                    return txConfigurationRepository.saveAndFlush(txConfiguration);
                }).orElse(null);
    }

    private void saveNodeToConfigureDetails(List<NodeToBeConfigured> nodesToConfigure, TxConfiguration txConfiguration) {
        nodesToConfigure.forEach(n -> constructNodeToConfigure(txConfiguration, n));
    }


    private void constructNodeToConfigure(TxConfiguration txConfiguration, NodeToBeConfigured n) {
    	if(n!=null && n.isIsNOCActionRequired()) {
	        NodeToConfigure nodeToConfigure = new NodeToConfigure();
	        nodeToConfigure.setIsAceActionRequired(Objects.equals(n.isIsACEActionRequired(), true) ? "Y" : "N");
	        nodeToConfigure.setIsCienaActionRequired(Objects.equals(n.isIsCienaActionRequired(), true) ? "Y" : "N");
	        nodeToConfigure.setIsNocActionRequired(Objects.equals(n.isIsNOCActionRequired(), true) ? "Y" : "N");
	        nodeToConfigure.setNodeDef(n.getNodeDef());
	        nodeToConfigure.setNodeDefId(String.valueOf(n.getNodeDefId()));
	        nodeToConfigure.setNodeName(n.getNodeName());
	        nodeToConfigure.setNodeTypeId(String.valueOf(n.getNodeTypeId()));
	        nodeToConfigure.setNodeType(n.getNodeType());
	        nodeToConfigure.setNodeAlias1(n.getNodeAlias1());
	        nodeToConfigure.setNodeAlias2(n.getNodeAlias2());
	        nodeToConfigure.setTxConfiguration(txConfiguration);
	        nodeToConfigureRepository.save(nodeToConfigure);
    	}
        
    }

    @Transactional
    public void extractCustomerPort(GetCLRInfoResponse getCLRInfoResponse) {
        try {
            LOGGER.info(String.format("Entering extract customer port method for service Code : %s", getCLRInfoResponse.getResponse().getServiceId()));
            ScServiceDetail serviceDetail =scServiceDetailRepository.findByUuidAndMstStatus_code(getCLRInfoResponse.getResponse().getServiceId(),"INPROGRESS");
			if (serviceDetail == null) {
				serviceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(
						getCLRInfoResponse.getResponse().getServiceId(), TaskStatusConstants.TERMINATION_INPROGRESS);

			}
            
            if (serviceDetail!=null) {
            	
            	ScServiceDetail scServiceDetail = serviceDetail;
            	
            	ScComponentAttribute endMuxNodeNameA = scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "endMuxNodeName", "LM", "A");
            	ScComponentAttribute interfaceType = scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "interfaceType", "LM", "A");

            	String [] workerInterfaceType=new String []{"Link"};
            	
				/*if ("Electrical".equalsIgnoreCase(interfaceType.getAttributeValue())) {
					workerInterfaceType[0] = "Ethernet Link";
				}
				else if ("Optical".equalsIgnoreCase(interfaceType.getAttributeValue())) {
					workerInterfaceType[0] = "Fiber Link";

				}*/
            	if(endMuxNodeNameA!=null) {
                    List<Worker> workerList = getCLRInfoResponse
                            .getResponse()
                            .getService().getWorker()
                            .stream()
                            .filter(worker -> workerInterfaceType[0].equalsIgnoreCase(worker.getObjectType()))
                            .collect(Collectors.toList());

                    List<String> customerNodePort = new ArrayList<>();
                    workerList.stream()
                            .forEach(worker -> {
                                if (worker.getANodeAlias1().equalsIgnoreCase(endMuxNodeNameA.getAttributeValue())) {
                                    if (worker.getZEndNodeDef().equalsIgnoreCase("Cisco Generic Device")) {
                                        customerNodePort.add(worker.getAPortAlias1());
                                    }
                                } else if (worker.getZNodeAlias1().equalsIgnoreCase(endMuxNodeNameA.getAttributeValue())) {
                                    if (worker.getAEndNodeDef().equalsIgnoreCase("Cisco Generic Device")) {
                                        customerNodePort.add(worker.getZPortAlias1());
                                    }
                                }
                            });

                    if (!CollectionUtils.isEmpty(customerNodePort))
                        saveInComponentAttribute(customerNodePort.get(0), scServiceDetail,"A");
                }
                    
                if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("NPL")) {
                	ScComponentAttribute endMuxNodeNameB = scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "endMuxNodeName", "LM", "B");
                	ScComponentAttribute interfaceTypeB = scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "interfaceType", "LM", "B");

                	String [] workerInterfaceTypeB=new String []{"Link"};
                	
    				/*if ("Electrical".equalsIgnoreCase(interfaceTypeB.getAttributeValue())) {
    					workerInterfaceTypeB[0] = "Ethernet Link";
    				}
    				else if ("Optical".equalsIgnoreCase(interfaceTypeB.getAttributeValue())) {
    					workerInterfaceTypeB[0] = "Fiber Link";

    				}*/
                	if(endMuxNodeNameB!=null) {
                        List<Worker> workerList = getCLRInfoResponse
                                .getResponse()
                                .getService().getWorker()
                                .stream()
                                .filter(worker ->workerInterfaceTypeB[0].equalsIgnoreCase(worker.getObjectType()))
                                .collect(Collectors.toList());

                        List<String> customerNodePort = new ArrayList<>();
                        workerList.stream()
                                .forEach(worker -> {
                                    if (worker.getANodeAlias1().equalsIgnoreCase(endMuxNodeNameB.getAttributeValue())) {
                                        if (worker.getZEndNodeDef().equalsIgnoreCase("Cisco Generic Device")) {
                                            customerNodePort.add(worker.getAPortAlias1());
                                        }
                                    } else if (worker.getZNodeAlias1().equalsIgnoreCase(endMuxNodeNameB.getAttributeValue())) {
                                        if (worker.getAEndNodeDef().equalsIgnoreCase("Cisco Generic Device")) {
                                            customerNodePort.add(worker.getZPortAlias1());
                                        }
                                    }
                                });

                        if (!CollectionUtils.isEmpty(customerNodePort))
                            saveInComponentAttribute(customerNodePort.get(0), scServiceDetail,"B");
                    }
                	
                }


            }
        } catch (Exception e) {
            LOGGER.error("Exception in extractCustomerPort", e);
        }

    }

    private Map<String, String> saveInComponentAttribute(String customerNodePort, ScServiceDetail scServiceDetail,String siteType) {
        LOGGER.info(String.format("Customer Node port : %s for service code : %s", customerNodePort, scServiceDetail.getUuid()));
        Map<String, String> customerNodePortMap = new HashMap<>();
        customerNodePortMap.put("endMuxNodePort", customerNodePort);
        componentAndAttributeService.updateAttributes(scServiceDetail.getId(),customerNodePortMap, AttributeConstants.COMPONENT_LM,siteType);
        return customerNodePortMap;
    }
}
