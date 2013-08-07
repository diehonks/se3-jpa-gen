package unittests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import persistence.daos.StudentDAO;
import persistence.entities.Student;



public class TestStudent {
	
	private StudentDAO studentDAO = null;
	private long studentID = 0;
	
	@BeforeClass
    public void oneTimeSetUp() {
		studentDAO = new StudentDAO();
    }
 
    @Test
    public void createStudent() {
    	Student student = new Student();
    	student.setFirstName("TestVal1");
    	student.setLastName("TestVal2");
    	student.setMatrikelNr("TestVal3");
    	studentID = student.getId();
    	Assert.assertEquals(studentID, 0);
    	studentID = studentDAO.createStudent(student).getId();
    	System.out.println(studentID);
    	Assert.assertEquals((studentID>0), true);
    	studentDAO.createStudent(new Student());
    }
    
    @Test(dependsOnMethods="createStudent")
    public void readStudent() {
    	Student student = studentDAO.readStudent(studentID);
    	Assert.assertEquals(student.getFirstName(), "TestVal1");
    	Assert.assertEquals(student.getLastName(), "TestVal2");
    	Assert.assertEquals(student.getMatrikelNr(), "TestVal3");
    	int count = studentDAO.readAllStudents().size();
    	Assert.assertEquals(count, 2);
    }
    
    @Test(dependsOnMethods="readStudent")
    public void updateStudent() {
    	Student student = studentDAO.readStudent(studentID);
    	student.setFirstName("TestVal4");
    	student.setLastName("TestVal5");
    	student.setMatrikelNr("TestVal6");
    	studentDAO.updateStudent(student);
    	Student sameStudent = studentDAO.readStudent(studentID);
    	Assert.assertEquals(sameStudent.getFirstName(), "TestVal4");
    	Assert.assertEquals(sameStudent.getLastName(), "TestVal5");
    	Assert.assertEquals(sameStudent.getMatrikelNr(), "TestVal6");
    }
    
    @Test(dependsOnMethods="updateStudent")
    public void deleteStudent() {
    	Student student = studentDAO.readStudent(studentID);
    	studentDAO.deleteStudent(student);
    	student = studentDAO.readStudent(studentID);
    	Assert.assertNull(student); 
    }
}
