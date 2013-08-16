package {{cls.package}};

public class {{cls.name}} \\
%if not cls.inherits_from is None:
extends {{cls.inherits_from.name}} \\
%end
%if len(cls.implements) > 0:
implements {{', '.join([interface.name for interface in cls.implements])}}
%end
{
    
    %include members cls=cls
    
    %include operations clazz=cls
}
