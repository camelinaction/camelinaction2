package camelinaction;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/**
 * Run this main to initialize and setup the tables in the database.
 */
public class InitDatabase {

    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/quartz";

        Class.forName("org.postgresql.Driver");
        Connection db = DriverManager.getConnection(url, "quartz", "quartz");

        System.out.println("Initializing database and creating tables");
        db.setAutoCommit(false);
        ScriptUtils.executeSqlScript(db, new FileSystemResource("tables_postgres.sql"));
        db.setAutoCommit(true);

        db.close();

        System.out.println("Database initialized");
    }

}
