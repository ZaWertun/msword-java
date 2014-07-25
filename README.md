Small utility written in Java to replace patterns in template DOC / DOCX file
passed as JSON object to standard input and write result to standard output.

Patterns in the template must be in form `${some_pattern_name}`. 

Command line usage:
```
echo '{"what to find1": "replace with1", "what to find 2": "replace with 2", ...}' | java -jar msword-java-1.1.0.jar "path/to/template.doc(x)"`
```
