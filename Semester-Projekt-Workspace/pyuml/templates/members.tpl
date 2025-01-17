%def typedef(cls, m):
    %if hasattr(m, 'umlnode') and 'Unboxed' in m.umlnode.profiles:
        %if m.type.name == 'Long':
long \\
        %else:
UNBOX TYPE {{m.type.name}} NOT IMPLEMENTED IN members.tpl
        %end
    %else:
{{m.type.name}} \\
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
{{', '.join([str(n)+'='+str(v) for n, v in profile.items()])}} \\
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
        %if 'GeneratedValue' in m.umlnode.profiles:
    @GeneratedValue
    %end
%end
%if m.type.__class__.__name__ == 'JClass':
    {{m.multiplicity}}
%end
    {{m.visibility}} \\
%typedef_multiplicity(cls, m)
%end