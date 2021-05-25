package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_CONNECTED_SITE;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.tcl.dias.servicefulfillment.entity.entities.MstVendorCpeInternational;
import com.tcl.dias.servicefulfillment.entity.repository.MstVendorCpeInternationalRepository;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.servicefulfillment.beans.BomResources;
import com.tcl.dias.common.servicefulfillment.beans.CpeBomResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.CpeCostDetails;
import com.tcl.dias.servicefulfillment.entity.entities.MstCostCatalogue;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.repository.CpeCostDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstCostCatalogueRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("sdwanCpeDelegate")
public class IzosdwanCPEConfigurationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanCPEConfigurationDelegate.class);
		
		@Autowired
		WorkFlowService workFlowService;
		
		@Autowired
		ScComponentRepository scComponentRepository;
		
		@Autowired
		ScComponentAttributesRepository scComponentAttributesRepository;
		
		@Autowired
		ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
		
		@Autowired
		MstCostCatalogueRepository mstCostCatalogueRepository;
		
		@Autowired
		CpeCostDetailsRepository cpeCostDetailsRepository;

		@Autowired
		MstVendorCpeInternationalRepository mstVendorCpeInternationalRepository;
		
	public void execute(DelegateExecution execution) {
		logger.info("SdwanCPEConfigurationDelegate.execute method invoked");
		Integer cpeOverlayComponentId = (Integer) execution.getVariable("cpeOverlayComponentId");
		Integer serviceId = (Integer) execution.getVariable("serviceId");
		String serviceCode = (String) execution.getVariable("serviceCode");
		logger.info("serviceId={},serviceCode={},cpeOverlayComponentId={},excutionProcessInstId={}", serviceId,serviceCode,cpeOverlayComponentId,execution.getProcessInstanceId());
		String errorMessage = "";
		Map<String, String> scComponentAttributesMap = new HashMap<String, String>();
		Optional<ScComponent> scComponentOptional =scComponentRepository.findById(cpeOverlayComponentId);
		if(scComponentOptional.isPresent()){
			ScComponent scComponent=scComponentOptional.get();
			Set<ScComponentAttribute> scComponentAttrSet=scComponent.getScComponentAttributes();
			for (ScComponentAttribute scComponentAttr : scComponentAttrSet) {
				scComponentAttributesMap.put(scComponentAttr.getAttributeName(),scComponentAttr.getAttributeValue());
			}
			logger.info("scComponentAttributesMap:{}", scComponentAttributesMap);		
			String cpeType = StringUtils.trimToEmpty(scComponentAttributesMap.get("CPE"));
			String siteType = StringUtils.trimToEmpty(scComponent.getSiteType());
			
			execution.setVariable("site_type", siteType);
			logger.info("cpeType={},siteType={}", cpeType,siteType);
			if (cpeType.toLowerCase().contains("rental")) {
				cpeType ="Rental";
				execution.setVariable("cpeType",cpeType );
			} else if (cpeType.toLowerCase().contains("outright")) {
				cpeType ="Outright";
				execution.setVariable("cpeType", cpeType);
				execution.setVariable("isCpeAvailableInInventory",false);
				execution.setVariable("cpeHardwarePRCompleted",false);
				execution.setVariable("cpeHardwarePoStatus","Failure");
				execution.setVariable("cpeLicenseNeeded",true);
				execution.setVariable("cpeLicensePRCompleted",false);
				execution.setVariable("cpeLicencePoStatus","Failure");
			} else {
				execution.setVariable("cpeType", cpeType);
			}
			String cpeSiScope  = "Customer provided";
			String cpeManagementType = StringUtils.trimToEmpty(scComponentAttributesMap.get("cpeManagementType"));
			if ("Fully Managed".equalsIgnoreCase(cpeManagementType)) {
				logger.info("cpeManagementType={}",cpeManagementType);
				cpeSiScope = "Supply, Installation, Support";
				execution.setVariable("cpeSiScope", cpeSiScope);
				execution.setVariable("isCPEConfiguredByCustomer", false);
				execution.setVariable("isCPEArrangedByCustomer", false);
			}
			execution.setVariable(IS_CONNECTED_SITE, true);
			
			String cpeBasicChasisServiceParamId = StringUtils.trimToEmpty(scComponentAttributesMap.get("CPE Basic Chassis"));
			CpeBomResource cpeBomResource=null;
			try {
				cpeBomResource=getCpeBomDetails(cpeBasicChasisServiceParamId);
			} catch (TclCommonException e) {
				logger.error(
						"SdwanCPEConfigurationDelegate------------------- getting error cpeBom message details----------->{}",e);
			}
			Set<String> vendorCodeSet= new HashSet<>();
			Set<String> vendorNameSet= new HashSet<>();
			Map<String,String> vendorDetailMap=new HashMap<>();
			if (cpeBomResource != null) {
				logger.info("cpeBomResource exists");
				String bomName=cpeBomResource.getBomName();
				logger.info("bomName:{}", bomName);
				List<String> productCodeList=new ArrayList<>();
				for(BomResources bomResource:cpeBomResource.getResources()){
					productCodeList.add(bomResource.getProductCode());
				}
				logger.info("ProductCodeList size::{}", productCodeList.size());
				List<MstCostCatalogue> mstCostCatalogueList=mstCostCatalogueRepository.findByBundledBomAndProductCodeIn(bomName,productCodeList);
				List<CpeCostDetails> cpeCostList= new ArrayList<>();
				if(mstCostCatalogueList!=null && !mstCostCatalogueList.isEmpty()){
					logger.info("costCatalogueList exists::{}",mstCostCatalogueList.size());
					for(MstCostCatalogue mstCostCatalogue:mstCostCatalogueList){
						CpeCostDetails cpeCostDetails=new CpeCostDetails();
						
						cpeCostDetails.setBundledBom(mstCostCatalogue.getBundledBom());
						cpeCostDetails.setOem(mstCostCatalogue.getOem());
						cpeCostDetails.setMaterialCode(cpeType.equalsIgnoreCase("Rental")?mstCostCatalogue.getRentalMaterialCode():mstCostCatalogue.getSaleMaterialCode());
						cpeCostDetails.setServiceNumber(mstCostCatalogue.getServiceNumber());
						cpeCostDetails.setShortText(mstCostCatalogue.getShortText());
						cpeCostDetails.setProductCode(mstCostCatalogue.getProductCode());
						cpeCostDetails.setDescription(mstCostCatalogue.getDescription());
						cpeCostDetails.setCategory(mstCostCatalogue.getCategory());
						cpeCostDetails.setHsnCode(mstCostCatalogue.getHsnCode());
						
						cpeCostDetails.setComponentId(cpeOverlayComponentId);
						cpeCostDetails.setCreatedDate(new Timestamp(new Date().getTime()));
						cpeCostDetails.setServiceCode(serviceCode);
						cpeCostDetails.setServiceId(serviceId);

						Optional<BomResources> optionalBomResources = cpeBomResource.getResources().stream().
								filter(bomResources -> bomResources.getProductCode()
										.equalsIgnoreCase(mstCostCatalogue.getProductCode())).findFirst();
						BomResources bomResources = optionalBomResources.get();
						cpeCostDetails.setQuantity(bomResources.getQuantity()==0?1:bomResources.getQuantity());

						Boolean isDomesticSite = (Boolean) execution.getVariable("isDomesticSite");
						if(isDomesticSite){
							logger.info("SdwanCPEConfigurationDelegate domestic service id: {}", serviceId);
							cpeCostDetails.setPerListPriceUsd(mstCostCatalogue.getPerListPriceUsd());
							cpeCostDetails.setCalculatedPrice(cpeCostDetails.getQuantity()*mstCostCatalogue.getPerListPriceUsd());
							cpeCostDetails.setCurrency(mstCostCatalogue.getCurrency());
							cpeCostDetails.setIncrementalRate(mstCostCatalogue.getIncrementalRate());
							cpeCostDetails.setProcurementDiscountPercentage(mstCostCatalogue.getProcurementDiscountPercentage());
							cpeCostDetails.setVendorCode(mstCostCatalogue.getVendorCode());
							cpeCostDetails.setVendorName(mstCostCatalogue.getVendorName());
							vendorCodeSet.add(mstCostCatalogue.getVendorCode());
							vendorNameSet.add(mstCostCatalogue.getVendorName());
							vendorDetailMap.put(mstCostCatalogue.getVendorCode(), mstCostCatalogue.getVendorName());
						}else{
							logger.info("SdwanCPEConfigurationDelegate international service id: {}", serviceId);
							if(bomResources.getListPrice()!=null){	
								cpeCostDetails.setPerListPriceUsd(bomResources.getListPrice());
								cpeCostDetails.setCalculatedPrice(cpeCostDetails.getQuantity()*bomResources.getListPrice());
							}else {
								cpeCostDetails.setPerListPriceUsd(0.0);
								cpeCostDetails.setCalculatedPrice(0.0);
							}
							cpeCostDetails.setCurrency("USD");
							cpeCostDetails.setIncrementalRate(0.0);
							cpeCostDetails.setProcurementDiscountPercentage(0.0);

							MstVendorCpeInternational mstVendorCpeInternational = null;
							String country = (String) execution.getVariable("country");
							if(country != null && !country.isEmpty()) {
								if(country.equalsIgnoreCase("United States of America")||
										country.equalsIgnoreCase("Russia")||
										country.equalsIgnoreCase("Singapore")||
										country.equalsIgnoreCase("Hong Kong")
								){
									logger.info("international country: {}", country);
									mstVendorCpeInternational = mstVendorCpeInternationalRepository.findByCountry(country);
								} else {
									country = "Rest of the World";
									logger.info("international country: {}", country);
									mstVendorCpeInternational = mstVendorCpeInternationalRepository.findByCountry(country);
								}
								if(Objects.nonNull(mstVendorCpeInternational)) {
									logger.info("mstVendorCpeInternational vendor code for country: {},{}", mstVendorCpeInternational.getVendorCode()
											,country);
									cpeCostDetails.setVendorCode(mstVendorCpeInternational.getVendorCode());
									cpeCostDetails.setVendorName(mstVendorCpeInternational.getVendorName());
									vendorCodeSet.add(mstVendorCpeInternational.getVendorCode());
									vendorNameSet.add(mstVendorCpeInternational.getVendorName());
									vendorDetailMap.put(mstVendorCpeInternational.getVendorCode(), mstVendorCpeInternational.getVendorName());
								}
							}
						}
						cpeCostList.add(cpeCostDetails);
					}
					cpeCostDetailsRepository.saveAll(cpeCostList);
				}
			}
			logger.info("VendorCode size:{}", vendorCodeSet.size());
			logger.info("VendorName size:{}", vendorNameSet.size());
			logger.info("VendorDetailMap size:{}", vendorDetailMap.size());
			List<String> vendorCodes= new ArrayList<>();
			vendorCodes.addAll(vendorCodeSet);
			List<String> vendorNames= new ArrayList<>();
			vendorNames.addAll(vendorNameSet);
			execution.setVariable("vendorCodes", vendorCodes);
			execution.setVariable("vendorNames", vendorNames);
			execution.setVariable("vendorDetails", vendorDetailMap);
			execution.setVariable("vendorsCount", vendorCodes.size());
			logger.info("Execution Variables:{}", execution.getVariables());
			workFlowService.processServiceTask(execution);
	        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
		}
		
	}
	
	public CpeBomResource getCpeBomDetails(String cpeBasicChasisServiceParamId) throws TclCommonException {
		logger.info("getCpeBomDetails:{}",cpeBasicChasisServiceParamId);
		if (StringUtils.isNotBlank(cpeBasicChasisServiceParamId)) {
			Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
					.findById(Integer.valueOf(cpeBasicChasisServiceParamId));
			if (scAdditionalServiceParam.isPresent()) {
				String bomResponse = scAdditionalServiceParam.get().getValue();
				logger.info("constructQuantityRequest bomResponse for bomResponse is:{}",bomResponse);
				CpeBomResource[] bomResourcess = Utils.convertJsonToObject(bomResponse, CpeBomResource[].class);
				return bomResourcess[0];
			}
		}
		return null;
	}

}
