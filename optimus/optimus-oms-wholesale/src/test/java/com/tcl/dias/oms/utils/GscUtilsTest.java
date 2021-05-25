package com.tcl.dias.oms.utils;

import com.tcl.dias.oms.gsc.util.GscUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * THis class contains all the test cases related to {@link GscUtils}
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscUtilsTest {

    @Test
    public void testValidatePhoneNumberShouldBeSuccessfullForCorrectNumbers() {
        assertTrue(GscUtils.validatePhoneNumber("91 9840480567")
                .isValid());
        assertTrue(GscUtils.validatePhoneNumber("43 517 661062")
                .isValid());
    }

    @Test
    public void testValidatePhoneNumberShouldFailForWrongCountryCodeNumbers() {
        assertEquals("Invalid country code in number 9840480567", GscUtils.validatePhoneNumber("9840480567")
                .getError());
        assertEquals("Invalid country code in number 43517661062", GscUtils.validatePhoneNumber("43517661062")
                .getError());
    }

    @Test
    public void testValidatePhoneNumberShouldFailForCorrectCountryCodeNumbersWithWrongPhoneNumbers() {
        assertEquals("Invalid phone number 517 661062  for region IN", GscUtils.validatePhoneNumber("91 517 661062")
                .getError());
        assertEquals("Invalid phone number 9840480567  for region AT", GscUtils.validatePhoneNumber("43 9840480567")
                .getError());
    }
}
