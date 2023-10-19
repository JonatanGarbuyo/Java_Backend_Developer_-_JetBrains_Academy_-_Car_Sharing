package carsharing.customer;

public class Customer {
    private int id;
    private String name;
    private Integer rentedCarId = null;

    public Customer(String name){
        this.name = name;
    }
    public Customer(int id, String name, Integer rentedCarId){
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
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

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer id) {
        this.rentedCarId = id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

