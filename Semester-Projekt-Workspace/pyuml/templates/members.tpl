%for m in members:
    {{m.visibility}} {{m.type.package}}.{{m.type.name}} {{m.name}};
%end