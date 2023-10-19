package carsharing.car;

public class Car {
    private int id;
    private String name;
    private int companyId;

    public Car(String name, int companyId){
        this.name = name;
        this.companyId = companyId;
    }

    public Car(int id, String name, int companyId){
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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

    @Override
    public String toString() {
        return this.name;
    }
}

