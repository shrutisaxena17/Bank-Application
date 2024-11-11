package com.example.BankApplication.service;

import com.example.BankApplication.dao.ManagerDAO;
import com.example.BankApplication.exception.NotFoundException;
import com.example.BankApplication.model.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

@Service
public class ManagerService {

    private final ManagerDAO managerDAO;

    @Autowired
    public ManagerService(ManagerDAO managerDAO) {
        this.managerDAO = managerDAO;
    }

    public void createManager(Manager manager) {
        managerDAO.create(manager);
    }

    public List<Manager> getAllManagers() {
        return managerDAO.findAll();
    }

    public Manager getManagerById(int id) {
        try {
            Manager manager = managerDAO.findById(id);
            if (manager == null) {
                throw new NotFoundException("Manager does not exist with ID: " + id);
            }
            return manager;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Manager does not exist with ID: " + id);
        }
    }

    public Manager updateManager(Manager manager) {
        int updatedRows = managerDAO.update(manager);
        if (updatedRows > 0) {
            return manager;
        } else {
            throw new NotFoundException("Failed to update manager with ID: " + manager.getId());
        }
    }

    public void deleteManager(int id) {
        try {
            int deletedRows = managerDAO.delete(id);
            if (deletedRows == 0) {
                throw new NotFoundException("Failed to delete manager with ID: " + id);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Manager does not exist with ID: " + id);
        }
    }
}
