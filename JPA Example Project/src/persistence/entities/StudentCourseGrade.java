package persistence.entities;

import javax.persistence.Entity;

@Entity
public class StudentCourseGrade extends AEntity {

	private static final long serialVersionUID = 5203842557843569360L;

	private Student student;
	private Course course;
	private float grade;

	public Student getStudent() {
		return student;
	}

	public void setStudent(final Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(final Course course) {
		this.course = course;
	}

	public float getGrade() {
		return grade;
	}

	public void setGrade(final float grade) {
		this.grade = grade;
	}
}
