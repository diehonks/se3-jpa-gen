package {{cls.package}};

public class {{cls.name}} {
    %include members members=cls.members
    
    %include operations clazz=cls
}