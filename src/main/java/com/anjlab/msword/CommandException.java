package com.anjlab.msword;

import org.json.JSONException;

public class CommandException extends Exception
{

    public CommandException(String message, JSONException e)
    {
        super(message, e);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
