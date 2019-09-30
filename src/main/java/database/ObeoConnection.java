package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ObeoConnection {
    private static Connection c = initialiseConnection();

    public static Connection getInstance() {
        return c;
    }

    private static Connection initialiseConnection() {
        Connection c = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://awsdatabase.cvf0jytnrndh.eu-west-2.rds.amazonaws.com:5432/obeoDatabase",
                            "postgres", "obeoisnotoreo");

            c.setAutoCommit(false);
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return c;
    }

    public static void closeConnection() throws Exception {
        c.close();
    }

}
