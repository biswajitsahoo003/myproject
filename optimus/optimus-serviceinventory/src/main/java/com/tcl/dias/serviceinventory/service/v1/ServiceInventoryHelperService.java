package com.tcl.dias.serviceinventory.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tcl.dias.common.serviceinventory.bean.ScComponentAttributeBean;
import com.tcl.dias.common.serviceinventory.bean.ScSolutionComponentBean;
import com.tcl.dias.common.serviceinventory.bean.SdwanScOrderBean;
import com.tcl.dias.serviceinventory.entity.entities.SiSolutionComponent;
import com.tcl.dias.serviceinventory.entity.repository.SiSolutionComponentRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.serviceinventory.bean.CpeBean;
import com.tcl.dias.common.serviceinventory.bean.EthernetInterfaceBean;
import com.tcl.dias.common.serviceinventory.bean.IpAddressDetailBean;
import com.tcl.dias.common.serviceinventory.bean.MuxDetailsBean;
import com.tcl.dias.common.serviceinventory.bean.OptimusRfDataBean;
import com.tcl.dias.common.serviceinventory.bean.ScComponentBean;
import com.tcl.dias.common.serviceinventory.bean.ScContractInfoBean;
import com.tcl.dias.common.serviceinventory.bean.ScOrderAttributeBean;
import com.tcl.dias.common.serviceinventory.bean.ScOrderBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceDetailBean;
import com.tcl.dias.common.serviceinventory.bean.ScServiceSlaBean;
import com.tcl.dias.common.serviceinventory.bean.ServiceDetailBean;
import com.tcl.dias.common.serviceinventory.bean.ServiceQoBean;
import com.tcl.dias.common.serviceinventory.bean.UniswitchDetailBean;
import com.tcl.dias.common.serviceinventory.bean.VrfBean;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.dao.v1.ServiceInventoryDao;
import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIAttachment;
import com.tcl.dias.serviceinventory.entity.entities.SIComponent;
import com.tcl.dias.serviceinventory.entity.entities.SIComponentAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIContractInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIOrderAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceSla;
import com.tcl.dias.serviceinventory.entity.entities.ServiceInvRf;
import com.tcl.dias.serviceinventory.entity.entities.SiServiceContact;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetCommercialRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetComponentRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetRelationRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAttachmentRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIComponentAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIComponentRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIContractInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIOrderAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIOrderRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceDetailRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceSlaRepository;
import com.tcl.dias.serviceinventory.entity.repository.ServiceInvRfRepository;
import com.tcl.dias.serviceinventory.entity.repository.SiServiceAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SiServiceContactRepository;
import com.tcl.dias.serviceinventory.entity.repository.VwOrderServiceAssetInfoRepository;
import com.tcl.dias.serviceinventory.helper.mapper.ServiceInventoryHelperMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used to assign the task details
 *
 */
