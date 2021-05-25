package com.tcl.dias.products.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.productcatelog.entity.entities.GvpnSlaView;
import com.tcl.dias.productcatelog.entity.entities.IasSLAView;
import com.tcl.dias.productcatelog.entity.entities.IzoPcSlaView;
import com.tcl.dias.productcatelog.entity.entities.NplSlaView;
import com.tcl.dias.productcatelog.entity.entities.ProductSlaCosSpec;
import com.tcl.dias.products.constants.SLAParameters;

/**
 * Data transfer object for SLA details
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class SLADto {

	@NotNull(message = Constants.SLA_FACTOR_EMPTY)
	private String factor;

	@NotNull(message = Constants.SLA_VALUE_EMPTY)
	private String value;

	/*
	 * method to retrieve the SLA factor
	 */
	public String getFactor() {
		return factor;
	}

	/*
	 * method to set the SLA factor
	 */
	public void setFactor(String factor) {
		this.factor = factor;
	}

	/*
	 * method to retrieve the value for the SLA factor
	 */
	public String getValue() {
		return value;
	}

	/*
	 * method to set the value for the SLA factor
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * constructor for the SLADto object based on the IasSLAView entity object
	 */
	public SLADto(IasSLAView slaView, Integer tier) {
		if (slaView != null) {
			// this.setFactor(slaView.getSla());
			this.setValue(getTierBasedValueForIAS(slaView, tier));
			this.setFactor(slaView.getSlaName());
		}

	}

	/*
	 * Method to retrieve value based on tier for IAS
	 */
	private String getTierBasedValueForIAS(IasSLAView iasSlaView, Integer tier) {
		String factorValue = null;
		switch (tier) {

		case 1:
			factorValue = iasSlaView.getTier1Value();
			break;
		case 2:
			factorValue = iasSlaView.getTier2Value();
			break;
		case 3:
			factorValue = iasSlaView.getTier3Value();
			break;
		default:
			break;
		}
		return factorValue;
	}

	/*
	 * Method to retrieve value based on tier
	 */
	private String getTierBasedValue(GvpnSlaView gvpnSlaView, Integer tier) {
		String factorValue = null;
		switch (tier) {

		case 1:
			factorValue = gvpnSlaView.getTier1Value();
			break;
		case 2:
			factorValue = gvpnSlaView.getTier2Value();
			break;
		case 3:
			factorValue = gvpnSlaView.getTier3Value();
			break;
		case 4:
			factorValue = gvpnSlaView.getTier4Value();
			break;
		default:
			break;
		}
		return factorValue;
	}

	/*
	 * constructor for the SLADto object based on the GvpnSlaView entity object
	 */
	public SLADto(List<GvpnSlaView> gvpnSlaViewList, Integer tier) {
		if (gvpnSlaViewList != null && !gvpnSlaViewList.isEmpty() && tier != null) {
			this.setFactor(gvpnSlaViewList.get(0).getSlaName());
			this.setValue(getTierBasedValue(gvpnSlaViewList.get(0), tier));

		}

	}

	/*
	 * constructor for the SLADto object based on the GvpnSlaView entity object
	 */
	public SLADto(GvpnSlaView gvpnSlaView, Integer tier) {
		if (gvpnSlaView != null && tier != null) {
			this.setFactor(gvpnSlaView.getSlaName());
			this.setValue(getTierBasedValue(gvpnSlaView, tier));
		}
	}

	/*
	 * constructor for the SLADto object
	 */
	public SLADto(@NotNull(message = Constants.SLA_FACTOR_EMPTY) String factor,
			@NotNull(message = Constants.SLA_VALUE_EMPTY) String value) {
		super();
		this.factor = factor;
		this.value = value;
	}

	/*
	 * Constructor for SLADto with ProductSlaCosSpec and cosValue as parameters
	 */
	public SLADto(ProductSlaCosSpec productSlaCosSpec, Integer cosValue) {
		this.setFactor(SLAParameters.PKT_DELIVERY_RATIO_SERV_LEVEL_TGT.getName());
		switch (cosValue) {
		case 1:
			this.setValue(productSlaCosSpec.getCos1Value());
			break;
		case 2:
			this.setValue(productSlaCosSpec.getCos2Value());
			break;
		case 3:
			this.setValue(productSlaCosSpec.getCos3Value());
			break;
		case 4:
			this.setValue(productSlaCosSpec.getCos4Value());
			break;
		case 5:
			this.setValue(productSlaCosSpec.getCos5Value());
			break;
		case 6:
			this.setValue(productSlaCosSpec.getCos6Value());
			break;
		default:
			break;
		}
	}

	/*
	 * @author Thamizhselvi Perumal constructor for the SLADto object based on the
	 * NplSLAView entity object
	 */
	public SLADto(NplSlaView slaView) {
		if (slaView != null) {
			this.setFactor(slaView.getSlaName());
			this.setValue(slaView.getDefaultValue());
		}
	}

	/*
	 * default constructor for the SLADto object
	 */
	public SLADto() {

	}
	
	public SLADto(IzoPcSlaView entity) {
		if (Objects.nonNull(entity)) {
			this.setFactor(entity.getSlaName());
			this.setValue(entity.getDefaultValue());
		}
	}

	@Override
	public String toString() {
		return "SLADto [factor=" + factor + ", value=" + value + "]";
	}

}
