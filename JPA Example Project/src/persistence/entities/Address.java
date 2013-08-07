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
	public void setStreet(String street) {
		this.street = street;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	} 
}
