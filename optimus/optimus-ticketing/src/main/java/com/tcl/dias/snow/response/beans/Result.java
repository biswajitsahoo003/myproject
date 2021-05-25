package com.tcl.dias.snow.response.beans;

/**
 * @author KRUTSRIN
 * 
 * A subclass to hold result from SNOW response
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
public class Result
{
    private Stats stats;

    private Groupby_fields[] groupby_fields;

    public Stats getStats ()
    {
        return stats;
    }

    public void setStats (Stats stats)
    {
        this.stats = stats;
    }

    public Groupby_fields[] getGroupby_fields ()
    {
        return groupby_fields;
    }

    public void setGroupby_fields (Groupby_fields[] groupby_fields)
    {
        this.groupby_fields = groupby_fields;
    }

   
}
	