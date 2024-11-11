package com.example.BankApplication.service;

import com.example.BankApplication.dao.AddressDAO;
import com.example.BankApplication.exception.NotFoundException;
import com.example.BankApplication.model.Address;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressService {

    private final AddressDAO addressDAO;

    public AddressService(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public Address createAddress(Address address) {
        addressDAO.create(address);
        return address;
    }

    public List<Address> getAllAddresses() {
        return addressDAO.findAll();
    }

    public Address getAddressById(int addressId) {
        try {
            return addressDAO.findById(addressId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Address does not exist with Address Id: " + addressId);
        }
    }

    public Address updateAddress(Address address) {
        try {
            addressDAO.update(address);
            return address;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Address does not exist with Address Id: " + address.getId());
        }
    }

    public void deleteAddress(int addressId) {
        try {
            Address address = addressDAO.findById(addressId);
            if (address == null) {
                throw new NotFoundException("Address does not exist with Address Id: " + addressId);
            }
            addressDAO.delete(addressId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Address does not exist with Address Id: " + addressId);
        }
    }
}
