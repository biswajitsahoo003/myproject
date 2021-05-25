package com.tcl.dias.sfdc.component;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.SfdcUpdateProductBean;
import com.tcl.dias.sfdc.bean.SfdcUpdateProductServiceBean;
import com.tcl.dias.sfdc.bean.SfdcUpdateProductServices;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

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
public class SfdcUpdateProcessMapper implements ISfdcMapper {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SfdcUpdateProcessMapper.class);
	/**
	 * transfortToSfdcRequest
	 * 
	 * @param omsInput
	 * @return
	 */
	@Override
	public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {
		SfdcUpdateProductServiceBean sfdcProcessRequest = null;

		ProductServiceBean productServiceBean = (ProductServiceBean) Utils.convertJsonToObject(omsInput,
				ProductServiceBean.class);
		LOGGER.info("productServiceBean Request from oms:{}",productServiceBean);
		LOGGER.info("recordTypeName in input:{}",productServiceBean.getRecordTypeName());
		sfdcProcessRequest = new SfdcUpdateProductServiceBean();
		sfdcProcessRequest.setIsCancel(productServiceBean.getIsCancel());
		sfdcProcessRequest.setProductSolutionCode(productServiceBean.getProductSolutionCode());
		SfdcUpdateProductBean productBean = new SfdcUpdateProductBean();
		productBean.setL2FeasibilityCommercialManagerName(productServiceBean.getL2feasibilityCommercialManagerName());
		productBean.setOpportunityId(productServiceBean.getOpportunityId());
		SfdcUpdateProductServices productsservices = new SfdcUpdateProductServices();
		productsservices.setId(productServiceBean.getProductId());
		productsservices.setBigMachinesARCC(productServiceBean.getBigMachinesArc()!=null?String.valueOf(productServiceBean.getBigMachinesArc()):null);
		productsservices.setBigMachinesMRCC(productServiceBean.getBigMachinesMrc()!=null?String.valueOf(productServiceBean.getBigMachinesMrc()):null);
		productsservices.setBigMachinesNRCC(productServiceBean.getBigMachinesNrc()!=null?String.valueOf(productServiceBean.getBigMachinesNrc()):null);
		productsservices.setBigMachinesTCVC(productServiceBean.getBigMachinesTcv()!=null?String.valueOf(productServiceBean.getBigMachinesTcv()):null);
		productsservices.setPreviousMrc(productServiceBean.getPreviousMrc()!=null?String.valueOf(productServiceBean.getPreviousMrc()):null);
		productsservices.setPreviousNrc(productServiceBean.getPreviousNrc()!=null?String.valueOf(productServiceBean.getPreviousNrc()):null);
		productsservices.setName(productServiceBean.getProductName());
		productsservices.setCurrencyIsoCode(productServiceBean.getCurrencyIsoCode());
		productsservices.setDoYouNeedTrainingForThisProductC(productServiceBean.getIsTrainingRequire());
		productsservices.setIDCBandwidthC(productServiceBean.getIdcBandwidth());
		productsservices.setIDCFloorC(productServiceBean.getIdcFloor());
		productsservices.setIDCLocationC(productServiceBean.getIdcLocation());
		productsservices.setMultiVRFSolutionC(productServiceBean.getMultiVRFSolution());
		productsservices.setOrderTypeC(productServiceBean.getOrderType());
		productsservices.setTypeC(productServiceBean.getType());
		productsservices.setProductTypeC(productServiceBean.getProductType());
		productsservices.setProductNRCC(productServiceBean.getProductNRC()!=null?String.valueOf(productServiceBean.getProductNRC()):null);
		productsservices.setProductMRCC(productServiceBean.getProductMRC()!=null?String.valueOf(productServiceBean.getProductMRC()):null);
		productsservices.setProductLineOfBusinessC(productServiceBean.getProductLineOfBusiness());
		productsservices.setCloudEnablementC(productServiceBean.getCloudEnablementC());
		productsservices.setCloudProviderC(productServiceBean.getCloudProvider());
		
		//gvpn specific data
		productsservices.setOfPortsC(productServiceBean.getOfPortsC());
		//gvpn specific data
		productsservices.setTermMonthsC(productServiceBean.getTermInMonthsC());
		//gvpn specific data
		productsservices.setCloudEnablementC(productServiceBean.getCloudEnablementC());
		//for npl
		productsservices.setQuantityC(productServiceBean.getQuantityC());
		//for npl
		productsservices.setInterfaceC(productServiceBean.getInterfaceC());
		productsservices.setBandwidthCircuitSpeed(productServiceBean.getBandwidthCircuitSpeed());
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
		productsservices.setOrderTypeC(productServiceBean.getOrderType());
		//For Cross Connect related attribute
		productsservices.setMediaType(productServiceBean.getMediaType());
		productsservices.setSubTypeC(productServiceBean.getSubType());
		productsservices.setOfFiber(productServiceBean.getOfFiberC());
		productsservices.setTypeOfFiberEntry(productServiceBean.getTypeOfFiberEntry());
		productBean.setProductsservices(productsservices);
		productBean.setRecordTypeName(productServiceBean.getRecordTypeName());
		sfdcProcessRequest.setUpdateRequestV1(productBean);

		// For teamsdr
		if(Objects.nonNull(productServiceBean.getParentQuoteToLeId())){
			sfdcProcessRequest.setParentQuoteToLeId(productServiceBean.getParentQuoteToLeId());
		}
		LOGGER.info("recordTypeName in sfdc update request:{}",sfdcProcessRequest.getUpdateRequestV1().getRecordTypeName());
		LOGGER.info("sfdcUpdateProcessRequest:{}",sfdcProcessRequest);
		return sfdcProcessRequest;
	}

}
