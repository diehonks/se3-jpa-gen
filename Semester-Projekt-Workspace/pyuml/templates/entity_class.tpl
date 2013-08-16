package {{cls.package}};

import javax.persistence.Entity;

@Entity
public class {{cls.name}} \\
%if not cls.inherits_from is None:
extends {{cls.inherits_from.name}} \\
%end
%if len(cls.implements) > 0:
implements {{', '.join([interface.name for interface in cls.implements])}}
%end
{
    private static final long serialVersionUID = {{id(cls)}}L;
    
    %include members cls=cls
    
    %include gettersetter cls=cls
    
    %include operations cls=cls
    
    
}
