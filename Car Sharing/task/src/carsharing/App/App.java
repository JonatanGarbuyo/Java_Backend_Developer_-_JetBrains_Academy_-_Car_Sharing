package carsharing.App;

import carsharing.car.Car;
import carsharing.car.CarDao;
import carsharing.company.Company;
import carsharing.company.CompanyDao;
import carsharing.customer.Customer;
import carsharing.customer.CustomerDao;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final String ENTER_NAME = "\nEnter the %s name:\n";
    private static final String INVALID_INPUT = "Invalid choice. Please try again.";
    private static final String INVALID_NAME = "Name cannot be empty. Please try again.";
    private static final String LIST_EMPTY = "\nThe %s list is empty!\n";
    private static final String MENU_BACK = "0. Back";
    private static final String YOU_DID_NOT_RENT = "\nYou didn't rent a car!\n";

    private static final String LOGIN_MESSAGE = """
            1. Log in as a manager
            2. Log in as a customer
            3. Create a customer
            0. Exit
            """;
    private static final String MANAGER_MENU = """
            1. Company list
            2. Create a company
            0. Back
            """;
    private static final String COMPANY_MENU = """
            1. Car list
            2. Create a car
            0. Back
            """;
    private static final String CUSTOMER_MENU = """
            1. Rent a car
            2. Return a rented car
            3. My rented car
            0. Back
            """;
    private static final String YOUR_RENTED_CAR = """
            Your rented car:
            %s
            Company:
            %s
            """;

    private final Scanner scanner = new Scanner(System.in);
    private final CompanyDao companyDao;
    private final CarDao carDao;
    private final CustomerDao customerDao;

    public App(Connection connection) {
        this.carDao = new CarDao(connection);
        this.companyDao = new CompanyDao(connection);
        this.customerDao = new CustomerDao(connection);
        this.companyDao.createTable();
        this.carDao.createTable();
        this.customerDao.createTable();
    }

    public void run() {
        while (true) {
            int choice = getIntInput(LOGIN_MESSAGE);
            switch (choice) {
                case 1:
                    System.out.println();
                    managerMenu();
                    break;
                case 2:
                    customerMenu();
                    break;
                case 3:
                    createCustomer();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println(INVALID_INPUT);
            }
        }
    }

    private void createCustomer() {
        System.out.printf(ENTER_NAME, "customer");
        String customerName = scanner.nextLine();

        if (customerName != null && !customerName.isEmpty()) {
            Customer newCustomer = new Customer(customerName);
            customerDao.add(newCustomer);
            System.out.println("The customer was added!\n");
        } else {
            System.out.println();
            System.out.println(INVALID_NAME);
        }
    }

    private void customerMenu() {
        Customer customer = selectCustomer();
        if (customer == null) {
            return;
        }
        System.out.println();

        while (true) {
            int choice = getIntInput(CUSTOMER_MENU);

            switch (choice) {
                case 1:
                    rentACar(customer);
                    break;
                case 2:
                    returnRentedCar(customer);
                    break;
                case 3:
                    printRentedCar(customer);
                    break;
                case 0:
                    System.out.println();
                    return;
                default:
                    System.out.println(INVALID_INPUT);
            }
        }
    }

    private void printRentedCar(Customer customer) {
        if (customer.getRentedCarId() == null) {
            System.out.println(YOU_DID_NOT_RENT);
            return;
        }
        Car car = carDao.findById(customer.getRentedCarId());
        Company company = companyDao.findById(car.getCompanyId());
        System.out.println();
        System.out.printf(YOUR_RENTED_CAR, car.getName(), company.getName());
        System.out.println();
    }

    private void returnRentedCar(Customer customer) {
        Integer rentedCarId = customerDao.findById(customer.getId()).getRentedCarId();
        if (rentedCarId == null) {
            System.out.println(YOU_DID_NOT_RENT);
        } else {
            customer.setRentedCarId(null);
            customerDao.update(customer);
            System.out.println("\nYou've returned a rented car!\n");
        }
    }

    private void rentACar(Customer customer) {
        Integer rentedCarId = customerDao.findById(customer.getId()).getRentedCarId();

        if (rentedCarId != null && rentedCarId >= 0) {
            System.out.println("\nYou've already rented a car!\n");
            return;
        }

        Company company = selectCompany();
        if (company == null) {
            return;
        }

        List<Car> carList = carDao.findByCompanyId(company.getId());

        if (carList.isEmpty()) {
            System.out.printf("\nNo available cars in the '%s' company\n", company.getName());
        } else {
            System.out.println("\nChoose a car:");
            printItemList(carList);
            System.out.println(MENU_BACK);

            int carIndex = getIntInput("");
            if (carIndex > 0) {
                Car car = carList.get(carIndex - 1);
                customer.setRentedCarId(car.getId());
                customerDao.update(customer);
                System.out.printf("\nYou rented '%s'\n", car.getName());
                System.out.println();
            }
        }
    }

    private Customer selectCustomer() {
        List<Customer> customerList = customerDao.findAll();

        if (customerList.isEmpty()) {
            System.out.printf(LIST_EMPTY, "customer");
            System.out.println();
        } else {
            System.out.println("\nCustomer list:");
            printItemList(customerList);
            System.out.println(MENU_BACK);

            int companyIndex = getIntInput("");
            if (companyIndex > 0) {
                return customerList.get(companyIndex - 1);
            }
        }
        return null;
    }

    public void managerMenu() {
        while (true) {
            int choice = getIntInput(MANAGER_MENU);

            switch (choice) {
                case 1:
                   Company company = selectCompany();
                   System.out.println();
                   if (company != null) {
                       printCompanyMenu(company);
                   }
                    break;
                case 2:
                    createCompany();
                    break;
                case 0:
                    System.out.println();
                    return;
                default:
                    System.out.println(INVALID_INPUT);
            }
        }
    }

    public Company selectCompany() {
        List<Company> companies = companyDao.findAll();

        if (companies.isEmpty()) {
            System.out.printf(LIST_EMPTY, "company");
        } else {
            System.out.println("\nChoose the company:");
            printItemList(companies);
            System.out.println(MENU_BACK);

            int companyIndex = getIntInput("");
            if (companyIndex > 0) {
                return companies.get(companyIndex - 1);
            }
        }
        return null;
    }

    private void printCompanyMenu(Company company) {
        System.out.printf("'%s' company\n", company);

        while (true) {
            int choice = getIntInput(COMPANY_MENU);

            switch (choice) {
                case 1:
                    printCarList(company.getId());
                    System.out.println();
                    break;
                case 2:
                    createCar(company.getId());
                    break;
                case 0:
                    System.out.println();
                    return;
                default:
                    System.out.println(INVALID_INPUT);
            }
        }
    }

    public void createCompany() {
        System.out.printf(ENTER_NAME, "company");
        String companyName = scanner.nextLine();

        if (companyName != null && !companyName.isEmpty()) {
            Company newCompany = new Company(companyName);
            this.companyDao.add(newCompany);
            System.out.println("The company was created!\n");
        } else {
            System.out.println();
            System.out.println(INVALID_NAME);
        }
    }

    private void printCarList(int id) {
        List<Car> carList = carDao.findByCompanyId(id);

        if (carList == null || carList.isEmpty()) {
            System.out.printf(LIST_EMPTY, "car");
        } else {
            System.out.println("\nCar list:");
            printItemList(carList);
        }
    }

    private void createCar(int companyId) {
        System.out.printf(ENTER_NAME, "car");
        String carName = scanner.nextLine();

        if (carName != null && !carName.isEmpty()) {
            Car newCar = new Car(carName, companyId);
            this.carDao.add(newCar);
            System.out.println("The car was added!\n");
        } else {
            System.out.println();
            System.out.println(INVALID_NAME);
        }
    }

    public <T> void printItemList(List<T> items) {
        for (int index = 0; index < items.size(); index++) {
            T item = items.get(index);
            System.out.println((index + 1) + ". " + item);
        }
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.println(INVALID_INPUT);
            System.out.print(prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}

