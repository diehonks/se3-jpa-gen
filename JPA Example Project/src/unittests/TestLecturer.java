package unittests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import persistence.daos.LecturerDAO;
import persistence.entities.CoreTheme;
import persistence.entities.Lecturer;

public class TestLecturer {
	
	private LecturerDAO lecturerDAO = null;
	private long lecturerID = 0;
	
	@BeforeClass
    public void oneTimeSetUp() {
		lecturerDAO = new LecturerDAO();
    }
	
	@Test
    public void createLecturer() {
    	Lecturer lecturer = new Lecturer();
    	lecturer.setFirstName("TestVal1");
    	lecturer.setLastName("TestVal2");
    	lecturer.setCoreTheme(CoreTheme.ARCHITECTURE);
    	lecturerID = lecturer.getId();
    	Assert.assertEquals(lecturerID, 0);
    	lecturerID = lecturerDAO.createLecturer(lecturer).getId();
    	Assert.assertEquals((lecturerID>0), true);
    	lecturerDAO.createLecturer(new Lecturer());
    }
	
	@Test(dependsOnMethods="createLecturer")
    public void readLecturer() {
		Lecturer lecturer = lecturerDAO.readLecturer(lecturerID);
    	Assert.assertEquals(lecturer.getFirstName(), "TestVal1");
    	Assert.assertEquals(lecturer.getLastName(), "TestVal2");
    	Assert.assertEquals(lecturer.getCoreTheme(), CoreTheme.ARCHITECTURE);
    	int count = lecturerDAO.readAllLecturer().size();
    	Assert.assertEquals(count, 2);
    }
	
	@Test(dependsOnMethods="readLecturer")
    public void updateLecturer() {
		Lecturer lecturer = lecturerDAO.readLecturer(lecturerID);
		lecturer.setFirstName("TestVal4");
		lecturer.setLastName("TestVal5");
		lecturer.setCoreTheme(CoreTheme.ECONOMICS);
    	lecturerDAO.updateLecturer(lecturer);
    	Lecturer sameLecturer = lecturerDAO.readLecturer(lecturerID);
    	Assert.assertEquals(sameLecturer.getFirstName(), "TestVal4");
    	Assert.assertEquals(sameLecturer.getLastName(), "TestVal5");
    	Assert.assertEquals(sameLecturer.getCoreTheme(), CoreTheme.ECONOMICS);
    }
    
    @Test(dependsOnMethods="updateLecturer")
    public void deleteLecturer() {
    	Lecturer lecturer  = lecturerDAO.readLecturer(lecturerID);
    	lecturerDAO.deleteLecturer(lecturer);
    	lecturer = lecturerDAO.readLecturer(lecturerID);
    	Assert.assertNull(lecturer); 
    }
}
