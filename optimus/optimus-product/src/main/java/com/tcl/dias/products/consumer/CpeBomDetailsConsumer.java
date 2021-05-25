package com.tcl.dias.products.consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.products.constants.ExceptionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.BomInventoryCatalogAssocResponse;
import com.tcl.dias.common.beans.CpeBom;
import com.tcl.dias.common.beans.CpeBomDetails;
import com.tcl.dias.common.beans.CpeDetails;
import com.tcl.dias.common.beans.GvpnInternationalCpeDto;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.productcatelog.entity.entities.BomRentalSalesCodeAssoc;
import com.tcl.dias.productcatelog.entity.repository.BomRentalSalesCodeAssocRepository;
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.ResourceDto;
import com.tcl.dias.products.gvpn.service.v1.GVPNProductService;
import com.tcl.dias.products.service.v1.ProductsService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * this class used to get the Bom details
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class CpeBomDetailsConsumer {

	@Autowired
	ProductsService productFamilyService;

	@Autowired
	BomRentalSalesCodeAssocRepository bomRentalSalesCodeAssocRepository;
	
	@Autowired
	GVPNProductService gvpnProductService;

	private static final Logger LOGGER = LoggerFactory.getLogger(CpeBomDetailsConsumer.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cpe.bom.queue}") })
	public String processSlaDetails(String request) throws TclCommonException {
		String response = "";
		try {
			if (request != null) {
				List<String> list = Arrays.asList((request).split(","));
				LOGGER.info("Input Received {}", list);
				if (!list.isEmpty()) {
					Set<CpeBomDto> cpeBom = productFamilyService.getCpeBomDetails(list);
					LOGGER.info("Getting the BOM Details {} :: Size {}", cpeBom, cpeBom.size());
					CpeDetails cpeDeatils = new CpeDetails();
					if (cpeBom != null) {
						cpeBom.forEach(cpe -> {
							CpeBom details = new CpeBom();

							details.setBomName(cpe.getBomName());
							details.setUniCode(cpe.getUniCode());
							if (cpe.getResources() != null) {
								cpe.getResources().forEach(resource -> {
									CpeBomDetails bom = new CpeBomDetails();
									bom.setLongDesc(resource.getLongDesc());
									bom.setListPrice(resource.getListPrice());
									bom.setListPriceCurrencyId(resource.getListPriceCurrencyId());
									bom.setProductCode(resource.getProductCode());
									bom.setHsnCode(resource.getHsnCode());
									bom.setProviderId(resource.getProviderId());
									bom.setProductCategory(resource.getProductCategory());
									details.getCpeBomDetails().add(bom);
								});
							}
							cpeDeatils.getCpeBoms().add(details);
						});
					}
					String res = Utils.convertObjectToJson(cpeDeatils);
					LOGGER.info("Product Catelog response {}", res);
					return res;
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting bom details  ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cpe.bom.resource.queue}") })
	public String processCpeBom(String request) throws TclCommonException {
		String response = "";
		try {
			if (request!= null) {
				List<String> list = Arrays.asList((request).split(","));
				LOGGER.info("Input Received {}", list);
				if (!list.isEmpty()) {
					Set<CpeBomDto> cpeBom = productFamilyService.getCpeBomDetails(list);
					String res = Utils.convertObjectToJson(cpeBom);
					LOGGER.info("Product Catelog response {}", res);
					return res;
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting bom details  ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cpe.bom.rentalsales.queue}") })
	public String getBomRentalSalesDetails(String request) throws TclCommonException {
		String response = "";
		try {
			if (request != null) {
				String bomName = request;
				LOGGER.info("Input Received BOM Name  {}", bomName);
				List<Map<String, String>> responseMapper = new ArrayList<>();
				List<BomRentalSalesCodeAssoc> bomRentalSales = bomRentalSalesCodeAssocRepository.findByBomName(bomName);
				for (BomRentalSalesCodeAssoc bomRentalSalesCodeAssoc : bomRentalSales) {
					Map<String, String> bomMapper = new HashMap<>();
					bomMapper.put("BOM_NAME", bomRentalSalesCodeAssoc.getBomName());
					bomMapper.put("RENTAL_CODE", bomRentalSalesCodeAssoc.getRentalCode());
					bomMapper.put("SALES_CODE", bomRentalSalesCodeAssoc.getSalesCode());
					responseMapper.add(bomMapper);
				}
				String res = Utils.convertObjectToJson(responseMapper);
				LOGGER.info("Product Catelog getBomRentalSalesDetails {}", res);
				return res;
			}
		} catch (Exception e) {
			LOGGER.error("error in getting getBomRentalSalesDetails ", e);
		}
		return response;

	}

	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cpe.bom.intl.queue}") })
	public String processIntlbomDetails(String request) throws TclCommonException {

		String response = "";
		try {
			if (request != null) {
				List<GvpnInternationalCpeDto> listCpeIntl = Utils.fromJson(request, new TypeReference<List<GvpnInternationalCpeDto>>() {
				});
				LOGGER.info("Input Received cpe intl BOM  {}",listCpeIntl);
				if (listCpeIntl!=null) {
					Set<ResourceDto> resourceDto = productFamilyService.getCpeInternationalBomDetails(listCpeIntl);
					LOGGER.info("Getting the International BOM Details {} :: Size {}",resourceDto,resourceDto.size());
					CpeDetails cpeDeatils = new CpeDetails();
					List<CpeBom> cpeBomList=new ArrayList<CpeBom>();
					List<CpeBomDetails> cpeBomDetailsList=new ArrayList<CpeBomDetails>();
					if (resourceDto != null) {
						CpeBom cpebom = new CpeBom();
						resourceDto.forEach(res -> {
							cpebom.setBomName(res.getBomName());
							CpeBomDetails cpeBomDetails = new CpeBomDetails();
							cpeBomDetails.setLongDesc(res.getLongDesc());
							cpeBomDetails.setListPrice(res.getListPrice());
							cpeBomDetails.setProductCode(res.getProductCode());
							cpeBomDetails.setProductCategory(res.getProductCategory());
							cpeBomDetailsList.add(cpeBomDetails);
						});
						cpebom.setCpeBomDetails(cpeBomDetailsList);
						cpeBomList.add(cpebom);
						cpeDeatils.setCpeBoms(cpeBomList);
					}
					String res=Utils.convertObjectToJson(cpeDeatils);
					LOGGER.info("Product Catelog Internationalresponse {}",res);
					return res;
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting international bom details  ", e);
		}
		return response;

	}
	
	
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cpe.bom.ntw.products.queue}") })
	public String getBomDetailsForNtwProducts(String request) throws TclCommonException {
		String response = "";
		try {
			if (request != null) {
				List<String> list = Arrays.asList((request).split(","));
				LOGGER.info("Input Received {}", list);
				if (!list.isEmpty()) {
					Set<CpeBomDto> cpeBom = productFamilyService.getCpeBomDetailsForNtwProducts(list);
					LOGGER.info("Getting the BOM Details {} :: Size {}", cpeBom, cpeBom.size());
					CpeDetails cpeDeatils = new CpeDetails();
					if (cpeBom != null) {
						cpeBom.forEach(cpe -> {
							CpeBom details = new CpeBom();

							details.setBomName(cpe.getBomName());
							details.setUniCode(cpe.getUniCode());
							if (cpe.getResources() != null) {
								cpe.getResources().forEach(resource -> {
									CpeBomDetails bom = new CpeBomDetails();
									bom.setLongDesc(resource.getLongDesc());
									bom.setListPrice(resource.getListPrice());
									bom.setListPriceCurrencyId(resource.getListPriceCurrencyId());
									bom.setProductCode(resource.getProductCode());
									bom.setHsnCode(resource.getHsnCode());
									bom.setProviderId(resource.getProviderId());
									bom.setProductCategory(resource.getProductCategory());
									details.getCpeBomDetails().add(bom);
								});
							}
							cpeDeatils.getCpeBoms().add(details);
						});
					}
					String res = Utils.convertObjectToJson(cpeDeatils);
					LOGGER.info("Product Catelog response {}", res);
					return res;
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting bom details  ", e);
		}
		return response;
	}

	/**
	 * Queue to get cpe bom intl for gsc gvpn
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cpe.bom.gsc.gvpn.intl.queue}") })
	public String processGscGvpnIntlbomDetails(String request){
		String response = "";
		try {
			if (Objects.nonNull(request)) {
				List<GvpnInternationalCpeDto> listCpeIntl = Utils.fromJson(request, new TypeReference<List<GvpnInternationalCpeDto>>() {
				});
				LOGGER.info("Input Received cpe gsc gvpn intl BOM  {}", listCpeIntl);
				if (Objects.nonNull(listCpeIntl) && !listCpeIntl.isEmpty()) {
					Set<ResourceDto> resourceDto = productFamilyService.getCpeInternationalBomDetailsForGscGvpn(listCpeIntl);
					LOGGER.info("Getting the International BOM Details  of gsc gvpn {} :: Size {}", resourceDto, resourceDto.size());
					CpeDetails cpeDeatils = new CpeDetails();
					List<CpeBom> cpeBomList = new ArrayList<CpeBom>();
					List<CpeBomDetails> cpeBomDetailsList = new ArrayList<CpeBomDetails>();
					if (Objects.nonNull(resourceDto) && !resourceDto.isEmpty()) {
						CpeBom cpebom = new CpeBom();
						resourceDto.forEach(res -> {
							cpebom.setBomName(res.getBomName());
							CpeBomDetails cpeBomDetails = new CpeBomDetails();
							cpeBomDetails.setLongDesc(res.getLongDesc());
							cpeBomDetails.setListPrice(res.getListPrice());
							cpeBomDetails.setProductCode(res.getProductCode());
							cpeBomDetails.setProductCategory(res.getProductCategory());
							cpeBomDetails.setQuantity(res.getQuantity());
							cpeBomDetailsList.add(cpeBomDetails);
						});
						cpebom.setCpeBomDetails(cpeBomDetailsList);
						cpeBomList.add(cpebom);
						cpeDeatils.setCpeBoms(cpeBomList);
					}
					String res = Utils.convertObjectToJson(cpeDeatils);
					LOGGER.info("Product Catalog International gsc gvpn response {}", res);
					return res;
				}
				else{
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting international bom details for gsc gvpn ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cpe.bom.details.queue}") })
	public String getCpeVarientByBandwidth(String cpeBomRequest) {
		String cpeVarient = null;
		if(StringUtils.isEmpty(cpeBomRequest)) {
			LOGGER.error("No request to process cpe bom details.");
		}else {
			try {
				Map<String,Object> requestMap = (HashMap)Utils.convertJsonToObject(cpeBomRequest, HashMap.class);
				Double bw = (Double)requestMap.get("bandwidth");
				String portInterface = (String)requestMap.get("portInterface");
				String routingProtocol = (String)requestMap.get("routingProtocol");
				String productName = (String)requestMap.get("product");
				LOGGER.info("Input request values {} , {} , {}, {}",bw,portInterface,routingProtocol,requestMap.get("cpeManagementOption"));
				Set<CpeBomDto> cpeBomSet = null;
				if(productName.equalsIgnoreCase("IAS"))
					cpeBomSet = productFamilyService.getCpeBom(bw.intValue(), portInterface, routingProtocol);
				else if(productName.equalsIgnoreCase("GVPN")) {
					String managementType = (String)requestMap.get("cpeManagementOption");
					cpeBomSet = new HashSet<>(gvpnProductService.getCpeBom(bw, portInterface, routingProtocol,managementType,"macd"));
				}
				LOGGER.info("Cpe bom details size {}", cpeBomSet.size());
				if(cpeBomSet!=null && !cpeBomSet.isEmpty())
					cpeVarient = cpeBomSet.stream().findFirst().get().getBomName();
			}catch(Exception e) {
				LOGGER.error("Error in processing cpe bom data", e);
			}
		}
		return cpeVarient;
	}

	/**
	 * Queue to get gsc gvpn bom details
	 *
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cpe.gsc.gvpn.bom.queue}") })
	public String processGscGvpnbomDetails(String request) {
		String response = "";
		try {
			if (Objects.nonNull(request)) {
				List<GvpnInternationalCpeDto> listCpe = Utils.fromJson(request, new TypeReference<List<GvpnInternationalCpeDto>>() {
				});
				LOGGER.info("Input Received for India - gsc gvpn cpe{}", listCpe);
				if (Objects.nonNull(listCpe) && !listCpe.isEmpty()) {
					Set<ResourceDto> resourceDto = productFamilyService.getCpeBomDetailsForGscGvpn(listCpe);
					LOGGER.info("Getting the International BOM Details for india of gsc gvpn {} :: Size {}", resourceDto, resourceDto.size());
					CpeDetails cpeDeatils = new CpeDetails();
					List<CpeBom> cpeBomList = new ArrayList<CpeBom>();
					List<CpeBomDetails> cpeBomDetailsList = new ArrayList<CpeBomDetails>();
					if (Objects.nonNull(resourceDto) && !resourceDto.isEmpty()) {
						CpeBom cpebom = new CpeBom();
						resourceDto.forEach(res -> {
							cpebom.setBomName(res.getBomName());
							CpeBomDetails cpeBomDetails = new CpeBomDetails();
							cpeBomDetails.setLongDesc(res.getLongDesc());
							cpeBomDetails.setListPrice(res.getListPrice());
							cpeBomDetails.setProductCode(res.getProductCode());
							cpeBomDetails.setProductCategory(res.getProductCategory());
							cpeBomDetails.setQuantity(res.getQuantity());
							cpeBomDetailsList.add(cpeBomDetails);

						});
						cpebom.setCpeBomDetails(cpeBomDetailsList);
						cpeBomList.add(cpebom);
						cpeDeatils.setCpeBoms(cpeBomList);
					}
					response = Utils.convertObjectToJson(cpeDeatils);
					LOGGER.info("Product Catalog India gsc gvpn response {}", response);
//					return res;
				} else {
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting bom details in india - gsc gvpn ", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cpe.bom.product.catalog.queue}") })
	public String getEquivalentCpeBomForInventoryBom(String request) throws TclCommonException {
		String response = "";
		try {
			if (request != null) {
				LOGGER.info("getEquivalentCpeBomForInventoryBom request {}", request);
				BomInventoryCatalogAssocResponse cpeResponse = productFamilyService.getEquivalentProductCatalogBomNameForInventoryCpe(request);
				
					String res = Utils.convertObjectToJson(cpeResponse);
					LOGGER.info("BomInventoryCatalogAssocResponse cpe bom response {}", res);
					return res;
				
			}
		} catch (Exception e) {
			LOGGER.error("error in getting bom details  ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cpe.bom.verify.queue}") })
	public Boolean verifyCpeBomExist(String request) throws TclCommonException {
		Boolean response = false;
		try {
			if (request != null) {
				response = productFamilyService.verifyCpeByName(request);
				LOGGER.info("Checked CPE aviability and CPE Available {} ", response);
			}
		} catch (Exception e) {
			LOGGER.error("error in getting bom details  ", e);
		}
		return response;
	}

}

