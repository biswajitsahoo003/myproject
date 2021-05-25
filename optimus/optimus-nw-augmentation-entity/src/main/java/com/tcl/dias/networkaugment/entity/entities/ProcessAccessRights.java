package com.tcl.dias.networkaugment.entity.entities;

import org.hibernate.cfg.annotations.reflection.XMLContext;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "process_access_right")
public class ProcessAccessRights implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_name")
    String groupName;

    /*@Column(name = "process_id")
    Integer processId;*/

    @Column(name =  "access_right")
    String accessRight;

    @Column(name = "to_org_only")
    String toOrgOnly;

    // bi-directional many-to-one association to ScOrder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    private ProcessNameMaster processNameMaster;

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

    public String getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight;
    }

    public String getToOrgOnly() {
        return toOrgOnly;
    }

    public void setToOrgOnly(String toOrgOnly) {
        this.toOrgOnly = toOrgOnly;
    }

    public ProcessNameMaster getProcessNameMaster() {
        return processNameMaster;
    }

    public void setProcessNameMaster(ProcessNameMaster processNameMaster) {
        this.processNameMaster = processNameMaster;
    }
}
