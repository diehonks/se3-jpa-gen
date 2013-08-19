package {{cls.package}};

%def profile_imports(profile_name_import_dict):
    %profiles = set()
    %for m in cls.members:
        %if hasattr(m, 'umlnode'):
            %profiles = profiles.union(m.umlnode.profiles)
        %end
    %end
    %for profile in profiles:
import {{profile_name_import_dict[profile]}};
    %end
%end

%profile_imports({
%'Column': 'javax.persistence.Column',
%'GeneratedValue': 'javax.persistence.GeneratedValue',
%'Id': 'javax.persistence.Id',
%})


%for m in cls.members:
%if '@ManyToMany' == m.multiplicity and m.type.__class__.__name__ == 'JClass':
import javax.persistence.ManyToMany;
%break
%end
%end

%for m in cls.members:
%if '@OneToMany' == m.multiplicity and m.type.__class__.__name__ == 'JClass':
import javax.persistence.OneToMany;
%break
%end
%end

%for m in cls.members:
%if '@OneToOne' == m.multiplicity and m.type.__class__.__name__ == 'JClass':
import javax.persistence.OneToOne;
%break
%end
%end

%for m in cls.members:
%if m.upper == '*':
import java.util.List;
import java.util.ArrayList;
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
    
    public {{cls.name}}(){
        //constructor stub!
    }
    
    %include operations cls=cls
    
    %include gettersetter cls=cls
    
    
}
