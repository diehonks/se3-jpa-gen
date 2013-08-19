package persistence.daos;

import java.util.List;

import persistence.entities.Course;
import persistence.entities.Student;
import persistence.entities.StudentCourseGrade;

public class StudentCourseGradeDAO extends ADAO {

	public StudentCourseGrade createStudentCourseGrade(
			final StudentCourseGrade studentCourseGrade) {
		return create(studentCourseGrade);
	}

	public StudentCourseGrade readStudentCourseGrade(final long id) {
		return read(StudentCourseGrade.class, id);
	}

	public List<StudentCourseGrade> readStudentCourseGrades(Student student){
		return readByJPQL("select t from StudentCourseGrade t where t.student.id = "+student.getId());
	}
	
	public List<StudentCourseGrade> readStudentCourseGrades(Course course){
		return readByJPQL("select t from StudentCourseGrade t where t.course.id = "+course.getId());
	}
	
	public List<StudentCourseGrade> readAllStudentCourseGrade() {
		return readByJPQL("select t from StudentCourseGrade t");
	}
	
	public StudentCourseGrade updateStudentCourseGrade(
			final StudentCourseGrade studentCourseGrade) {
		return update(studentCourseGrade);
	}

	

	public void deleteStudentCourseGrade(
			final StudentCourseGrade studentCourseGrade) {
		delete(studentCourseGrade);
	}
	
	public void deleteAllStudentCourseGrades() {
		deleteAll(StudentCourseGrade.class);
	}

}
