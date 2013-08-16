%def typedef(cls, m):
    %if cls.package == m.type.package:
 {{m.type.name}}\\
    %else:
 {{m.type.package}}.{{m.type.name}}\\
    %end
%end

%def typedef_multiplicity(cls, m):
    %if m.upper == '*':
 LinkedList<\\
%typedef(cls, m)
> \\
    %else:
        %typedef(cls, m)
    %end
%end

%def print_profile_attr(profile):
    %for n, v in profile.items():
        %if v == 'true' or v == 'false':
            {{n}} = {{v}},
        %else:
            {{n}} = "{{v}}",
        %end
    %end
%end

%for m in cls.members:
%if hasattr(m, 'umlnode'):
    %if 'Id' in m.umlnode.profiles:
    @Id
    %end
    %if 'Column' in m.umlnode.profiles:
    @Column(
        %print_profile_attr(m.umlnode.profiles['Column'])
    )
    %end
    %if 'Profile:GeneratedValue' in m.umlnode.profiles:
    @GeneratedValue
    %end
%end
    {{m.multiplicity}}
    {{m.visibility}} \\
%typedef_multiplicity(cls, m)
 {{m.name}};
%end