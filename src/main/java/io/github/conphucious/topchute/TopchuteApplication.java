package io.github.conphucious.topchute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@EnableCaching
@SpringBootApplication
public class TopchuteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TopchuteApplication.class, args);
//        String jdbcUrl = "jdbc:h2:~/tato"; // Database file will be created here
//        String username = "sa"; // Default username
//        String password = ""; // Default password
//
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
//            System.out.println("H2 database file created and connected successfully.");
//            // You can now execute SQL statements using this connection
//        } catch (SQLException e) {
//            System.err.println("Error creating or connecting to H2 database: " + e.getMessage());
//        }
    }

}
