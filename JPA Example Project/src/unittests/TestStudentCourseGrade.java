package unittests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import persistence.daos.CourseDAO;
import persistence.daos.StudentCourseGradeDAO;
import persistence.daos.StudentDAO;
import persistence.entities.Address;
import persistence.entities.Course;
import persistence.entities.Student;
import persistence.entities.StudentCourseGrade;

public class TestStudentCourseGrade {

	private StudentCourseGradeDAO courseGradeDAO = null;
	private StudentDAO studentDAO = null;
	private CourseDAO courseDAO = null;
	private long courseGradeID = 0;
	private Student student = null;
	private Course course = null;

	@BeforeClass
	public void initTest() {
		courseGradeDAO = new StudentCourseGradeDAO();
		studentDAO = new StudentDAO();
		courseDAO = new CourseDAO();
	}

	@Test
	public void createStudentCourseGrade() {
		final StudentCourseGrade courseGrade = new StudentCourseGrade();
		student = new Student();
		studentDAO.createStudent(student);
		courseGrade.setStudent(student);
		course = new Course();
		courseDAO.createCourse(course);
		courseGrade.setCourse(course);
		courseGrade.setGrade(2.3f);
		courseGradeID = courseGrade.getId();
		Assert.assertEquals(courseGradeID, 0);
		courseGradeID = courseGradeDAO.createStudentCourseGrade(courseGrade)
				.getId();
		Assert.assertEquals((courseGradeID > 0), true);
	}

	@Test(dependsOnMethods = "createStudentCourseGrade")
	public void readStudentCourseGrade() {
		final StudentCourseGrade courseGrade = courseGradeDAO
				.readStudentCourseGrade(courseGradeID);
		Assert.assertEquals(courseGrade.getStudent(), student);
		Assert.assertEquals(courseGrade.getCourse(), course);
		Assert.assertEquals(courseGrade.getGrade(), 2.3f);
		int count = courseGradeDAO.readAllStudentCourseGrade().size();
		Assert.assertEquals(count, 1);

		StudentCourseGrade newCourseGrade = new StudentCourseGrade();
		Student newStudi = new Student();
		studentDAO.createStudent(newStudi);
		Course newCourse = new Course();
		courseDAO.createCourse(newCourse);

		courseGradeDAO.createStudentCourseGrade(newCourseGrade);
		count = courseGradeDAO.readAllStudentCourseGrade().size();
		Assert.assertEquals(count, 2);
	}

	@Test(dependsOnMethods = "readStudentCourseGrade")
	public void updateStudentCourseGrade() {
		final StudentCourseGrade courseGrade = courseGradeDAO
				.readStudentCourseGrade(courseGradeID);
		student = new Student();
		studentDAO.createStudent(student);
		courseGrade.setStudent(student);

		course = new Course();
		courseDAO.createCourse(course);
		courseGrade.setCourse(course);
		courseGrade.setGrade(1.0f);
		courseGradeDAO.updateStudentCourseGrade(courseGrade);

		final StudentCourseGrade sameCourseGrade = courseGradeDAO
				.readStudentCourseGrade(courseGradeID);
		Assert.assertEquals(sameCourseGrade.getStudent(), student);
		Assert.assertEquals(sameCourseGrade.getCourse(), course);
		Assert.assertEquals(sameCourseGrade.getGrade(), 1.0f);
	}

	@Test(dependsOnMethods = "updateStudentCourseGrade")
	public void deleteStudentCourseGrade() {
		StudentCourseGrade courseGrade = courseGradeDAO
				.readStudentCourseGrade(courseGradeID);
		int count = courseGradeDAO.readAllStudentCourseGrade().size();
		Assert.assertEquals(count, 2);
		courseGradeDAO.deleteStudentCourseGrade(courseGrade);
		count = courseGradeDAO.readAllStudentCourseGrade().size();
		Assert.assertEquals(count, 1);
		courseGrade = courseGradeDAO.readStudentCourseGrade(courseGradeID);
		Assert.assertNull(courseGrade);
	}
}
