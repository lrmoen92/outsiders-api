package slogan.motion.outsidersapi.configuration;

import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class PostgresConfig {
	
//	PostgresConfig() {
//	    Connection c = null;
//	    try {
//	       Class.forName("org.postgresql.Driver");
//	       c = DriverManager
//	          .getConnection("jdbc:postgresql://localhost:5432/postgres",
//	          "postgres", "outsiders");
//	       c.setSchema("outsiders");
//	    } catch (Exception e) {
//	       e.printStackTrace();
//	       System.err.println(e.getClass().getName()+": "+e.getMessage());
//	       System.exit(0);
//	    }
//	    System.out.println("Opened database successfully");
//	}


}
