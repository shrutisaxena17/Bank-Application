package com.example.BankApplication.dao;

import com.example.BankApplication.model.Address;
import com.example.BankApplication.model.Manager;
import com.example.BankApplication.model.Phone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ManagerDAO {

    private final JdbcTemplate jdbcTemplate;

    public ManagerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Manager> rowMapper = new RowMapper<Manager>() {
        @Override
        public Manager mapRow(ResultSet rs, int rowNum) throws SQLException {
            Manager manager = new Manager();
            manager.setId(rs.getInt("id"));
            manager.setFirstName(rs.getString("first_name"));
            manager.setLastName(rs.getString("last_name"));
            manager.setAge((Integer) rs.getObject("age"));
            manager.setEmailAddress(rs.getString("email_address"));
            return manager;
        }
    };

    private static final String INSERT_MANAGER = "INSERT INTO Manager (first_name, last_name, age, email_address) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_MANAGERS = "SELECT * FROM Manager";
    private static final String SELECT_MANAGER_BY_ID = "SELECT * FROM Manager WHERE id = ?";
    private static final String UPDATE_MANAGER = "UPDATE Manager SET first_name = ?, last_name = ?, age = ?, email_address = ? WHERE id = ?";
    private static final String DELETE_MANAGER = "DELETE FROM Manager WHERE id = ?";

    public void create(Manager manager) {
        KeyHolder keyHolder = new GeneratedKeyHolder();


        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_MANAGER, new String[]{"id"});
            ps.setString(1, manager.getFirstName());
            ps.setString(2, manager.getLastName());
            ps.setObject(3, manager.getAge(), java.sql.Types.INTEGER);
            ps.setString(4, manager.getEmailAddress());
            return ps;
        }, keyHolder);

        int managerId = keyHolder.getKey().intValue();


        if (manager.getAddress() != null) {
            String addressSql = "INSERT INTO Address (street, city, state, pincode, country, manager_id) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(addressSql,
                    manager.getAddress().getStreet(),
                    manager.getAddress().getCity(),
                    manager.getAddress().getState(),
                    manager.getAddress().getPincode(),
                    manager.getAddress().getCountry(),
                    managerId
            );
        }


        if (manager.getPhone() != null) {
            String phoneSql = "INSERT INTO Phone (number, type, manager_id) VALUES (?, ?, ?)";
            jdbcTemplate.update(phoneSql,
                    manager.getPhone().getNumber(),
                    manager.getPhone().getType(),
                    managerId
            );
        }
    }

    public List<Manager> findAll() {
        List<Manager> managers = jdbcTemplate.query(SELECT_ALL_MANAGERS, rowMapper);


        for (Manager manager : managers) {
            fetchAssociatedData(manager);
        }

        return managers;
    }

    public Manager findById(int id) {
        Manager manager = jdbcTemplate.queryForObject(SELECT_MANAGER_BY_ID, rowMapper, id);
        fetchAssociatedData(manager);
        return manager;
    }

    private void fetchAssociatedData(Manager manager) {
        String addressSql = "SELECT * FROM Address WHERE manager_id = ?";
        String phoneSql = "SELECT * FROM Phone WHERE manager_id = ?";


        try {
            Address address = jdbcTemplate.queryForObject(addressSql, new Object[]{manager.getId()}, (rs, rowNum) -> {
                Address addr = new Address();
                addr.setId(rs.getInt("id"));
                addr.setStreet(rs.getString("street"));
                addr.setCity(rs.getString("city"));
                addr.setState(rs.getString("state"));
                addr.setPincode(rs.getString("pincode"));
                addr.setCountry(rs.getString("country"));
                addr.setManagerId(rs.getInt("manager_id"));
                return addr;
            });
            manager.setAddress(address);
        } catch (Exception e) {
            manager.setAddress(null);
        }


        try {
            Phone phone = jdbcTemplate.queryForObject(phoneSql, new Object[]{manager.getId()}, (rs, rowNum) -> {
                Phone ph = new Phone();
                ph.setId(rs.getInt("id"));
                ph.setNumber(rs.getString("number"));
                ph.setType(rs.getString("type"));
                ph.setManagerId(rs.getInt("manager_id"));
                return ph;
            });
            manager.setPhone(phone);
        } catch (Exception e) {
            manager.setPhone(null);
        }
    }

    public int update(Manager manager) {
        return jdbcTemplate.update(UPDATE_MANAGER, manager.getFirstName(), manager.getLastName(), manager.getAge(), manager.getEmailAddress(), manager.getId());
    }

    public int delete(int id) {
        return jdbcTemplate.update(DELETE_MANAGER, id);
    }
}
