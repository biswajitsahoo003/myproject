package com.tcl.dias.oms.izopc.pdf.beans;

import java.util.List;


/**
 * 
 * Bean class holding solution information of IZOPC
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzoPcSolution {
	
	private String serviceId;
	private String cloudProvider;
	

	/*private String serviceVariant;
	private String lastMile;
	private String resilency;
	private String cpe;*/
	private String portBandwidth;
	private String solutionImage;
	private String solutionName;
	private String topology;
	private List<IzoPcSolutionSiteDetail> siteDetails;
	/*public String getServiceVariant() {
		return serviceVariant;
	}
	public void setServiceVariant(String serviceVariant) {
		this.serviceVariant = serviceVariant;
	}
	public String getLastMile() {
		return lastMile;
	}
	public void setLastMile(String lastMile) {
		this.lastMile = lastMile;
	}
	public String getResilency() {
		return resilency;
	}
	public void setResilency(String resilency) {
		this.resilency = resilency;
	}
	public String getCpe() {
		return cpe;
	}
	public void setCpe(String cpe) {
		this.cpe = cpe;
	}*/
	public String getPortBandwidth() {
		return portBandwidth;
	}
	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}
	public String getSolutionImage() {
		return solutionImage;
	}
	public void setSolutionImage(String solutionImage) {
		this.solutionImage = solutionImage;
	}
	public String getSolutionName() {
		return solutionName;
	}
	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}
	public List<IzoPcSolutionSiteDetail> getSiteDetails() {
		return siteDetails;
	}
	public void setSiteDetails(List<IzoPcSolutionSiteDetail> siteDetails) {
		this.siteDetails = siteDetails;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getCloudProvider() {
		return cloudProvider;
	}
	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}
	public String getTopology() {
		return topology;
	}
	public void setTopology(String topology) {
		this.topology = topology;
	}

	
}
