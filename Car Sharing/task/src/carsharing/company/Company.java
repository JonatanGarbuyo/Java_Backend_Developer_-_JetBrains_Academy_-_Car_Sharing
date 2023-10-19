package carsharing.company;

import java.util.List;

import carsharing.car.Car;


public class Company {
    private int id;
    private String name;
    private List<Car> cars;

    public Company(String name){
        this.name = name;
    }

    public Company(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
