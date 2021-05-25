package com.tcl.dias.products.dto;

import java.util.Objects;

public class CpeBandwidthBean {
        String cpeName;
        Integer maxBandwidth;

        public String getCpeName() {
            return cpeName;
        }

        public void setCpeName(String cpeName) {
            this.cpeName = cpeName;
        }

        public Integer getMaxBandwidth() {
            return maxBandwidth;
        }

        public void setMaxBandwidth(Integer maxBandwidth) {
            this.maxBandwidth = maxBandwidth;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CpeBandwidthBean that = (CpeBandwidthBean) o;
            return Objects.equals(cpeName, that.cpeName) &&
                    Objects.equals(maxBandwidth, that.maxBandwidth);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cpeName, maxBandwidth);
        }
    }
