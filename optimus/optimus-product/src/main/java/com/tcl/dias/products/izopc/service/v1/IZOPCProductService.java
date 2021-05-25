package com.tcl.dias.products.izopc.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.IzopcDcRequestBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.productcatelog.entity.entities.ProductDataCentreAssoc;
import com.tcl.dias.productcatelog.entity.repository.CloudProviderAttributeRepository;
import com.tcl.dias.productcatelog.entity.repository.IzoPcSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductDataCentreAssocRepository;
import com.tcl.dias.productcatelog.entity.repository.ProviderRepository;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.izopc.beans.CloudProviderAttributeBean;
import com.tcl.dias.products.izopc.beans.DataCenterForCloudProvidersBean;
import com.tcl.dias.products.izopc.beans.DataCenterProviderDetails;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This class contains the IZOPCProductService related details.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class IZOPCProductService {
	private static final Logger LOGGER = LoggerFactory.getLogger(IZOPCProductService.class);

	@Autowired
	ProductDataCentreAssocRepository productDataCentreAssocRepository;
	
	@Autowired
	CloudProviderAttributeRepository cloudProviderAttributeRepository;
	
	@Autowired
	IzoPcSlaViewRepository izoPcSlaViewRepository;
	
	@Autowired
	ProviderRepository providerRepository;
	
	
	/**
	 * @author vivek getCloudProviderDetails
	 * @return
	 * @throws TclCommonException
	 */
	public List<String> getCloudProviderDetails() throws TclCommonException {

		return productDataCentreAssocRepository.findDistinctCloudProviderName();

	}


	/**
	 * @author vivek getCloudProviderDetails
	 * @return
	 * @throws TclCommonException
	 */
	/*public List<CloudProviderBean> getCloudProviderDetails() throws TclCommonException {

		return providerRepository.findByIsActive("Y").stream().map(CloudProviderBean::new).collect(Collectors.toList());

	}*/

	/**
	 * @author vivek getCloudProviderDetails
	 * @param providerName
	 * @return
	 * @throws TclCommonException 
	 */
	public List<DataCenterProviderDetails> getDataCenterDetails(String providerName) throws TclCommonException {
		if (Objects.isNull(providerName)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_ERROR);
		}
		List<DataCenterProviderDetails> dataCenterProviderDetails = new ArrayList<>();
		List<ProductDataCentreAssoc> productDataCentreAssocs = productDataCentreAssocRepository
				.findByCloudProviderName(providerName);
		if (!productDataCentreAssocs.isEmpty()) {
			productDataCentreAssocs.forEach(prodAssoc -> {
				DataCenterProviderDetails dataCenterProviderDetail = new DataCenterProviderDetails();
				dataCenterProviderDetail.setDataCenterCity(prodAssoc.getDataCenterCity());
				dataCenterProviderDetail.setDataCenterDec(prodAssoc.getDataCenterDec());
				dataCenterProviderDetail.setDataCenterSiteCode(prodAssoc.getDataCentrSiteCode());
				dataCenterProviderDetail.setDataCentreAddress(prodAssoc.getDataCenterAddress());
				dataCenterProviderDetail.setInterfaceName(prodAssoc.getInterfaceName());
				dataCenterProviderDetail.setRemarks(prodAssoc.getRemarks());
				dataCenterProviderDetail.setDataCenterLatitude(prodAssoc.getDataCenterLatitude());
				dataCenterProviderDetail.setDataCenterLongitude(prodAssoc.getDataCenterLongitude());
				dataCenterProviderDetail.setDataCenterCd(prodAssoc.getDataCenterCd());
				dataCenterProviderDetails.add(dataCenterProviderDetail);

			});

		}
		return dataCenterProviderDetails;
	}
	
	public List<DataCenterForCloudProvidersBean> getDataCenterDetailsForCloudProviderList(
			List<String> cloudProviderList) throws TclCommonException {
		if (cloudProviderList == null || cloudProviderList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<DataCenterForCloudProvidersBean> dataCenterBeanList = new ArrayList<>();
		try {
			cloudProviderList.forEach(cloudProvider -> {
				try {
					dataCenterBeanList.add(
									new DataCenterForCloudProvidersBean(cloudProvider, getDataCenterDetails(cloudProvider)));
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(e);
				}
			});
		} catch (Exception e) {
			throw new TclCommonException(e.getCause(), ResponseResource.R_CODE_ERROR);
		}

		return dataCenterBeanList;
	}
	/**
	 * getCloudProviderAttribute -  method to retrieve attribute details of a cloud provider
	 * @param providerName
	 * @param attributeName
	 * @return
	 * @throws TclCommonException
	 */
	public List<CloudProviderAttributeBean> getCloudProviderAttribute(String providerName , String attributeName) throws TclCommonException{
		if (Objects.isNull(providerName) || Objects.isNull(attributeName)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_ERROR);
		}
			return cloudProviderAttributeRepository.findByCloudProviderNameAndAttributeName(providerName, attributeName).stream()
				.map(CloudProviderAttributeBean::new).collect(Collectors.toList());
		
	}
	
	/**
	 * Method to return data center details based on dc code
	 * @param dcCode
	 * @return
	 * @throws TclCommonException
	 */
	public DataCenterProviderDetails getDataCenter(String dcCode) throws TclCommonException {
		DataCenterProviderDetails dataCenter = new DataCenterProviderDetails();
		if (Objects.isNull(dcCode)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_ERROR);
		}
		List<ProductDataCentreAssoc> dataCenterList = productDataCentreAssocRepository.findByDataCenterCd(dcCode);
		if (dataCenterList.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_ERROR);
		}
		dataCenter.setDataCentreAddress(dataCenterList.get(0).getDataCenterAddress());
		dataCenter.setDataCenterCd(dataCenterList.get(0).getDataCenterCd());
		dataCenter.setDataCenterCity(dataCenterList.get(0).getDataCenterCity());
		dataCenter.setDataCenterLatitude(dataCenterList.get(0).getDataCenterLatitude());
		dataCenter.setDataCenterLongitude(dataCenterList.get(0).getDataCenterLongitude());
		dataCenter.setDataCenterSiteCode(dataCenterList.get(0).getDataCentrSiteCode());
		dataCenter.setDataCenterDec(dataCenterList.get(0).getDataCenterDec());
		return dataCenter;
	}
	
	/**
	 * Method to return SLA details for IZO PC
	 * @return
	 */
	public List<SLADto> getSlaDetails() {
		return izoPcSlaViewRepository.findAll().stream().map(SLADto::new).collect(Collectors.toList());
		
	}


	/**
	 * method to get cloud provider alias 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public Object getCloudProviderAlias(String request) throws TclCommonException {
		if(StringUtils.isEmpty(request))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR);
		String providerAlias = StringUtils.EMPTY;
		String providerName = (String)Utils.convertJsonToObject(request, String.class);	
		if(!StringUtils.isEmpty(providerName)) 
			providerAlias = providerRepository.findByName(providerName).getAliasName();
			
				return providerAlias;
	}
	
	public String getDcInterconnectCodeFromPopIdAndCloudProvider(IzopcDcRequestBean izopcDcRequestBean) {
		try {
			LOGGER.info("Pop location ID is {} and Cloud Provider is {}", izopcDcRequestBean.getPopId(),
					izopcDcRequestBean.getCloudProvider());
			if (izopcDcRequestBean != null && izopcDcRequestBean.getCloudProvider() != null
					&& izopcDcRequestBean.getPopId() != null) {
				ProductDataCentreAssoc productDataCentreAssoc = productDataCentreAssocRepository
						.findByDataCenterCdAndCloudProviderName(izopcDcRequestBean.getPopId(),
								izopcDcRequestBean.getCloudProvider());
				if (productDataCentreAssoc != null && productDataCentreAssoc.getDataCenterDec() != null) {
					return productDataCentreAssoc.getDataCenterDec();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on getting Dc interconnect code");
		}
		return CommonConstants.EMPTY;
	}

}
