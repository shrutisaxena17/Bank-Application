package com.example.BankApplication.dao;

import com.example.BankApplication.model.Address;
import com.example.BankApplication.model.Customer;
import com.example.BankApplication.model.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Customer> rowMapper = new RowMapper<>() {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("customerId"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setAge(rs.getInt("age"));
            customer.setGender(rs.getString("gender"));
            customer.setEmailAddress(rs.getString("email_address"));
            customer.setPanNumber(rs.getString("pan_number"));
            customer.setAadharCardNumber(rs.getString("aadhar_card_number"));
            customer.setCustomerUniqueId(rs.getString("customer_unique_id"));
            return customer;
        }
    };
    private static final String SELECT_CUSTOMER_BY_ACCOUNT_NUMBER="SELECT c.* FROM Customer c JOIN Account a ON c.customer_unique_id = a.customer_unique_id WHERE a.account_number = ?";



    private String generateCustomerUniqueId(Customer customer) {
        String firstPart = customer.getFirstName().substring(0, Math.min(3, customer.getFirstName().length())).toUpperCase();
        String secondPart = customer.getLastName().substring(0, Math.min(3, customer.getLastName().length())).toUpperCase();
        String panPart = customer.getPanNumber().substring(0, Math.min(5, customer.getPanNumber().length())).toUpperCase();
        return firstPart + secondPart + panPart;
    }

    public String create(Customer customer) {

        String uniqueId = generateCustomerUniqueId(customer);
        customer.setCustomerUniqueId(uniqueId);


        String sql = "INSERT INTO customer(first_name, last_name, age, gender, email_address, pan_number, aadhar_card_number, customer_unique_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


        jdbcTemplate.update(sql,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getAge(),
                customer.getGender(),
                customer.getEmailAddress(),
                customer.getPanNumber(),
                customer.getAadharCardNumber(),
                uniqueId
        );


        if (customer.getAddress() != null) {
            String sqlAddress = "INSERT INTO Address (street, city, state, pincode, country, customer_unique_id) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlAddress,
                    customer.getAddress().getStreet(),
                    customer.getAddress().getCity(),
                    customer.getAddress().getState(),
                    customer.getAddress().getPincode(),
                    customer.getAddress().getCountry(),
                    uniqueId
            );
        }

        if (customer.getPhone() != null) {
            String sqlPhone = "INSERT INTO Phone (number, type, customer_unique_id) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlPhone,
                    customer.getPhone().getNumber(),
                    customer.getPhone().getType(),
                    uniqueId
            );
        }
        return uniqueId;
    }

    public List<Customer> findAll() {
        String customerSql = "SELECT * FROM customer";
        String addressSql = "SELECT * FROM address WHERE customer_unique_id = ?";
        String phoneSql = "SELECT * FROM phone WHERE customer_unique_id = ?";

        List<Customer> customers = jdbcTemplate.query(customerSql, rowMapper);

        for (Customer customer : customers) {

            Address address = jdbcTemplate.queryForObject(addressSql, new Object[]{customer.getCustomerUniqueId()}, (rs, rowNum) -> {
                Address addr = new Address();
                addr.setId(rs.getInt("id"));
                addr.setStreet(rs.getString("street"));
                addr.setCity(rs.getString("city"));
                addr.setState(rs.getString("state"));
                addr.setPincode(rs.getString("pincode"));
                addr.setCountry(rs.getString("country"));
                addr.setCustomerUniqueId(customer.getCustomerUniqueId());
                return addr;
            });


            Phone phone = jdbcTemplate.queryForObject(phoneSql, new Object[]{customer.getCustomerUniqueId()}, (rs, rowNum) -> {
                Phone ph = new Phone();
                ph.setId(rs.getInt("id"));
                ph.setNumber(rs.getString("number"));
                ph.setType(rs.getString("type"));
                return ph;
            });

            customer.setAddress(address);
            customer.setPhone(phone);
        }

        return customers;
    }

    public Customer findByUniqueId(String uniqueId) {
        String customerSql = "SELECT * FROM customer WHERE customer_unique_id = ?";
        String addressSql = "SELECT * FROM address WHERE customer_unique_id = ?";
        String phoneSql = "SELECT * FROM phone WHERE customer_unique_id = ?";

        Customer customer = jdbcTemplate.queryForObject(customerSql, new Object[]{uniqueId}, rowMapper);


        Address address = jdbcTemplate.queryForObject(addressSql, new Object[]{uniqueId}, (rs, rowNum) -> {
            Address addr = new Address();
            addr.setId(rs.getInt("id"));
            addr.setStreet(rs.getString("street"));
            addr.setCity(rs.getString("city"));
            addr.setState(rs.getString("state"));
            addr.setPincode(rs.getString("pincode"));
            addr.setCountry(rs.getString("country"));
            addr.setCustomerUniqueId(uniqueId);
            return addr;
        });


        Phone phone = jdbcTemplate.queryForObject(phoneSql, new Object[]{uniqueId}, (rs, rowNum) -> {
            Phone ph = new Phone();
            ph.setId(rs.getInt("id"));
            ph.setNumber(rs.getString("number"));
            ph.setType(rs.getString("type"));
            return ph;
        });

        customer.setAddress(address);
        customer.setPhone(phone);

        return customer;
    }

    public int update(Customer customer) {
        String sql = "UPDATE customer SET first_name=?, last_name=?, age=?, gender=?, email_address=?, pan_number=?, aadhar_card_number=?, customer_unique_id=? WHERE customer_unique_id=?";
        return jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName(), customer.getAge(), customer.getGender(),
                customer.getEmailAddress(), customer.getPanNumber(), customer.getAadharCardNumber(), customer.getCustomerUniqueId(), customer.getCustomerUniqueId());
    }

    public int deleteByUniqueId(String uniqueId) {
        String sql = "DELETE FROM customer WHERE customer_unique_id=?";
        return jdbcTemplate.update(sql, uniqueId);
    }

    public Customer findCustomerAccountNumber(String accountNumber) {
        return jdbcTemplate.queryForObject(SELECT_CUSTOMER_BY_ACCOUNT_NUMBER, new Object[]{accountNumber},rowMapper);
    }
}
