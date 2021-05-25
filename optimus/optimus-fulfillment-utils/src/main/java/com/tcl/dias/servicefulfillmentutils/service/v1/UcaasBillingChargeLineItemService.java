package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.graphbuilder.math.func.LnFunction;
import com.tcl.dias.common.servicefulfillment.beans.CpeBomResource;
import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScWebexServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScWebexServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.beans.LineItemDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.BillingConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;

/**
 * Service Class for Webex Products
 * 
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used for the document related service
 */
@Service
@Transactional(readOnly = true,isolation=Isolation.READ_COMMITTED)
public class UcaasBillingChargeLineItemService {
	
	@Autowired
	ScWebexServiceCommercialRepository scWebexServiceCommercialRepository;
	
	@Autowired
	GenevaIpcOrderEntryRepository genevaIpcOrderEntryRepository;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScServiceAttributeRepository serviceAttributeRepository;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	LineItemDetailsBean lineItembean = null;
	

	public List<LineItemDetailsBean> loadLineItems(Integer serviceId,String billingMethod) {	
		List<ScWebexServiceCommercial> commercials = scWebexServiceCommercialRepository.findByScServiceDetailId(serviceId);
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		List<LineItemDetailsBean> lineItembeanList= new ArrayList<LineItemDetailsBean>();
		commercials.forEach(lineItem->{
			Double nrc = lineItem.getNrc() != null ? lineItem.getNrc() : 0;
			Double mrc = lineItem.getMrc() != null ? lineItem.getMrc() : 0;
			Double arc = lineItem.getArc() != null ? lineItem.getArc() : 0;
			Double unitNrc = lineItem.getUnitNrc() != null ? lineItem.getUnitNrc() : 0;
			String accountNumber = "OPTACC_".concat(scServiceDetail.getId().toString());
			if((Objects.nonNull(lineItem.getComponentType())&& lineItem.getComponentType().equals("License"))&& (arc>0 || mrc >0 || nrc>0)) {
			    lineItembean = new LineItemDetailsBean();
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(BillingConstants.WEBEX_LICENSE);
				lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembean.setBillingMethod(billingMethod);
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setCpeModel(lineItem.getComponentName());
				lineItembean.setQuantity(lineItem.getQuantity() !=null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setUnitOfMeasurement("NA");
				lineItembean.setIsProrated("Yes");
				lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
				lineItembean.setBillingType("Cisco WebEx CCA");
				lineItembeanList.add(lineItembean);
				
			}
			if((Objects.nonNull(lineItem.getComponentType())&& lineItem.getComponentType().equals("Endpoint"))&& (arc>0 || mrc >0 || nrc>0 || unitNrc>0)) {
				
				List<ScComponent> scComponent = scComponentRepository.findByScServiceDetailIdAndComponentName(serviceId,lineItem.getComponentName());
				scComponent.forEach(component -> {
					lineItembean = new LineItemDetailsBean();
					lineItembean.setMrc(String.format("%.2f", mrc));
					lineItembean.setNrc(String.format("%.2f", unitNrc > 0 ? unitNrc : nrc));
					lineItembean.setArc(String.format("%.2f", arc));
					lineItembean.setLineitem(BillingConstants.WEBEX_ENDPOINT);
					lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
					lineItembean.setHsnCode(lineItem.getHsnCode());
					lineItembean.setBillingMethod(billingMethod);
					lineItembean.setAccountNumber(accountNumber.concat("_CPE"));
					lineItembean.setDescription(lineItem.getComponentDesc());
					lineItembean.setCpeModel(lineItem.getComponentName());
					lineItembean.setQuantity(lineItem.getQuantity() !=null ? lineItem.getQuantity().toString() : "1");
					lineItembean.setUnitOfMeasurement("NA");
					lineItembean.setIsProrated("Yes");
					lineItembean.setServiceType("CPE");
					lineItembean.setBillingType("CPE");
					lineItembean.setComponent(component.getId().toString());
					lineItembean.setSupportType(lineItem.getEndpointManagementType());
					lineItembeanList.add(lineItembean);
				});

			}
			if((Objects.nonNull(lineItem.getComponentType())&& lineItem.getComponentType().equals("Subscription"))&& (arc>0 || mrc >0 || nrc>0)) {
				lineItembean = new LineItemDetailsBean();
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(BillingConstants.WEBEX_SUBSCRIPTION);
				lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembean.setAccountNumber(accountNumber);
				lineItembean.setBillingMethod(billingMethod);
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setCpeModel(lineItem.getComponentName());
				lineItembean.setQuantity(lineItem.getQuantity() !=null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setUnitOfMeasurement("NA");
				lineItembean.setIsProrated("Yes");
				lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
				lineItembean.setBillingType("Cisco WebEx CCA");
				lineItembeanList.add(lineItembean);
			}
			
		});
		
		return lineItembeanList;
	}
	

}
