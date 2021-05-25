package com.tcl.dias.sfdc.component;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.CreateFeasibilityRequest;
import com.tcl.dias.sfdc.bean.FRequest;
import com.tcl.dias.sfdc.bean.SfdcFeasibilityRequestBean;
import com.tcl.dias.sfdc.bean.SfdcFeasibilityUpdateRequestBean;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the SfdcFeasibilityMapper.java class.
 * 
 *
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class SfdcFeasibilityMapper implements ISfdcMapper {

	/**
	 * transfortToSfdcRequest
	 * @param omsInput
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {
		SfdcFeasibilityRequestBean sfdcRequestBean = null;
		SfdcFeasibilityUpdateRequestBean sfdcUpdateReqBean;
		FeasibilityRequestBean requestBean = (FeasibilityRequestBean)Utils.convertJsonToObject(omsInput, FeasibilityRequestBean.class);
		if(requestBean != null) {
			if(requestBean.getCreateOrUpdate().equalsIgnoreCase("update")) {
				sfdcUpdateReqBean = new SfdcFeasibilityUpdateRequestBean();
				sfdcUpdateReqBean.setUpdateRequestV1(new ArrayList<CreateFeasibilityRequest>());
				CreateFeasibilityRequest updateFeasibilityRequest = new CreateFeasibilityRequest();
				FRequest freq = new FRequest();
				freq.setSiteContactNameAEndC(requestBean.getSiteContactNameAEndC());
				freq.setSiteLocalContactNumberAEndC(requestBean.getSiteLocalContactNumberAEndC());
				freq.setId(requestBean.getId());
				freq.setPortCircuitCapacityC(requestBean.getPortCircuitCapacityC());
				freq.setInterfaceC(requestBean.getInterfaceC());
				freq.setIllLocalLoopCapacityC(requestBean.getIllLocalLoopCapacityC());
				freq.setIllLocalLoopCapacityUnitC(requestBean.getIllLocalLoopCapacityUnitC());
				updateFeasibilityRequest.setFReq(freq);
				sfdcUpdateReqBean.getUpdateRequestV1().add(updateFeasibilityRequest);
				return sfdcUpdateReqBean;
			} else {
				sfdcRequestBean = new SfdcFeasibilityRequestBean();
				sfdcRequestBean.setCreateRequestV1(new ArrayList<CreateFeasibilityRequest>());
				CreateFeasibilityRequest createFeasibilityRequest = new CreateFeasibilityRequest();
				createFeasibilityRequest.setProductsServices(requestBean.getProductsServices());
				createFeasibilityRequest.setRecordTypeName(requestBean.getRecordTypeName());
				FRequest freq = new FRequest();
				freq.setAddressAEndC(requestBean.getAddressAEndC());
				freq.setAddressLine1AEndC(requestBean.getAddressLine1AEndC());
				freq.setAddressLine2AEndC(requestBean.getAddressLine2AEndC());
				freq.setAvailableTelecomPRIProviderAEndC(requestBean.getAvailableTelecomPRIProviderAEndC());
				freq.setCityAEndC(requestBean.getCityAEndC());
				freq.setStateAEndC(requestBean.getStateAEndC());
				freq.setCountryAEndC(requestBean.getCountryAEndC());
				freq.setContinentAEndC(requestBean.getContinentAEndC());
				freq.setOtherPOPAEndC(requestBean.getOtherPOPAEndC());
				freq.setPinZipAEndC(requestBean.getPinZipAEndC());
				freq.setPortCircuitCapacityC(requestBean.getPortCircuitCapacityC());
				freq.setRequestTypeC(requestBean.getRequestTypeC());
				freq.setStatus(requestBean.getStatus());
				freq.setSpecialRequirementsC(requestBean.getSpecialRequirementsC());
				freq.setInterfaceC(requestBean.getInterfaceC());
				freq.setIllLocalLoopCapacityC(requestBean.getIllLocalLoopCapacityC());
				freq.setIllLocalLoopCapacityUnitC(requestBean.getIllLocalLoopCapacityUnitC());
				freq.setSiteContactNameAEndC(requestBean.getSiteContactNameAEndC());
				//ADDED for multi vrf gvpn
				if(requestBean.getProductName().equalsIgnoreCase("GVPN")){
					freq.setMasterVRFC(requestBean.getIsMultiVrf());
					freq.setNoOfVRFsC(requestBean.getNoOfVrf());
				}
				if(requestBean.getProductName().equalsIgnoreCase("IAS") || requestBean.getProductName().equalsIgnoreCase("GVPN")){
					freq.setSalesRemarks(requestBean.getSalesRemarks());
				}
				freq.setSiteLocalContactNumberAEndC(requestBean.getSiteLocalContactNumberAEndC());
				if (requestBean.getProductName().equalsIgnoreCase("NPL") || requestBean.getProductName().equalsIgnoreCase("NDE")) {
					freq.setAddressBEndC(requestBean.getAddressBEndC());
					freq.setAddressLine1BEndC(requestBean.getAddressLine1BEndC());
					freq.setAddressLine2BEndC(requestBean.getAddressLine2BEndC());
					freq.setAvailableTelecomPRIProviderBEndC(requestBean.getAvailableTelecomPRIProviderBEndC());
					freq.setCityBEndC(requestBean.getCityBEndC());
					freq.setStateBEndC(requestBean.getStateBEndC());
					freq.setCountryBEndC(requestBean.getCountryBEndC());
					freq.setContinentBEndC(requestBean.getContinentBEndC());
					freq.setOtherPOPBEndC(requestBean.getOtherPOPBEndC());
					freq.setPinZipBEndC(requestBean.getPinZipBEndC());
					if(requestBean.getProductName().equalsIgnoreCase("NDE")) {
						freq.setTypeOfTaskC(requestBean.getTypeOfTaskC());
						freq.setInterfaceAEndC(requestBean.getInterfaceAEndC());
						freq.setInterfaceBEndC(requestBean.getInterfaceBEndC());
						createFeasibilityRequest.setCityAEnd(requestBean.getCityAEnd());
						createFeasibilityRequest.setCityBEnd(requestBean.getCityBEnd());
					}
				}
				createFeasibilityRequest.setFReq(freq);
				sfdcRequestBean.getCreateRequestV1().add(createFeasibilityRequest);
			}
		}
		return sfdcRequestBean;
	}

}
