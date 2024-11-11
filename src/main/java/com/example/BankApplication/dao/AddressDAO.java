package com.example.BankApplication.dao;

import com.example.BankApplication.model.Address;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AddressDAO {

    private final JdbcTemplate jdbcTemplate;

    public AddressDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private final RowMapper<Address> rowMapper = new RowMapper<Address>() {
        @Override
        public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
            Address address = new Address();
            address.setId(rs.getInt("id"));
            address.setStreet(rs.getString("street"));
            address.setCity(rs.getString("city"));
            address.setState(rs.getString("state"));
            address.setPincode(rs.getString("pincode"));
            address.setCountry(rs.getString("country"));
            address.setCustomerUniqueId(rs.getString("customer_unique_id"));
            address.setManagerId((Integer) rs.getObject("manager_id"));
            return address;
        }
    };


    private static final String INSERT_ADDRESS = "INSERT INTO Address (street, city, state, pincode, country, customer_unique_id, manager_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_ADDRESSES = "SELECT * FROM Address";
    private static final String SELECT_ADDRESS_BY_ID = "SELECT * FROM Address WHERE id = ?";
    private static final String UPDATE_ADDRESS = "UPDATE Address SET street = ?, city = ?, state = ?, pincode = ?, country = ?, customer_unique_id = ?, manager_id = ? WHERE id = ?";
    private static final String DELETE_ADDRESS = "DELETE FROM Address WHERE id = ?";

    public int create(Address address) {
        return jdbcTemplate.update(INSERT_ADDRESS, address.getStreet(), address.getCity(), address.getState(),
                address.getPincode(), address.getCountry(), address.getCustomerUniqueId(), address.getManagerId());
    }

    public List<Address> findAll() {
        return jdbcTemplate.query(SELECT_ALL_ADDRESSES, rowMapper);
    }

    public Address findById(int id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ADDRESS_BY_ID, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int update(Address address) {
        return jdbcTemplate.update(UPDATE_ADDRESS, address.getStreet(), address.getCity(), address.getState(),
                address.getPincode(), address.getCountry(), address.getCustomerUniqueId(), address.getManagerId(), address.getId());
    }

    public int delete(int id) {
        return jdbcTemplate.update(DELETE_ADDRESS, id);
    }
}
