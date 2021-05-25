package com.tcl.dias.networkaugment.entity.entities;


import javax.persistence.*;

@Entity
@Table(name="nwa_mst_master_data")

public class NwaMstMasterData {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "attribute_option_id")
    private Integer attribute_option_id;

    @Column(name = "attribute_key")
    private String attribute_key;

    @Column(name = "attribute_description")
    private String attribute_description;

    @Column(name = "attribute_option_value")
    private String attribute_option_value;

    @Column(name = "attribute_option_key")
    private String attribute_option_key;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getAttribute_option_id() {
        return attribute_option_id;
    }

    public void setAttribute_option_id(Integer attribute_option_id) {
        this.attribute_option_id = attribute_option_id;
    }

    public String getAttribute_key() {
        return attribute_key;
    }

    public void setAttribute_key(String attribute_key) {
        this.attribute_key = attribute_key;
    }

    public String getAttribute_description() {
        return attribute_description;
    }

    public void setAttribute_description(String attribute_description) {
        this.attribute_description = attribute_description;
    }

    public String getAttribute_option_value() {
        return attribute_option_value;
    }

    public void setAttribute_option_value(String attribute_option_value) {
        this.attribute_option_value = attribute_option_value;
    }

    public String getAttribute_option_key() {
        return attribute_option_key;
    }

    public void setAttribute_option_key(String attribute_option_key) {
        this.attribute_option_key = attribute_option_key;
    }


}
