/**
 * 
 */
package com.tcl.dias.customer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tcl.dias.customer.consumer.AttachmentListenerTest;
import com.tcl.dias.customer.integration.AttachMentListnerTest;
import com.tcl.dias.customer.integration.CustomerIntegrationTest;

/**
 * @author KusumaK
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
    CustomerIntegrationTest.class,AttachmentListenerTest.class,AttachMentListnerTest.class
})

public class CustomerTestSuite {

}
