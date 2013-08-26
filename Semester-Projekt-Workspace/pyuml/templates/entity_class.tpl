package {{cls.package}};

%def profile_imports(profile_name_import_dict):
    %profiles = set()
    %for m in cls.members:
        %if hasattr(m, 'umlnode'):
            %profiles = profiles.union(m.umlnode.profiles)
        %end
    %end
    %for profile in profiles:
        %if profile in profile_name_import_dict:
import {{profile_name_import_dict[profile]}};
        %end
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
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
    %if 'Entity' in cls.umlnode.profiles:
        if (((AEntity) obj).getId() != getId()) {
            return false;
        }
    %else:
        {{cls.name}} other = ({{cls.name}})obj;
        %for m in cls.members:
            %if not hasattr(m.type, 'umlnode') or not 'Entity' in m.type.umlnode.profiles:
        
        if(this.get{{m.name[0].upper()+m.name[1:]}}() == null){
            if(other.get{{m.name[0].upper()+m.name[1:]}}() != null){
                return false;
            }
        } else {
            if(!this.get{{m.name[0].upper()+m.name[1:]}}().equals(other.get{{m.name[0].upper()+m.name[1:]}}())){
                return false;
            }
        }
            %end
        %end
    %end
        return true;
    }    
}
