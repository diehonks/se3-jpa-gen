package persistence.entities;

import javax.persistence.Entity;

@Entity
public class Lecturer extends APerson {
	private static final long serialVersionUID = -7863253407347231537L;
	
	CoreTheme coreTheme;

	public CoreTheme getCoreTheme() {
		return coreTheme;
	}

	public void setCoreTheme(CoreTheme coreTheme) {
		this.coreTheme = coreTheme;
	}
}
