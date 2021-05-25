package com.tcl.dias.networkaugmentation.beans;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.ArrayList;

public class NwaMstMasterDataBean implements Serializable {

    private String attribute_key;

    private ArrayList<String> attribute_option_value=new ArrayList<>();

    public String getAttribute_key() {
        return attribute_key;
    }

    public void setAttribute_key(String attribute_key) {
        this.attribute_key = attribute_key;
    }

    public ArrayList<String> getAttribute_option_value() {
        return attribute_option_value;
    }

    public void setAttribute_option_value(ArrayList<String> attribute_option_value) {
        this.attribute_option_value = attribute_option_value;
    }
}
