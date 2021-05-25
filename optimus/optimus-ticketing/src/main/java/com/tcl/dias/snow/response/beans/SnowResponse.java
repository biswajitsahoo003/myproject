package com.tcl.dias.snow.response.beans;

/**
 * @author KRUTSRIN
 * 
 * A class to hold response from SNOW
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
public class SnowResponse
{
    private Result[] result;

    public Result[] getResult ()
    {
        return result;
    }

    public void setResult (Result[] result)
    {
        this.result = result;
    }

    
}
