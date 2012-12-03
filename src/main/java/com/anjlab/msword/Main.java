package com.anjlab.msword;

import org.json.JSONException;

public class Main {

    public static void main(String[] args) throws Exception
    {
        try
        {
            MSWordProcessor processor = MSWordProcessor.createFromJSON(System.in);
            
            processor.process();
        }
        catch (JSONException e)
        {
            throw new Exception("https://gist.github.com/005c69458b116311d2a8", e);
        }
    }
    
}
