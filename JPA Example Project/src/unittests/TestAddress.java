package unittests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import persistence.daos.AddressDAO;
import persistence.entities.Address;

public class TestAddress {

	private AddressDAO addressDAO = null;
	private long addressID = 0;

	@BeforeClass
	public void initTest() {
		addressDAO = new AddressDAO();
	}

	@Test
	public void createAddress() {
		final Address address = new Address();
		address.setStreet("Teststreet");
		address.setHouseNumber("12b");
		address.setPostalCode("25686");
		addressID = address.getId();
		Assert.assertEquals(addressID, 0);
		addressID = addressDAO.createAddress(address).getId();
		Assert.assertEquals((addressID > 0), true);
		Assert.assertEquals(addressID != addressDAO
				.createAddress(new Address()).getId(), true);
	}

	@Test(dependsOnMethods = "createAddress")
	public void readAddress() {
		final Address address = addressDAO.readAddress(addressID);
		Assert.assertEquals(address.getStreet(), "Teststreet");
		Assert.assertEquals(address.getHouseNumber(), "12b");
		Assert.assertEquals(address.getPostalCode(), "25686");
		int count = addressDAO.readAllAddresss().size();
		Assert.assertEquals(count, 2);
		addressDAO.createAddress(new Address());
		count = addressDAO.readAllAddresss().size();
		Assert.assertEquals(count, 3);
	}

	@Test(dependsOnMethods = "createAddress")
	public void updateAddress() {
		final Address address = addressDAO.readAddress(addressID);
		// Assert.assertEquals(address.getStreet(), "Teststreet");
		address.setStreet("Musterstrasse");
		Assert.assertNotEquals(address.getStreet(), "Teststreet");
		// Assert.assertEquals(address.getHouseNumber(), "12b");
		address.setHouseNumber("99");
		Assert.assertNotEquals(address.getHouseNumber(), "12b");
		// Assert.assertEquals(address.getPostalCode(), "25686");
		address.setPostalCode("98765");
		Assert.assertNotEquals(address.getPostalCode(), "25686");
		addressDAO.updateAddress(address);
		final Address sameAddressObj = addressDAO.readAddress(addressID);
		Assert.assertEquals(sameAddressObj.getStreet(), "Musterstrasse");
		Assert.assertEquals(sameAddressObj.getHouseNumber(), "99");
		Assert.assertEquals(sameAddressObj.getPostalCode(), "98765");
	}

	@Test(dependsOnMethods = "updateAddress")
	public void deleteAddress() {
		Address address = addressDAO.readAddress(addressID);
		int count = addressDAO.readAllAddresss().size();
		Assert.assertEquals(count, 3);
		addressDAO.deleteAddress(address);
		count = addressDAO.readAllAddresss().size();
		Assert.assertEquals(count, 2);
		address = addressDAO.readAddress(addressID);
		Assert.assertNull(address);
	}
}
