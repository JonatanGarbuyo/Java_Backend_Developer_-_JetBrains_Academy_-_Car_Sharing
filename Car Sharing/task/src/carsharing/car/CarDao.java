package carsharing.car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import carsharing.db.Dao;

public class CarDao implements Dao<Car> {
    private static final String CREATE_TABLE =  "CREATE TABLE IF NOT EXISTS CAR (" +
                                                "id INT AUTO_INCREMENT PRIMARY KEY," +
                                                "name VARCHAR (255) UNIQUE  NOT NULL," +
                                                "company_id INT NOT NULL," +
                                                "FOREIGN KEY (company_id) REFERENCES COMPANY(id));";
    private static final String SELECT = "SELECT * FROM CAR WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM CAR";
    private static final String SELECT_CARS_BY_COMPANY_ID = "SELECT * FROM CAR WHERE company_id = ?";
    private static final String INSERT = "INSERT INTO CAR (name, company_id) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE CAR SET name = ?, companyId = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM CAR WHERE id = ?";
    private static final String SELECT_RENTED_CAR_ID = "SELECT rented_car_id FROM CUSTOMER WHERE rented_car_id = ?";
    private final Connection connection;

    public CarDao(Connection connection) {
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
    public void add(Car car) {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, car.getName());
            statement.setInt(2, car.getCompanyId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Car mapper(ResultSet resultSet) throws SQLException {
        return new Car(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("company_id")
            );
    }

    @Override
    public List<Car> findAll() {
        List<Car> carList = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSetItems = statement.executeQuery(SELECT_ALL)){
            while (resultSetItems.next()) {
                Car car = mapper(resultSetItems);
                carList.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  carList;
    }

    @Override
    public Car findById(int id) {
        Car car = null;

        try (PreparedStatement statement = connection.prepareStatement(SELECT)){
             statement.setInt(1, id);
             ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                car = mapper(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  car;
    }

    public List<Car> findByCompanyId(int companyId) {
        List<Car> carList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_CARS_BY_COMPANY_ID)) {
            statement.setInt(1, companyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Car car = mapper(resultSet);
                    if (isCarAvailable(car.getId())) {
                        carList.add(car);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carList;
    }

    private boolean isCarAvailable(int carId) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_RENTED_CAR_ID)) {
            statement.setInt(1, carId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return !resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void update(Car car) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, car.getName());
            statement.setInt(2, car.getCompanyId());
            statement.setInt(3, car.getId());
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
