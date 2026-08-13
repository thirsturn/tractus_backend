import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class FixDb {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://ep-rough-haze-atqmslkp-pooler.c-9.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require";
        String user = "neondb_owner";
        String pass = "npg_f59NnhjalGQH";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            
            String sql = "ALTER TABLE users ADD COLUMN role VARCHAR(255) DEFAULT 'ROLE_USER';";
            stmt.executeUpdate(sql);
            System.out.println("Successfully added 'role' column to 'users' table.");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