@Service
public class ServiceInventoryHelperService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryHelperService.class);

	@Autowired
	SIServiceSlaRepository siServiceSlaRepository;

	@Autowired
	SIOrderRepository siOrderRepository;

	@Autowired
	VwOrderServiceAssetInfoRepository vwOrderServiceAssetInfoRepository;

	@Autowired
	SiServiceContactRepository siServiceContactRepository;

	@Autowired
	SIServiceDetailRepository siServiceDetailRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	AuthTokenDetailRepository authTokenDetailRepository;

	@Autowired
	SIAssetRepository siAssetRepository;

	@Autowired
	SIAssetAttributeRepository siAssetAttributeRepository;

	@Autowired
	SIAssetRelationRepository siAssetRelationRepository;

	@Autowired
	SIServiceInfoRepository siServiceInfoRepository;

	@Autowired
	ServiceInventoryDao serviceInventoryDao;

	@Autowired
	RestClientService restClientService;

	@Autowired
	SiServiceAttributeRepository siServiceAttributeRepository;

	@Autowired
	VwOrderServiceAssetDetailSpecification vwOrderServiceAssetDetailSpecification;

	@Autowired
	SIAssetComponentRepository siAssetComponentRepository;

	@Autowired
	SIAssetCommercialRepository siAssetCommercialRepository;

	@Autowired
	ServiceInventoryHelperMapper serviceInventoryMapper;

	@Autowired
	SIContractInfoRepository siContractInfoRepository;

	@Autowired
	SIOrderAttributeRepository siOrderAttributeRepository;

	@Autowired
	ServiceInvRfRepository serviceInvRfRepository;

	@Autowired
	SIComponentAttributeRepository siComponentAttributeRepository;

	@Autowired
	SIComponentRepository siComponentRepository;

	@Autowired
	SIAttachmentRepository siAttachmentRepository;

	@Autowired
	SiSolutionComponentRepository siSolutionComponentRepository;

	/**
	 * Method to persist fulfillment data to service inventory
	 *
	 * @param ScOrderBean scOrderBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED,readOnly=false)
	public void processServiceInventoryData(ScOrderBean scOrderBean) {
		LOGGER.info("processServiceInventoryData method invoked");

		mapScOrderDetailsToSIEntity(scOrderBean);
		LOGGER.info("processServiceInventoryData method ends");
	}

	private void mapScOrderDetailsToSIEntity(ScOrderBean scOrderBean) {
		LOGGER.info("mapScOrderDetailsToSIEntity method invoked for order code:{}",scOrderBean.getOpOrderCode());
		// SIOrder
		SIOrder order = siOrderRepository.findFirstByOpOrderCodeOrderByIdDesc(scOrderBean.getOpOrderCode());
		
		Optional<ScServiceDetailBean> oScServiceDetailBean=scOrderBean.getScServiceDetails().stream().findFirst();
		String productName = null;
		ScServiceDetailBean scServiceDetailBean = null;

		if(oScServiceDetailBean.isPresent()){
			productName = oScServiceDetailBean.get().getErfPrdCatalogProductName();
			scServiceDetailBean = oScServiceDetailBean.get();
			LOGGER.info("Service Code to be processed {}", scServiceDetailBean.getUuid());
		}
		if(scOrderBean.getDemoFlag()!=null && scOrderBean.getDemoFlag().equalsIgnoreCase("Y") 
				&& scOrderBean.getOrderType()!=null && scOrderBean.getOrderType().equalsIgnoreCase("MACD")
				&& scOrderBean.getOrderCategory()!=null && scOrderBean.getOrderCategory().equalsIgnoreCase("DEMO_EXTENSION")) {
			LOGGER.info("Demo Extension Order::{}",scOrderBean.getOpOrderCode());
			for(ScServiceDetailBean scServiceDetail : scOrderBean.getScServiceDetails()) {
				if(scServiceDetail.getOrderType()!=null && scServiceDetail.getOrderType().equalsIgnoreCase("MACD") 
						&& scServiceDetail.getOrderCategory()!=null && scServiceDetail.getOrderCategory().equalsIgnoreCase("DEMO_EXTENSION")
						&& ("GVPN".equals(productName) || "IAS".equals(productName))) {
					LOGGER.info("Demo Extension Service Detail Id::{}",scServiceDetail.getId());
					
					SIServiceDetail activeSiServiceDetail=siServiceDetailRepository.findFirstByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(scServiceDetail.getUuid(), "Active", "Y",scServiceDetail.getUuid(), "Active", "Y");
					if(activeSiServiceDetail!=null) {
						LOGGER.info("Demo Extension Active Service Detail Id exists ::{}",activeSiServiceDetail.getId());
						activeSiServiceDetail.setOrderType(scServiceDetail.getOrderType());
						activeSiServiceDetail.setOrderCategory(scServiceDetail.getOrderCategory());
						activeSiServiceDetail.setSiOrderUuid(scServiceDetail.getScOrderUuid());
						activeSiServiceDetail.setMrc(scServiceDetail.getMrc());
						activeSiServiceDetail.setNrc(scServiceDetail.getNrc());
						activeSiServiceDetail.setArc(scServiceDetail.getArc());
						activeSiServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
						activeSiServiceDetail.setUpdatedBy("Optimus_O2C");
						siServiceDetailRepository.saveAndFlush(activeSiServiceDetail);
					}

					SIOrder activeSiOrder=activeSiServiceDetail.getSiOrder();
					if(activeSiOrder!=null) {
						LOGGER.info("Demo Extension Active Order Detail Id exists ::{}",activeSiOrder.getId());
						activeSiOrder.setUuid(scOrderBean.getUuid());
						activeSiOrder.setOpOrderCode(scOrderBean.getOpOrderCode());
						activeSiOrder.setOrderType(scOrderBean.getOrderType());
						activeSiOrder.setOrderCategory(scOrderBean.getOrderCategory());
						activeSiOrder.setTpsCrmOptyId(scOrderBean.getSfdcOptyId());
						activeSiOrder.setUpdatedDate(new Timestamp(new Date().getTime()));
						activeSiOrder.setUpdatedBy("Optimus_O2C");
						siOrderRepository.saveAndFlush(activeSiOrder);
					}
					
					ScContractInfoBean currentScContractInfo=scOrderBean.getScContractInfos().stream().findFirst().orElse(null);
					SIContractInfo activeSiContractInfo = siContractInfoRepository.findFirstBySiOrder_id(activeSiServiceDetail.getSiOrder().getId());
					if(activeSiContractInfo!=null && currentScContractInfo!=null && currentScContractInfo.getOrderTermInMonths()!=null) {
						LOGGER.info("Demo Extension Active Contract Info Id exists ::{}",activeSiContractInfo.getId());
							activeSiContractInfo.setOrderTermInMonths(currentScContractInfo.getOrderTermInMonths());
							activeSiContractInfo.setMrc(currentScContractInfo.getMrc());
							activeSiContractInfo.setNrc(currentScContractInfo.getNrc());
							activeSiContractInfo.setArc(currentScContractInfo.getArc());
							activeSiContractInfo.setUpdatedDate(new Timestamp(new Date().getTime()));
							activeSiContractInfo.setUpdatedBy("Optimus_O2C");
							siContractInfoRepository.saveAndFlush(activeSiContractInfo);
					}
					
					ScServiceAttributeBean currentDemoPeriodInDays = scServiceDetail.getScServiceAttributes().stream()
							.filter(attr -> attr.getAttributeName().equalsIgnoreCase("DEMO_PERIOD_IN_DAYS")).findFirst().orElse(null);
					if(currentDemoPeriodInDays!=null && currentDemoPeriodInDays.getAttributeValue()!=null) {
						LOGGER.info("Demo Period In Days exists::{} for Service Detail Id::{}",currentDemoPeriodInDays.getAttributeValue(),scServiceDetail.getId());
						SIServiceAttribute activeDemoPeriodInDays=siServiceAttributeRepository.findBySiServiceDetailAndAttributeNameOrderByIdDesc(activeSiServiceDetail,"DEMO_PERIOD_IN_DAYS");
						if(activeDemoPeriodInDays!=null) {
							LOGGER.info("Demo Extension Active Demo In Period In Days exists::{} for Service Id::{}",activeDemoPeriodInDays.getId());
							activeDemoPeriodInDays.setAttributeValue(currentDemoPeriodInDays.getAttributeValue());
							activeDemoPeriodInDays.setUpdatedDate(new Timestamp(new Date().getTime()));
							activeDemoPeriodInDays.setUpdatedBy("Optimus_O2C");
							siServiceAttributeRepository.saveAndFlush(activeDemoPeriodInDays);
						}
					}
					
					Map<String, String> atMap= new HashMap<>();
					for(ScComponentBean scComponentBean:scServiceDetail.getScComponentBeans()) {
						scComponentBean.getScComponentAttributeBeans().stream().forEach(scCompAttr -> {
							if(scCompAttr.getAttributeName().equalsIgnoreCase("demoBillEndDate")) {
								LOGGER.info("Demo Bill End Date exists::{} for Service Detail Id::{}",scCompAttr.getAttributeValue());
								atMap.put("demoBillEndDate", scCompAttr.getAttributeValue());
							}else if(scCompAttr.getAttributeName().equalsIgnoreCase("demoExtensionDays")) {
								LOGGER.info("Demo Extension Days exists::{} for Service Detail Id::{}",scCompAttr.getAttributeValue());
								atMap.put("demoExtensionDays", scCompAttr.getAttributeValue());
							}else if(scCompAttr.getAttributeName().equalsIgnoreCase("demoDays")) {
								LOGGER.info("Demo Days exists::{} for Service Detail Id::{}",scCompAttr.getAttributeValue());
								atMap.put("demoDays", scCompAttr.getAttributeValue());
							}
						});
					}
					updateAttributes(activeSiServiceDetail.getId(),atMap,"LM","A");
				}
			}
		}else if ("RENEWALS".equalsIgnoreCase(scServiceDetailBean.getOrderType())) {
			LOGGER.info("RENEWALS Order:{}", scOrderBean.getOpOrderCode());
			if ("GVPN".equals(productName) || "IAS".equals(productName)) {
				LOGGER.info("GVPN or IAS Renewal Order::{}", scOrderBean.getOpOrderCode());
				for (ScServiceDetailBean scServiceDetail : scOrderBean.getScServiceDetails()) {
					LOGGER.info("Renewal GVPN or IAS Service Detail Bean Id::{}", scServiceDetail.getId());
					SIServiceDetail activeSiServiceDetail = siServiceDetailRepository
							.findFirstByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(
									scServiceDetail.getUuid(), "Active", "Y", scServiceDetail.getUuid(), "Active", "Y");
					SIOrder activeSiOrder = activeSiServiceDetail.getSiOrder();
					updateRenewalBaseServiceDetails(scServiceDetail,activeSiServiceDetail);
					updateRenewalOrderDetails(scOrderBean,activeSiOrder);
					Set<ScOrderAttributeBean> scOrderAttributes=scOrderBean.getScOrderAttributes();
					updateRenewalOrderAttributes(scOrderAttributes,activeSiOrder);
					Set<ScContractInfoBean> scContractInfos = scOrderBean.getScContractInfos();
					updateRenewalContractBaseDetails(scContractInfos,activeSiOrder);
					updateRenewalComponentAttributes(scServiceDetail,activeSiServiceDetail);
					ScOrderAttributeBean commercialAttributeBean = scOrderBean.getScOrderAttributes().stream()
							.filter(soa -> "IS_COMMERCIAL".equalsIgnoreCase(soa.getAttributeName())).findFirst().orElse(null);
					if (commercialAttributeBean != null && "Y".equalsIgnoreCase(commercialAttributeBean.getAttributeValue())) {
						LOGGER.info("GVPN or IAS Renewal Service Id with Commercial::{}", scServiceDetail.getId());
						updateRenewalServiceBillingDetails(scServiceDetail,activeSiServiceDetail);
						updateRenewalContractBillingDetails(scContractInfos,activeSiOrder);
					}
				}
			} else if ("NPL".equals(productName)) {
				LOGGER.info("NPL Renewal Order::{}", scOrderBean.getOpOrderCode());
				for (ScServiceDetailBean scServiceDetail : scOrderBean.getScServiceDetails()) {
					LOGGER.info("Renewal NPL Service Detail Bean Id::{}", scServiceDetail.getId());
					List<SIServiceDetail> activeSiServiceDetailList = siServiceDetailRepository
							.findByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(
									scServiceDetailBean.getUuid(), "Active", CommonConstants.Y,
									scServiceDetailBean.getUuid(), "Active", CommonConstants.Y);
					if (activeSiServiceDetailList != null && !activeSiServiceDetailList.isEmpty()) {
						for (SIServiceDetail activeSiServiceDetail : activeSiServiceDetailList) {
							LOGGER.info("NPL SI Active Service Detail Id exists ::{}", activeSiServiceDetail.getId());
							SIOrder activeSiOrder = activeSiServiceDetail.getSiOrder();
							updateRenewalBaseServiceDetails(scServiceDetail, activeSiServiceDetail);
							updateRenewalOrderDetails(scOrderBean, activeSiOrder);
							Set<ScOrderAttributeBean> scOrderAttributes=scOrderBean.getScOrderAttributes();
							updateRenewalOrderAttributes(scOrderAttributes,activeSiOrder);
							Set<ScContractInfoBean> scContractInfos = scOrderBean.getScContractInfos();
							updateRenewalContractBaseDetails(scContractInfos,activeSiOrder);
							updateRenewalComponentAttributes(scServiceDetail,activeSiServiceDetail);
							ScOrderAttributeBean commercialAttributeBean = scOrderBean.getScOrderAttributes().stream()
									.filter(soa -> "IS_COMMERCIAL".equalsIgnoreCase(soa.getAttributeName())).findFirst().orElse(null);
							if (commercialAttributeBean != null && "Y".equalsIgnoreCase(commercialAttributeBean.getAttributeValue())) {
								LOGGER.info("NPL Renewal Service Id with Commercial::{}", scServiceDetail.getId());
								updateRenewalServiceBillingDetails(scServiceDetail, activeSiServiceDetail);
								updateRenewalContractBillingDetails(scContractInfos,activeSiOrder);
							}
						}
					}
				}
			}
		}else if ("MACD".equalsIgnoreCase(scServiceDetailBean.getOrderType()) && scServiceDetailBean.getOrderSubCategory()!=null && scServiceDetailBean.getOrderSubCategory().toLowerCase().contains("novation")) {
			LOGGER.info("Novation Order:{}", scOrderBean.getOpOrderCode());
			if ("NPL".equals(productName)) {
				LOGGER.info("NPL Novation Order::{}", scOrderBean.getOpOrderCode());
				for (ScServiceDetailBean scServiceDetail : scOrderBean.getScServiceDetails()) {
					LOGGER.info("Novation NPL Service Detail Bean Id::{}", scServiceDetail.getId());
					List<SIServiceDetail> activeSiServiceDetailList = siServiceDetailRepository
							.findByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(
									scServiceDetailBean.getUuid(), "Active", CommonConstants.Y,
									scServiceDetailBean.getUuid(), "Active", CommonConstants.Y);
					if (activeSiServiceDetailList != null && !activeSiServiceDetailList.isEmpty()) {
						for (SIServiceDetail activeSiServiceDetail : activeSiServiceDetailList) {
							LOGGER.info("NPL SI Active Service Detail Id exists ::{}", activeSiServiceDetail.getId());
							SIOrder activeSiOrder = activeSiServiceDetail.getSiOrder();
							updateNovationServiceDetails(scServiceDetail, activeSiServiceDetail);
							updateNovationOrderDetails(scOrderBean, activeSiOrder);
							Set<ScContractInfoBean> scContractInfos = scOrderBean.getScContractInfos();
							updateNovationContractDetails(scContractInfos,activeSiOrder);
							updateNovationServiceRfDetails(scServiceDetail,scOrderBean);
						}
					}
				}
			}else {
				LOGGER.info("Other than NPL Novation Order::{}", scOrderBean.getOpOrderCode());
				for (ScServiceDetailBean scServiceDetail : scOrderBean.getScServiceDetails()) {
					LOGGER.info("Novation  Service Detail Bean Id::{}", scServiceDetail.getId());
					SIServiceDetail activeSiServiceDetail = siServiceDetailRepository
							.findFirstByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(
									scServiceDetail.getUuid(), "Active", "Y", scServiceDetail.getUuid(), "Active", "Y");
					SIOrder activeSiOrder = activeSiServiceDetail.getSiOrder();
					updateNovationServiceDetails(scServiceDetail, activeSiServiceDetail);
					updateNovationOrderDetails(scOrderBean, activeSiOrder);
					Set<ScContractInfoBean> scContractInfos = scOrderBean.getScContractInfos();
					updateNovationContractDetails(scContractInfos,activeSiOrder);
					updateNovationServiceRfDetails(scServiceDetail,scOrderBean);
				}
			} 
		}else if(Objects.isNull(order) || (Objects.nonNull(order) && (Objects.nonNull(productName) && ("GVPN".equals(productName) || "IAS".equals(productName) ||
				"NPL".equals(productName) || productName.contains("IZO") || productName.contains("BYON") || productName.contains("DIA") || "UCAAS".equals(productName) ||
				"GSIP".equals(productName) || "WEBEX_SOLUTION".equals(productName) || "Microsoft Cloud Solutions".equals(productName) || "TEAMSDR_SOLUTION".equals(productName))))) {
			boolean isContractInfoExists = false;
			SIContractInfo existingSIContractInfo = null;
			if((Objects.nonNull(productName) && ("GVPN".equals(productName) || "IAS".equals(productName))) && Objects.nonNull(order)) {
				if(!CollectionUtils.isEmpty(order.getSiContractInfos())) {
					Optional<SIContractInfo> siContractInfoOptional = order.getSiContractInfos().stream().findFirst();
					if(siContractInfoOptional.isPresent()) {
						existingSIContractInfo = siContractInfoOptional.get();
					}
					isContractInfoExists = true;
				}
			}

			ServiceQoBean serviceQoBean = scOrderBean.getServiceQoBean();
			ServiceDetailBean serviceDetailBean = scOrderBean.getServiceDetailBean();

			LOGGER.info("ServiceQoBean : {}", serviceQoBean);
            LOGGER.info("serviceDetailBean Data : {}",serviceDetailBean);

            // For NPL, 2 records are created, so both are terminated
            if ("NPL".equals(productName)) {
				LOGGER.info("NPL");
				if (serviceDetailBean.getUuid() != null) {
					LOGGER.info("NPL UUID exists:: {}", serviceDetailBean.getUuid());
					List<SIServiceDetail> existingSiServiceDetailList = siServiceDetailRepository
							.findByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(
									scServiceDetailBean.getUuid(),"Active",
									CommonConstants.Y, scServiceDetailBean.getUuid(), "Active",
									CommonConstants.Y);
					if (existingSiServiceDetailList != null && !existingSiServiceDetailList.isEmpty()) {
						for(SIServiceDetail existingSiServiceDetail:existingSiServiceDetailList) {
							LOGGER.info("1-SI-Optimus-Terminated-ServiceId: {}", serviceDetailBean.getUuid());
							existingSiServiceDetail.setServiceStatus("Terminated");
							existingSiServiceDetail.setCircuitStatus("Terminated");
							existingSiServiceDetail.setIsActive("N");
							existingSiServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
							existingSiServiceDetail.setUpdatedBy("Optimus_O2C");
							siServiceDetailRepository.saveAndFlush(existingSiServiceDetail);
						}
					}
				}

				if (scServiceDetailBean.getOrderSubCategory() != null
						&& scServiceDetailBean.getOrderSubCategory().toLowerCase().contains("parallel")
						&& scServiceDetailBean.getParentUuid() != null
						&& !scServiceDetailBean.getParentUuid().isEmpty()) {
					LOGGER.info("Parent Uuid to be terminated : {}", scServiceDetailBean.getParentUuid());
					List<SIServiceDetail> parentSiServiceDetailList = siServiceDetailRepository
							.findByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(
									scServiceDetailBean.getParentUuid(),"Active",
									CommonConstants.Y, scServiceDetailBean.getParentUuid(), "Active",
									CommonConstants.Y);
					if (parentSiServiceDetailList != null && !parentSiServiceDetailList.isEmpty()){
						for(SIServiceDetail parentSiServiceDetail:parentSiServiceDetailList) {
							LOGGER.info("2-SI-Optimus-Terminated-ServiceId: {}", serviceDetailBean.getParentUuid());
							parentSiServiceDetail.setServiceStatus("Terminated");
							parentSiServiceDetail.setCircuitStatus("Terminated");
							parentSiServiceDetail.setIsActive("N");
							parentSiServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
							parentSiServiceDetail.setUpdatedBy("Optimus_O2C");
							siServiceDetailRepository.saveAndFlush(parentSiServiceDetail);
						}
					}
				}
			}
			// ILL and GVPN
			else {
				LOGGER.info("Product - {}", productName);
				if (serviceDetailBean.getUuid() != null) {
					LOGGER.info("Other than NPL UUID exists:: {}", serviceDetailBean.getUuid());
					SIServiceDetail existingSiServiceDetail = siServiceDetailRepository
							.findFirstByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(
									scServiceDetailBean.getUuid(),"Active",
									CommonConstants.Y, scServiceDetailBean.getUuid(), "Active",
									CommonConstants.Y);
					// If record exists, set as inactive
					if (existingSiServiceDetail != null) {
						LOGGER.info("3-SI-Optimus-Terminated-ServiceId: {} with Id::{}", serviceDetailBean.getUuid(),existingSiServiceDetail.getId());
						existingSiServiceDetail.setServiceStatus("Terminated");
						existingSiServiceDetail.setCircuitStatus("Terminated");
						existingSiServiceDetail.setIsActive("N");
						existingSiServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
						existingSiServiceDetail.setUpdatedBy("Optimus_O2C");
						siServiceDetailRepository.saveAndFlush(existingSiServiceDetail);
					}
				}
            	
            	if((scServiceDetailBean.getOrderSubCategory() != null
						&& scServiceDetailBean.getOrderSubCategory().toLowerCase().contains("parallel")) 
						&& serviceDetailBean.getParentUuid() != null && !serviceDetailBean.getParentUuid().isEmpty()) {
					LOGGER.info("ParentUUID : {}", serviceDetailBean.getParentUuid());
					SIServiceDetail parentSiServiceDetail = siServiceDetailRepository
							.findFirstByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveOrUuidAndServiceStatusIgnoreCaseAndIsActive(
									scServiceDetailBean.getParentUuid(),"Active",
									CommonConstants.Y, scServiceDetailBean.getParentUuid(), "Active",
									CommonConstants.Y);
					if (parentSiServiceDetail != null) {
						LOGGER.info("4-SI-Optimus-Terminated-ServiceId: {} with Id::{}", serviceDetailBean.getParentUuid(),parentSiServiceDetail.getId());
						parentSiServiceDetail.setServiceStatus("Terminated");
						parentSiServiceDetail.setCircuitStatus("Terminated");
						parentSiServiceDetail.setIsActive("N");
						parentSiServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
						parentSiServiceDetail.setUpdatedBy("Optimus_O2C");
						siServiceDetailRepository.saveAndFlush(parentSiServiceDetail);
					}
				}
			}

			SIOrder siOrderEntity = null;
			if (Objects.nonNull(order)) {
				siOrderEntity = order;
				siOrderEntity.setPartnerCuid(scOrderBean.getPartnerCuid());
			} else {
				siOrderEntity = serviceInventoryMapper.mapScOrderBeanToEntity(scOrderBean);
			}
			Set<SIOrderAttribute> siOrderAttrs = new HashSet<>();

			// SIOrderAttr
			if (siOrderEntity.getSiOrderAttributes() == null || siOrderEntity.getSiOrderAttributes().isEmpty()) {
				siOrderAttrs = mapScOrderAttrToEntity(scOrderBean, siOrderEntity);
			}

			EthernetInterfaceBean ethernetInterfaceBean = scOrderBean.getEthernetInterfaceBean();

			Map<String, String> siServiceAttrMap = new HashMap<>();

			Set<SIContractInfo> siContractInfos = new HashSet<>();
			Map<String, String> siAssetMap = new HashMap<>();

			Set<SIServiceDetail> siServiceDetails = new HashSet<>();
			Set<SIComponentAttribute> siComponentAttributes = new HashSet<>();
			Set<SIServiceSla> siServiceSlas = new HashSet<>();
			Set<SIAttachment> siAttachments = new HashSet<>();
			List<SIComponent> siComponents = new ArrayList<>();
			Map<Integer, List<SIComponent>> siComponentMap = new HashMap<>();

			if("NPL".equalsIgnoreCase(productName)) {
				saveSiServiceDetailsForTypeB(scOrderBean, scServiceDetailBean, siOrderEntity, siServiceAttrMap, siOrderAttrs, serviceDetailBean, isContractInfoExists, existingSIContractInfo);
			} else {
				for(ScServiceDetailBean scServiceDetail : scOrderBean.getScServiceDetails()) {
					LOGGER.info("Other than NPL Service Id:: {}", scServiceDetail.getId());
					SIServiceDetail siServiceEntity = serviceInventoryMapper.mapScServiceEntityToBean(scServiceDetail);

					saveRfData(scServiceDetail);

					if(Objects.nonNull(serviceQoBean)) {
						populateServiceQoAttributes(serviceQoBean, siServiceAttrMap);
					}
					if(Objects.nonNull(serviceDetailBean)) {
						populateServiceDetailAttributes(serviceDetailBean, siServiceAttrMap);
					}
					if(Objects.nonNull(ethernetInterfaceBean)) {
						populateEthernetInterfaceAttributes(ethernetInterfaceBean, siServiceAttrMap);
					}

					if(!CollectionUtils.isEmpty(scOrderBean.getUniswitchDetailBeans())) {
						scOrderBean.getUniswitchDetailBeans().forEach(uniswitchDetailBean -> {
							populateUniswitchAttributes(uniswitchDetailBean, siServiceAttrMap);
						});
					}

					if(!CollectionUtils.isEmpty(scOrderBean.getCpeBeans())) {
						scOrderBean.getCpeBeans().forEach(cpeBean -> {
							populateCpeAttributes(cpeBean, siServiceAttrMap);
						});
					}

					populateOtherAttributes(scOrderBean, siServiceAttrMap, scServiceDetail, siServiceEntity);

					try {
						if (!CollectionUtils.isEmpty(scServiceDetail.getScServiceSlas())) {
							scServiceDetail.getScServiceSlas().forEach(scServiceSlaBean -> {
								siServiceSlas.add(mapScServiceSlaToBean(scServiceSlaBean, siServiceEntity));
							});
						}
					}
					catch(Exception ex){
						LOGGER.error("Error while saving scServiceSlas for service Id {} -> {}",scOrderBean.getUuid(), ex);
					}

					// SIServiceContact
					Set<SiServiceContact> siServiceContacts = new HashSet<>();
					SiServiceContact siServiceContact = serviceInventoryMapper.mapServiceContact(scServiceDetail, scOrderBean);
					siServiceContact.setSiServiceDetail(siServiceEntity);
					siServiceContacts.add(siServiceContact);
					scServiceDetail.getScAttachments().stream().forEach(scAttachmentBean -> {
						siAttachments.add(serviceInventoryMapper.mapScServiceAttachmentToEntity(scAttachmentBean, siServiceEntity));
					});

					String cpeModel = "";

					String billingCurrency[] = {null};
					String crnId = null;
					String billingType[] = {null};
					for (ScOrderAttributeBean scOrderAttribute : scOrderBean.getScOrderAttributes()) {
						if ("Billing Currency".equalsIgnoreCase(scOrderAttribute.getAttributeName())) {
							billingCurrency[0] = scOrderAttribute.getAttributeValue();
						}
						if ("CRN_ID".equalsIgnoreCase(scOrderAttribute.getAttributeName())) {
							crnId = scOrderAttribute.getAttributeValue();
						}
						if ("BILLING_TYPE".equals(scOrderAttribute.getAttributeName()) || "Billing Type".equals(scOrderAttribute.getAttributeName())) {
							billingType[0] = scOrderAttribute.getAttributeValue();
						}
					}

					siServiceEntity.setCircuitStatus(serviceDetailBean.getServiceStatus());

					List<ScComponentBean> scComponentBeans = scServiceDetail.getScComponentBeans();

					if(!CollectionUtils.isEmpty(scComponentBeans) && ("Microsoft Cloud Solutions".equals(productName) || "TEAMSDR_SOLUTION".equals(productName) || scServiceDetail.getUuid().contains("UCDR"))){
						LOGGER.info("Inside replace logic of endpoint to cpe for teamsDR {} ",productName);
						replaceAttributeNameEndpointToCpe(scComponentBeans);
					}
					if (!CollectionUtils.isEmpty(scComponentBeans)) {
						for (ScComponentBean scComponentBean : scComponentBeans) {
							siComponentMap = new HashMap<>();
							SIComponent siComponent = serviceInventoryMapper.mapScComponentToEntity(scComponentBean);
							if (!CollectionUtils.isEmpty(scComponentBean.getScComponentAttributeBeans())) {
								scComponentBean.getScComponentAttributeBeans().forEach(attrBean -> {
									if ("nonCpeAccountNumber".equalsIgnoreCase(attrBean.getAttributeName())) {
										siServiceEntity.setBillingAccountId(attrBean.getAttributeValue());
									}
									if ("commissioningDate".equalsIgnoreCase(attrBean.getAttributeName())) {
										siServiceEntity.setServiceCommissionedDate(Timestamp.valueOf(attrBean.getAttributeValue() + " 00:00:00"));
										siAssetMap.put("commissioningDate", attrBean.getAttributeValue());
									}
									if ("cpeType".equalsIgnoreCase(attrBean.getAttributeName())) {
										siAssetMap.put("cpeType", attrBean.getAttributeValue());
									}
									if ("cpeInstallationPrDate".equalsIgnoreCase(attrBean.getAttributeName())) {
										siAssetMap.put("cpeInstallationPrDate", attrBean.getAttributeValue());
									}
									if ("cpeSupplyHardwarePrVendorName".equalsIgnoreCase(attrBean.getAttributeName())) {
										siAssetMap.put("cpeSupplyHardwarePrVendorName", attrBean.getAttributeValue());
									}
									if("popSiteCode".equalsIgnoreCase(attrBean.getAttributeName())){
										siServiceEntity.setPopSiteCode(attrBean.getAttributeValue());
									}
									if("sourceCity".equalsIgnoreCase(attrBean.getAttributeName())){
										if(Objects.nonNull(siServiceEntity.getSourceCity())) {
											siServiceEntity.setSourceCity(attrBean.getAttributeValue());
										}
									}
									if("sourceState".equalsIgnoreCase(attrBean.getAttributeName())){
										if(Objects.nonNull(siServiceEntity.getSourceState())) {
											siServiceEntity.setSourceState(attrBean.getAttributeValue());
										}
									}
									SIComponentAttribute siComponentAttribute = serviceInventoryMapper
											.mapScComponentAttrToEntity(attrBean, siComponent);
									siComponentAttributes.add(siComponentAttribute);
								});
								updateAdditionalComponentAttributes(siServiceAttrMap, scComponentBean.getScComponentAttributeBeans());
							}
							siComponent.setSIComponentAttributes(siComponentAttributes);
							siComponents.add(siComponent);
						}
						siComponentMap.put(scServiceDetail.getErfScServiceId(), siComponents);
					}

					for (ScServiceAttributeBean scServiceAttributeBean : scServiceDetail.getScServiceAttributes()) {
						if ("VPN Topology".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
							siServiceEntity.setServiceTopology(scServiceAttributeBean.getAttributeValue());
						}
						if ("Site Type".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
							siServiceEntity.setSiteTopology(scServiceAttributeBean.getAttributeValue());
						}
						if ("Service type".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
							siServiceEntity.setServiceType(scServiceAttributeBean.getAttributeValue());
						}
						if ("bts_site_name".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
							siServiceAttrMap.put("BSO_NAME", scServiceAttributeBean.getAttributeValue());
						}
						if ("Selected_solution_BW".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
							siServiceAttrMap.put("B_END_LL_BANDWIDTH", scServiceAttributeBean.getAttributeValue());
						}
						if ("service_variant".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
							siServiceAttrMap.put("PRODUCT_FLAVOUR", scServiceAttributeBean.getAttributeValue());
						}
						if("Resiliency".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())){
							siServiceEntity.setResiliencyInd(scServiceAttributeBean.getAttributeValue());
						}
						if("cpe_variant".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())){
							cpeModel = scServiceAttributeBean.getAttributeValue();
						}
					}

					updateAdditionalServiceAttributes(siServiceAttrMap, scServiceDetail.getScServiceAttributes(), productName);
					/*if(CommonConstants.GVPN.equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())
							&& scServiceDetail.getMultiVrfSolution()!=null && CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getMultiVrfSolution())){
                        getMultiVrfServiceAttributesToUpdate(siServiceAttrMap, scServiceDetail.getScServiceAttributes());
					}*/
					if (isContractInfoExists && Objects.nonNull(existingSIContractInfo)) {
						siServiceEntity.setSiContractInfo(existingSIContractInfo);
					} else {
						for (ScContractInfoBean contractingInfo : scOrderBean.getScContractInfos()) {
							SIContractInfo siContractInfo = serviceInventoryMapper.mapContractingInfoEntityToBean(contractingInfo);
							siContractInfo.setBillingType(billingType[0]);
							siContractInfo.setBillingCurrency(billingCurrency[0]);
							siContractInfo.setSiOrder(siOrderEntity);
							siContractInfos.add(siContractInfo);
							siServiceEntity.setSiContractInfo(siContractInfo);
						}
					}

					siAssetMap.put("CPE_MANAGED", scServiceDetail.getServiceOption());
					siAssetMap.put("SCOPE_OF_MANAGEMENT", serviceDetailBean.getScopeOfManagement());

					createSiRecords(scOrderBean, scServiceDetail.getUuid(), siServiceEntity, siAssetMap, siServiceAttrMap, cpeModel);

					saveSiServiceAttributes(siServiceAttrMap, siServiceEntity);

					siOrderEntity.setTpsSapCrnId(crnId);
					siServiceEntity.setSiOrder(siOrderEntity);
					siServiceEntity.setBillingType(billingType[0]);
					siServiceDetails.add(siServiceEntity);
				}
				persistOrderToServiceInventory(siOrderEntity, siServiceDetails, siOrderAttrs, siContractInfos,
						siServiceSlas, siAttachments, siComponentMap);
			}
		}
		LOGGER.info("mapScOrderDetailsToSIEntity method ends");
	}

	private SIServiceSla mapScServiceSlaToBean(ScServiceSlaBean scServiceSlaBean, SIServiceDetail siServiceDetail) {
		SIServiceSla siServiceSla = new SIServiceSla();
		siServiceSla.setCreatedBy(scServiceSlaBean.getCreatedBy());
		siServiceSla.setCreatedTime(scServiceSlaBean.getCreatedTime());
		siServiceSla.setIsActive(scServiceSlaBean.getIsActive());
		siServiceSla.setSiServiceDetail(siServiceDetail);
		siServiceSla.setSlaComponent(scServiceSlaBean.getSlaComponent());
		siServiceSla.setUpdatedBy(scServiceSlaBean.getUpdatedBy());
		siServiceSla.setUpdatedTime(scServiceSlaBean.getUpdatedTime());
		siServiceSla.setSlaValue(scServiceSlaBean.getSlaValue());
		return siServiceSla;
	}

	private void saveSiServiceDetailsForTypeB(ScOrderBean scOrderBean, ScServiceDetailBean scServiceDetailBean, SIOrder siOrder, Map<String, String> siServiceAttrMap, Set<SIOrderAttribute> siOrderAttrs, ServiceDetailBean serviceDetailBean, boolean isContractInfoExists, SIContractInfo existingSIContractInfo) {
		List<ScComponentBean> scComponentBeans = scServiceDetailBean.getScComponentBeans();

		List<SIComponent> siComponents = new ArrayList<>();
		Map<Integer, List<SIComponent>> siComponentMap = new HashMap<>();
		Set<SIServiceDetail> siServiceDetails = new HashSet<>();
		Set<SIContractInfo> siContractInfos = new HashSet<>();
		Set<SIServiceSla> siServiceSlas = new HashSet<>();
		Set<SIAttachment> siAttachments = new HashSet<>();

		if(!CollectionUtils.isEmpty(scComponentBeans)) {

			for(ScComponentBean scComponentBean : scComponentBeans) {
				Set<SIComponentAttribute> siComponentAttributes = new HashSet<>();
				SIServiceDetail siServiceEntity = serviceInventoryMapper.mapScServiceEntityToBean(scServiceDetailBean);

				populateServiceDetailAttributes(serviceDetailBean, siServiceAttrMap);

				siServiceEntity.setCircuitStatus(serviceDetailBean.getServiceStatus());

				if("A".equalsIgnoreCase(scComponentBean.getSiteType())){
					siServiceEntity.setSiteType("SiteA");
					populateOtherAttributes(scOrderBean, siServiceAttrMap, scServiceDetailBean, siServiceEntity);
					// Adding SLA Attributes
					try {
						if (!CollectionUtils.isEmpty(scServiceDetailBean.getScServiceSlas())) {
							scServiceDetailBean.getScServiceSlas().forEach(scServiceSlaBean -> {
								siServiceSlas.add(mapScServiceSlaToBean(scServiceSlaBean, siServiceEntity));
							});
						}
					}
					catch(Exception ex){
						LOGGER.error("Error while saving scServiceSlas for service Id {} -> {}",scOrderBean.getUuid(), ex);
					}

					scServiceDetailBean.getScAttachments().stream().forEach(scAttachmentBean -> {
						siAttachments
								.add(serviceInventoryMapper.mapScServiceAttachmentToEntity(scAttachmentBean, siServiceEntity));
					});
				}
				else{
					siServiceEntity.setSiteType("SiteB");
				}

				Set<SiServiceContact> siServiceContacts = new HashSet<>();
				SiServiceContact siServiceContact = serviceInventoryMapper.mapServiceContact(scServiceDetailBean, scOrderBean);
				siServiceContact.setSiServiceDetail(siServiceEntity);
				siServiceContacts.add(siServiceContact);

				String billingCurrency[] = {null};
				String crnId = null;
				String billingType[] = {null};
				for (ScOrderAttributeBean scOrderAttribute : scOrderBean.getScOrderAttributes()) {
					if ("Billing Currency".equalsIgnoreCase(scOrderAttribute.getAttributeName())) {
						billingCurrency[0] = scOrderAttribute.getAttributeValue();
					}
					if ("CRN_ID".equalsIgnoreCase(scOrderAttribute.getAttributeName())) {
						crnId = scOrderAttribute.getAttributeValue();
					}
					if ("BILLING_TYPE".equals(scOrderAttribute.getAttributeName())) {
						billingType[0] = scOrderAttribute.getAttributeValue();
					}
				}

				for (ScServiceAttributeBean scServiceAttributeBean : scServiceDetailBean.getScServiceAttributes()) {
					if ("VPN Topology".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
						siServiceEntity.setServiceTopology(scServiceAttributeBean.getAttributeValue());
					}
					if ("Site Type".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
						siServiceEntity.setSiteTopology(scServiceAttributeBean.getAttributeValue());
					}
					if ("Service type".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
						siServiceEntity.setServiceType(scServiceAttributeBean.getAttributeValue());
					}
					if ("bts_site_name".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
						siServiceAttrMap.put("BSO_NAME", scServiceAttributeBean.getAttributeValue());
					}
					if ("Selected_solution_BW".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
						siServiceAttrMap.put("B_END_LL_BANDWIDTH", scServiceAttributeBean.getAttributeValue());
					}
					if ("service_variant".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())) {
						siServiceAttrMap.put("PRODUCT_FLAVOUR", scServiceAttributeBean.getAttributeValue());
					}
					if("Resiliency".equalsIgnoreCase(scServiceAttributeBean.getAttributeName())){
						siServiceEntity.setResiliencyInd(scServiceAttributeBean.getAttributeValue());
					}
				}

				updateAdditionalServiceAttributes(siServiceAttrMap, scServiceDetailBean.getScServiceAttributes(), "");

				updateAdditionalComponentAttributes(siServiceAttrMap, scComponentBean.getScComponentAttributeBeans());

				if (isContractInfoExists && Objects.nonNull(existingSIContractInfo)) {
					siServiceEntity.setSiContractInfo(existingSIContractInfo);
				} else {
					for (ScContractInfoBean contractingInfo : scOrderBean.getScContractInfos()) {
						SIContractInfo siContractInfo = serviceInventoryMapper.mapContractingInfoEntityToBean(contractingInfo);
						siContractInfo.setBillingCurrency(billingCurrency[0]);
						siContractInfo.setSiOrder(siOrder);
						siContractInfos.add(siContractInfo);
						siServiceEntity.setSiContractInfo(siContractInfo);
					}
				}

				SIComponent siComponent = serviceInventoryMapper.mapScComponentToEntity(scComponentBean);

				if (!CollectionUtils.isEmpty(scComponentBean.getScComponentAttributeBeans())) {
					scComponentBean.getScComponentAttributeBeans().forEach(attrBean -> {
						if ("commissioningDate".equalsIgnoreCase(attrBean.getAttributeName())) {
							siServiceEntity.setServiceCommissionedDate(Timestamp.valueOf(attrBean.getAttributeValue() + " 00:00:00"));
						}
						if("siteAddress".equalsIgnoreCase(attrBean.getAttributeName())){
							siServiceEntity.setSiteAddress(attrBean.getAttributeValue());
						}
						if("siteAddressId".equalsIgnoreCase(attrBean.getAttributeName())){
							siServiceEntity.setErfLocSiteAddressId(attrBean.getAttributeValue());
						}
						if("sourceCity".equalsIgnoreCase(attrBean.getAttributeName())){
							if(Objects.nonNull(siServiceEntity.getSourceCity())) {
								siServiceEntity.setSourceCity(attrBean.getAttributeValue());
							}
						}
						if("sourceState".equalsIgnoreCase(attrBean.getAttributeName())){
							if(Objects.nonNull(siServiceEntity.getSourceState())) {
								siServiceEntity.setSourceState(attrBean.getAttributeValue());
							}
						}
						SIComponentAttribute siComponentAttribute = serviceInventoryMapper.mapScComponentAttrToEntity(attrBean, siComponent);
						siComponentAttributes.add(siComponentAttribute);
					});
				}

				siComponent.setSIComponentAttributes(siComponentAttributes);
				siComponents.add(siComponent);
				saveSiServiceAttributes(siServiceAttrMap, siServiceEntity);

				siOrder.setTpsSapCrnId(crnId);
				siServiceEntity.setSiOrder(siOrder);
				siServiceEntity.setBillingType(billingType[0]);
				siServiceDetails.add(siServiceEntity);
			}
			siComponentMap.put(scServiceDetailBean.getErfScServiceId(), siComponents);
			persistOrderToServiceInventoryNpl(siOrder, siServiceDetails, siOrderAttrs, siServiceSlas, siAttachments, siContractInfos, siComponentMap);
		}
	}

	public static int getTotalAdditionalIps(String input, int ipTypeValue) {
		int totalAdditionalIps = 0;
		try {
			int calculatedValue = ipTypeValue - Integer.parseInt(input);
			totalAdditionalIps = (int) Math.pow(2, calculatedValue);
		}catch(Exception e) {
			LOGGER.error("Exception in getTotalAdditionalIps >> input is {} | exception is {}", input, e);
			return 0;
		}
		return totalAdditionalIps;
	}

	private static void updateAdditionalServiceAttributes(Map<String,String> serviceAttrMap, Set<ScServiceAttributeBean> scServiceAttributeBeans, String productName){
		LOGGER.info("Updating Additional Service Attributes");
		if(!StringUtils.isEmpty(productName) && "GVPN".equalsIgnoreCase(productName)) {
			serviceAttrMap.put("COS1", getScServiceAttributeValue("cos 1", scServiceAttributeBeans));
			serviceAttrMap.put("COS2", getScServiceAttributeValue("cos 2", scServiceAttributeBeans));
			serviceAttrMap.put("COS3", getScServiceAttributeValue("cos 3", scServiceAttributeBeans));
			serviceAttrMap.put("COS4", getScServiceAttributeValue("cos 4", scServiceAttributeBeans));
			serviceAttrMap.put("COS5", getScServiceAttributeValue("cos 5", scServiceAttributeBeans));
			serviceAttrMap.put("COS6", getScServiceAttributeValue("cos 6", scServiceAttributeBeans));
		}
		String ipv6AddrPoolSize = getScServiceAttributeValue("IPv6 Address Pool Size for Additional IPs", scServiceAttributeBeans);
		if(StringUtils.isEmpty(ipv6AddrPoolSize)){
			ipv6AddrPoolSize = getScServiceAttributeValue("IPv6 Address Pool Size", scServiceAttributeBeans);
		}
		serviceAttrMap.put("IPV6_ADDR_POOL_SIZE", ipv6AddrPoolSize);
		String ipv4AddrPoolSize = getScServiceAttributeValue("IPv4 Address Pool Size for Additional IPs", scServiceAttributeBeans);
		if(StringUtils.isEmpty(ipv4AddrPoolSize)){
			ipv4AddrPoolSize = getScServiceAttributeValue("IPv4 Address Pool Size", scServiceAttributeBeans);
		}
		serviceAttrMap.put("IPV4_ADDR_POOL_SIZE", ipv4AddrPoolSize);

		serviceAttrMap.put("additional_lan_ipv4_req", getScServiceAttributeValue("Additional IPs", scServiceAttributeBeans));

		String ipv6AddrPoolSizeValue = null, ipv4AddrPoolSizeValue = null;

		if(StringUtils.isNotEmpty(ipv6AddrPoolSize)) {
			if(ipv6AddrPoolSize.contains("/")){
				ipv6AddrPoolSize = ipv6AddrPoolSize.substring(ipv6AddrPoolSize.indexOf("/")+1);
			}
			ipv6AddrPoolSizeValue = String.valueOf(getTotalAdditionalIps(ipv6AddrPoolSize, 128));
		}

		if(StringUtils.isNotEmpty(ipv4AddrPoolSize)) {
			if(ipv4AddrPoolSize.contains("/")) {
				ipv4AddrPoolSize = ipv4AddrPoolSize.substring(ipv4AddrPoolSize.indexOf("/") + 1);
			}
			ipv4AddrPoolSizeValue = String.valueOf(getTotalAdditionalIps(ipv4AddrPoolSize, 32));
		}

		serviceAttrMap.put("no_additional_ipv6_address", ipv6AddrPoolSizeValue);
		serviceAttrMap.put("no_additional_ipv4_address", ipv4AddrPoolSizeValue);
	}

	private static String getScServiceAttributeValue(String attributeName, Set<ScServiceAttributeBean> scServiceAttributeBeans) {
		if(!CollectionUtils.isEmpty(scServiceAttributeBeans)) {
			Optional<ScServiceAttributeBean> oScServiceAttributeBean = scServiceAttributeBeans.stream().filter(attr -> attr.getAttributeName().equalsIgnoreCase(attributeName)).findFirst();
			if(oScServiceAttributeBean.isPresent()) {
				ScServiceAttributeBean scServiceAttributeBean = oScServiceAttributeBean.get();
				return scServiceAttributeBean.getAttributeValue();
			}
		}
		return null;
	}

	private static void updateAdditionalComponentAttributes(Map<String, String> serviceAttrMap, List<ScComponentAttributeBean> scComponentAttributeBeans) {
		LOGGER.info("Updating Additional Component Attributes");
		serviceAttrMap.put("WAN_IP_PROV_BY_CUST", getScComponentAttributeValue("wanIpProvidedByCust", scComponentAttributeBeans));
		serviceAttrMap.put("IPV4_CUST_WAN_IP", getScComponentAttributeValue("wanIpAddress", scComponentAttributeBeans));
	}

	private static String getScComponentAttributeValue(String attributeName, List<ScComponentAttributeBean> scComponentAttributeBeans) {
		if(!CollectionUtils.isEmpty(scComponentAttributeBeans)) {
			Optional<ScComponentAttributeBean> oScComponentAttributeBean = scComponentAttributeBeans.stream().filter(attr -> attr.getAttributeName().equalsIgnoreCase(attributeName)).findFirst();
			if(oScComponentAttributeBean.isPresent()) {
				ScComponentAttributeBean scComponentAttributeBean = oScComponentAttributeBean.get();
				return scComponentAttributeBean.getAttributeValue();
			}
		}
		return "";
	}

	@Transactional(readOnly=false)
	public void saveRfData(ScServiceDetailBean scServiceDetailBean) {
		ServiceInvRf serviceInvRf = null;
		OptimusRfDataBean optimusRfDataBean = scServiceDetailBean.getOptimusRfDataBean();

		//delete the previous records mapped to service code/uuid.
		String serviceCode = scServiceDetailBean.getUuid();
		if(org.springframework.util.StringUtils.isEmpty(serviceCode)) serviceCode = optimusRfDataBean.getCircuitId();

		List<ServiceInvRf> existingServiceInvRfs = serviceInvRfRepository.findByServiceCode(serviceCode);

		if((Objects.nonNull(optimusRfDataBean)) && !org.springframework.util.StringUtils.isEmpty(optimusRfDataBean)) {

			if (!CollectionUtils.isEmpty(existingServiceInvRfs)) {
				serviceInvRfRepository.deleteAllByServiceCode(serviceCode);
				LOGGER.info("Deleting ServiceInvRf {}",serviceCode);
			}

			List<ServiceInvRf> existingBlankServiceInvRfs = serviceInvRfRepository.findByServiceCodeIsNull();

			if(!CollectionUtils.isEmpty(existingBlankServiceInvRfs)) { //removing empty records
				for(ServiceInvRf existingBlankServiceInvRf : existingBlankServiceInvRfs) {
					serviceInvRfRepository.deleteById(existingBlankServiceInvRf.getId());
					LOGGER.info("Deleting ServiceInvRf with id {}", existingBlankServiceInvRf.getId());
				}
			}
			//save the new record
			serviceInvRf = new ServiceInvRf();
			LOGGER.info("persist RF Order to Inventory", scServiceDetailBean.getUuid());
			serviceInvRf.setBhCapacity(optimusRfDataBean.getBhCapacity());
			serviceInvRf.setBsEthernetExtender(optimusRfDataBean.getBsEthernetExtender());
			serviceInvRf.setSeqRfcRadwin(optimusRfDataBean.getSeqRfcRadwin());
			serviceInvRf.setServiceStatus(optimusRfDataBean.getServiceStatus());
			serviceInvRf.setBuildingHeight(optimusRfDataBean.getBuildingHeight());
			serviceInvRf.setStatus(optimusRfDataBean.getStatus());
			serviceInvRf.setFlag("Y");
			serviceInvRf.setThroughputAcceptance(optimusRfDataBean.getThroughputAcceptance());
			serviceInvRf.setDateOfAcceptance(optimusRfDataBean.getDateOfAcceptance());
			serviceInvRf.setCity(optimusRfDataBean.getCity());
			serviceInvRf.setLatitude(optimusRfDataBean.getLatitude());
			serviceInvRf.setPolarisation(optimusRfDataBean.getPolarisation());
			serviceInvRf.setSsDateAcceptance(optimusRfDataBean.getSsDateAcceptance());
			serviceInvRf.setBhConfigSwitchConv(optimusRfDataBean.getBhConfigSwitchConv());
			serviceInvRf.setMac(optimusRfDataBean.getMac());
			String mac = null;
			if(Objects.nonNull(optimusRfDataBean.getMac())) mac = optimusRfDataBean.getMac().replace(':', ' ');
			serviceInvRf.setMac(mac);
			serviceInvRf.setSsMac(optimusRfDataBean.getSsMac());
			serviceInvRf.setBsIp(optimusRfDataBean.getBsIp());
			serviceInvRf.setBsAntennaGain(optimusRfDataBean.getBsAntennaGain());
			serviceInvRf.setBsPoleHeight(optimusRfDataBean.getBsPoleHeight());
			serviceInvRf.setBsBuildingHeight(optimusRfDataBean.getBsBuildingHeight());
			serviceInvRf.setVendor(optimusRfDataBean.getVendor());
			serviceInvRf.setBhCircuitId(optimusRfDataBean.getBhCircuitId());
			serviceInvRf.setCircuitId(optimusRfDataBean.getCircuitId());
			serviceInvRf.setSsAntennaHeight(optimusRfDataBean.getSsAntennaHeight());
			serviceInvRf.setEthernetExtender(optimusRfDataBean.getEthernetExtender());
			serviceInvRf.setAntennaHeight(optimusRfDataBean.getAntennaHeight());
			serviceInvRf.setBsAntennaType(optimusRfDataBean.getBsAntennaType());
			serviceInvRf.setCustBuildingHeight(optimusRfDataBean.getCustBuildingHeight());
			serviceInvRf.setSsLongitude(optimusRfDataBean.getSsLongitude());
			serviceInvRf.setEthconverterIp(optimusRfDataBean.getEthconverterIp());
			serviceInvRf.setBsLongitude(optimusRfDataBean.getBsLongitude());
			serviceInvRf.setBackhaulType(optimusRfDataBean.getBackhaulType());
			serviceInvRf.setSsDuringAccept(optimusRfDataBean.getSsDuringAccept());
			serviceInvRf.setSectorId(optimusRfDataBean.getSectorId());
			serviceInvRf.setTerminationDate(optimusRfDataBean.getTerminationDate());
			serviceInvRf.setHssuUsed(optimusRfDataBean.getHssuUsed());
			serviceInvRf.setAntennaBeamWidth(optimusRfDataBean.getAntennaBeamWidth());
			serviceInvRf.setBsName(optimusRfDataBean.getBsName());
			serviceInvRf.setSsAntennaGain(optimusRfDataBean.getSsAntennaGain());
			serviceInvRf.setBtsSiteId(optimusRfDataBean.getBtsSiteId());
			serviceInvRf.setCustomerName(optimusRfDataBean.getCustomerName());
			serviceInvRf.setDlCinrDuringAcceptance(optimusRfDataBean.getDlCinrDuringAcceptance());
			serviceInvRf.setSwitchIp(optimusRfDataBean.getSwitchIp());
			serviceInvRf.setBsLatitude(optimusRfDataBean.getBsLatitude());
			serviceInvRf.setSsPoleHeight(optimusRfDataBean.getSsPoleHeight());
			serviceInvRf.setCableLength(optimusRfDataBean.getCableLength());
			serviceInvRf.setSsPolarisation(optimusRfDataBean.getSsPolarisation());
			serviceInvRf.setQosBw(optimusRfDataBean.getQosBw());
			serviceInvRf.setLongitude(optimusRfDataBean.getLongitude());
			serviceInvRf.setSsAntennaMountType(optimusRfDataBean.getSsAntennaMountType());
			serviceInvRf.setComponentId(optimusRfDataBean.getComponentId());
			serviceInvRf.setHssuPort(optimusRfDataBean.getHssuPort());
			serviceInvRf.setSsAntennaType(optimusRfDataBean.getSsAntennaType());
			serviceInvRf.setTowerHeight(optimusRfDataBean.getTowerHeight());
			serviceInvRf.setLmType(optimusRfDataBean.getLmType());
			serviceInvRf.setSsRssiAcceptance(optimusRfDataBean.getSsRssiAcceptance());
			serviceInvRf.setBsPolarisation(optimusRfDataBean.getBsPolarisation());
			serviceInvRf.setBsTowerHeight(optimusRfDataBean.getBsTowerHeight());
			serviceInvRf.setPeHostname(optimusRfDataBean.getPeHostname());
			serviceInvRf.setBsoCktId(optimusRfDataBean.getBsoCktId());
			serviceInvRf.setApIp(optimusRfDataBean.getApIp());
			serviceInvRf.setBsCableLength(optimusRfDataBean.getBsCableLength());
			serviceInvRf.setSsEthernetExtender(optimusRfDataBean.getSsEthernetExtender());
			serviceInvRf.setBsRssDuringAcceptance(optimusRfDataBean.getBsRssDuringAcceptance());
			serviceInvRf.setSwitchConverterPort(optimusRfDataBean.getSwitchConverterPort());
			serviceInvRf.setSsBsName(optimusRfDataBean.getSsBsName());
			serviceInvRf.setStructureType(optimusRfDataBean.getStructureType());
			serviceInvRf.setPoleHeight(optimusRfDataBean.getPoleHeight());
			serviceInvRf.setBsAntennaMountType(optimusRfDataBean.getBsAntennaMountType());
			serviceInvRf.setSsCableLength(optimusRfDataBean.getSsCableLength());
			serviceInvRf.setBsMac(optimusRfDataBean.getBsMac());
			serviceInvRf.setSsMimoDiversity(optimusRfDataBean.getSsMimoDiversity());
			serviceInvRf.setDlRssiDuringAcceptance(optimusRfDataBean.getDlRssiDuringAcceptance());
			serviceInvRf.setBhBso(optimusRfDataBean.getBhBso());
			serviceInvRf.setConverterType(optimusRfDataBean.getConverterType());
			serviceInvRf.setPeIp(optimusRfDataBean.getPeIp());
			serviceInvRf.setSrNonumber(optimusRfDataBean.getSrNonumber());
			serviceInvRf.setTypeOfOrder("ADD_SITE".equalsIgnoreCase(optimusRfDataBean.getTypeOfOrder()) ? "ADDITION OF SITE" : optimusRfDataBean.getTypeOfOrder());
			serviceInvRf.setLmAction("ADD_SITE".equalsIgnoreCase(optimusRfDataBean.getTypeOfOrder()) ? "NEW" : optimusRfDataBean.getLmAction());
			serviceInvRf.setBsAntennaHeight(optimusRfDataBean.getBsAntennaHeight());
			serviceInvRf.setSsIp(optimusRfDataBean.getSsIp());
			serviceInvRf.setPopConverterIp(optimusRfDataBean.getPopConverterIp());
			serviceInvRf.setServiceType(optimusRfDataBean.getServiceType());
			serviceInvRf.setSsBhBso(optimusRfDataBean.getSsBhBso());
			serviceInvRf.setLensReflector(optimusRfDataBean.getLensReflector());
			serviceInvRf.setAntennaType(optimusRfDataBean.getAntennaType());
			serviceInvRf.setActionType(optimusRfDataBean.getActionType());
			serviceInvRf.setConverterIp(optimusRfDataBean.getConverterIp());
			serviceInvRf.setSrNo(optimusRfDataBean.getSrNo());
			serviceInvRf.setSsLatitude(optimusRfDataBean.getSsLatitude());
			String custAddress = null;
			if(Objects.nonNull(optimusRfDataBean.getCustomerAddress())) custAddress = optimusRfDataBean.getCustomerAddress().replace(',', ' ');
			serviceInvRf.setCustomerAddress(custAddress);
			serviceInvRf.setState(optimusRfDataBean.getState());
			serviceInvRf.setBackhaulProvider(optimusRfDataBean.getBackhaulProvider());
			serviceInvRf.setCommissionDate(optimusRfDataBean.getCommissionDate());
			serviceInvRf.setAggregationSwitch(optimusRfDataBean.getAggregationSwitch());
			serviceInvRf.setProvider(optimusRfDataBean.getProvider());
			serviceInvRf.setSsTowerHeight(optimusRfDataBean.getSsTowerHeight());
			serviceInvRf.setBsMimoDiversity(optimusRfDataBean.getBsMimoDiversity());
			serviceInvRf.setBsAddress(optimusRfDataBean.getBsAddress());
			serviceInvRf.setDeviceType(optimusRfDataBean.getDeviceType());
			serviceInvRf.setServiceCode(optimusRfDataBean.getCircuitId());
			serviceInvRf.setInfraColoProviderId(optimusRfDataBean.getInfraColoProviderId());
			serviceInvRf.setInfraColoProviderName(optimusRfDataBean.getInfraColoProviderName());
			serviceInvRf.setLastUpdatedBy(optimusRfDataBean.getLastUpdatedBy());
			serviceInvRf.setLastUpdatedDate(optimusRfDataBean.getLastUpdatedDate());
			serviceInvRf.setTaskStage(optimusRfDataBean.getTaskStage());
			if(!org.springframework.util.StringUtils.isEmpty(serviceInvRf)) serviceInvRfRepository.save(serviceInvRf);
		} else if((!CollectionUtils.isEmpty(existingServiceInvRfs)) && ("MACD".equals(existingServiceInvRfs.get(0).getLmAction())) && ("CMIP".equals(existingServiceInvRfs.get(0).getTaskStage()))) {
			LOGGER.info("persist E2E RF Order to Inventory", scServiceDetailBean.getUuid());
			existingServiceInvRfs.get(0).setLastUpdatedBy("OPTIMUS");
			existingServiceInvRfs.get(0).setStatus("ACTIVE");
			existingServiceInvRfs.get(0).setServiceStatus("ACTIVE");
			existingServiceInvRfs.get(0).setLastUpdatedDate((new Timestamp(new Date().getTime())).toString());
			existingServiceInvRfs.get(0).setTaskStage("E2E");
			serviceInvRfRepository.save(existingServiceInvRfs.get(0));
		}
	}


	private void createSiRecords(ScOrderBean scOrderBean, String serviceCode, SIServiceDetail siServiceEntity, Map<String, String> siAttrMap, Map<String, String> attrMap, String cpeModel) {
		Set<SIAsset> siAssets = new HashSet<>();
		if(!CollectionUtils.isEmpty(scOrderBean.getCpeBeans())) {
			scOrderBean.getCpeBeans().forEach(cpe -> {
				siAssets.add(serviceInventoryMapper.saveCpeAsset(cpe, serviceCode, siServiceEntity, siAttrMap, cpeModel));
			});
		}
		if(!CollectionUtils.isEmpty(scOrderBean.getUniswitchDetailBeans())){
			scOrderBean.getUniswitchDetailBeans().forEach(uniswitchDetailBean -> {
				siAssets.add(serviceInventoryMapper.saveUniSwitchDetails(uniswitchDetailBean, serviceCode, siServiceEntity));
				attrMap.put("BUSINESS_SWITCH_IP", uniswitchDetailBean.getMgmtIp());
				attrMap.put("BUSINESS_SWITCH_HOSTNAME", uniswitchDetailBean.getHostName());
			});
		}
		if(!CollectionUtils.isEmpty(scOrderBean.getRouterDetailBeans())){
			scOrderBean.getRouterDetailBeans().forEach(routerDetailBean -> {
				siAssets.add(serviceInventoryMapper.saveRouterDetailAsset(routerDetailBean, serviceCode, siServiceEntity));
				LOGGER.info("routerDetailBean.getIpv4MgmtAddress::{}", routerDetailBean.getIpv4MgmtAddress());
				attrMap.put("PE_NAME", routerDetailBean.getRouterHostname());
				attrMap.put("PE_MANAGEMENT_IP", routerDetailBean.getIpv4MgmtAddress());
			});
		}
		if(Objects.nonNull(scOrderBean.getMuxDetails())){
			MuxDetailsBean muxDetailsBean = scOrderBean.getMuxDetails();
			siAssets.add(serviceInventoryMapper.saveMuxDetails(muxDetailsBean,serviceCode, siServiceEntity));
		}
		siServiceEntity.setSiAssets(siAssets);
		if(Objects.nonNull(scOrderBean.getVrfBean())){
			VrfBean vrfBean = scOrderBean.getVrfBean();
			siServiceEntity.setMastervrfServiceId(vrfBean.getMasterVrfServiceId());
			siServiceEntity.setIsmultivrf(convertBooleanToByte(vrfBean.getMultiVrf()));
		}
	}

	private void persistOrderToServiceInventoryNpl(SIOrder siOrderEntity,  Set<SIServiceDetail> siServiceDetails,
													Set<SIOrderAttribute> siOrderAttributes, Set<SIServiceSla> siServiceSlas,
													Set<SIAttachment> siAttachments, Set<SIContractInfo> siContractInfos,
													Map<Integer, List<SIComponent>> siComponentMap) {
		LOGGER.info("persistOrderToServiceInventory method invoked for NPL Scenario");
		siOrderRepository.save(siOrderEntity);
		siContractInfoRepository.saveAll(siContractInfos);
		siAttachmentRepository.saveAll(siAttachments);
		siOrderAttributeRepository.saveAll(siOrderAttributes);
		siServiceDetailRepository.saveAll(siServiceDetails);
		siServiceSlaRepository.saveAll(siServiceSlas);
		SIServiceDetail parentSiServiceDetail = siServiceDetails.stream().findFirst().get();
		Iterator it1 = siServiceDetails.iterator();
		Iterator it2 = siComponentMap.get(parentSiServiceDetail.getErfScServiceId()).iterator();
		while(it1.hasNext() && it2.hasNext()){
			SIServiceDetail siServiceDetail = (SIServiceDetail) it1.next();
			SIComponent siComponent = (SIComponent) it2.next();
			siComponent.setSiServiceDetailId(siServiceDetail.getId());
			siComponent.getSIComponentAttributes().forEach(siComponentAttribute -> {
				siComponentAttribute.setSiServiceDetailId(siServiceDetail.getId());
				siComponentAttribute.setSiComponent(siComponent);
			});
			siComponentRepository.save(siComponent);
		}
		LOGGER.info("persistOrderToServiceInventory method ends");
	}

	private void persistOrderToServiceInventory(SIOrder siOrderEntity,  Set<SIServiceDetail> siServiceDetails,
												Set<SIOrderAttribute> siOrderAttributes,
												Set<SIContractInfo> siContractInfos,
												Set<SIServiceSla> siServiceSlas,
												Set<SIAttachment> siAttachments,
												Map<Integer, List<SIComponent>> siComponentMap) {
		LOGGER.info("persistOrderToServiceInventory method invoked");
		siOrderRepository.save(siOrderEntity);
		siContractInfoRepository.saveAll(siContractInfos);
		siOrderAttributeRepository.saveAll(siOrderAttributes);
		siAttachmentRepository.saveAll(siAttachments);
		siServiceSlaRepository.saveAll(siServiceSlas);
		siServiceDetailRepository.saveAll(siServiceDetails);
		SIServiceDetail siServiceDetail = siServiceDetails.stream().findFirst().get();
		if(siComponentMap!=null && siComponentMap.containsKey(siServiceDetail.getErfScServiceId()) && siComponentMap.get(siServiceDetail.getErfScServiceId())!=null) {
			Iterator iterator = siComponentMap.get(siServiceDetail.getErfScServiceId()).iterator();
			while(iterator.hasNext()) {
				SIComponent siComponent = (SIComponent) iterator.next();
				siComponent.setSiServiceDetailId(siServiceDetail.getId());
				if(!CollectionUtils.isEmpty(siComponent.getSIComponentAttributes())) {
					siComponent.getSIComponentAttributes().forEach(siComponentAttribute -> {
						siComponentAttribute.setSiServiceDetailId(siServiceDetail.getId());
						siComponentAttribute.setSiComponent(siComponent);
					});
				}
				siComponentRepository.save(siComponent);
			}
		}
		LOGGER.info("persistOrderToServiceInventory method ends");
	}

	private Set<SIOrderAttribute> mapScOrderAttrToEntity(ScOrderBean scOrderBean, SIOrder siOrderEntity) {
		Set<SIOrderAttribute> siOrderAttrs = new HashSet<>();
		for (ScOrderAttributeBean scOrderAttribute : scOrderBean.getScOrderAttributes()) {
			SIOrderAttribute siAttrEntity = serviceInventoryMapper.mapScOrderAttrEntityToBean(scOrderAttribute);
			siAttrEntity.setSiOrder(siOrderEntity);
			siOrderAttrs.add(siAttrEntity);
		}
		return siOrderAttrs;
	}

	private Set<SIContractInfo> mapScContractInfoToEntity(ScOrderBean scOrderBean, SIOrder siOrderEntity) {
		Set<SIContractInfo> siContractingInfos = new HashSet<>();
		scOrderBean.getScContractInfos().stream().forEach(contractingInfo -> {
			SIContractInfo siContrEntity = serviceInventoryMapper.mapContractingInfoEntityToBean(contractingInfo);
			siContrEntity.setSiOrder(siOrderEntity);
			siContractingInfos.add(siContrEntity);
		});
		return siContractingInfos;
	}

	private void populateServiceQoAttributes(ServiceQoBean serviceQoBean, Map<String, String> attrMap) {
		LOGGER.info("Populating Service Qo Attributes");
		attrMap.put("COS_MODEL", serviceQoBean.getCosType());
		attrMap.put("COS_PROFILE", serviceQoBean.getCosProfile());
		if(!CollectionUtils.isEmpty(serviceQoBean.getServiceCosCriteriaBeans())) {
			LOGGER.info("ServiceCosCriteriaBeans Size : {}", serviceQoBean.getServiceCosCriteriaBeans().size());
			serviceQoBean.getServiceCosCriteriaBeans().forEach(serviceCosCriteriaBean -> {
				String cosName = serviceCosCriteriaBean.getCosName();
				LOGGER.info("Cos Name : {}", cosName);
				String classificationCriteria = serviceCosCriteriaBean.getClassificationCriteria();
				LOGGER.info("Classification Criteria : {}", classificationCriteria);
				String criteriaValues = "";
				switch(classificationCriteria) {
					case "DSCP":
						criteriaValues = Stream.of(serviceCosCriteriaBean.getDhcpVal1(), serviceCosCriteriaBean.getDhcpVal2(), serviceCosCriteriaBean.getDhcpVal3(), serviceCosCriteriaBean.getDhcpVal4(), serviceCosCriteriaBean.getDhcpVal5(), serviceCosCriteriaBean.getDhcpVal6(), serviceCosCriteriaBean.getDhcpVal7(), serviceCosCriteriaBean.getDhcpVal8()).map(criteria -> (Objects.isNull(criteria) ? " " : criteria)).collect(Collectors.joining(","));
						break;
					case "ipprecedence":
						criteriaValues = Stream.of(serviceCosCriteriaBean.getIpprecedenceVal1(), serviceCosCriteriaBean.getIpprecedenceVal2(), serviceCosCriteriaBean.getIpprecedenceVal3(), serviceCosCriteriaBean.getIpprecedenceVal4(), serviceCosCriteriaBean.getIpprecedenceVal5(), serviceCosCriteriaBean.getIpprecedenceVal6(), serviceCosCriteriaBean.getIpprecedenceVal7(), serviceCosCriteriaBean.getIpprecedenceVal8()).map(criteria -> (Objects.isNull(criteria) ? " " : criteria)).collect(Collectors.joining(","));
						break;
					default:
						break;
				}
				// Attribute Names to be changed.. Currently mapping as per SatSoc View
				switch(cosName) {
					case "COS1":
						attrMap.put("cos 1 criteria", classificationCriteria);
						attrMap.put("COS 1criteria value", criteriaValues);
						break;
					case "COS2":
						attrMap.put("cos 2 criteria", classificationCriteria);
						attrMap.put("COS 2criteria value", criteriaValues);
						break;
					case "COS3":
						attrMap.put("cos 3 criteria", classificationCriteria);
						attrMap.put("COS 3criteria value", criteriaValues);
						break;
					case "COS4":
						attrMap.put("cos 4 criteria", classificationCriteria);
						attrMap.put("COS 4criteria value", criteriaValues);
						break;
					case "COS5":
						attrMap.put("cos 5 criteria", classificationCriteria);
						attrMap.put("COS 5criteria value", criteriaValues);
						break;
					case "COS6":
						attrMap.put("cos 6 criteria", classificationCriteria);
						attrMap.put("COS 6criteria value", criteriaValues);
						break;
					default:
						LOGGER.info("Unexpected Cos Name {}", cosName);
						break;
				}
			});
		}
		LOGGER.info("Exiting after populating ServiceQos");
	}

	private void populateServiceDetailAttributes(ServiceDetailBean serviceDetailBean, Map<String, String> attrMap) {
		attrMap.put("SCOPE_OF_MANAGEMENT", serviceDetailBean.getScopeOfManagement());
		attrMap.put("SERVICE_LINK", serviceDetailBean.getSvcLinkServiceId());
		attrMap.put("PRISEC", Objects.nonNull(serviceDetailBean.getRedundancyRole()) ? serviceDetailBean.getRedundancyRole().toUpperCase() : null);
		attrMap.put("NETP_SERVICE_STATUS", serviceDetailBean.getServiceStatus());
		attrMap.put("OPPORTUNITY_BID_CATEGORY", serviceDetailBean.getOptyBidCategory());
		if(serviceDetailBean.getAsdOpportunity() == null || serviceDetailBean.getAsdOpportunity() == (byte) 0) {
			attrMap.put("ASD_OPPORTUNITY", "NO");
		} else {
			attrMap.put("ASD_OPPORTUNITY", "YES");
		}
		attrMap.put("remoteAsNumber", serviceDetailBean.getRemoteAsNumber());
		attrMap.put("BSN_SWITCH_UPLINKPORT", serviceDetailBean.getBusinessSwitchUplinkPort());
		attrMap.put("MulticastRPAddress", serviceDetailBean.getMulticastRPAddress());
		attrMap.put("MulticastType", serviceDetailBean.getMulticastType());
		attrMap.put("DEFAULT_MDT", serviceDetailBean.getDefaultMdt());
		attrMap.put("DATA_MDT", serviceDetailBean.getDataMdt());
		attrMap.put("WAN_PIM_MODE", serviceDetailBean.getWanPimMode());
		attrMap.put("AUTO_DISCOVERY", serviceDetailBean.getAutoDiscoveryOption());
		attrMap.put("RP_LOCATION", serviceDetailBean.getRpLocation());
		attrMap.put("MDT_THRESHOLD", serviceDetailBean.getDataMdtThreshold());
	}

	private void populateEthernetInterfaceAttributes(EthernetInterfaceBean ethernetInterfaceBean, Map<String, String> attrMap) {
		LOGGER.info("Populating Ethernet Interface Attributes to Service Attributes");
		attrMap.put("PHYSICAL_PEPORT", ethernetInterfaceBean.getPhysicalPort());
		attrMap.put("LOGICAL_PE_INTERFACE", ethernetInterfaceBean.getInterfaceName());
		attrMap.put("VLAN", ethernetInterfaceBean.getInnerVlan());
		attrMap.put("outerVlan", ethernetInterfaceBean.getOuterVlan());
		attrMap.put("TATA_WAN_IPv4", ethernetInterfaceBean.getModifiedIpv4Address());
		attrMap.put("TATA_WAN_IPv6", ethernetInterfaceBean.getModifiedIpv6Address());
		attrMap.put("TATA_WAN_SECONDARY_IPv4", ethernetInterfaceBean.getModifiedSecondaryIpv4Address());
		attrMap.put("TATA_WAN_SECONDARY_IPv6", ethernetInterfaceBean.getModifiedSecondaryIpv6Address());
		attrMap.put("CUST_WAN_IPv4", ethernetInterfaceBean.getCeModifiedIpv4Address());
		attrMap.put("CUST_WAN_IPv6", ethernetInterfaceBean.getCeModifiedIpv6Address());
		attrMap.put("CUST_WAN_SECONDARY_IPv4", ethernetInterfaceBean.getCeModifiedSecondaryIpv4Address());
		attrMap.put("CUST_WAN_SECONDARY_IPv6", ethernetInterfaceBean.getCeModifiedSecondaryIpv6Address());
	}

	private void populateUniswitchAttributes(UniswitchDetailBean uniswitchDetailBean, Map<String, String> attrMap) {
		attrMap.put("BSN_SWITCH_HANDOFFPORT", uniswitchDetailBean.getHandoff());
	}

	private void populateCpeAttributes(CpeBean cpeBean, Map<String, String> attrMap) {
		attrMap.put("IPV4_CUST_END_LOOPBACK", cpeBean.getMgmtLoopbackV4address());
		attrMap.put("EQUIPMENT_MODEL", cpeBean.getModel());
		attrMap.put("HOST_NAME", cpeBean.getHostName());
		attrMap.put("CPE_SERIAL_NO", cpeBean.getDeviceId());
		attrMap.put("EQUIPMENTMAKE", cpeBean.getMake());
	}

	private void populateOtherAttributes(ScOrderBean scOrderBean, Map<String, String> attrMap, ScServiceDetailBean scServiceDetailBean, SIServiceDetail siServiceDetail) {
		attrMap.put("REGION", scOrderBean.getRegion());
		attrMap.put("SERVICE_VARIANT", scServiceDetailBean.getServiceVariant());
		attrMap.put("DATA_SOURCE__C", "OPTIMUS");
		IpAddressDetailBean ipAddressDetailBean = scOrderBean.getIpAddressDetailBean();
		if(Objects.nonNull(ipAddressDetailBean)) {
			attrMap.put("lanv4Address", ipAddressDetailBean.getLanv4Address());
			attrMap.put("wanv4Address", ipAddressDetailBean.getWanv4Address());
			attrMap.put("lanv6Address", ipAddressDetailBean.getLanv6Address());
			attrMap.put("wanv6Address", ipAddressDetailBean.getWanv6Address());
			attrMap.put("lanv4ProvidedBy", ipAddressDetailBean.getLanv4ProvidedBy());
			attrMap.put("wanv4ProvidedBy", ipAddressDetailBean.getWanv4ProvidedBy());
			attrMap.put("lanv6ProvidedBy", ipAddressDetailBean.getLanv6ProvidedBy());
			attrMap.put("wanv6ProvidedBy", ipAddressDetailBean.getWanv6ProvidedBy());
		}
	}

	private void saveSiServiceAttributes(Map<String, String> attrMap, SIServiceDetail siServiceDetail) {
		if(!CollectionUtils.isEmpty(attrMap)) {
			attrMap.forEach((k, v) -> {
				SIServiceAttribute attribute = new SIServiceAttribute();
				attribute.setAttributeName(k);
				attribute.setAttributeValue(v);
				attribute.setIsActive("Y");
				attribute.setSiServiceDetail(siServiceDetail);
				attribute.setCategory("ACTIVATION");
				attribute.setCreatedDate(new Timestamp(new Date().getTime()));
				siServiceAttributeRepository.save(attribute);
			});
		}
	}


	private Byte convertBooleanToByte(Boolean value) {
		return (value == null || value == false) ? (byte) 0 : (byte) 1;
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, readOnly = false)
	public void processSdwanInventoryData(SdwanScOrderBean sdwanScOrderBean) {
		LOGGER.info("Inside processSdwanInventoryData method for solution Code {}", sdwanScOrderBean.getSolutionCode());
		String solutionCode = sdwanScOrderBean.getSolutionCode();
		List<ScOrderBean> scOrderBeans = sdwanScOrderBean.getScOrderBeans();
		if(!CollectionUtils.isEmpty(scOrderBeans)) {
			scOrderBeans.forEach(scOrderBean -> {
				mapScOrderDetailsToSIEntity(scOrderBean);
			});
		}
		SIServiceDetail solutionSiServiceDetail = siServiceDetailRepository.findFirstByUuidAndServiceStatusIgnoreCaseAndIsActiveOrderByIdDesc(solutionCode, "Active", CommonConstants.Y);

		List<SiSolutionComponent> componentsWithSolCodeAsParent = siSolutionComponentRepository.findByParentServiceCode(solutionCode);
		if(!CollectionUtils.isEmpty(componentsWithSolCodeAsParent)) {
			componentsWithSolCodeAsParent.forEach(component -> {
				component.setSiServiceDetail2(solutionSiServiceDetail);
				siSolutionComponentRepository.save(component);
			});
		}

		SIOrder siOrder = null;
		if(Objects.nonNull(sdwanScOrderBean.getParentOrderCode())) {
			siOrder = siOrderRepository.findFirstByOpOrderCodeOrderByIdDesc(sdwanScOrderBean.getParentOrderCode());
		}
		List<SiSolutionComponent> siSolutionComponents = siSolutionComponentRepository.findBySolutionCode(solutionCode);
		if(!CollectionUtils.isEmpty(siSolutionComponents)) {
			siSolutionComponents.forEach(siSolutionComponent -> {
				siSolutionComponent.setSiServiceDetail3(solutionSiServiceDetail);
				siSolutionComponentRepository.save(siSolutionComponent);
			});
		}
		Map<String, String> cpeSiteReference = sdwanScOrderBean.getCpeSiteReference();
		LOGGER.info("CPE Site Reference for solution code {} : {}", solutionCode, cpeSiteReference);
		// Sc Solution Components to be processed later
		if(Objects.nonNull(solutionSiServiceDetail) && !CollectionUtils.isEmpty(sdwanScOrderBean.getScSolutionComponentBeans())) {
			for(ScSolutionComponentBean scSolutionComponentBean : sdwanScOrderBean.getScSolutionComponentBeans()) {
				persistSiSolutionComponent(scSolutionComponentBean, solutionCode, solutionSiServiceDetail, siOrder, cpeSiteReference);
			}
		}
	}

	private void persistSiSolutionComponent(ScSolutionComponentBean scSolutionComponentBean, String solutionCode, SIServiceDetail solutionSiServiceDetail, SIOrder siOrder, Map<String, String> cpeSiteReference) {
		String serviceCode = scSolutionComponentBean.getServiceCode();
		LOGGER.info("Inside persistSiSolutionComponent method for serviceCode {}", serviceCode);
		String parentServiceCode = scSolutionComponentBean.getParentServiceCode();
		LOGGER.info("Parent Service Code {}", parentServiceCode);

		// Check if Si Solution Component already exists
		SiSolutionComponent existingSiSolutionComponent = siSolutionComponentRepository.findFirstByServiceCodeAndIsActive(serviceCode, "Y");
		SIServiceDetail existingSiServiceDetail = siServiceDetailRepository.findFirstByTpsServiceIdAndServiceStatusAndIsActive(serviceCode, "Active", "Y");
		SIServiceDetail existingParentSiServiceDetail = siServiceDetailRepository.findFirstByTpsServiceIdAndServiceStatusAndIsActive(parentServiceCode, "Active", "Y");

		if(Objects.isNull(existingSiSolutionComponent)) {
			LOGGER.info("Service Code {} does not exist in Si Solution Component ", serviceCode);
			SiSolutionComponent siSolutionComponent = new SiSolutionComponent();
			siSolutionComponent.setSiServiceDetail3(solutionSiServiceDetail);
			if(Objects.nonNull(existingSiServiceDetail)) {
				LOGGER.info("Service Code {} exists in Si Service Detail", serviceCode);
				siSolutionComponent.setSiServiceDetail1(existingSiServiceDetail);
			}
			// Updating the parent Si Service Detail
			List<SiSolutionComponent> parentSiSolutionComponents = siSolutionComponentRepository.findByParentServiceCode(serviceCode);
			if(!CollectionUtils.isEmpty(parentSiSolutionComponents) && Objects.nonNull(existingSiServiceDetail)) {
				parentSiSolutionComponents.forEach(parentSiSolutionComponent -> {
					parentSiSolutionComponent.setSiServiceDetail2(existingSiServiceDetail);
					String referencedServiceCode = parentSiSolutionComponent.getServiceCode();
					SIServiceDetail referencedSiServiceDetail = parentSiSolutionComponent.getSiServiceDetail2();
					// Get All SI Components
					List<SIComponent> siComponents = siComponentRepository.findBySiServiceDetailId(referencedSiServiceDetail.getId());
					if(!CollectionUtils.isEmpty(siComponents) && !CollectionUtils.isEmpty(cpeSiteReference)) {
						siComponents.forEach(siComponent -> {
							if(siComponent.getSiteType().equalsIgnoreCase(cpeSiteReference.get(referencedServiceCode))) {
								parentSiSolutionComponent.setCpeComponentId(siComponent.getId());
							}
						});
					}
					siSolutionComponentRepository.save(parentSiSolutionComponent);
				});
			}
			siSolutionComponent.setSiServiceDetail2(existingParentSiServiceDetail);
			siSolutionComponent.setParentServiceCode(parentServiceCode);
			siSolutionComponent.setIsActive("Y");
			siSolutionComponent.setO2cTriggeredStatus(scSolutionComponentBean.getO2cTriggeredStatus());
			siSolutionComponent.setPriority(scSolutionComponentBean.getPriority());
			siSolutionComponent.setServiceCode(scSolutionComponentBean.getServiceCode());
			siSolutionComponent.setSolutionCode(scSolutionComponentBean.getSolutionCode());
			siSolutionComponent.setSiOrder(siOrder);
			siSolutionComponent.setOrderCode(scSolutionComponentBean.getOrderCode());
			siSolutionComponent.setComponentGroup(scSolutionComponentBean.getComponentGroup());
			siSolutionComponent.setPriority(scSolutionComponentBean.getPriority());
			siSolutionComponentRepository.save(siSolutionComponent);
		} else {
			LOGGER.info("Si Solution Component exists for service Code {}", serviceCode);
			// Updating the parent Si Service Detail
			List<SiSolutionComponent> parentSiSolutionComponents = siSolutionComponentRepository.findByParentServiceCode(serviceCode);
			if(!CollectionUtils.isEmpty(parentSiSolutionComponents) && Objects.nonNull(existingSiServiceDetail)) {
				parentSiSolutionComponents.forEach(parentSiSolutionComponent -> {
					parentSiSolutionComponent.setSiServiceDetail2(existingSiServiceDetail);
					String referencedServiceCode = parentSiSolutionComponent.getServiceCode();
					SIServiceDetail referencedSiServiceDetail = parentSiSolutionComponent.getSiServiceDetail2();
					// Get All SI Components
					List<SIComponent> siComponents = siComponentRepository.findBySiServiceDetailId(referencedSiServiceDetail.getId());
					if(!CollectionUtils.isEmpty(siComponents) && !CollectionUtils.isEmpty(cpeSiteReference)) {
						siComponents.forEach(siComponent -> {
							if(siComponent.getSiteType().equalsIgnoreCase(cpeSiteReference.get(referencedServiceCode))) {
								parentSiSolutionComponent.setCpeComponentId(siComponent.getId());
							}
						});
					}
					siSolutionComponentRepository.save(parentSiSolutionComponent);
				});
			}
			existingSiSolutionComponent.setSiServiceDetail1(existingSiServiceDetail);
			siSolutionComponentRepository.save(existingSiSolutionComponent);
		}
		LOGGER.info("persistSiSolutionComponent method has ended for service Code {}", serviceCode);
	}

	private void getMultiVrfServiceAttributesToUpdate(Map<String,String> serviceAttrMap, Set<ScServiceAttributeBean> scServiceAttributeBeans){
		LOGGER.info("Inside getMultiVrfServiceAttributesToUpdate method");
		serviceAttrMap.put("FLEXICOS", getScServiceAttributeValue("FLEXICOS", scServiceAttributeBeans));
		serviceAttrMap.put("MASTER_VRF_FLAG", getScServiceAttributeValue("MASTER_VRF_FLAG", scServiceAttributeBeans));
		serviceAttrMap.put("MULTI_VRF_SOLUTION", getScServiceAttributeValue("MULTI_VRF_SOLUTION", scServiceAttributeBeans));
		serviceAttrMap.put("TOTAL_VRF_BANDWIDTH_MBPS", getScServiceAttributeValue("TOTAL_VRF_BANDWIDTH_MBPS", scServiceAttributeBeans));
		serviceAttrMap.put("NUMBER_OF_VRFS", getScServiceAttributeValue("NUMBER_OF_VRFS", scServiceAttributeBeans));
		serviceAttrMap.put("SLAVE_VRF_SERVICE_ID", getScServiceAttributeValue("SLAVE_VRF_SERVICE_ID", scServiceAttributeBeans));
		serviceAttrMap.put("MASTER_VRF_SERVICE_ID", getScServiceAttributeValue("MASTER_VRF_SERVICE_ID", scServiceAttributeBeans));
		serviceAttrMap.put("CUSTOMER_PROJECT_NAME", getScServiceAttributeValue("CUSTOMER_PROJECT_NAME", scServiceAttributeBeans));
		serviceAttrMap.put("VRF based billing", getScServiceAttributeValue("VRF based billing", scServiceAttributeBeans));

	}
	
	public void updateAttributes(Integer serviceId, Map<String, String> atMap, String componentName,String siteType) {
		SIComponent siComponent = siComponentRepository
				.findFirstBySiServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceId, componentName,siteType);
		if (siComponent != null) {
			atMap.forEach((key, value) -> {
				LOGGER.info("Key : {}, Value : {}", key, value);
				SIComponentAttribute siComponentAttribute = siComponentAttributeRepository
						.getAttributeDetailByComponentIdAndName(siComponent.getId(), key);
				if (siComponentAttribute == null) {
					createComponentAttr(key, value, serviceId, siComponent, "Optimus_O2C");
				} else {
					updateComponentAttr(key, value, siComponentAttribute, "Optimus_O2C");
				}

			});
		}
	}
	
	public void updateComponentAttr(String attrName, String attrValue, SIComponentAttribute siComponentAttribute,
			String userName) {

		if (attrValue != null && !attrValue.isEmpty() && !attrValue.equals(siComponentAttribute.getAttributeValue())) {
			siComponentAttribute.setAttributeName(attrName);
			siComponentAttribute.setAttributeValue(attrValue);
			siComponentAttribute.setAttributeAltValueLabel(attrValue);
			siComponentAttribute.setUpdatedBy(userName);
			siComponentAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			siComponentAttributeRepository.saveAndFlush(siComponentAttribute);
		}
	}

	
	private void createComponentAttr(String attrName, String attrValue, Integer serviceId, SIComponent siComponent,
									 String userName) {
		if(Objects.nonNull(attrValue)) {
			SIComponentAttribute attribute = new SIComponentAttribute();
			attribute.setAttributeName(attrName);
			attribute.setAttributeValue(attrValue);
			attribute.setAttributeAltValueLabel(attrName);
			attribute.setSiComponent(siComponent);
			attribute.setSiServiceDetailId(serviceId);
			attribute.setIsActive("Y");
			attribute.setCreatedDate(new Timestamp(new Date().getTime()));
			attribute.setCreatedBy(userName);
			siComponentAttributeRepository.saveAndFlush(attribute);
		}
	}
	
	private void updateRenewalBaseServiceDetails(ScServiceDetailBean scServiceDetail,
			SIServiceDetail activeSiServiceDetail) {
		LOGGER.info("updateRenewalBaseServiceDetails method invoked for active si service id ::{}", activeSiServiceDetail.getId());
		if (activeSiServiceDetail != null) {
			LOGGER.info("SI Service Detail Id exists ::{}", activeSiServiceDetail.getId());
			activeSiServiceDetail.setOrderType(scServiceDetail.getOrderType());
			activeSiServiceDetail.setOrderCategory(scServiceDetail.getOrderCategory());
			activeSiServiceDetail.setSiOrderUuid(scServiceDetail.getScOrderUuid());
			activeSiServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
			activeSiServiceDetail.setUpdatedBy("Optimus_O2C");
			siServiceDetailRepository.saveAndFlush(activeSiServiceDetail);
		}
	}
	
	private void updateRenewalServiceBillingDetails(ScServiceDetailBean scServiceDetail,
			SIServiceDetail activeSiServiceDetail) {
		LOGGER.info("updateRenewalServiceBillingDetails method invoked for active si service id ::{}", activeSiServiceDetail.getId());
		if (activeSiServiceDetail != null) {
			LOGGER.info("SI Service Detail Id exists ::{}", activeSiServiceDetail.getId());
			activeSiServiceDetail.setBillingCompletedDate(scServiceDetail.getBillingCompletedDate());
			activeSiServiceDetail.setMrc(scServiceDetail.getMrc());
			activeSiServiceDetail.setNrc(scServiceDetail.getNrc());
			activeSiServiceDetail.setArc(scServiceDetail.getArc());
			siServiceDetailRepository.saveAndFlush(activeSiServiceDetail);
		}
	}
	
	private void updateRenewalOrderDetails(ScOrderBean scOrderBean,SIOrder activeSiOrder){
		LOGGER.info("updateRenewalOrderDetails method invoked for active si order id ::{}", activeSiOrder.getId());
		if (activeSiOrder != null) {
			LOGGER.info("SI Order Detail Id exists ::{}", activeSiOrder.getId());
			activeSiOrder.setUuid(scOrderBean.getUuid());
			activeSiOrder.setOpOrderCode(scOrderBean.getOpOrderCode());
			activeSiOrder.setOrderType(scOrderBean.getOrderType());
			activeSiOrder.setOrderCategory(scOrderBean.getOrderCategory());
			activeSiOrder.setTpsCrmOptyId(scOrderBean.getSfdcOptyId());
			activeSiOrder.setUpdatedDate(new Timestamp(new Date().getTime()));
			activeSiOrder.setUpdatedBy("Optimus_O2C");
			siOrderRepository.saveAndFlush(activeSiOrder);
		}
	}
	
	private void updateRenewalOrderAttributes(Set<ScOrderAttributeBean> scOrderAttributes,SIOrder activeSiOrder) {
		LOGGER.info("updateRenewalOrderAttribute method invoked for active si order id ::{}", activeSiOrder.getId());
		ScOrderAttributeBean effectiveDateAttributeBean = scOrderAttributes.stream()
				.filter(soa -> "Effective Date".equalsIgnoreCase(soa.getAttributeName())).findFirst().orElse(null);
		if (effectiveDateAttributeBean != null && effectiveDateAttributeBean.getAttributeValue() != null) {
			LOGGER.info("Effective Date Bean Value exists::{}",effectiveDateAttributeBean.getAttributeValue());
			SIOrderAttribute effectiveDateOrderAttribute = siOrderAttributeRepository
					.findFirstBySiOrderAndAttributeNameOrderByIdDesc(activeSiOrder, "Effective Date");
			if (effectiveDateOrderAttribute != null) {
				LOGGER.info("Effective Date exists::{} for Order Id::{}", activeSiOrder.getId());
				effectiveDateOrderAttribute.setAttributeValue(effectiveDateAttributeBean.getAttributeValue());
				effectiveDateOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
				effectiveDateOrderAttribute.setUpdatedBy("Optimus_O2C");
				siOrderAttributeRepository.saveAndFlush(effectiveDateOrderAttribute);
			} else {
				LOGGER.info("Effective Date not exists::{} for Order Id::{}", activeSiOrder.getId());
				SIOrderAttribute siOrderAttribute = new SIOrderAttribute();
				siOrderAttribute.setAttributeAltValueLabel(effectiveDateAttributeBean.getAttributeAltValueLabel());
				siOrderAttribute.setAttributeName(effectiveDateAttributeBean.getAttributeName());
				siOrderAttribute.setAttributeValue(effectiveDateAttributeBean.getAttributeValue());
				siOrderAttribute.setCategory(effectiveDateAttributeBean.getCategory());
				siOrderAttribute.setCreatedBy("Optimus_O2C");
				siOrderAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
				siOrderAttribute.setIsActive(CommonConstants.Y);
				siOrderAttribute.setUpdatedBy("Optimus_O2C");
				siOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
				siOrderAttribute.setSiOrder(activeSiOrder);
				siOrderAttributeRepository.saveAndFlush(siOrderAttribute);
			}
		}
	}
	
	private void updateRenewalContractBaseDetails(Set<ScContractInfoBean> scContractInfos,SIOrder activeSiOrder) {
		LOGGER.info("updateRenewalContractDetails method invoked for active si order id ::{}", activeSiOrder.getId());
		ScContractInfoBean currentScContractInfo = scContractInfos.stream().findFirst().orElse(null);
		SIContractInfo activeSiContractInfo = siContractInfoRepository
				.findFirstBySiOrder_id(activeSiOrder.getId());
		if (activeSiContractInfo != null && currentScContractInfo != null
				&& currentScContractInfo.getOrderTermInMonths() != null) {
			LOGGER.info("SI Contract Info Id exists ::{}", activeSiContractInfo.getId());
			activeSiContractInfo.setOrderTermInMonths(currentScContractInfo.getOrderTermInMonths());
			activeSiContractInfo
					.setContractStartDate(new Timestamp(currentScContractInfo.getContractStartDate().getTime()));
			activeSiContractInfo
					.setContractEndDate(new Timestamp(currentScContractInfo.getContractEndDate().getTime()));
			activeSiContractInfo.setUpdatedDate(new Timestamp(new Date().getTime()));
			activeSiContractInfo.setUpdatedBy("Optimus_O2C");
			siContractInfoRepository.saveAndFlush(activeSiContractInfo);
		}
	}
	
	private void updateRenewalContractBillingDetails(Set<ScContractInfoBean> scContractInfos,SIOrder activeSiOrder) {
		LOGGER.info("updateRenewalContractBillingDetails method invoked for active si order id ::{}", activeSiOrder.getId());
		ScContractInfoBean currentScContractInfo = scContractInfos.stream().findFirst().orElse(null);
		SIContractInfo activeSiContractInfo = siContractInfoRepository
				.findFirstBySiOrder_id(activeSiOrder.getId());
		if (activeSiContractInfo != null && currentScContractInfo != null
				&& currentScContractInfo.getOrderTermInMonths() != null) {
			LOGGER.info("SI Contract Info Id exists ::{}", activeSiContractInfo.getId());
			activeSiContractInfo.setMrc(currentScContractInfo.getMrc());
			activeSiContractInfo.setNrc(currentScContractInfo.getNrc());
			activeSiContractInfo.setArc(currentScContractInfo.getArc());
			siContractInfoRepository.saveAndFlush(activeSiContractInfo);
		}
	}
	
	private void updateRenewalComponentAttributes(ScServiceDetailBean scServiceDetail,SIServiceDetail activeSiServiceDetail) {
		LOGGER.info("updateRenewalComponentAttributes method invoked for active si service id ::{}", activeSiServiceDetail.getId());
		Map<String, String> atMap = new HashMap<>();
		for (ScComponentBean scComponentBean : scServiceDetail.getScComponentBeans()) {
			scComponentBean.getScComponentAttributeBeans().stream().forEach(scCompAttr -> {
				if (scCompAttr.getAttributeName().equalsIgnoreCase("PO_DATE")) {
					LOGGER.info("PO Date exists::{} for Service Detail Id::{}",
							scCompAttr.getAttributeValue());
					atMap.put("PO_DATE", scCompAttr.getAttributeValue());
				} else if (scCompAttr.getAttributeName().equalsIgnoreCase("PO_NUMBER")) {
					LOGGER.info("PO Number Days exists::{} for Service Detail Id::{}",
							scCompAttr.getAttributeValue());
					atMap.put("PO_NUMBER", scCompAttr.getAttributeValue());
				}
			});
		}
		SIComponent siComponent = siComponentRepository.findFirstBySiServiceDetailId(activeSiServiceDetail.getId());
		if (siComponent != null && siComponent.getSiteType() != null) {
			LOGGER.info("Renewal siComponent id ::{}", siComponent.getId());
			updateAttributes(activeSiServiceDetail.getId(), atMap, "LM", siComponent.getSiteType());
		}
	}

	private void replaceAttributeNameEndpointToCpe(List<ScComponentBean> scComponentBeans){
		LOGGER.info("Inside replaceEndpointWithCpeAttributeName method {}",scComponentBeans.size());
		scComponentBeans.stream().forEach(scComponentBean -> {
			scComponentBean.getScComponentAttributeBeans().stream().forEach(scComponentAttributeBean -> {
				LOGGER.info("Attriute start with endpoint replace by cpe {}",scComponentAttributeBean.getAttributeName());
				if(org.apache.commons.lang3.StringUtils.containsIgnoreCase(scComponentAttributeBean.getAttributeName(),"endpoint")){
					scComponentAttributeBean.setAttributeName(scComponentAttributeBean.getAttributeName().replace("endpoint","cpe"));				}
			});
		});
	}
	
	private void updateNovationOrderDetails(ScOrderBean scOrderBean,SIOrder activeSiOrder){
		LOGGER.info("updateNovationOrderDetails method invoked for active si order id ::{}", activeSiOrder.getId());
		if (activeSiOrder != null) {
			LOGGER.info("SI Order Detail Id exists ::{}", activeSiOrder.getId());
			activeSiOrder.setUuid(scOrderBean.getUuid());
			activeSiOrder.setOpOrderCode(scOrderBean.getOpOrderCode());
			activeSiOrder.setOrderType(scOrderBean.getOrderType());
			activeSiOrder.setOrderCategory(scOrderBean.getOrderCategory());
			activeSiOrder.setTpsCrmOptyId(scOrderBean.getSfdcOptyId());
			activeSiOrder.setErfCustLeId(scOrderBean.getErfCustLeId());
			activeSiOrder.setErfCustLeName(scOrderBean.getErfCustLeName());
			activeSiOrder.setUpdatedDate(new Timestamp(new Date().getTime()));
			activeSiOrder.setUpdatedBy("Optimus_O2C");
			siOrderRepository.saveAndFlush(activeSiOrder);
		}
	}
	
	private void updateNovationContractDetails(Set<ScContractInfoBean> scContractInfos,SIOrder activeSiOrder) {
		LOGGER.info("updateNovationContractDetails method invoked for active si order id ::{}", activeSiOrder.getId());
		ScContractInfoBean currentScContractInfo = scContractInfos.stream().findFirst().orElse(null);
		SIContractInfo activeSiContractInfo = siContractInfoRepository
				.findFirstBySiOrder_id(activeSiOrder.getId());
		if (activeSiContractInfo != null && currentScContractInfo != null
				&& currentScContractInfo.getOrderTermInMonths() != null) {
			LOGGER.info("SI Contract Info Id exists ::{}", activeSiContractInfo.getId());
			activeSiContractInfo.setErfCustLeId(currentScContractInfo.getErfCustLeId());
			activeSiContractInfo.setErfCustLeName(currentScContractInfo.getErfCustLeName());
			activeSiContractInfo.setUpdatedDate(new Timestamp(new Date().getTime()));
			activeSiContractInfo.setUpdatedBy("Optimus_O2C");
			siContractInfoRepository.saveAndFlush(activeSiContractInfo);
		}
	}
	
	private void updateNovationServiceDetails(ScServiceDetailBean scServiceDetail,
			SIServiceDetail activeSiServiceDetail) {
		LOGGER.info("updateNovationServiceDetails method invoked for active si service id ::{}", activeSiServiceDetail.getId());
		if (activeSiServiceDetail != null) {
			LOGGER.info("SI Service Detail Id exists ::{}", activeSiServiceDetail.getId());
			activeSiServiceDetail.setOrderType(scServiceDetail.getOrderType());
			activeSiServiceDetail.setOrderCategory(scServiceDetail.getOrderCategory());
			activeSiServiceDetail.setOrderSubCategory(scServiceDetail.getOrderSubCategory());
			activeSiServiceDetail.setSiOrderUuid(scServiceDetail.getScOrderUuid());
			activeSiServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
			activeSiServiceDetail.setUpdatedBy("Optimus_O2C");
			siServiceDetailRepository.saveAndFlush(activeSiServiceDetail);
		}
	}
	
	private void updateNovationServiceRfDetails(ScServiceDetailBean scServiceDetail,ScOrderBean scOrderBean) {
		LOGGER.info("updateNovationServiceRfDetails method invoked for active sc service id ::{}", scServiceDetail.getId());
		if (scServiceDetail.getUuid() != null) {
			LOGGER.info("SC Service Detail UuId exists ::{}", scServiceDetail.getUuid());
			List<ServiceInvRf> serviceInvRfList=serviceInvRfRepository.findByServiceCode(scServiceDetail.getUuid());
			if(serviceInvRfList!=null && !serviceInvRfList.isEmpty()) {
				LOGGER.info("serviceInvRfList exists ::{}", serviceInvRfList.size());
				serviceInvRfList.stream().forEach(serviceInvRf -> {
					LOGGER.info("serviceInvRf Id ::{}", serviceInvRf.getId());
					serviceInvRf.setCustomerName(scOrderBean.getErfCustLeName());
					serviceInvRf.setLastUpdatedBy("OPTIMUS");
					serviceInvRf.setLastUpdatedDate(new Timestamp(new Date().getTime()).toString());
					serviceInvRfRepository.saveAndFlush(serviceInvRf);
				});
			}
		}
	}
}
