%for op in cls.operations:
    {{op.visibility}} \\
    %if op.name != cls.name:
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
        %if op.name == cls.name:
        //constructor stub
        %else:
        //method body stub
        %end
    %if not op.returns is None:
        return null;
    %end
    }
%end