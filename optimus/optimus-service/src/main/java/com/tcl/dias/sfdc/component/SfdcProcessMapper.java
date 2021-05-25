package com.tcl.dias.sfdc.component;

import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.SfdcProductBean;
import com.tcl.dias.sfdc.bean.SfdcProductServiceBean;
import com.tcl.dias.sfdc.bean.SfdcProductServices;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * This file contains the SfdcProcessMapper.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class SfdcProcessMapper implements ISfdcMapper {

	/**
	 * transfortToSfdcRequest
	 * 
	 * @param omsInput
	 * @return
	 */
	@Override
	public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {
		SfdcProductServiceBean sfdcProcessRequest = null;

		ProductServiceBean productServiceBean = (ProductServiceBean) Utils.convertJsonToObject(omsInput,
				ProductServiceBean.class);
		sfdcProcessRequest = new SfdcProductServiceBean();
		sfdcProcessRequest.setQuoteLeId(productServiceBean.getQuoteToLeId());
		SfdcProductBean productBean = new SfdcProductBean();
		productBean.setIsCancel(productServiceBean.getIsCancel());
		productBean.setL2FeasibilityCommercialManagerName(productServiceBean.getL2feasibilityCommercialManagerName());
		productBean.setOpportunityId(productServiceBean.getOpportunityId());
		SfdcProductServices productsservices = new SfdcProductServices();
		productsservices.setCurrencyIsoCode(productServiceBean.getCurrencyIsoCode());
		productsservices.setDoYouNeedTrainingForThisProductC(productServiceBean.getIsTrainingRequire());
		productsservices.setIDCBandwidthC(productServiceBean.getIdcBandwidth());
		productsservices.setIDCFloorC(productServiceBean.getIdcFloor());
		productsservices.setIDCLocationC(productServiceBean.getIdcLocation());
		productsservices.setMultiVRFSolutionC(productServiceBean.getMultiVRFSolution());
		productsservices.setOrderTypeC(productServiceBean.getOrderType());
		productsservices.setTypeC(productServiceBean.getType());
		productsservices.setProductTypeC(productServiceBean.getProductType());
		productsservices.setProductLineOfBusinessC(productServiceBean.getProductLineOfBusiness());
		//gvpn specific data
		productsservices.setOfPortsC(productServiceBean.getOfPortsC());
		//gvpn specific data
		productsservices.setTermMonthsC(productServiceBean.getTermInMonthsC());
		//gvpn specific data
		productsservices.setCloudEnablementC(productServiceBean.getCloudEnablementC());
		productsservices.setProductFlavourC(productServiceBean.getProductFlavourC());
		productsservices.setMssTypeC(productServiceBean.getMssTypeC());
		//for npl
		productsservices.setQuantityC(productServiceBean.getQuantityC());
		//for npl BM Transition
		productsservices.setSubTypeC(productServiceBean.getSubType());
		productsservices.setBandwidthCircuitSpeedC(productServiceBean.getBandwidthCircuitSpeed());
		productsservices.setMediaTypeC(productServiceBean.getMediaType());
		productsservices.setOfFiber(productServiceBean.getOfFiberC());
		productsservices.setTypeOfFiberEntry(productServiceBean.getTypeOfFiberEntry());

		productsservices.setInterfaceC(productServiceBean.getInterfaceC());
		productsservices.setBandwidthCircuitSpeedC(productServiceBean.getBandwidthCircuitSpeed());
		// for npl update
		productsservices.setaEndCountryC(productServiceBean.getaEndCountryC());
		productsservices.setbEndCountryC(productServiceBean.getbEndCountryC());
		productsservices.setaEndCityC(productServiceBean.getaEndCityC());
		productsservices.setbEndCityC(productServiceBean.getbEndCityC());
		
		//for gsc update
		productsservices.setOpportunityNameC(productServiceBean.getOpportunityNameC());
		productsservices.setDataOrMobileC(productServiceBean.getDataOrMobileC());
		productsservices.setpOCAttachedC(productServiceBean.getpOCAttachedC());
		productsservices.setInterconnectTypeC(productServiceBean.getInterconnectTypeC());
		productsservices.setEnabledForUnifiedAccessC(productServiceBean.getEnabledForUnifiedAccessC());
		productsservices.setProductCategoryC(productServiceBean.getProductCategoryC());
		productsservices.setCallTypeC(productServiceBean.getCallTypeC());
		productsservices.setPrimaryFeaturesC(productServiceBean.getPrimaryFeaturesC());
		productsservices.setLnsC(productServiceBean.getLnsC());
		productsservices.setItfsC(productServiceBean.getItfsC());
		productsservices.setUifnC(productServiceBean.getUifnC());
		productsservices.setAudioConferencingAccessNoServiceC(productServiceBean.getAudioConferencingAccessNoServiceC());
		productsservices.setAudioConferencingDTFServiceC(productServiceBean.getAudioConferencingDTFServiceC());
		
		//for nde create
		productsservices.setCctIdC(productServiceBean.getHubId());
		productsservices.setProductSubTypeC(productServiceBean.getProductSubType());

		// For teamsdr
		if(Objects.nonNull(productServiceBean.getProductNRC())){
			productsservices.setProductNRCC(String.valueOf(productServiceBean.getProductNRC()));
		}

		// For teamsdr
		if(Objects.nonNull(productServiceBean.getProductMRC())){
			productsservices.setProductMRCC(String.valueOf(productServiceBean.getProductMRC()));
		}
		
		productBean.setProductsservices(productsservices);
		productBean.setRecordTypeName(productServiceBean.getRecordTypeName());
		
		sfdcProcessRequest.setCreateRequestV1(productBean);
		sfdcProcessRequest.setProductSolutionCode(productServiceBean.getProductSolutionCode());
		
		//for nde create product

		// For teamsdr
		if(Objects.nonNull(productServiceBean.getParentQuoteToLeId())){
			sfdcProcessRequest.setParentQuoteToLeId(productServiceBean.getParentQuoteToLeId());
		}

		return sfdcProcessRequest;
	}

}
