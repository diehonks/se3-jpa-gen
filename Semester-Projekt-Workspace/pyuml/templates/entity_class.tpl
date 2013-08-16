package {{cls.package}};

%for m in cls.members:
%if hasattr(m, 'umlnode') and 'Column' in m.umlnode.profiles:
import javax.persistence.Column;
%break
%end
%end

%for m in cls.members:
%if hasattr(m, 'umlnode') and 'Id' in m.umlnode.profiles:
import javax.persistence.Id;
%break
%end
%end

%if 'Entity' in cls.umlnode.profiles:
import javax.persistence.Entity;

@Entity
%end
%if 'MappedSuperClass' in cls.umlnode.profiles:
import javax.persistence.MappedSuperclass;
@MappedSuperclass
%end
%if 'Embeddable' in cls.umlnode.profiles:
import javax.persistence.Embeddable;
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
