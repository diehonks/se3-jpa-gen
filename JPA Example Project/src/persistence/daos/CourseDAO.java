package persistence.daos;

import java.util.List;

import persistence.entities.Course;

public class CourseDAO extends ADAO {

	public Course createCourse(final Course course) {
		return create(course);
	}

	public Course readCourse(final long id) {
		return read(Course.class, id);
	}

	public Course updateCourse(final Course course) {
		return update(course);
	}

	public List<Course> readAllCourse() {
		return readByJPQL("select t from Course t");
	}

	public void deleteCourse(final Course course) {
		delete(course);
	}

}
