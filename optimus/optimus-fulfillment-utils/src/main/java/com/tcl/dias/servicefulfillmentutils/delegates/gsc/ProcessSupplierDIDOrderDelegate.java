/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.repository.GscFlowGroupRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscNumberStatus;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;

/**
 * @author vivek
 *
 */

@Component("processSupplierDIDOrderDelegate")
public class ProcessSupplierDIDOrderDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(ProcessSupplierDIDOrderDelegate.class);

	@Autowired
	GscService gscService;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	ScAssetAttributeRepository scAssetAttributeRepository;

	@Autowired
	ScAssetRepository scAssetRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	GscFlowGroupRepository gscFlowGroupRepository;

	@Override
	public void execute(DelegateExecution execution) {
		Integer serviceId = null;
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			execution.setVariable(GscConstants.DID_PORT_CHANGE, false);
			execution.setVariable(GscConstants.DID_NEW_ORDER, false);
			String userName = Utils.getSource();

			logger.info("Inside ProcessSupplierDIDOrderDelegate order delegate variables {}", executionVariables);
			serviceId = (Integer) executionVariables.get(SERVICE_ID);
			Integer sipServiceId=executionVariables.get("sipServiceId")!=null?(Integer)executionVariables.get("sipServiceId"):null;
			List<String> attributes = new ArrayList<String>();
			attributes.add("listOfNumbersToBePorted");
			attributes.add("quantityOfNumbers");
			attributes.add("portingServiceNeeded");

			

			Map<String, String> scComponentAttributes = commonFulfillmentUtils.getComponentAttributes(serviceId,
					AttributeConstants.COMPONENT_LM, "A", attributes);
			
			String listOfNumbersToBePorted = scComponentAttributes.getOrDefault("listOfNumbersToBePorted", null);
			
			listOfNumbersToBePorted=StringUtils.trimToNull(listOfNumbersToBePorted);
			
			if (sipServiceId != null) {
				Map<String, String> atMap = new HashMap<String, String>();
				atMap.put("sipServiceId", sipServiceId.toString());
				componentAndAttributeService.updateAttributes(serviceId, atMap, AttributeConstants.COMPONENT_GSCLICENSE,
						"A");
			}

			if (listOfNumbersToBePorted != null && Integer.valueOf(listOfNumbersToBePorted) > 0) {
				logger.info("port changes yes for service {}", serviceId);

				execution.setVariable(GscConstants.DID_PORT_CHANGE, true);

			}
			String quantityOfNumbers = scComponentAttributes.getOrDefault("quantityOfNumbers", null);
			Integer newDiDNumber = null;
			if (quantityOfNumbers != null && listOfNumbersToBePorted != null
					&& Integer.valueOf(listOfNumbersToBePorted) >= 0) {
				newDiDNumber = Integer
						.valueOf(Integer.valueOf(quantityOfNumbers) - Integer.valueOf(listOfNumbersToBePorted));
			}
			else if(listOfNumbersToBePorted==null && quantityOfNumbers!=null) {
				newDiDNumber=Integer.valueOf(quantityOfNumbers);
			}
			if (newDiDNumber != null && newDiDNumber > 0) {
				logger.info("DID new order available changes yes for service {}", serviceId);

				execution.setVariable(GscConstants.DID_NEW_ORDER, true);

			}

			List<ScAsset> scAssetInfo = scAssetRepository.findByScServiceDetail_id(serviceId);
			List<Integer> didNewNumberScAsset = new ArrayList<>();

			if (!scAssetInfo.isEmpty()) {
				List<Integer> portScAssets = new ArrayList<>();

				for (ScAsset sc : scAssetInfo) {
					for (ScAssetAttribute attribute : sc.getScAssetAttributes()) {
						if ("isPortNumber".equalsIgnoreCase(attribute.getAttributeName())
								&& "Yes".equalsIgnoreCase(attribute.getAttributeValue())) {
							gscService.updatedScAssetStatus(sc.getId(),
									GscNumberStatus.PLACE_SUPPLIER_DID_PORTING_ORDER, "system");
							portScAssets.add(sc.getId());
							break;
						}
					}
				}
				if (!portScAssets.isEmpty()) {
					GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup(
							"ProcessSupplierOrderDID-" + "PORT-CHANGE", execution.getProcessInstanceId(),
							"ProcessInstanceId", "system", portScAssets);
					gscFlowGroupRepository.save(createGscFlowGroup);
					execution.setVariable(GscConstants.KEY_GSC_FLOW_GROUP_ID_PORT, createGscFlowGroup.getId());
				}
			

			}
			
			if (newDiDNumber != null && Integer.valueOf(newDiDNumber) > 0) {
				for (int i = 0; i < newDiDNumber; i++) {
					ScAsset didNewAsset = gscService.createScAssetWithStatus(serviceId, null, "Toll-Free", userName,
							GscNumberStatus.PLACE_SUPPLIER_DID_NEW_NUMBER);
					scAssetRepository.save(didNewAsset);
					didNewNumberScAsset.add(didNewAsset.getId());
				}
			}
			if (!didNewNumberScAsset.isEmpty()) {
				GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup("ProcessSupplierOrderDID-" + "NEW-NUM",
						execution.getProcessInstanceId(), "ProcessInstanceId", "system", didNewNumberScAsset);
				gscFlowGroupRepository.save(createGscFlowGroup);

				execution.setVariable(GscConstants.KEY_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());
			}

		} catch (Exception e) {
			logger.error("Exception in ProcessSupplierDIDOrderDelegate :{} and service id:{}", e, serviceId);
		}
	}
}
