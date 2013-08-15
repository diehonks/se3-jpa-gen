package persistence.daos;

import java.util.List;

import persistence.entities.Lecturer;

public class LecturerDAO extends ADAO {

	public Lecturer createLecturer(final Lecturer lecturer) {
		return create(lecturer);
	}

	public Lecturer readLecturer(final long id) {
		return read(Lecturer.class, id);
	}

	public Lecturer updateLecturer(final Lecturer lecturer) {
		return update(lecturer);
	}

	public List<Lecturer> readAllLecturer() {
		return readByJPQL("select t from Lecturer t");
	}

	public void deleteLecturer(final Lecturer lecturer) {
		delete(lecturer);
	}

}
