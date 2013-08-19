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
	public void create{{cls.name.lower()}}() {
		{{cls.name.lower()}}DAO.deleteAll{{cls.name}}s();
		final {{cls.name}} {{cls.name.lower()}} = new {{cls.name}}();
		
        /*
        for op in operations ...
        
        lecturer.setFirstName("TestVal1");
		lecturer.setLastName("TestVal2");
		lecturer.setCoreTheme(StudyTheme.ARCHITECTURE);
        
        */
		{{cls.name.lower()}}ID = {{cls.name.lower()}}.getId();
		Assert.assertEquals({{cls.name.lower()}}ID, 0);
		{{cls.name.lower()}}ID = {{cls.name.lower()}}DAO.create{{cls.name}}({{cls.name.lower()}}).getId();
		Assert.assertEquals(({{cls.name.lower()}}ID > 0), true);
		{{cls.name.lower()}}DAO.create{{cls.name}}(new {{cls.name}}());
	}

	%test_dependency(cls, test_previous, 'create'+cls.name)
	public void readLecturer() {
		final Lecturer lecturer = lecturerDAO.readLecturer(lecturerID);
		Assert.assertEquals(lecturer.getFirstName(), "TestVal1");
		Assert.assertEquals(lecturer.getLastName(), "TestVal2");
		Assert.assertEquals(lecturer.getCoreTheme(), StudyTheme.ARCHITECTURE);
		final int count = lecturerDAO.readAllLecturer().size();
		Assert.assertEquals(count, 2);
	}

	%test_dependency(cls, test_previous, 'read'+cls.name)
	public void updateLecturer() {
		final Lecturer lecturer = lecturerDAO.readLecturer(lecturerID);
		lecturer.setFirstName("TestVal4");
		lecturer.setLastName("TestVal5");
		lecturer.setCoreTheme(StudyTheme.ECONOMICS);
		lecturerDAO.updateLecturer(lecturer);
		final Lecturer sameLecturer = lecturerDAO.readLecturer(lecturerID);
		Assert.assertEquals(sameLecturer.getFirstName(), "TestVal4");
		Assert.assertEquals(sameLecturer.getLastName(), "TestVal5");
		Assert.assertEquals(sameLecturer.getCoreTheme(), StudyTheme.ECONOMICS);
	}

	%test_dependency(cls, test_previous, 'update'+cls.name)
	public void deleteLecturer() {
		Lecturer lecturer = lecturerDAO.readLecturer(lecturerID);
		lecturerDAO.deleteLecturer(lecturer);
		lecturer = lecturerDAO.readLecturer(lecturerID);
		Assert.assertNull(lecturer);
	}
}
