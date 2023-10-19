package carsharing.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import carsharing.db.Dao;

public class CompanyDao implements Dao<Company> {
    private static final String CREATE_TABLE =  "CREATE TABLE IF NOT EXISTS COMPANY (" +
                                                "id INT AUTO_INCREMENT PRIMARY KEY," +
                                                "name VARCHAR (255) UNIQUE NOT NULL );";
    private static final String SELECT = "SELECT * FROM COMPANY WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM COMPANY";
    private static final String INSERT = "INSERT INTO COMPANY (name) VALUES (?)";
    private static final String UPDATE =  "UPDATE COMPANY SET name = '?' WHERE id = ?";
    private static final String DELETE = "DELETE FROM COMPANY WHERE id = ?";
    private final Connection connection;

    public CompanyDao(Connection connection) {
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
    public void add(Company company) {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, company.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Company mapper(ResultSet resultSet) throws SQLException {
        return new Company(resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }

    @Override
    public List<Company> findAll() {
        List<Company> CompanyList = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSetItems = statement.executeQuery(SELECT_ALL)){
            while (resultSetItems.next()) {
                Company company = mapper(resultSetItems);
                CompanyList.add(company);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  CompanyList;
    }

    @Override
    public Company findById(int id) {
        Company company = null;

        try (PreparedStatement statement = connection.prepareStatement(SELECT)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                company = mapper(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  company;
    }


    @Override
    public void update(Company company) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, company.getName());
            statement.setInt(2, company.getId());
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
