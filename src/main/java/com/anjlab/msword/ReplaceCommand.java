package com.anjlab.msword;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReplaceCommand implements ICommand
{
    @Override
    public void run(JSONObject commandJson, HWPFDocument doc) throws CommandException
    {
        try
        {
            Range range = doc.getRange();
            
            JSONArray args = commandJson.getJSONArray("args");
            
            for (int i = 0; i < args.length(); i++)
            {
                JSONArray pair = args.getJSONArray(i);
                
                range.replaceText(pair.getString(0), pair.getString(1));
            }
        }
        catch (JSONException e)
        {
            throw new CommandException("Error executing command", e);
        }
    }
}
