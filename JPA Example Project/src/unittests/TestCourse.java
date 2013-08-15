package unittests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import persistence.entities.Course;
import persistence.entities.Lecturer;
import persistence.entities.Period;
import persistence.entities.Student;
import persistence.entities.StudyPath;
import persistence.entities.StudyTheme;
import persistence.daos.CourseDAO;

/* Integrated test for period class */

public class TestCourse {

	private CourseDAO courseDAO = null;
	private long courseID = 0;
	private StudyPath coursePath = null;

	@BeforeClass
	public void oneTimeSetUp() {
		courseDAO = new CourseDAO();
	}

	@Test
	public void createCourse() {
		final Course course = new Course();
		course.setName("TestName");
		final Period period = new Period();
		period.setStartDate(new java.sql.Date(1000));
		period.setEndDate(new java.sql.Date(2000));
		course.setCoursePeriod(period);

		final StudyPath path = new StudyPath();
		path.setName("TestPathName");
		path.setTheme(StudyTheme.INFORMATICS);

		course.setStudyPath(path);
		courseID = course.getId();
		coursePath = course.getStudyPath();
		Assert.assertEquals(courseID, 0);
		courseID = courseDAO.createCourse(course).getId();
		Assert.assertEquals((courseID > 0), true);
		Assert.assertEquals(courseID != courseDAO.createCourse(new Course())
				.getId(), true);
	}

	@Test(dependsOnMethods = "createCourse")
	public void readCourse() {
		final Course course = courseDAO.readCourse(courseID);
		Assert.assertEquals(course.getName(), "TestName");

		final Period period = course.getCoursePeriod();
		Assert.assertEquals(period.getStartDate().getTime(), 1000);
		Assert.assertEquals(period.getEndDate().getTime(), 2000);

		Assert.assertEquals(course.getStudyPath(), coursePath);
		final int count = courseDAO.readAllCourse().size();
		Assert.assertEquals(count, 2);
	}

	@Test(dependsOnMethods = "readCourse")
	public void updateCourse() {
		final Course course = courseDAO.readCourse(courseID);
		course.setName("NewTestName");
		Period period = new Period();
		period.setStartDate(new java.sql.Date(3000));
		period.setEndDate(new java.sql.Date(4000));
		course.setCoursePeriod(period);
		StudyPath newPath = new StudyPath();
		course.setStudyPath(newPath);
		courseDAO.updateCourse(course);

		final Course sameCourse = courseDAO.readCourse(courseID);
		Assert.assertEquals(sameCourse.getName(), "NewTestName");
		period = sameCourse.getCoursePeriod();
		Assert.assertEquals(period.getStartDate().getTime(), 3000);
		Assert.assertEquals(period.getEndDate().getTime(), 4000);
		Assert.assertEquals(sameCourse.getStudyPath(), newPath);
	}

	@Test(dependsOnMethods = "updateCourse")
	public void deleteCourse() {
		Course course = courseDAO.readCourse(courseID);
		int count = courseDAO.readAllCourse().size();
		Assert.assertEquals(count, 2);
		courseDAO.deleteCourse(course);
		Assert.assertEquals(count, 1);
		course = courseDAO.readCourse(courseID);
		Assert.assertNull(course);
	}

	@Test
	public void listAccess() {
		final Course course = new Course();
		final Lecturer lecturer = new Lecturer();
		Assert.assertEquals(course.getLecturers().size(), 0);
		course.addLecturer(lecturer);
		Assert.assertEquals(course.getLecturers().size(), 1);
		course.addLecturer(new Lecturer());
		Assert.assertEquals(course.getLecturers().size(), 2);
		Assert.assertEquals(course.getLecturers().get(0), lecturer);
		course.removeLecturer(lecturer);
		Assert.assertEquals(course.getLecturers().size(), 1);

		final Student student = new Student();
		Assert.assertEquals(course.getStudents().size(), 0);
		course.addStudent(student);
		Assert.assertEquals(course.getStudents().size(), 1);
		course.addStudent(new Student());
		Assert.assertEquals(course.getStudents().size(), 2);
		Assert.assertEquals(course.getStudents().get(0), student);
		course.removeStudent(student);
		Assert.assertEquals(course.getStudents().size(), 1);
	}
}
