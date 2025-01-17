%def test_dependency(cls, test_previous, method_previous):
    @Test(groups = {"Test{{cls.name}}"}\\
%if test_previous != '':
, dependsOnGroups = {"{{test_previous}}"}\\
%end
%if method_previous != '':
, dependsOnMethods = {"{{method_previous}}"}\\
%end
)
%end

%def defaultValue(type, name, n=1):
%if type.__class__.__name__ == 'JClass':
    %return 'new %s()' % type.name;
%elif type.__class__.__name__ == 'JPrimitive':
    %if type.name == 'String':
        %return '"%s%s%d"' % (name, type.name, n)
    %elif type.name == 'Long':
        %return 13*n
    %elif type.name == 'Double':
        %return 1.34*n
    %else:
        %return 'UNIMPLEMENTED PRIMITIVE %s' % (type.name)
    %end
%elif type.__class__.__name__ == 'JEnum':
    %return '%s.%s' % (type.name, type.literals[n])
%else:
    %return 'DEFAULT TYPE NOT IMPLEMENTED (%s)' % type.__class__.__name__
%end
%end

package unittests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import persistence.daos.{{cls.name}}DAO;
import {{cls.package}}.{{cls.name}};

%for m in cls.members:
%if m.upper == '*':
import java.util.List;
import java.util.ArrayList;
%break
%end
%end

%for m in cls.members_rec():
import {{m.type.package}}.{{m.type.name}};
    %isjclass = m.type.__class__.__name__ == 'JClass'
    %isentity = hasattr(m.type, 'umlnode') and 'Entity' in m.type.umlnode.profiles
    %if isjclass and isentity and not m.type.abstract:
import {{m.type.package[:m.type.package.rindex('.')]}}.daos.{{m.type.name}}DAO;
    %end
%end

public class Test{{cls.name}} {

	private {{cls.name}}DAO {{cls.name.lower()}}DAO = null;
    
    %#iterate over members to prepare DAOs for all entity classes
    %for m in cls.members:
        %isjclass = m.type.__class__.__name__ == 'JClass'
        %isentity = hasattr(m.type, 'umlnode') and 'Entity' in m.type.umlnode.profiles
        %if isjclass and isentity and not m.type.abstract:
    private {{m.type.name}}DAO {{m.type.name.lower()}}DAO = null;
    private long {{m.type.name.lower()}}ID = 0;
        %end
    %end
    
	private long {{cls.name.lower()}}ID = 0;
    
    %for m in cls.members:
        %if m.visibility == 'public':
            %if m.upper != '*' and m.type.__class__.__name__ == 'JClass':
    {{m.type.name}} {{m.name.lower()}} = null;
            %end
        %end
    %end

	@BeforeClass
	public void oneTimeSetUp() {
		{{cls.name.lower()}}DAO = new {{cls.name}}DAO();
        %#initialize member DAOs
        %for m in cls.members:
            %isjclass = m.type.__class__.__name__ == 'JClass'
            %isentity = hasattr(m.type, 'umlnode') and 'Entity' in m.type.umlnode.profiles
            %if isjclass and isentity and not m.type.abstract:
        {{m.type.name.lower()}}DAO = new {{m.type.name}}DAO();
            %end
        %end
	}

	%test_dependency(cls, test_previous, '')
	public void create{{cls.name}}() {
		{{cls.name.lower()}}DAO.deleteAll{{cls.name}}s();
		final {{cls.name}} {{cls.name.lower()}} = new {{cls.name}}();
		
        
        %for m in cls.members_rec():
        %if m.visibility == 'public':
            %if m.upper != '*':
                %if m.type.__class__.__name__ == 'JClass':
        {{m.name.lower()}} = {{defaultValue(m.type, m.name)}};
        {{cls.name.lower()}}.set{{m.name[0].upper()+m.name[1:]}}({{m.name.lower()}});
                    %if hasattr(m.type, 'umlnode') and 'Entity' in m.type.umlnode.profiles:
        {{m.type.name.lower()}}ID = {{m.type.name.lower()}}DAO.create{{m.type.name}}({{m.name.lower()}}).getId();
                    %end
                %else:
        {{cls.name.lower()}}.set{{m.name[0].upper()+m.name[1:]}}({{defaultValue(m.type, m.name)}});
                %end
            %end
        %end
        %end
       
		{{cls.name.lower()}}ID = {{cls.name.lower()}}.getId();
		Assert.assertEquals({{cls.name.lower()}}ID, 0);
		{{cls.name.lower()}}ID = {{cls.name.lower()}}DAO.create{{cls.name}}({{cls.name.lower()}}).getId();
		Assert.assertEquals(({{cls.name.lower()}}ID > 0), true);
		Assert.assertEquals({{cls.name.lower()}}ID != {{cls.name.lower()}}DAO
            .create{{cls.name}}(new {{cls.name}}()).getId(), true);
	}

