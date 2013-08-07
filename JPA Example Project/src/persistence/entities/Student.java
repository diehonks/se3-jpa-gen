package persistence.entities;

import javax.persistence.Entity;

@Entity
public class Student extends APerson {
	private static final long serialVersionUID = -7863253407347231537L;
	
	private String matrikelNr;

	public String getMatrikelNr() {
		return matrikelNr;
	}

	public void setMatrikelNr(String matrikelNr) {
		this.matrikelNr = matrikelNr;
	}
}
