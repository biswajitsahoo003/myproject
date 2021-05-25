package com.tcl.dias.snow.response.beans;

/**
 * @author KRUTSRIN
 * 
 * A subclass to hold groupFields from SNOW response
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
public class Groupby_fields
{
    private String display_value;

    private String field;

    private String value;

    public String getDisplay_value ()
    {
        return display_value;
    }

    public void setDisplay_value (String display_value)
    {
        this.display_value = display_value;
    }

    public String getField ()
    {
        return field;
    }

    public void setField (String field)
    {
        this.field = field;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

}
	
