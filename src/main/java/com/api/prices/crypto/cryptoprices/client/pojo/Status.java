package com.api.prices.crypto.cryptoprices.client.pojo;

import java.sql.Timestamp;

public class Status
{
    private String error_message;

    private String elapsed;

    private String credit_count;

    private String error_code;

    private Timestamp timestamp;

    public String getError_message ()
{
    return error_message;
}

    public void setError_message (String error_message)
    {
        this.error_message = error_message;
    }

    public String getElapsed ()
    {
        return elapsed;
    }

    public void setElapsed (String elapsed)
    {
        this.elapsed = elapsed;
    }

    public String getCredit_count ()
    {
        return credit_count;
    }

    public void setCredit_count (String credit_count)
    {
        this.credit_count = credit_count;
    }

    public String getError_code ()
    {
        return error_code;
    }

    public void setError_code (String error_code)
    {
        this.error_code = error_code;
    }

    public Timestamp getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [error_message = "+error_message+", elapsed = "+elapsed+", credit_count = "+credit_count+", error_code = "+error_code+", timestamp = "+timestamp+"]";
    }
}
