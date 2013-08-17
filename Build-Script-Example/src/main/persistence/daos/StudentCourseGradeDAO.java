package persistence.daos;

import java.util.List;

import persistence.entities.StudentCourseGrade;

public class StudentCourseGradeDAO extends ADAO {

	public StudentCourseGrade createStudentCourseGrade(
			final StudentCourseGrade studentCourseGrade) {
		return create(studentCourseGrade);
	}

	public StudentCourseGrade readStudentCourseGrade(final long id) {
		return read(StudentCourseGrade.class, id);
	}

	public StudentCourseGrade updateStudentCourseGrade(
			final StudentCourseGrade studentCourseGrade) {
		return update(studentCourseGrade);
	}

	public List<StudentCourseGrade> readAllStudentCourseGrade() {
		return readByJPQL("select t from StudentCourseGrade t");
	}

	public void deleteStudentCourseGrade(
			final StudentCourseGrade studentCourseGrade) {
		delete(studentCourseGrade);
	}
	
	public void deleteAllStudentCourseGrades() {
		deleteAll(StudentCourseGrade.class);
	}

}
