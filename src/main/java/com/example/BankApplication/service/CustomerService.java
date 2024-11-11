package com.example.BankApplication.service;

import com.example.BankApplication.dao.CustomerDAO;
import com.example.BankApplication.exception.NotFoundException;
import com.example.BankApplication.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Autowired
    private OtpService otpService;


    public String registerCustomer(Customer customer) {
        String registrationId = UUID.randomUUID().toString();
        String otp = otpService.generateOtp(registrationId);
        otpService.sendOtpByEmail(customer.getEmailAddress(), otp);
        otpService.storeCustomerData(registrationId, customer);
        return registrationId;
    }


    public String verifyOtpAndCreateCustomer(String registrationId, String otp) {
        if (otpService.isOtpValid(registrationId, otp)) {
   Customer customer = otpService.getStoredCustomerData(registrationId);
            if (customer != null) {
                return customerDAO.create(customer);
            }
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    public Customer getCustomerByUniqueId(String uniqueId) {
        try {
            Customer customer = customerDAO.findByUniqueId(uniqueId);
            if (customer == null) {
                throw new NotFoundException("Customer does not exist with Unique ID: " + uniqueId);
            }
            return customer;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Customer does not exist with Unique ID: " + uniqueId);
        }
    }

    public Customer updateCustomer(Customer customer) {
        Customer existingCustomer = getCustomerByUniqueId(customer.getCustomerUniqueId());
        customerDAO.update(existingCustomer);
        return existingCustomer;
    }

    public void deleteCustomer(String uniqueId) {
        try {
            customerDAO.deleteByUniqueId(uniqueId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Customer does not exist with Unique ID: " + uniqueId);
        }
    }

    public Customer findCustomerByAccountId(String accountId) {
        return  customerDAO.findCustomerAccountNumber(accountId);
    }
}
