package com.tcl.dias.serviceinventory.service.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.serviceinventory.beans.IpcAttributeDetail;
import com.tcl.dias.serviceinventory.beans.IpcComponentDetail;
import com.tcl.dias.serviceinventory.beans.IpcProductSolutionBean;
import com.tcl.dias.serviceinventory.beans.IpcSolutionDetail;
import com.tcl.dias.serviceinventory.constants.ExceptionConstants;
import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetCommercial;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetComponent;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIOrderAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceDetailRepository;
import com.tcl.dias.serviceinventory.util.ServiceInventoryUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class IpcServiceInventoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IpcServiceInventoryService.class);
	
	@Autowired
	SIServiceDetailRepository siServiceDetailRepository;
	
	@Autowired
	SIAssetRepository siAssetRepository;
	
	@Autowired
	SIOrderAttributeRepository siOrderAttributeRepository;
	
	public static final List<String> ATTRIBUTES = new ArrayList<>();
	public static final List<String> COMPONENTS = new ArrayList<>();
	
	static {
		ATTRIBUTES.add("vCPU");
		ATTRIBUTES.add("vRAM");
		ATTRIBUTES.add("Storage");
		ATTRIBUTES.add("Hypervisor");
		ATTRIBUTES.add("Storage Type");
		ATTRIBUTES.add("Storage Value");
		ATTRIBUTES.add("IOPS Value");
		ATTRIBUTES.add("Type");
		ATTRIBUTES.add("Version");
		ATTRIBUTES.add("Quantity");
		ATTRIBUTES.add("minimumCommitment");
		ATTRIBUTES.add("portBandwidth");
		ATTRIBUTES.add("accessOption");
		
		COMPONENTS.add("Flavor");
		COMPONENTS.add("OS");
		COMPONENTS.add("IPC Common");
		COMPONENTS.add("Additional Storage");
		COMPONENTS.add("Storage Partition");
	}
	
	@Transactional
	public List<IpcProductSolutionBean> getIpcExistingSolutionDetails(String serviceId) throws TclCommonException {
		LOGGER.info("Get IPC Existing Solution Details for Service ID: {}", serviceId);
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<IpcProductSolutionBean> ipcExistingProductSolutions = new ArrayList<>();
		try {
			Optional<SIServiceDetail> siServiceDetail = siServiceDetailRepository.findTopByTpsServiceIdAndServiceStatusOrderByIdDesc(serviceId, "Active");
			if(siServiceDetail.isPresent()) {
				Set<SIAsset> siAssets = siServiceDetail.get().getSiAssets();
				siAssets.forEach(siAsset -> {
					IpcProductSolutionBean ipcProductSolutionBean = new IpcProductSolutionBean();
					ipcProductSolutionBean.setOfferingName(siAsset.getName());
					
					List<IpcSolutionDetail> cloudSolutions = new ArrayList<>();
					
					List<SIAssetCommercial> siAssetCommercials = siAsset.getSiAssetCommercials();
					siAssetCommercials.forEach(siAssetCommercial -> {
						IpcSolutionDetail cloudSolution = new IpcSolutionDetail();
						cloudSolution.setOfferingName(siAsset.getName());
						cloudSolution.setMrc(siAssetCommercial.getMrc());
						cloudSolution.setNrc(siAssetCommercial.getNrc());
						cloudSolution.setArc(siAssetCommercial.getArc());
						cloudSolution.setPpuRate(siAssetCommercial.getPpuRate());
						cloudSolution.setCloudCode(siAsset.getCloudCode());
						cloudSolution.setParentCloudCode(siAsset.getParentCloudCode());
						cloudSolution.setAssetId(siAsset.getId());
						
						Map<String, List<IpcAttributeDetail>> attributesMap = new LinkedHashMap<>();
						Set<SIAssetAttribute> siAssetAttributes = siAsset.getSiAssetAttributes();
						siAssetAttributes.forEach(siAssetAttribute -> {
							List<IpcAttributeDetail> attributes;
							if(attributesMap.containsKey(siAssetAttribute.getCategory())) {
								attributes = attributesMap.get(siAssetAttribute.getCategory());
							} else {
								attributes = new ArrayList<>();
							}
							
							if(siAssetAttribute.getAttributeName() != null && !siAssetAttribute.getAttributeName().isEmpty()) {
								IpcAttributeDetail attribute = new IpcAttributeDetail();
								attribute.setName(siAssetAttribute.getAttributeName());
								attribute.setValue(siAssetAttribute.getAttributeValue());
								attributes.add(attribute);
							}
							
							attributesMap.put(siAssetAttribute.getCategory(), attributes);
						});
						
						List<IpcComponentDetail> components = new ArrayList<>();
						if(("IPC addon").equals(siAsset.getName())) {
							List<SIAssetComponent> siAssetComponents = siAssetCommercial.getSiAssetComponents();
							siAssetComponents.forEach(siAssetComponent -> {
								IpcComponentDetail component = new IpcComponentDetail();
								component.setName(siAssetComponent.getItem());
								component.setMrc(siAssetComponent.getMrc());
								component.setNrc(siAssetComponent.getNrc());
								component.setArc(siAssetComponent.getArc());
								component.setAttributes(getSortedAttributes(attributesMap.get(siAssetComponent.getItem())));
								components.add(component);
							});
						} else {
							attributesMap.forEach((componentName, attributes) -> {
								IpcComponentDetail component = new IpcComponentDetail();
								component.setName(componentName);
								component.setAttributes(getSortedAttributes(attributes));
								components.add(component);
							});
						}
						cloudSolution.setComponents(getSortedComponents(components));
						cloudSolutions.add(cloudSolution);
					});
					ipcProductSolutionBean.setCloudSolutions(cloudSolutions);
					ipcExistingProductSolutions.add(ipcProductSolutionBean);
				});
			} else {
				LOGGER.info("Service Detail not found: " + serviceId);
			}
		} catch (Exception ex) {
			LOGGER.error("Error in getIpcExistingSolutionDetails: ", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return ipcExistingProductSolutions;
	}
	
	private List<IpcAttributeDetail> getSortedAttributes(List<IpcAttributeDetail> attributes) {
		Collections.sort(attributes, (final IpcAttributeDetail o1, final IpcAttributeDetail o2) -> {
			Integer firstIndex = ATTRIBUTES.indexOf(o1.getName());
			Integer secondIndex = ATTRIBUTES.indexOf(o2.getName());
			return firstIndex.compareTo(secondIndex);
		});
		return attributes;
	}
	
	private List<IpcComponentDetail> getSortedComponents(List<IpcComponentDetail> components) {
		Collections.sort(components, (final IpcComponentDetail o1, final IpcComponentDetail o2) -> {
			Integer firstIndex = COMPONENTS.indexOf(o1.getName());
			Integer secondIndex = COMPONENTS.indexOf(o2.getName());
			return firstIndex.compareTo(secondIndex);
		});
		return components;
	}
	
	public String updateCommissionDate( String serviceCode, String orderCode, String commissionDate) {
		SIServiceDetail siServiceDetail = siServiceDetailRepository.findFirstByTpsServiceIdAndSiOrderUuidOrderByIdDesc(serviceCode, orderCode);
		if( null != siServiceDetail) {
			siServiceDetail.setServiceCommissionedDate(ServiceInventoryUtils.formatStringToDate(commissionDate));
			siServiceDetailRepository.save(siServiceDetail);
		}
		return "SUCCESS";
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<IpcSolutionDetail> getAssetDetailsByCloudCodes(Map<String, Object> inputReqM) {
		List<IpcSolutionDetail> ipcSolutionDetails = new ArrayList<IpcSolutionDetail>();
		List<SIAsset> siAssetL = new ArrayList<SIAsset>();
		String orderCode = inputReqM.containsKey(CommonConstants.ORDERCODE)
				&& Objects.nonNull(inputReqM.get(CommonConstants.ORDERCODE))
						? (String) inputReqM.get(CommonConstants.ORDERCODE)
						: null;
		if (null != orderCode) {
			siAssetL = siAssetRepository.findBySiServiceDetail_SiOrderUuid(orderCode);
		} else if (inputReqM.containsKey(CommonConstants.CLOUD_CODES)) {
			Set<String> cloudCodeS = new HashSet<String>((List<String>) inputReqM.get(CommonConstants.CLOUD_CODES));
			siAssetL = siAssetRepository.findByCloudCodeInOrderByIdDesc(cloudCodeS,
					PageRequest.of(0, cloudCodeS.size()));
		}
		if (!siAssetL.isEmpty()) {
			ipcSolutionDetails = siAssetL.stream().filter(x -> !x.getSiAssetCommercials().isEmpty())
					.map(siAsset -> new IpcSolutionDetail(siAsset.getName(),
							siAsset.getSiAssetCommercials().get(0).getMrc(),
							siAsset.getSiAssetCommercials().get(0).getNrc(),
							siAsset.getSiAssetCommercials().get(0).getArc(),
							siAsset.getSiAssetCommercials().get(0).getPpuRate(), siAsset.getCloudCode(),
							siOrderAttributeRepository.findFirstBySiOrderIdAndAttributeNameOrderByIdDesc(
									siAsset.getSiServiceDetail().getSiOrder().getId(),
									LeAttributesConstants.BILLING_CURRENCY).getAttributeValue()))
					.collect(Collectors.toList());
		}
		return ipcSolutionDetails;
	}

}
