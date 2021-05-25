package com.tcl.dias.l2oworkflow.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mf_support_group")
@NamedQuery(name = "MfSupportGroup.findAll", query = "SELECT m FROM MfSupportGroup m")
public class MfSupportGroup implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "dependant_team")
    private String dependantTeam;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDependantTeam() {
        return dependantTeam;
    }

    public void setDependantTeam(String dependantTeam) {
        this.dependantTeam = dependantTeam;
    }
}
