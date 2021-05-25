package com.tcl.dias.oms.gsc.util;

import com.tcl.dias.oms.gsc.util.annotations.ComponentAttributeValue;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for GscComponentAttributeValuesHelper class
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscComponentAttributeValuesHelperTest {

    GscComponentAttributeValuesHelper attributeValuesHelper = new GscComponentAttributeValuesHelper();

    @Test
    public void populateComponentAttributeValues() {
        TestBean testBean = new TestBean();
        Map<String, String> attributeValueMap = new HashMap<>();
        attributeValueMap.put("Attribute 1", "Field value 1");
        attributeValueMap.put("Attribute 2", "Field value 2");
        attributeValueMap.put("Attribute 3", "Field value 3");
        attributeValuesHelper.populateComponentAttributeValues(testBean, () -> attributeValueMap);
        assertEquals("Field value 2", testBean.getField2());
        assertEquals("Field value 3", testBean.getField3());
        assertNull(testBean.getField1());
    }

    @Test
    public void handleComponentAttributeValues() {
        TestBean testBean = new TestBean();
        testBean.setField1("Field value 1");
        testBean.setField2("Field value 2");
        testBean.setField3("Field value 3");
        Map<String, String> attributeValueMap = new HashMap<>();
        attributeValuesHelper.handleComponentAttributeValues(testBean, attributeValueMap::putAll);
        assertEquals(2, attributeValueMap.size());
        assertNull(attributeValueMap.get("Attribute 1"));
        assertEquals("Field value 2", attributeValueMap.get("Attribute 2"));
        assertEquals("Field value 3", attributeValueMap.get("Attribute 3"));
    }

    public static class TestBean {
        private String field1;
        @ComponentAttributeValue(attributeName = "Attribute 2")
        private String field2;
        @ComponentAttributeValue(attributeName = "Attribute 3")
        private String field3;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }

        public String getField3() {
            return field3;
        }

        public void setField3(String field3) {
            this.field3 = field3;
        }
    }
}
