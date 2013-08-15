package unittests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import persistence.daos.StudentDAO;
import persistence.entities.Address;
import persistence.entities.Course;
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
		final Student student = new Student();
		student.setFirstName("TestVal1");
		student.setLastName("TestVal2");
		student.setMatrikelNr("TestVal3");
		studentID = student.getId();
		Assert.assertEquals(studentID, 0);
		studentID = studentDAO.createStudent(student).getId();
		Assert.assertEquals((studentID > 0), true);
		studentDAO.createStudent(new Student());
	}

	@Test(dependsOnMethods = "createStudent")
	public void readStudent() {
		final Student student = studentDAO.readStudent(studentID);
		Assert.assertEquals(student.getFirstName(), "TestVal1");
		Assert.assertEquals(student.getLastName(), "TestVal2");
		Assert.assertEquals(student.getMatrikelNr(), "TestVal3");
		final int count = studentDAO.readAllStudents().size();
		Assert.assertEquals(count, 2);
	}

	@Test(dependsOnMethods = "readStudent")
	public void updateStudent() {
		final Student student = studentDAO.readStudent(studentID);
		student.setFirstName("TestVal4");
		student.setLastName("TestVal5");
		student.setMatrikelNr("TestVal6");

		student.setAddress(new Address());

		Assert.assertEquals(student.getCourses().size(), 0);
		final Course course = new Course();
		student.addCourse(course);

		studentDAO.updateStudent(student);
		final Student sameStudent = studentDAO.readStudent(studentID);
		Assert.assertEquals(sameStudent.getFirstName(), "TestVal4");
		Assert.assertEquals(sameStudent.getLastName(), "TestVal5");
		Assert.assertEquals(sameStudent.getMatrikelNr(), "TestVal6");

		final Address address = sameStudent.getAddress();
		Assert.assertTrue(address.getId() > 0);

		final List<Course> courses = sameStudent.getCourses();
		Assert.assertEquals(courses.size(), 1);
		Assert.assertTrue(courses.get(0).getId() > 0);
	}

	@Test(dependsOnMethods = "updateStudent")
	public void deleteStudent() {
		Student student = studentDAO.readStudent(studentID);
		studentDAO.deleteStudent(student);
		student = studentDAO.readStudent(studentID);
		Assert.assertNull(student);
	}
}
