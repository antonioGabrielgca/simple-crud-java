/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import connection.ConnectionFactory;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.bean.User;

/**
 *
 * @author gabriel
 */
public class UserDAO {

    public String record(User user) throws ClassNotFoundException, SQLException {

        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("INSERT INTO USER (NAME, NASC, PASSWD, SALARY, OCCUPATION) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getDate().toString());
            stmt.setString(3, user.getPasswd());
            stmt.setBigDecimal(4, user.getSalary());
            stmt.setString(5, user.getOccupation());

            stmt.executeUpdate();

            return "Saved successfully";

        } catch (SQLException ex) {
            return "Error while saving";
        } finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }

    }

    public ArrayList<User> read() throws SQLException {

        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<User> users = new ArrayList<>();

        try {
            stmt = conn.prepareStatement("SELECT * FROM USER");
            rs = stmt.executeQuery();

            while (rs.next()) {

                User user = new User();

                user.setId(rs.getInt("ID"));
                user.setName(rs.getString("NAME"));
                user.setDate(convertDateOut(rs.getString("NASC")));
                user.setOccupation(rs.getString("OCCUPATION"));
                user.setSalary(rs.getBigDecimal("SALARY"));

                users.add(user);

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar dados!");
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return users;
    }

    public ArrayList<User> readOfName(String name) {

        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<User> users = new ArrayList<>();

        try {
            stmt = conn.prepareStatement("SELECT * FROM USER WHERE NAME LIKE ?");

            stmt.setString(1, "%" + name + "%");

            rs = stmt.executeQuery();

            while (rs.next()) {

                User user = new User();

                user.setId(rs.getInt("ID"));
                user.setName(rs.getString("NAME"));
                user.setDate(convertDateOut(rs.getString("NASC")));
                user.setOccupation(rs.getString("OCCUPATION"));
                user.setSalary(rs.getBigDecimal("SALARY"));

                users.add(user);

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error Fetching!");
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return users;
    }

    public String getPasswd(int id) {

        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String passwd = null;

        try {
            stmt = conn.prepareStatement("SELECT PASSWD FROM USER WHERE ID = ?");
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            while (rs.next()) {
                passwd = rs.getString("PASSWD");
            }

        } catch (SQLException ex) {
            return null;
            //Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

        return passwd;
    }

    public String changePasswd(String newPasswd, int id) {

        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("UPDATE USER SET PASSWD = ? WHERE ID = ?");
            stmt.setString(1, newPasswd);
            stmt.setInt(2, id);

            stmt.executeUpdate();

            return "Password changed!";

        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return "Erro to Update password!";
        } finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }

    }

    public User getInfoOfUser(int id) {

        User user = new User();

        user.setId(-1);
        user.setName("NOT");
        user.setDate(LocalDate.now());
        user.setOccupation("NOT");
        user.setSalary(BigDecimal.ZERO);
        user.setAge(-1);

        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            stmt = conn.prepareStatement("SELECT * FROM USER WHERE ID = ?");
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            while (rs.next()) {

                user.setId(rs.getInt("ID"));
                user.setName(rs.getString("NAME"));
                user.setDate(convertDateOut(rs.getString("NASC")));
                user.setOccupation(rs.getString("OCCUPATION"));
                user.setSalary(rs.getBigDecimal("SALARY"));

            }

            return user;

        } catch (SQLException ex) {
            return user;
            //Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs);
        }

    }

    public String deleteUserDb(int id){
     
        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = conn.prepareStatement("DELETE FROM USER WHERE ID = ?");
            stmt.setInt(1, id);
       
            stmt.executeUpdate();
            
            return "Deleting Successfully!";
        } catch (SQLException ex) {
            return "Error Deleting User!";
            //Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(conn, stmt);
        }
        
    }
    
    public static LocalDate convertDateOut(String date) {

        DateTimeFormatter fmt = DateTimeFormatter
                .ofPattern("uuuu-MM-dd")
                .withResolverStyle(ResolverStyle.STRICT);
        LocalDate localDate = LocalDate.parse(date, fmt);

        return localDate;
    }

}
