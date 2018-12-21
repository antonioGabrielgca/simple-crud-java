/*
 * Class responsible for opening and closing connection
 */
package connection;

/* Import require packages */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabriel
 */
public class ConnectionFactory {

    /* JDBC driver name and database url */
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/name_db";
    
    /* database credentials */
<<<<<<< HEAD
    private static final String USER = "username";
    private static final String PASS = "password";
=======
    private static final String USER = "user_db";
    private static final String PASS = "password_db";
>>>>>>> f6af90d24b526c8ab4ad5a7063608362a4cd17d6

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);     /* register JDBC driver */
            return DriverManager.getConnection(URL, USER, PASS);  /* open a connection */
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException("** Connection Error **", ex);
        }
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();   /* Clean up environment */
            }
        } catch (SQLException ex) {  /* Handle errors for JDBC */
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void closeConnection(Connection conn, PreparedStatement stmt) {
        closeConnection(conn);

        try {
            if (stmt != null) {
                stmt.close();  /* Clean up Environment */
            }
        } catch (SQLException ex) { /* Handle errors for JDBC */
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {
        closeConnection(conn, stmt);

        try {
            if (rs != null) {
                rs.close();  /* Clean up environment */
            }
        } catch (SQLException ex) {  /* Handle errors for JDBC */
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
