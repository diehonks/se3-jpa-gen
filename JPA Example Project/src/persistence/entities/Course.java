package persistence.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Course extends AEntity {

	private static final long serialVersionUID = 1L;
	private String name;
	@Embedded
	private Period coursePeriod;
	@ManyToOne
	private StudyPath studyPath;
	@OneToMany
	private final List<Lecturer> lecturers = new ArrayList<Lecturer>();
	@ManyToMany
	private final List<Student> students = new ArrayList<Student>();

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Period getCoursePeriod() {
		return coursePeriod;
	}

	public void setCoursePeriod(final Period coursePeriod) {
		this.coursePeriod = coursePeriod;
	}

	public StudyPath getStudyPath() {
		return studyPath;
	}

	public void setStudyPath(final StudyPath studyPath) {
		this.studyPath = studyPath;
	}

	public void addLecturer(final Lecturer lecturer) {
		lecturers.add(lecturer);
	}

	public void removeLecturer(final Lecturer lecturer) {
		lecturers.remove(lecturer);
	}

	public List<Lecturer> getLecturers() {
		return new ArrayList<Lecturer>(lecturers);
	}

	public void addStudent(final Student student) {
		students.add(student);
	}

	public void removeStudent(final Student student) {
		students.remove(students);

	}

	public List<Student> getStudents() {
		return new ArrayList<Student>(students);
	}

}
