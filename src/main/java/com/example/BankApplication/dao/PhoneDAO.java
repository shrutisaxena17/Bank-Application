package com.example.BankApplication.dao;

import com.example.BankApplication.model.Phone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PhoneDAO {

    private final JdbcTemplate jdbcTemplate;

    public PhoneDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Phone> rowMapper = new RowMapper<Phone>() {
        @Override
        public Phone mapRow(ResultSet rs, int rowNum) throws SQLException {
            Phone phone = new Phone();
            phone.setId(rs.getInt("id"));
            phone.setNumber(rs.getString("number"));
            phone.setType(rs.getString("type"));
            phone.setCustomerUniqueId(rs.getString("customer_unique_id")); // Updated to match the model
            phone.setManagerId((Integer) rs.getObject("manager_id"));   // Nullable column
            return phone;
        }
    };

    private static final String INSERT_PHONE = "INSERT INTO Phone (number, type, customer_unique_id, manager_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_PHONES = "SELECT * FROM Phone";
    private static final String SELECT_PHONE_BY_ID = "SELECT * FROM Phone WHERE id = ?";
    private static final String UPDATE_PHONE = "UPDATE Phone SET number = ?, type = ?, customer_unique_id = ?, manager_id = ? WHERE id = ?";
    private static final String DELETE_PHONE = "DELETE FROM Phone WHERE id = ?";

    public int create(Phone phone) {
        return jdbcTemplate.update(INSERT_PHONE, phone.getNumber(), phone.getType(), phone.getCustomerUniqueId(), phone.getManagerId());
    }

    public List<Phone> findAll() {
        return jdbcTemplate.query(SELECT_ALL_PHONES, rowMapper);
    }

    public Phone findById(int id) {
        return jdbcTemplate.queryForObject(SELECT_PHONE_BY_ID, rowMapper, id);
    }

    public int update(Phone phone) {
        return jdbcTemplate.update(UPDATE_PHONE, phone.getNumber(), phone.getType(), phone.getCustomerUniqueId(), phone.getManagerId(), phone.getId());
    }

    public int delete(int id) {
        return jdbcTemplate.update(DELETE_PHONE, id);
    }
}
