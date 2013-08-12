package persistence.entities;

import javax.persistence.Entity;

@Entity
public class Lecturer extends APerson {
	private static final long serialVersionUID = -7863253407347231537L;

	StudyTheme coreTheme;

	public StudyTheme getCoreTheme() {
		return coreTheme;
	}

	public void setCoreTheme(final StudyTheme coreTheme) {
		this.coreTheme = coreTheme;
	}
}
