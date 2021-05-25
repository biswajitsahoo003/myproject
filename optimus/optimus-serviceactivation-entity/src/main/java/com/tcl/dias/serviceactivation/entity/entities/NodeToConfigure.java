package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the node_to_configure database table.
 * 
 */
@Entity
@Table(name="node_to_configure")
@NamedQuery(name="NodeToConfigure.findAll", query="SELECT n FROM NodeToConfigure n")
public class NodeToConfigure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="is_ace_action_required")
	private String isAceActionRequired;

	@Column(name="is_ciena_action_required")
	private String isCienaActionRequired;

	@Column(name="is_noc_action_required")
	private String isNocActionRequired;

	@Column(name="node_alias_1")
	private String nodeAlias1;

	@Column(name="node_alias_2")
	private String nodeAlias2;

	@Column(name="node_def")
	private String nodeDef;

	@Column(name="node_def_id")
	private String nodeDefId;

	@Column(name="node_name")
	private String nodeName;

	@Column(name="node_type")
	private String nodeType;

	@Column(name="node_type_id")
	private String nodeTypeId;

	@Column(name = "configured_date")
	private String configuredDate;

	//bi-directional many-to-one association to TxConfiguration
	@ManyToOne
	@JoinColumn(name="tx_configuration_id")
	private TxConfiguration txConfiguration;

	public NodeToConfigure() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsAceActionRequired() {
		return this.isAceActionRequired;
	}

	public void setIsAceActionRequired(String isAceActionRequired) {
		this.isAceActionRequired = isAceActionRequired;
	}

	public String getIsCienaActionRequired() {
		return this.isCienaActionRequired;
	}

	public void setIsCienaActionRequired(String isCienaActionRequired) {
		this.isCienaActionRequired = isCienaActionRequired;
	}

	public String getIsNocActionRequired() {
		return this.isNocActionRequired;
	}

	public void setIsNocActionRequired(String isNocActionRequired) {
		this.isNocActionRequired = isNocActionRequired;
	}

	public String getNodeAlias1() {
		return this.nodeAlias1;
	}

	public void setNodeAlias1(String nodeAlias1) {
		this.nodeAlias1 = nodeAlias1;
	}

	public String getNodeAlias2() {
		return this.nodeAlias2;
	}

	public void setNodeAlias2(String nodeAlias2) {
		this.nodeAlias2 = nodeAlias2;
	}

	public String getNodeDef() {
		return this.nodeDef;
	}

	public void setNodeDef(String nodeDef) {
		this.nodeDef = nodeDef;
	}

	public String getNodeDefId() {
		return this.nodeDefId;
	}

	public void setNodeDefId(String nodeDefId) {
		this.nodeDefId = nodeDefId;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getNodeTypeId() {
		return this.nodeTypeId;
	}

	public void setNodeTypeId(String nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
	}

	public TxConfiguration getTxConfiguration() {
		return this.txConfiguration;
	}

	public void setTxConfiguration(TxConfiguration txConfiguration) {
		this.txConfiguration = txConfiguration;
	}

	public String getConfiguredDate() {
		return configuredDate;
	}

	public void setConfiguredDate(String configuredDate) {
		this.configuredDate = configuredDate;
	}
}