Small utility written in Java to replace patterns in template DOC / DOCX file
and write result to standard output.

Patterns in the template must be in form `${some_pattern_name}`. 

Command line usage:
```
java -jar msword-java-1.1.0.jar "path/to/template.doc(x)" '[["what to find1", "replace with1"], ["what to find 2", "replace with 2"], ...]'`
```
