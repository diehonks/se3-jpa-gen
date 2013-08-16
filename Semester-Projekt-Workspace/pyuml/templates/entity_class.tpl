package {{cls.package}};

%if 'Profile:Entity' in cls.umlnode.profiles:
import javax.persistence.Entity;

@Entity
%end
%if 'Profile:MappedSuperClass' in cls.umlnode.profiles:
@MappedSuperClass
%end
%if 'Profile:Embeddable' in cls.umlnode.profiles:
@Embeddable
%end
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
