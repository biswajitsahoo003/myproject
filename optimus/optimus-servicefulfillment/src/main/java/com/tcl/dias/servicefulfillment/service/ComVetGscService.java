package com.tcl.dias.servicefulfillment.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.gsc.BillingAddressBean;
import com.tcl.dias.servicefulfillment.beans.gsc.BillingDetailsBean;
import com.tcl.dias.servicefulfillment.beans.gsc.BillingProfileApiBean;
import com.tcl.dias.servicefulfillment.beans.gsc.BillingProfileBulkBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CommercialVettingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CustGSTNAddressBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CustTaxDetailsBean;
import com.tcl.dias.servicefulfillment.beans.gsc.OrgAddressBean;
import com.tcl.dias.servicefulfillment.beans.gsc.OrgDetailsBean;
import com.tcl.dias.servicefulfillment.beans.gsc.OrganisationBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ProductDetailsBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;

@Service
@Transactional(readOnly = true)
public class ComVetGscService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComVetGscService.class);

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	RestClientService restClientService;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;
	
	@Value("${gsc.billingdetail.url}")
	private String billingDetailUrl;

	@Value("${gsc.billingdetailsbulk.url}")
	private String billingDetailBulkUrl;

	@Value("${gsc.organisationdetail.url}")
	private String organisationUrl;

	@Value("${gsc.billingdetail.authorization}")
	private String authorization;

	public BillingProfileApiBean getBillingSingleDetail(Integer serviceId) {
		BillingProfileApiBean billingProfileApiBean = new BillingProfileApiBean();
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId, "LM",
				"A");
		String profileRelNo = scComponentAttributesmap.getOrDefault("profileId", "");
		String orgId = scServiceDetail.getCustOrgNo();
		BillingProfileBulkBean billingProfileBulkBean = getBillingBulkBean(profileRelNo, orgId);
		String url = billingDetailUrl + billingProfileBulkBean.getBillingProfiles().get(0).getParentOrgId() + "/"
				+ orgId + "/" + profileRelNo;
		LOGGER.info("Calling SECS Single Url {}", url);
		Map<String, String> headers = getHeaders(authorization);
		RestResponse response = restClientService.getWithProxy(url, headers, false);
		LOGGER.info("Calling SECS Single response {}", response);
		billingProfileApiBean = Utils.fromJson(response.getData(), new TypeReference<BillingProfileApiBean>() {
		});
		return billingProfileApiBean;
	}

	public BillingProfileBulkBean getBillingBulkBean(String profileRelNo, String orgId) {
		BillingProfileBulkBean billingProfileBulkBean = new BillingProfileBulkBean();
		String url = billingDetailBulkUrl;
		Map<String, String> param = new HashMap<>();
		param.put("profileRelNo", profileRelNo);
		param.put("orgId", orgId);
		LOGGER.info("Calling SECS Bulk Url {}, params {}", url, param);
		Map<String, String> headers = getHeaders(authorization);
		HttpHeaders httpHeaders = new HttpHeaders();
		headers.forEach((key, value) -> httpHeaders.set(key, value));
		RestResponse response = restClientService.getWithQueryParamWithProxy(url, param, httpHeaders);
		LOGGER.info("Calling SECS Bulk response {}", response);
		billingProfileBulkBean = Utils.fromJson(response.getData(), new TypeReference<BillingProfileBulkBean>() {
		});
		return billingProfileBulkBean;
	}

	public OrganisationBean getOrganisation(Integer serviceId) {
		OrganisationBean organisationBean = new OrganisationBean();
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		String orgId = scServiceDetail.getCustOrgNo();
		String url = organisationUrl + orgId;
		LOGGER.info("Calling SECS Org Url {}", url);
		Map<String, String> headers = getHeaders(authorization);
		RestResponse response = restClientService.getWithProxy(url, headers, false);
		LOGGER.info("Calling SECS Org response {}", response);
		organisationBean = Utils.fromJson(response.getData(), new TypeReference<OrganisationBean>() {
		});
		return organisationBean;
	}

	public CommercialVettingBean getCommercialVettingDetails(BillingProfileApiBean billingProfileApiBean,
			OrganisationBean organisationBean,Map<String, String> scComponentAttributesmap ) {
		CommercialVettingBean commercialVettingBean = new CommercialVettingBean();
		commercialVettingBean.setOrganizationNumber(organisationBean.getOrgId());
		commercialVettingBean.setFinanceAbbr(organisationBean.getOrgFinAbbrCd());

		StringBuilder legalAddress = new StringBuilder();
		for (OrgAddressBean address : organisationBean.getOrgAddress()) {
			if (address.getAddressType().equalsIgnoreCase("LEGAL")) {
				if (address.getAddressLine1() != null)
					legalAddress.append(address.getAddressLine1() + " ");
				if (address.getAddressLine2() != null)
					legalAddress.append(address.getAddressLine2() + " ");
				if (address.getAddressLine3() != null)
					legalAddress.append(address.getAddressLine3() + " ");
				commercialVettingBean.setLegalCountry(address.getCountry());
				// commercialVettingBean.setAddressNumber(address.getAddressSeqNo());
				break;
			}
		}
		commercialVettingBean.setLegalAddress(legalAddress.toString());
		commercialVettingBean.setEquipmentCountry(organisationBean.getCorpCntryName());
		commercialVettingBean.setOrgLink(organisationBean.getOrgIsLinkToOrg());
		commercialVettingBean.setAdminAbbr(organisationBean.getOrgAbbrCd());
		commercialVettingBean.setPrintTaxFlag(organisationBean.getPrintTaxFlag());
		commercialVettingBean.setCustomerType(organisationBean.getCustomerType());
		
		if(organisationBean.getOrgDetails() != null) {
			OrgDetailsBean orgDetails = organisationBean.getOrgDetails();
			commercialVettingBean.setOrganizationName(orgDetails.getOrgLegalName());
			commercialVettingBean.setBillingLanguage(orgDetails.getPrefLang());
			commercialVettingBean.setTaxExemption(orgDetails.getVatExemptionReason());
			commercialVettingBean.setStartDate(orgDetails.getOrgActvDate());
			commercialVettingBean.setOrganizationType(orgDetails.getOrgType());
			commercialVettingBean.setOrgBusinessType(orgDetails.getOrgBusType());
			commercialVettingBean.setCuid(orgDetails.getCuId());
			commercialVettingBean.setOrgShortName(orgDetails.getOrgAbbrName());
		}
		
		if(billingProfileApiBean.getBillingDetails() != null) {
			BillingDetailsBean billingDetails = billingProfileApiBean.getBillingDetails();
			commercialVettingBean.setBillingCompany(billingDetails.getBillingEntity());
			if(billingDetails.getCmsIdDetails() != null && !billingDetails.getCmsIdDetails().isEmpty()) {
				commercialVettingBean.setCmsId(billingDetails.getCmsIdDetails().get(0));
			}
			commercialVettingBean.setProfileNumber(billingProfileApiBean.getProfileRelNo());
			commercialVettingBean.setProfileType(billingDetails.getProfileType());
			commercialVettingBean.setStartDateProfile(billingDetails.getProfileStartDate());
			commercialVettingBean.setEndDateProfile(billingDetails.getProfileEndDate());
			commercialVettingBean.setBillingTerm(billingDetails.getBillingTerm());
			commercialVettingBean.setPaymentTerms(billingDetails.getPaymentTerm());
			commercialVettingBean.setPaymentMethod(billingDetails.getPaymentMethod());
			commercialVettingBean.setInvoiceCurrency(billingDetails.getCurrency());
			commercialVettingBean.setRapidCDRFlag(billingDetails.getRapidCDRFlag());
			commercialVettingBean.setRapidCDRID(billingDetails.getRapidCDRID());
			commercialVettingBean.setPrintFlag(billingDetails.getPrintFlag());
			commercialVettingBean.setEmailFlag(billingDetails.getEmailFlag());
			commercialVettingBean.setEmailAddress(billingDetails.getInvoiceEmail());
			commercialVettingBean.setCbfFlag(billingDetails.getBillingFormattingRequired());
			commercialVettingBean.setBillingTerm(billingDetails.getBillingTerm());
			commercialVettingBean.setDepartmentName(billingDetails.getDepartment());
			commercialVettingBean.setInvoiceGroup(billingDetails.getBillGrpCd());
			
			if(billingDetails.getDocumentNmDetails() != null && !billingDetails.getDocumentNmDetails().isEmpty()) {
				String documentNmDetails = new String();
				for (String documentNm : billingDetails.getDocumentNmDetails()) {
					documentNmDetails = documentNmDetails + documentNm;
				}
				commercialVettingBean.setDocuments(documentNmDetails);
			}
			
			if (billingDetails.getProductDetails() != null && !billingDetails.getProductDetails().isEmpty()) {
				String cmsId = scComponentAttributesmap.getOrDefault("cmsId", "");
				int cmsIndex = 0;
				if (billingDetails.getCmsIdDetails() != null && !billingDetails.getCmsIdDetails().isEmpty()) {
					cmsIndex = billingDetails.getCmsIdDetails().indexOf(cmsId);
				}
				if (cmsIndex >= 0 && billingDetails.getProductDetails().size() > cmsIndex) {
					ProductDetailsBean productDetailsBean = billingDetails.getProductDetails().get(cmsIndex);
					commercialVettingBean.setServiceAbbr(productDetailsBean.getProductName());
					commercialVettingBean.setInvoiceGroupFlag(productDetailsBean.getInvceGrpnFl());
				}
			}
			
			if(billingDetails.getCustTaxDetails() != null && !billingDetails.getCustTaxDetails().isEmpty()) {
				CustTaxDetailsBean custTaxDetailsBean =  billingDetails.getCustTaxDetails().get(0);
				if(custTaxDetailsBean.getTaxNumber() != null && !custTaxDetailsBean.getTaxNumber().isEmpty()) {					
					String taxNumbers = String.join(",", custTaxDetailsBean.getTaxNumber());
					commercialVettingBean.setTaxNumber(taxNumbers);
				}
			}
		}
		if(billingProfileApiBean.getBillingAddress() != null) {
			BillingAddressBean billingAddress = billingProfileApiBean.getBillingAddress();
			StringBuilder billingAddressStrBu = new StringBuilder();
			if(billingAddress.getAddressLine1() !=null)
				billingAddressStrBu.append(billingAddress.getAddressLine1()+" ");
			if(billingAddress.getAddressLine2() !=null)
				billingAddressStrBu.append(billingAddress.getAddressLine2()+" ");
			if(billingAddress.getAddressLine3() !=null)
				billingAddressStrBu.append(billingAddress.getAddressLine3()+" ");
			if(billingAddress.getAddressLine4() !=null)				
				billingAddressStrBu.append(billingAddress.getAddressLine4()+" ");
			if(billingAddress.getCity() !=null)
				billingAddressStrBu.append(billingAddress.getCity()+" ");
			if(billingAddress.getState() !=null)
				billingAddressStrBu.append(billingAddress.getState()+" ");
			if(billingAddress.getCountry() !=null)
				billingAddressStrBu.append(billingAddress.getCountry()+" ");
			if(billingAddress.getZipCode() !=null)
				billingAddressStrBu.append(billingAddress.getZipCode()+" ");
					
			commercialVettingBean.setBillingAddress(billingAddressStrBu.toString());
			commercialVettingBean.setBillingAddressNumber(billingAddress.getAddressSeq());
			
		}	
		
		if(billingProfileApiBean.getCustGSTNAddress() != null) {
			CustGSTNAddressBean gstnAddress = billingProfileApiBean.getCustGSTNAddress();
			 
			StringBuilder gstnAddressStrBu = new StringBuilder();
					if(gstnAddress.getGstn() !=null)
						gstnAddressStrBu.append(gstnAddress.getGstn()+" ");
					if(gstnAddress.getAddressLine1() !=null)
						gstnAddressStrBu.append(gstnAddress.getAddressLine1()+" ");
					if(gstnAddress.getAddressLine2() !=null)
						gstnAddressStrBu.append(gstnAddress.getAddressLine2()+" ");
					if(gstnAddress.getAddressLine3() !=null)
						gstnAddressStrBu.append(gstnAddress.getAddressLine3()+" ");
					if(gstnAddress.getAddressLine4() !=null)
						gstnAddressStrBu.append(gstnAddress.getAddressLine4()+" ");
					if(gstnAddress.getCity() !=null)
						gstnAddressStrBu.append(gstnAddress.getCity()+" ");
					if(gstnAddress.getState() !=null)
						gstnAddressStrBu.append(gstnAddress.getState()+" ");
					if(gstnAddress.getCountry() !=null)
						gstnAddressStrBu.append(gstnAddress.getCountry()+" ");
					if(gstnAddress.getZipCode() !=null)
						gstnAddressStrBu.append(gstnAddress.getZipCode()+" ");
								
			commercialVettingBean.setGstnNoAndAddress(gstnAddressStrBu.toString());
			commercialVettingBean.setGstnAddressNumber(gstnAddress.getAddressSeq());
		}	
		
		return commercialVettingBean;
	}

	public CommercialVettingBean constructCommercialVetting(Integer serviceId, Integer scOrderId) {

		BillingProfileApiBean billingProfileApiBean = getBillingSingleDetail(serviceId);
		OrganisationBean organisationBean = getOrganisation(serviceId);
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId, "LM","A");
		CommercialVettingBean commercialVettingBean = new CommercialVettingBean();
		commercialVettingBean = getCommercialVettingDetails(billingProfileApiBean, organisationBean,scComponentAttributesmap);
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		commercialVettingBean.setAccessType(scServiceDetail.getAccessType());
		commercialVettingBean.setOriginCountry(scServiceDetail.getSourceCountry());
		commercialVettingBean.setDestinationCountry(scServiceDetail.getDestinationCountry());
		commercialVettingBean.setSecsId(scServiceDetail.getCustOrgNo());
		commercialVettingBean.setSupplierContractingEntity(scServiceDetail.getScOrder().getErfCustSpLeName());
		commercialVettingBean.setOriginCity(scComponentAttributesmap.getOrDefault("cityName", ""));
		commercialVettingBean.setFixed(scComponentAttributesmap.getOrDefault("isratePerMinutefixed", ""));
		commercialVettingBean.setPayphone(scComponentAttributesmap.getOrDefault("isratePerMinutespecial", ""));
		commercialVettingBean.setMobile(scComponentAttributesmap.getOrDefault("isratePerMinutemobile", ""));
		commercialVettingBean.setCmsId(scComponentAttributesmap.getOrDefault("cmsId", ""));
		ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrderId);
		commercialVettingBean.setCustomerLegalEntity(scContractInfo.getErfCustLeName());
		Map<String, String> scOrderAttributesMap = commonFulfillmentUtils.getScOrderAttributes(scServiceDetail.getScOrder().getId());
		commercialVettingBean.setCustomerLeAddress(scOrderAttributesMap.get("Notice_Address"));
		return commercialVettingBean;
	}

	private Map<String, String> getHeaders(String authorization) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", authorization);
		headers.put("Host", ""); // as API expect this header, added with empty value
		headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.put("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}
}