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

%for m in cls.members:
%if hasattr(m, 'umlnode'):
    %if 'Profile:Id' in m.umlnode.profiles:
    @Id
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