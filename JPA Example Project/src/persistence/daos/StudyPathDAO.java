package persistence.daos;

import java.util.List;

import persistence.entities.StudyPath;

public class StudyPathDAO extends ADAO {

	public StudyPath createStudyPath(final StudyPath studyPath) {
		return create(studyPath);
	}

	public StudyPath readStudyPath(final long id) {
		return read(StudyPath.class, id);
	}

	public StudyPath updateStudyPath(final StudyPath studyPath) {
		return update(studyPath);
	}

	public List<StudyPath> readAllStudyPath() {
		return readByJPQL("select t from StudyPath t");
	}

	public void deleteStudyPath(final StudyPath studyPath) {
		delete(studyPath);
	}
}
