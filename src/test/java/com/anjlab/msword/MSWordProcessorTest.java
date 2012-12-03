package com.anjlab.msword;

import java.io.File;
import java.util.Date;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

public class MSWordProcessorTest
{
    @Test
    public void testFindAndReplaceInDoc() throws Exception
    {
        new File("target/testFindAndReplaceInDoc").mkdirs();
        
        String input =
                "{"
                + "  'src':'src/test/resources/example.doc',"
                + "  'output': 'target/testFindAndReplaceInDoc/example.doc',"
                + "  'commands': ["
                + "    {"
                + "      'command': 'find_and_replace',"
                + "      'args': [['<insert-date-here>', '" + new Date() + "'], ['#to-replace', 'replaced']]"
                + "    }"
                + "  ]"
                + "}";
        
        MSWordProcessor processor = new MSWordProcessor(new JSONObject(input));
        
        processor.process();
    }
    
    @Ignore
    @Test
    public void testFindAndReplaceInDocx() throws Exception
    {
        new File("target/testFindAndReplaceInDocx").mkdirs();
        
        String input =
                "{"
                + "  'src':'src/test/resources/example.docx',"
                + "  'output': 'target/testFindAndReplaceInDocx/example.docx',"
                + "  'commands': ["
                + "    {"
                + "      'command': 'find_and_replace',"
                + "      'args': [['<insert-date-here>', '" + new Date() + "'], ['#to-replace', 'replaced']]"
                + "    }"
                + "  ]"
                + "}";
        
        MSWordProcessor processor = new MSWordProcessor(new JSONObject(input));
        
        processor.process();
    }

}
