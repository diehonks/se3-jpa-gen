package persistence.daos;

import java.util.List;

import persistence.entities.Address;

public class AddressDAO extends ADAO {
	
	public Address createAddress(Address address){
		return create(address);
	}
	
	public Address readAddress(long id){
		return read(Address.class, id);
	}
	
	public Address updateAddress(Address address){
		return update(address);
	}
	
	public List<Address> readAllAddresss(){
		return readByJPQL("select t from Address t");
	}
	
	public void deleteAddress(Address address){
		delete(address);
	}

}
