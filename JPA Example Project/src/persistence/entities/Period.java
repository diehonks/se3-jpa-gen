package persistence.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Period {

	private java.sql.Date startDate;
	private java.sql.Date endDate;

	public java.sql.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(final java.sql.Date startDate) {
		this.startDate = startDate;
	}

	public java.sql.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(final java.sql.Date endDate) {
		this.endDate = endDate;
	}
}
