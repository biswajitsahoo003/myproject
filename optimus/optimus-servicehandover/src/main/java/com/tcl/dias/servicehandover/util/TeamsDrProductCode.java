package com.tcl.dias.servicehandover.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author yomagesh
 *
 */
public enum TeamsDrProductCode {

	MANAGED_USAGE("Managed Plan Usage Fee", "MSDRU02"), 
	MANAGED_OVERAGE("Managed Plan Overage Fee", "MSDRU10"),
	CONNECT_USAGE("Connect Plan Usage Fee", "MSDRU01"), 
	CONNECT_OVERAGE("Connect Plan Overage Fee", "MSDRU09"),
	CUSTOM_GSC_USAGE("Custom Plan GSC Usage Fee", "MSDRU03"),
	CUSTOM_GSC_OVERAGE("Custom Plan GSC Overage Fee", "MSDRU11"),
	CUSTOM_TENANT_USAGE("Custom Tenant Mgmt Usage Services Fee", "MSDRU04"),
	CUSTOM_TENANT_OVERAGE("Custom Plan Tenant Mgmt Overage Fee", "MSDRU12"),
	CUSTOM_TRAINING_USAGE("Custom Training Usage Services Fee", "MSDRU05"),
	CUSTOM_TRAINING_OVERAGE("Custom Plan Training Overage Fee", "MSDRU13"),
	CUSTOM_ENABLEMENT_USAGE("Custom Plan Enablement Usage Fee", "MSDRU06"),
	CUSTOM_ENABLEMENT_OVERAGE("Custom Plan Enablement Overage Fee", "MSDRU14"),
	CUSTOM_MONITORING_USAGE("Custom Plan Monitoring Mgmt Usage Fee", "MSDRU07"),
	CUSTOM_MONITORING_OVERAGE("Custom Plan Monitoring Mgmt Overage Fee", "MSDRU15"),
	CUSTOM_MANAGED_USAGE("Custom Plan Managed Support Usage Fee", "MSDRU08"),
	CUSTOM_MANAGED_OVERAGE("Custom Plan Managed Support Overage Fee", "MSDRU16"),
	REMOTE_SIMPLE_SER("Remote Simple Ser Request Overage Chgs", "MSDRSR01"),
	EXPDEITED_SIMPLE_SER("Expedited Simple Service Request Charges", "MSDRSR02"),
	PROFESSIONAL_T1("Professional Services T1 Technician", "MSDRSR03"),
	PROFESSIONAL_T2("Professional Services T2 Technician", "MSDRSR04"),
	PROFESSIONAL_T3("Professional Services T3 Engineer", "MSDRSR05"),
	PROFESSIONAL_DESIGN("Professional Services Design Engineer", "MSDRSR06"),
	PROFESSIONAL_PROJECT_MGMT("Professional Services Project Management", "MSDRSR07");
	
	String productName;
	String productCode;
	
	private static Map<String, String> productCodeMapping = new HashMap<>();

	static {
		for (TeamsDrProductCode productName : TeamsDrProductCode.values()) {
			productCodeMapping.put(productName.getProductName(), productName.getProductCode());
		}
	}

	private TeamsDrProductCode(String productName, String productCode) {
		this.productName = productName;
		this.productCode = productCode;
	}

	public static final String getCodebyProductName(String productName) {
		return productCodeMapping.get(productName);
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

}
