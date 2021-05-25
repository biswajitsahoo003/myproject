
package com.tcl.dias.serviceactivation.activation.netp.beans.response;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.serviceactivation.activation.netp.beans.response package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.serviceactivation.activation.netp.beans.response
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubmitAck }
     * 
     */
    public SubmitAck createSubmitAck() {
        return new SubmitAck();
    }

    /**
     * Create an instance of {@link ConfigAcknowledgement }
     * 
     */
    public ConfigAcknowledgement createConfigAcknowledgement() {
        return new ConfigAcknowledgement();
    }

    /**
     * Create an instance of {@link SubmitSuccessResponse }
     * 
     */
    public SubmitSuccessResponse createSubmitSuccessResponse() {
        return new SubmitSuccessResponse();
    }

    /**
     * Create an instance of {@link ConfigSuccessResponse }
     * 
     */
    public ConfigSuccessResponse createConfigSuccessResponse() {
        return new ConfigSuccessResponse();
    }

    /**
     * Create an instance of {@link SubmitFailureResponse }
     * 
     */
    public SubmitFailureResponse createSubmitFailureResponse() {
        return new SubmitFailureResponse();
    }

    /**
     * Create an instance of {@link ConfigFailureResponse }
     * 
     */
    public ConfigFailureResponse createConfigFailureResponse() {
        return new ConfigFailureResponse();
    }

    /**
     * Create an instance of {@link ErrorDetails }
     * 
     */
    public ErrorDetails createErrorDetails() {
        return new ErrorDetails();
    }

}
