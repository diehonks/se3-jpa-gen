%for op in clazz.operations:
    {{op.visibility}} \\
    %if op.name != clazz.name:
        %if op.returns is None:
void \\
        %else:
{{op.returns.package}}.{{op.returns.name}} \\
        %end
    %end
    %if op.abstract:
abstract \\
    %end
{{op.name}}() \\
    %if op.abstract:
;
    %else:
    {
        %if op.name == clazz.name:
        //constructor stub
        %else:
        //method body stub
        %end
    %if not op.returns is None:
        return null;
    %end
    }
%end