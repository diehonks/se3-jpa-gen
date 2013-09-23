%for op in cls.operations:
    %if hasattr(op, 'umlnode'):
        %if 'PrePersist' in op.umlnode.profiles:
    @PrePersist
        %end
        %if 'PostPresist' in op.umlnode.profiles:
    @PostPresist
        %end
        %if 'PreRemove' in op.umlnode.profiles:
    @PreRemove
        %end
        %if 'PostRemove' in op.umlnode.profiles:
    @PostRemove
        %end
        %if 'PreUpdate' in op.umlnode.profiles:
    @PreUpdate
        %end
        %if 'PostUpdate' in op.umlnode.profiles:
    @PostUpdate
        %end
        %if 'PostLoad' in op.umlnode.profiles:
    @PostLoad
        %end
    %end
    {{op.visibility[0]}} \\
    %if op.name != cls.name:
        %if op.returns is None:
void \\
        %else:
{{op.returns.package}}.{{op.returns.name}} \\
        %end
    %end
    %if op.abstract:
abstract \\
    %end
{{op.name}}()\\
    %if op.abstract:
;
    %else:
{
        %if op.name == cls.name:
        //constructor stub
        %else:
        //method body stub
        %end
    %if not op.returns is None:
        return null;
    %end
    }
%end
