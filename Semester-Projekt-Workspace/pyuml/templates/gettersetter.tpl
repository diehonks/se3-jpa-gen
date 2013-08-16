%def getter(m):
    public {{m.type.name}} get{{m.name[0].upper()+m.name[1:]}}(){
        return this.{{m.name}};
    }
%end

%def setter(m):
    public void set{{m.name[0].upper()+m.name[1:]}}({{m.type.name}} {{m.name}}){
        this.{{m.name}} = {{m.name}};
    }
%end

%for m in cls.members:
%if m.visibility == 'public':
    %getter(m)
    %setter(m)
%end
%end