package persistence.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Student extends APerson {
	private static final long serialVersionUID = -7863253407347231537L;

	private String matrikelNr;
	@ManyToMany
	private final List<Course> courses = new ArrayList<Course>();

	public String getMatrikelNr() {
		return matrikelNr;
	}

	public void setMatrikelNr(final String matrikelNr) {
		this.matrikelNr = matrikelNr;
	}

	public void addCourse(final Course course) {
		courses.add(course);
	}

	public void removeCourse(final Course course) {
		courses.remove(course);
	}

	public List<Course> getCourses() {
		return new ArrayList<Course>(courses);
	}
}
