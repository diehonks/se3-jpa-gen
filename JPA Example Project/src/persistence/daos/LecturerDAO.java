package persistence.daos;

import java.util.List;

import persistence.entities.Lecturer;

public class LecturerDAO extends ADAO {
	
	public Lecturer createLecturer(Lecturer lecturer){
		return create(lecturer);
	}
	
	public Lecturer readLecturer(long id){
		return read(Lecturer.class, id);
	}
	
	public Lecturer updateLecturer(Lecturer lecturer){
		return update(lecturer);
	}
	
	public List<Lecturer> readAllLecturer(){
		return readByJPQL("select t from Lecturer t");
	}
	
	public void deleteLecturer(Lecturer lecturer){
		delete(lecturer);
	}

}