	%test_dependency(cls, test_previous, 'create'+cls.name)
	public void read{{cls.name}}() {
		final {{cls.name}} {{cls.name.lower()}} = {{cls.name.lower()}}DAO.read{{cls.name}}({{cls.name.lower()}}ID);
        %for m in cls.members_rec():
        %if m.visibility == 'public':
            %if m.upper != '*':
                %if m.type.__class__.__name__ == 'JClass':
                    %if hasattr(m.type, 'umlnode') and 'Entity' in m.type.umlnode.profiles:
                    %# we have to compare the ids of the persisted object instead of the objects themselves
        Assert.assertTrue({{cls.name.lower()}}.get{{m.name[0].upper()+m.name[1:]}}().getId() == {{m.type.name.lower()}}ID);                        
                    %else:
        Assert.assertEquals({{cls.name.lower()}}.get{{m.name[0].upper()+m.name[1:]}}(), {{m.name.lower()}});
                    %end
                %else:
        Assert.assertEquals({{cls.name.lower()}}.get{{m.name[0].upper()+m.name[1:]}}(), {{defaultValue(m.type, m.name)}});
                %end
            %end
        %end
        %end
		
		final int count = {{cls.name.lower()}}DAO.readAll{{cls.name}}s().size();
		Assert.assertEquals(count, 2);
	}

	%test_dependency(cls, test_previous, 'read'+cls.name)
	public void update{{cls.name}}() {
    
		final {{cls.name}} {{cls.name.lower()}} = {{cls.name.lower()}}DAO.read{{cls.name}}({{cls.name.lower()}}ID);
                
        %for m in cls.members_rec():
            %if m.visibility == 'public':
                %if m.upper != '*':
                    %if m.type.__class__.__name__ == 'JClass':
                        %if hasattr(m.type, 'umlnode') and 'Entity' in m.type.umlnode.profiles:
        Assert.assertTrue({{cls.name.lower()}}.get{{m.name[0].upper()+m.name[1:]}}().getId() == {{m.type.name.lower()}}ID);                        
                        %end
        {{m.name.lower()}} = new {{m.type.name[0].upper()+m.type.name[1:]}}();
                        %if hasattr(m.type, 'umlnode') and 'Entity' in m.type.umlnode.profiles:
        {{m.name.lower()}} = {{m.type.name.lower()}}DAO.create{{m.type.name}}({{m.name.lower()}});
                        %end
        {{cls.name.lower()}}.set{{m.name[0].upper()+m.name[1:]}}({{m.name.lower()}});
        
                    %else:
        {{cls.name.lower()}}.set{{m.name[0].upper()+m.name[1:]}}({{defaultValue(m.type, m.name, 2)}});
                    %end
                %end
            %end
        %end
                
		{{cls.name.lower()}}ID = {{cls.name.lower()}}DAO.update{{cls.name}}({{cls.name.lower()}}).getId();
		
        final {{cls.name}} same{{cls.name}} = {{cls.name.lower()}}DAO.read{{cls.name}}({{cls.name.lower()}}ID);
        %for m in cls.members_rec():
            %if m.visibility == 'public':
                %if m.upper != '*':
                    %if m.type.__class__.__name__ == 'JClass':
                        %if hasattr(m.type, 'umlnode') and 'Entity' in m.type.umlnode.profiles:
		Assert.assertEquals(same{{cls.name}}.get{{m.name[0].upper()+m.name[1:]}}(), {{m.name.lower()}});
                        %else:
		Assert.assertEquals(same{{cls.name}}.get{{m.name[0].upper()+m.name[1:]}}(), {{m.name.lower()}});
                        %end
                    %else:
        Assert.assertEquals(same{{cls.name}}.get{{m.name[0].upper()+m.name[1:]}}(), {{defaultValue(m.type, m.name, 2)}});
                    %end
                %end
            %end
        %end
	}

	%test_dependency(cls, test_previous, 'update'+cls.name)
	public void delete{{cls.name}}() {
		{{cls.name}} {{cls.name.lower()}} = {{cls.name.lower()}}DAO.read{{cls.name}}({{cls.name.lower()}}ID);
		{{cls.name.lower()}}DAO.delete{{cls.name}}({{cls.name.lower()}});
		{{cls.name.lower()}} = {{cls.name.lower()}}DAO.read{{cls.name}}({{cls.name.lower()}}ID);
		Assert.assertNull({{cls.name.lower()}});
	}
}
