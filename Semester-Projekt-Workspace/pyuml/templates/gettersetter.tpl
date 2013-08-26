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
%def addToList(m):
    public void add{{m.name[0].upper()+m.name[1:]}}({{m.type.name}} {{m.name}}item){
        this.{{m.name}}.add({{m.name}}item);
    }
%end
%def removeFromList(m):
    public void remove{{m.name[0].upper()+m.name[1:]}}({{m.type.name}} {{m.name}}item){
        this.{{m.name}}.remove({{m.name}}item);
    }
%end
%def getFromList(m):
    public List<{{m.type.name}}> get{{m.name[0].upper()+m.name[1:]}}(){
        return new ArrayList<{{m.type.name}}>(this.{{m.name}});
    }
%end
%for m in cls.members:
%if m.visibility == 'public':
    %if m.upper == '*':
    %addToList(m)
    
    %removeFromList(m)
    
    %getFromList(m)
    
    %else:
    %getter(m)
    
    %setter(m)
    
    %end
%end
%end