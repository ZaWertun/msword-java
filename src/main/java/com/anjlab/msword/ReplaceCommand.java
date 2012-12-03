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
                
                replaceText(range, pair.getString(0), pair.getString(1));
            }
        }
        catch (JSONException e)
        {
            throw new CommandException("Error executing command", e);
        }
    }
    
    private void replaceText(Range range, String pPlaceHolder, String pValue) {
        boolean keepLooking = true;
        int offset = -pValue.length();
        while (keepLooking) {

            String text = range.text();
            offset = text.indexOf(pPlaceHolder, offset + pValue.length());
            if (offset >= 0)
                range.replaceText(pPlaceHolder, pValue, offset);
            else
                keepLooking = false;
        }
    }
}
