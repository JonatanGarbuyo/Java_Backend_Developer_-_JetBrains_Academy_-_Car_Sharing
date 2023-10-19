package carsharing.customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import carsharing.db.Dao;

public class CustomerDao implements Dao<Customer> {
    private static final String CREATE_TABLE =  "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "name VARCHAR (255) UNIQUE NOT NULL," +
            "rented_car_id INT," +
            "FOREIGN KEY (rented_car_id) REFERENCES CAR(id));";
    private static final String SELECT = "SELECT * FROM CUSTOMER WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM CUSTOMER";
    private static final String INSERT = "INSERT INTO CUSTOMER (name) VALUES (?)";
    private static final String UPDATE = "UPDATE CUSTOMER SET name = ?, rented_car_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM CUSTOMER WHERE id = ?";
    private final Connection connection;

    public CustomerDao(Connection connection) {
        this.connection = connection;
    }

    public void createTable(){
        try (Statement statement = connection.createStatement()){
            statement.execute(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, customer.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Customer mapper(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        Integer rentedCarId = resultSet.getInt("rented_car_id");

        if (resultSet.wasNull()) {
            rentedCarId = null;
        }

        return new Customer(id, name, rentedCarId);
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customerList = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSetItems = statement.executeQuery(SELECT_ALL)){
            while (resultSetItems.next()) {
                Customer customer = mapper(resultSetItems);
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  customerList;
    }

    @Override
    public Customer findById(int id) {
        Customer customer = null;

        try (PreparedStatement statement = connection.prepareStatement(SELECT)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                customer = mapper(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  customer;
    }

    @Override
    public void update(Customer customer) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, customer.getName());

            if (customer.getRentedCarId() != null) {
                statement.setInt(2, customer.getRentedCarId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }

            statement.setInt(3, customer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE)){
            statement.setInt(1, id);
            statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
