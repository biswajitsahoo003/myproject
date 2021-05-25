/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.custom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author vivek
 * use for restricting user access with custom validation
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestrictUserAccess {

}
