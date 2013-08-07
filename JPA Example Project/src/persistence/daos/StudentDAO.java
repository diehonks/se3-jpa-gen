package persistence.daos;

import java.util.List;

import persistence.entities.Student;


public class StudentDAO extends ADAO {
	
	public Student createStudent(Student student){
		return create(student);
	}
	
	public Student readStudent(long id){
		return read(Student.class, id);
	}
	
	public Student updateStudent(Student student){
		return update(student);
	}
	
	public List<Student> readAllStudents(){
		return readByJPQL("select t from Student t");
	}
	
	public void deleteStudent(Student student){
		delete(student);
	}
}
