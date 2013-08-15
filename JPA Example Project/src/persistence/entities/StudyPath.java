package persistence.entities;

import javax.persistence.Entity;

@Entity
public class StudyPath extends AEntity {

	private static final long serialVersionUID = 2150735703678120605L;
	private StudyTheme theme;
	private String name;

	public StudyTheme getTheme() {
		return theme;
	}

	public void setTheme(final StudyTheme theme) {
		this.theme = theme;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
