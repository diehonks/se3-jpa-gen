package {{cls.package}};

public class {{cls.name}} {
    %include members members=cls.members
    
    public {{cls.name}}(){
        //stub constructor
    }
    
    %include operations operations=cls.operations
}