package com.example.BankApplication.service;

import com.example.BankApplication.dao.PhoneDAO;
import com.example.BankApplication.exception.NotFoundException;
import com.example.BankApplication.model.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

@Service
public class PhoneService {

    private final PhoneDAO phoneDAO;

    @Autowired
    public PhoneService(PhoneDAO phoneDAO) {
        this.phoneDAO = phoneDAO;
    }

    public Phone createPhone(Phone phone) {
        phoneDAO.create(phone);
        return phone;
    }

    public List<Phone> getAllPhones() {
        return phoneDAO.findAll();
    }

    public Phone getPhoneById(int id) {
        try {
            Phone phone = phoneDAO.findById(id);
            if (phone == null) {
                throw new NotFoundException("Phone does not exist with ID: " + id);
            }
            return phone;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Phone does not exist with ID: " + id);
        }
    }

    public Phone updatePhone(Phone phone) {
        int updatedRows = phoneDAO.update(phone);
        if (updatedRows > 0) {
            return phone;
        } else {
            throw new NotFoundException("Failed to update phone with ID: " + phone.getId());
        }
    }

    public void deletePhone(int id) {
        try {
            int deletedRows = phoneDAO.delete(id);
            if (deletedRows == 0) {
                throw new NotFoundException("Failed to delete phone with ID: " + id);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Phone does not exist with ID: " + id);
        }
    }
}
