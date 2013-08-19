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
%if type.name == 'String':
%return '"%s%s%d"' % (name, type.name, n)
%else:
%return 'NOT_DEFAULT_TYPE_IMPLEMENTED'
%end
%end

package unittests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import persistence.daos.{{cls.name}}DAO;
import {{cls.package}}.{{cls.name}};

// import members!
//import persistence.entities.StudyTheme;

public class Test{{cls.name.lower()}} {

	private {{cls.name}}DAO {{cls.name.lower()}}DAO = null;
	private long {{cls.name.lower()}}ID = 0;

	@BeforeClass
	public void oneTimeSetUp() {
		{{cls.name.lower()}}DAO = new {{cls.name}}DAO();
	}

	%test_dependency(cls, test_previous, '')
	public void create{{cls.name}}() {
		{{cls.name.lower()}}DAO.deleteAll{{cls.name}}s();
		final {{cls.name}} {{cls.name.lower()}} = new {{cls.name}}();
		
        
        %for m in cls.members:
        %if m.visibility == 'public':
            %if m.upper != '*':
        {{cls.name.lower()}}.set{{m.name[0].upper()+m.name[1:]}}({{defaultValue(m.type, m.name)}})
            %end
        %end
        %end
       
		{{cls.name.lower()}}ID = {{cls.name.lower()}}.getId();
		Assert.assertEquals({{cls.name.lower()}}ID, 0);
		{{cls.name.lower()}}ID = {{cls.name.lower()}}DAO.create{{cls.name}}({{cls.name.lower()}}).getId();
		Assert.assertEquals(({{cls.name.lower()}}ID > 0), true);
		{{cls.name.lower()}}DAO.create{{cls.name}}(new {{cls.name}}());
		Assert.assertEquals({{cls.name.lower()}}ID != {{cls.name.lower()}}DAO
            .create{{cls.name}}(new {{cls.name}}()).getId(), true);
	}

	%test_dependency(cls, test_previous, 'create'+cls.name)
	public void read{{cls.name}}() {
		final {{cls.name}} {{cls.name.lower()}} = {{cls.name.lower()}}DAO.read{{cls.name}}({{cls.name.lower()}}ID);
        %for m in cls.members:
        %if m.visibility == 'public':
            %if m.upper != '*':
        Assert.assertEquals({{cls.name.lower()}}.get{{m.name[0].upper()+m.name[1:]}}(), {{defaultValue(m.type, m.name)}});
            %end
        %end
        %end
		
		final int count = {{cls.name.lower()}}DAO.readAll{{cls.name}}().size();
		Assert.assertEquals(count, 1);
	}

	%test_dependency(cls, test_previous, 'read'+cls.name)
	public void update{{cls.name}}() {
		final {{cls.name}} {{cls.name.lower()}} = {{cls.name.lower()}}DAO.read{{cls.name}}({{cls.name.lower()}}ID);
        %for m in cls.members:
        %if m.visibility == 'public':
            %if m.upper != '*':
        {{cls.name.lower()}}.set{{m.name[0].upper()+m.name[1:]}}({{defaultValue(m.type, m.name, 2)}})
            %end
        %end
        %end
		{{cls.name.lower()}}DAO.update{{cls.name}}({{cls.name.lower()}});
		final {{cls.name}} same{{cls.name}} = {{cls.name.lower()}}DAO.read{{cls.name}}({{cls.name.lower()}}ID);
        %for m in cls.members:
        %if m.visibility == 'public':
            %if m.upper != '*':
        Assert.assertEquals(same{{cls.name}}.get{{m.name[0].upper()+m.name[1:]}}(), {{defaultValue(m.type, m.name, 2)}});
            %end
        %end
        %end
	}

	%test_dependency(cls, test_previous, 'update'+cls.name)
	public void delete{{cls.name}}() {
		{{cls.name}} {{cls.name.lower()}} = {{cls.name.lower()}}DAO.read{{cls.name}}({{cls.name.lower()}}ID);
		{{cls.name.lower()}}DAO.deleteLecturer({{cls.name.lower()}});
		{{cls.name.lower()}} = {{cls.name.lower()}}DAO.read{{cls.name}}({{cls.name.lower()}}ID);
		Assert.assertNull({{cls.name.lower()}});
	}
}
