package com.anjlab.msword;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MSWordProcessor
{

    private JSONObject args;
    private Map<String, ICommand> commands;
    
    public MSWordProcessor(JSONObject args)
    {
        this.args = args;
        this.commands = new HashMap<String, ICommand>();
        
        this.commands.put(ICommand.FIND_AND_REPLACE, new FindAndReplaceCommand());
    }
    
    public void process() throws IOException, JSONException, CommandException
    {
        InputStream input = null;
        OutputStream output = null;
        
        try
        {
            String srcFilename = args.getString("src");
            
            input = new FileInputStream(srcFilename);
            
            POIFSFileSystem fs = new POIFSFileSystem(input);
            
            HWPFDocument doc = new HWPFDocument(fs);
            
            JSONArray commands = this.args.getJSONArray("commands");
            
            for (int i = 0; i < commands.length(); i++)
            {
                JSONObject jsonObject = commands.getJSONObject(i);
                String commandName = jsonObject.getString("command");
                
                ICommand command = this.commands.get(commandName);
                
                command.run(jsonObject, doc);
            }
            
            output = new FileOutputStream(args.getString("output"));
            
            doc.write(output);
        }
        finally
        {
            if (input != null)
            {
                try { input.close(); } catch (IOException e) {}
            }
            if (output != null)
            {
                try { output.close(); } catch (IOException e) {}
            }
        }
    }

    /**
     * 
     * @param in
     * 
     * <pre>
     * https://gist.github.com/005c69458b116311d2a8
{
  "src":"path/to/word.doc(x)",
  "output": "path/to/output_word.doc(x)",
  "commands": [
    {
      "command": "find_and_replace",
      "args": [["what to find1", "replace with1"], ["what to find 2", "replace with 2"]]
    }
  ]
}
     * </pre>
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws JSONException 
     */
    public static MSWordProcessor createFromJSON(InputStream in)
            throws UnsupportedEncodingException, IOException, JSONException
    {
        String json = IOUtils.readToEnd(in, "UTF-8");
        return new MSWordProcessor(new JSONObject(json));
    }

}
