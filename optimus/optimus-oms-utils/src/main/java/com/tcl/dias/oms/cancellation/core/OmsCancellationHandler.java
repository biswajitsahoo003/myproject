package com.tcl.dias.oms.cancellation.core;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.MDMServiceDetailBean;
import com.tcl.dias.oms.beans.CancellationBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderCloud;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteCloud;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public interface OmsCancellationHandler {
	
	Set<QuoteIllSite> createQuoteSite(List<OrderIllSite> orderIllSites, ProductSolution productSolution,
			CancellationBean cancellationBean) throws TclCommonException;
	
	List<QuoteProductComponent> createQuoteProductComponent(QuoteIllSite quoteIllSite, CancellationBean cancellationBean, OrderIllSite orderIllSite, MDMServiceDetailBean mdmServideDetailBean) throws TclCommonException;
	
	Set<OrderIllSite> createOrderSite(ProductSolution productSolution, OrderProductSolution oSolution, QuoteDetail detail) throws TclCommonException;
	
	Set<OrderNplLink> createOrderLink(OrderProductSolution orderProductSolution, List<QuoteNplLink> quoteNplLinks, CancellationBean cancellationBean) throws TclCommonException;
	
	List<OrderProductComponent> createOrderProductComponent(OrderIllSite orderIllSite, CancellationBean cancellationBean, List<QuoteProductComponent> quoteProductComponents) throws TclCommonException;
	
	List<QuoteProductComponent> createQuoteProductComponentForLinks(QuoteNplLink quoteNplLink,
			CancellationBean cancellationBean, OrderNplLink orderNplLink, MDMServiceDetailBean serviceDetailBean);

	QuoteNplLink createQuoteLink(OrderNplLink orderNplLink, ProductSolution productSolution,
			CancellationBean cancellationBean, MDMServiceDetailBean serviceDetailBean, QuoteIllSite siteA,
			QuoteIllSite siteB) throws TclCommonException;
	
	List<QuoteCloud> createQuoteCloud(List<OrderCloud> orderClouds, ProductSolution productSolution,
			CancellationBean cancellationBean) throws TclCommonException;
	
	Set<OrderCloud> createOrderCoud(ProductSolution productSolution, OrderProductSolution oSolution, QuoteDetail detail) throws TclCommonException;
	
	
	
	

}
