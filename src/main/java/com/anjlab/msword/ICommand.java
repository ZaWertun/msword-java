package com.anjlab.msword;

import org.apache.poi.hwpf.HWPFDocument;
import org.json.JSONObject;

public interface ICommand
{

    static final String FIND_AND_REPLACE = "find_and_replace";

    void run(JSONObject commandJson, HWPFDocument doc) throws CommandException;

}
