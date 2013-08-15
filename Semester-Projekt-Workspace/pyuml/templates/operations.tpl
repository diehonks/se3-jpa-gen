%for op in operations:
    {{op.visibility}} {{op.returns.package}}.{{op.returns.name}} {{op.name}}();
%end