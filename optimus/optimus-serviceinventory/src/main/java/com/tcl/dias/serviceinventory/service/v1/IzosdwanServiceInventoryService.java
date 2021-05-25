package com.tcl.dias.serviceinventory.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.IzoSdwanSiteDetails;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiCpeBean;
import com.tcl.dias.common.beans.SiSearchBean;
import com.tcl.dias.common.beans.SiServiceDetailBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.entities.ViewSdwanServiceSiteDetails;
import com.tcl.dias.serviceinventory.entity.entities.ViewSiServiceInfoAll;
import com.tcl.dias.serviceinventory.entity.entities.VwServiceAssetInfo;
import com.tcl.dias.serviceinventory.entity.entities.VwSiServiceInfoAll;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceDetailRepository;
import com.tcl.dias.serviceinventory.entity.repository.VwSdwanServiceClassificationSiteDetails;
import com.tcl.dias.serviceinventory.entity.repository.VwServiceAssetInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.VwSiServiceInfoAllRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This is the service class for IZOSDWAN Service Inventory part
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class IzosdwanServiceInventoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanServiceInventoryService.class);

	@Autowired
	VwSiServiceInfoAllRepository vwSiServiceInfoAllRepository;

	@Autowired
	VwSdwanServiceClassificationSiteDetails vwSdwanServiceClassificationPortDetails;
	@Autowired
	VwServiceAssetInfoRepository vwServiceAssetInfoRepository;
	
	@Autowired
	SIServiceDetailRepository siServiceDetailRepository;

	public List<SiServiceDetailBean> findAllServicesByCustomer(SiSearchBean siSearchBean) throws TclCommonException {

		List<SiServiceDetailBean> siServiceDetailBeans = new ArrayList<>();
		try {
			List<ViewSiServiceInfoAll> vwSiServiceInfoAlls = new ArrayList<>();
			if (siSearchBean.getLeIds() != null && !siSearchBean.getLeIds().isEmpty()) {
				vwSiServiceInfoAlls = vwSiServiceInfoAllRepository
						.findByOrderCustomerIdAndSourceCountryInAndProductFamilyNameInAndOrderCustLeIdIn(
								Integer.parseInt(siSearchBean.getCustomerId()), siSearchBean.getCountries(), siSearchBean.getProducts(),
								siSearchBean.getLeIds());
			} else {
				vwSiServiceInfoAlls = vwSiServiceInfoAllRepository
						.findByOrderCustomerIdAndSourceCountryInAndProductFamilyNameIn(
								Integer.parseInt(siSearchBean.getCustomerId()), siSearchBean.getCountries(), siSearchBean.getProducts());
			}
			if (vwSiServiceInfoAlls != null && !vwSiServiceInfoAlls.isEmpty()) {
				vwSiServiceInfoAlls = vwSiServiceInfoAlls.stream()
						.filter(info -> info.getIsActive().equals(CommonConstants.Y)).collect(Collectors.toList());
				if (vwSiServiceInfoAlls != null && !vwSiServiceInfoAlls.isEmpty()) {
					vwSiServiceInfoAlls.stream().forEach(vwSiServiceInfoAll -> {
						List<VwServiceAssetInfo> vwServiceAssetInfos = vwServiceAssetInfoRepository
								.findCpeAssestes(vwSiServiceInfoAll.getServiceId(),
										IzosdwanCommonConstants.CPE);
						constructSiServiceDetailBean(siServiceDetailBeans, vwSiServiceInfoAll, vwServiceAssetInfos);
					});
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on getting the service infomations..", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return siServiceDetailBeans;
	}

	private void constructSiServiceDetailBean(List<SiServiceDetailBean> siServiceDetailBeans,
			ViewSiServiceInfoAll vwSiServiceInfoAll, List<VwServiceAssetInfo> vwServiceAssetInfos) {
		if (!StringUtils.isAnyBlank(vwSiServiceInfoAll.getLatLong())
				&& (vwSiServiceInfoAll.getLocationId()!=null) && StringUtils.isNotBlank(vwSiServiceInfoAll.getServiceClassification())) {
			SiServiceDetailBean siServiceDetailBean = new SiServiceDetailBean();
			siServiceDetailBean.setAccessTopology(vwSiServiceInfoAll.getVpnTopology());
			siServiceDetailBean.setAccessType(vwSiServiceInfoAll.getAccessType());
			siServiceDetailBean.setAdditionalIpsRequired(vwSiServiceInfoAll.getAdditionalIpsReq());
			siServiceDetailBean.setAddressId(vwSiServiceInfoAll.getLocationId().toString());
			siServiceDetailBean.setArc(vwSiServiceInfoAll.getArc());
			siServiceDetailBean.setBandwidth(vwSiServiceInfoAll.getBandwidth());
			siServiceDetailBean.setBandwidthUnit(vwSiServiceInfoAll.getBandwidthUnit());
			siServiceDetailBean.setBillingFrequency(vwSiServiceInfoAll.getBillingFrequency());
			siServiceDetailBean.setBillingMethod(vwSiServiceInfoAll.getBillingMethod());
			siServiceDetailBean.setCos(vwSiServiceInfoAll.getGvpnCos());
			siServiceDetailBean.setCurrencyId(vwSiServiceInfoAll.getCurrencyId());
			siServiceDetailBean.setCustomerId(vwSiServiceInfoAll.getOrderCustomerId());
			siServiceDetailBean.setCustomerLeId(vwSiServiceInfoAll.getOrderCustLeId());
			siServiceDetailBean.setInterfaceType(vwSiServiceInfoAll.getSiteEndInterface());
			siServiceDetailBean.setLastMileBandwidth(vwSiServiceInfoAll.getLastMileBandwidth());
			siServiceDetailBean.setLastMileBandwidthUnit(vwSiServiceInfoAll.getLastMileBandwidthUnit());
			siServiceDetailBean.setLastmileProvider(vwSiServiceInfoAll.getLastMileProvider());
			siServiceDetailBean.setManagmentType(vwSiServiceInfoAll.getServiceManagementOption());
			siServiceDetailBean.setMrc(vwSiServiceInfoAll.getMrc());
			siServiceDetailBean.setNrc(vwSiServiceInfoAll.getNrc());
			siServiceDetailBean.setOffering(vwSiServiceInfoAll.getProductOfferingName());
			siServiceDetailBean.setLatLong(vwSiServiceInfoAll.getLatLong());
			siServiceDetailBean.setProduct(vwSiServiceInfoAll.getProductFamilyName());
			siServiceDetailBean.setPopAddress(vwSiServiceInfoAll.getPopAddress());
			siServiceDetailBean.setPortMode(vwSiServiceInfoAll.getPortMode());
			siServiceDetailBean.setPriSec(vwSiServiceInfoAll.getPrimaryOrSecondary());
			siServiceDetailBean.setRoutingProtocol(vwSiServiceInfoAll.getRoutingProtocol());
			siServiceDetailBean.setServiceId(vwSiServiceInfoAll.getServiceId());
			siServiceDetailBean.setServiceType(vwSiServiceInfoAll.getServiceType());
			siServiceDetailBean.setServiceVariant(vwSiServiceInfoAll.getServiceVarient());
			siServiceDetailBean.setSiteTopology(vwSiServiceInfoAll.getGvpnSiteTopology());
			siServiceDetailBean.setSiteType(vwSiServiceInfoAll.getServiceClassification());
			siServiceDetailBean.setSupplierId(vwSiServiceInfoAll.getOrderSpLeId());
			siServiceDetailBean.setTermsInMonths(String.valueOf(vwSiServiceInfoAll.getOrderTermInMonths()));
			siServiceDetailBean.setVpnTopology(vwSiServiceInfoAll.getVpnTopology());
			siServiceDetailBean.setVpnName(vwSiServiceInfoAll.getVpnName());
			siServiceDetailBean.setIpAddressArrangementType(vwSiServiceInfoAll.getIpAddressArrangement());
			siServiceDetailBean.setIpv4AddressPoolSize(vwSiServiceInfoAll.getIpv4AddressPoolSize());
			siServiceDetailBean.setIpv6AddressPoolSize(vwSiServiceInfoAll.getIpv6AddressPoolSize());
			siServiceDetailBean.setSiOrderId(vwSiServiceInfoAll.getOrderSysId());
			siServiceDetailBean.setSiServiceDetailId(vwSiServiceInfoAll.getId());
			siServiceDetailBean.setPrimaryServiceId(vwSiServiceInfoAll.getPrimaryServiceId());
			siServiceDetailBean.setContractEndDate(vwSiServiceInfoAll.getContractEndDate());
			siServiceDetailBean.setContractStartDate(vwSiServiceInfoAll.getContractStartDate());
			siServiceDetailBean.setCountry(vwSiServiceInfoAll.getSourceCountry());
			siServiceDetailBean.setCity(vwSiServiceInfoAll.getSourceCity());
			siServiceDetailBean.setSiteAddress(vwSiServiceInfoAll.getCustomerSiteAddress());
			siServiceDetailBean.setParentOpportunityId(vwSiServiceInfoAll.getOpportunityId());
			siServiceDetailBean.setGvpnSiteTopology(vwSiServiceInfoAll.getGvpnSiteTopology());
			Optional<SIServiceDetail> siServiceDetail =  siServiceDetailRepository.findById(vwSiServiceInfoAll.getId());
			if(siServiceDetail.isPresent()) {
				siServiceDetailBean.setPopSiteAddress(siServiceDetail.get().getPopSiteAddress());
				siServiceDetailBean.setPopSiteCode(siServiceDetail.get().getPopSiteCode());
				siServiceDetailBean.setSourceCity(siServiceDetail.get().getSourceCity());
				siServiceDetailBean.setSourceCountry(siServiceDetail.get().getSourceCountry());
				siServiceDetailBean.setSourceState(siServiceDetail.get().getSourceState());
			}
			// CPE Details
			if (vwServiceAssetInfos != null && !vwServiceAssetInfos.isEmpty()) {
				List<SiCpeBean> siCpeBeans = new ArrayList<>();
				vwServiceAssetInfos.stream().forEach(vwServiceAssetInfo -> {
					SiCpeBean siCpeBean = new SiCpeBean();
					siCpeBean.setIsShared(vwServiceAssetInfo.getIsSharedInd());
					siCpeBean.setModel(vwServiceAssetInfo.getModel());
					siCpeBean.setScope(vwServiceAssetInfo.getScopeOfManagement());
					siCpeBean.setSerialNo(vwServiceAssetInfo.getSerialNo());
					siCpeBean.setSupportType(vwServiceAssetInfo.getSupportType());
					siCpeBeans.add(siCpeBean);
				});
				siServiceDetailBean.setSiCpeBeans(siCpeBeans);
			}
			siServiceDetailBeans.add(siServiceDetailBean);
		}
	}

	/**
	 * this method gets the cpe port details from the inventory
	 * 
	 * @return
	 */
	public List<IzoSdwanSiteDetails> getCpePortDetails() {
		List<IzoSdwanSiteDetails> cpePortDet = new ArrayList<>();
		try {
			List<ViewSdwanServiceSiteDetails> portDet = vwSdwanServiceClassificationPortDetails.findAll();
			cpePortDet = extractPortDet(portDet);
		} catch (Exception e) {

		}
		return cpePortDet;
	}

	private List<IzoSdwanSiteDetails> extractPortDet(List<ViewSdwanServiceSiteDetails> portDet) {
		List<IzoSdwanSiteDetails> portDetails = portDet.stream().map(this::getPortDet).collect(Collectors.toList());
		return portDetails;
	}

	private IzoSdwanSiteDetails getPortDet(ViewSdwanServiceSiteDetails portDet) {
		IzoSdwanSiteDetails portDetails = new IzoSdwanSiteDetails();
		portDetails.setSiteTypeName(portDet.getSiteTypeName());
		portDetails.setNoOfCpePorts(portDet.getNoOfCpePorts());
		portDetails.setNoOfL2Ports(portDet.getNoOfL2Ports());
		portDetails.setNoOfL3Ports(portDet.getNoOfL3Ports());
		return portDetails;
	}

	public Integer getSelectedProfileData(SiSearchBean siSearchBean) throws TclCommonException {
		LOGGER.info("Inside the function getSelectedProfileData {}", siSearchBean.getCustomerId());
		List<ViewSiServiceInfoAll> siteDet = new ArrayList<>();
		Integer count = 0;
		try {
			siteDet = vwSiServiceInfoAllRepository.selectByOrderCustomerId(Integer.parseInt(siSearchBean.getCustomerId()),siSearchBean.getLeIds());
			if ((siteDet != null && !siteDet.isEmpty())) {
				siteDet = siteDet.stream().filter(site -> site.getIsActive().equals(CommonConstants.Y))
						.collect(Collectors.toList());
				if ((siteDet != null && !siteDet.isEmpty())) {
					LOGGER.info("Got list from the inventory {} {}", siteDet, siteDet.size());
					count = siteDet.size();
				}
			}

		} catch (Exception e) {
			LOGGER.info("Error in getting service inventory details ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Inside the function getSelectedProfileData count {}", count);
		return count;
	}
	
	public String  getPortMode(String id) {
		String portMode="";
		try {
			 portMode = vwSiServiceInfoAllRepository.findPortModeByServiceId(id);
		} catch (Exception e) {

		}
		return portMode;
	}
}
