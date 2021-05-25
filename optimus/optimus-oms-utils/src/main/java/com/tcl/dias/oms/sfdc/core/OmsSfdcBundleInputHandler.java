package com.tcl.dias.oms.sfdc.core;

import com.tcl.dias.common.sfdc.bean.Opportunity;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 * @author vpachava
 *
 */

public interface OmsSfdcBundleInputHandler {

	Opportunity getOpportunityBean(QuoteToLe quoteToLe, Opportunity opportunityBean) throws TclCommonException;

	ProductServiceBean getProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException;

	ProductServiceBean updateProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException;

}
