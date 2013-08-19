%def typedef(cls, m):
    %if cls.package == m.type.package:
 {{m.type.name}}\\
    %else:
 {{m.type.package}}.{{m.type.name}}\\
    %end
%end

%def typedef_multiplicity(cls, m):
    %if m.upper == '*':
List<\\
%typedef(cls, m)
> {{m.name}} = new ArrayList<\\
%typedef(cls, m)
>();

    %else:
        %typedef(cls, m)
        {{m.name}};
    %end
%end

%def print_profile_attr(profile):
    %i=0
    %a = [str(n)+'='+str(v) for n, v in profile.items()]
{{', '.join(a)}}
%end

%for m in cls.members:
%if hasattr(m, 'umlnode'):
    %if 'Id' in m.umlnode.profiles:
    @Id
    %end
    %if 'Column' in m.umlnode.profiles:
    @Column( \\
        %print_profile_attr(m.umlnode.profiles['Column']) 
    )
    %end
    %if 'Profile:GeneratedValue' in m.umlnode.profiles:
    @GeneratedValue
    %end
%end
%if m.type.__class__.__name__ == 'JClass':
    {{m.multiplicity}}
%end
    {{m.visibility}} \\
%typedef_multiplicity(cls, m)
%end