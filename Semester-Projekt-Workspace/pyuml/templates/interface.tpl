package {{itf.package}};

public interface {{itf.name}} {
    %include members members=itf.members
    
    %include operations operations=itf.operations
}
