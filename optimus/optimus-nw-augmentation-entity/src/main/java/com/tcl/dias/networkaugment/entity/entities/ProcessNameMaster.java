package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "process_name_master")
public class ProcessNameMaster implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "process_name")
    String process_name;

    @Column(name = "menu_header_name")
    String menuHeaderName;

    @Column(name = "menu_display_name")
    String menuDisplayName;

    @Column(name = "process_display_name")
    String processDisplayName;

    /*// bi-directional many-to-one association to ScServiceDetail
    @OneToMany(mappedBy = "ProcessNameMaster", cascade = CascadeType.ALL)
    private Set<ProcessAccessRights> processAccessRights;*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }

    public String getMenuHeaderName() {
        return menuHeaderName;
    }

    public void setMenuHeaderName(String menuHeaderName) {
        this.menuHeaderName = menuHeaderName;
    }

    public String getMenuDisplayName() {
        return menuDisplayName;
    }

    public void setMenuDisplayName(String menuDisplayName) {
        this.menuDisplayName = menuDisplayName;
    }

    public String getProcessDisplayName() {
        return processDisplayName;
    }

    public void setProcessDisplayName(String processDisplayName) {
        this.processDisplayName = processDisplayName;
    }

    /*public Set<ProcessAccessRights> getProcessAccessRights() {
        return processAccessRights;
    }

    public void setProcessAccessRights(Set<ProcessAccessRights> processAccessRights) {
        this.processAccessRights = processAccessRights;
    }*/

}
