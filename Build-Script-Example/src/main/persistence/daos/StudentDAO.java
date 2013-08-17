package persistence.daos;

import java.util.List;

import persistence.entities.Student;

public class StudentDAO extends ADAO {

	public Student createStudent(final Student student) {
		return create(student);
	}

	public Student readStudent(final long id) {
		return read(Student.class, id);
	}

	public Student updateStudent(final Student student) {
		return update(student);
	}

	public List<Student> readAllStudents() {
		return readByJPQL("select t from Student t");
	}

	public void deleteStudent(final Student student) {
		delete(student);
	}
	
	public void deleteAllStudents() {
		deleteAll(Student.class);
	}
}
