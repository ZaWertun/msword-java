Small utility written in Java to replace patterns in template DOC / DOCX file
passed as JSON object to standard input and write result to standard output.

Patterns in the template must be in form `${some_pattern_name}`.

Also pattern can be specified as `${sub_object.sub_sub_object.property}` when property should be found in nested object(s).
For pattern above corresponding JSON would look like:
`
{
    "sub_object": {
        "sub_sub_object": {
            "property": "…"            
        }
    }
}
`


Command line usage:
```
echo '{"what to find1": "replace with1", "what to find 2": "replace with 2", ...}' | java -jar msword-java-1.2.0.jar "path/to/template.doc(x)"`
```
