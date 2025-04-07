package org.csu.demo.persistence;

import org.csu.demo.domain.Address;

import java.util.List;

public interface AddressDao {
    public List<Address> getAllAddressById(int user_id);
    public void addAddress(Address address);
    public void deleteAddress(int id);
    public void updateAddress(Address address);
    public void updateDefaultAddress(int userId);
    public Address getAddressById(int id);
}
