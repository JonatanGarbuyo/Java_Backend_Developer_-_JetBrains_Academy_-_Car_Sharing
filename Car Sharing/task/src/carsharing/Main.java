package carsharing;

import carsharing.App.App;
import carsharing.db.Database;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        String databaseFileName = "carsharing";

        if (args.length >0 && args[0].startsWith("-databaseFileName")) {
            var parts = args[0].split(" ");
            if (parts.length == 2) {
                databaseFileName = parts[1];
            }
        }

        try {
            Database database = new Database(databaseFileName);
            Connection connection = database.getConnection();
            App app = new App(connection);
            app.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}