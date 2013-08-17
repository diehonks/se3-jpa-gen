package persistence.entities;

import javax.persistence.Entity;

@Entity
public class Address extends AEntity {

	private static final long serialVersionUID = -1771753643844433361L;

	private String street;
	private String houseNumber;
	private String postalCode;

	public String getStreet() {
		return street;
	}

	public void setStreet(final String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(final String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}
}
