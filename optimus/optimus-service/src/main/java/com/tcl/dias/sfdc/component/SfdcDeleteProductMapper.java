package com.tcl.dias.sfdc.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.bean.BaseBean;
import com.tcl.dias.sfdc.bean.SfdcDeleteProductBean;
import com.tcl.dias.sfdc.bean.SfdcDeleteProductServices;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the SfdcProcessMapper.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class SfdcDeleteProductMapper implements ISfdcMapper {

	/**
	 * transfortToSfdcRequest
	 * 
	 * @param omsInput
	 * @return
	 */
	@Override
	public BaseBean transfortToSfdcRequest(String omsInput) throws TclCommonException {
		SfdcDeleteProductBean sfdcDeleteRequest = null;

		ProductServiceBean productServiceBean = (ProductServiceBean) Utils.convertJsonToObject(omsInput,
				ProductServiceBean.class);
		sfdcDeleteRequest = new SfdcDeleteProductBean();
		sfdcDeleteRequest.setSourceSystem(productServiceBean.getSourceSystem());
		sfdcDeleteRequest.setOpportunityId(productServiceBean.getOpportunityId());
		List<SfdcDeleteProductServices> productsservices = new ArrayList<>();
		sfdcDeleteRequest.setProductservices(productsservices);
		if (productServiceBean.getProductIds() != null && !productServiceBean.getProductIds().isEmpty()) {
			for (String productId : productServiceBean.getProductIds()) {
				SfdcDeleteProductServices productsservice = new SfdcDeleteProductServices();
				productsservice.setProductServiceId(productId);
				productsservices.add(productsservice);
			}

		}

		// For teamsdr
		if(Objects.nonNull(productServiceBean.getParentTpsSfdcOptyId())){
			sfdcDeleteRequest.setParentTpsSfdcOptyId(productServiceBean.getParentTpsSfdcOptyId());
		}
		return sfdcDeleteRequest;
	}

}
