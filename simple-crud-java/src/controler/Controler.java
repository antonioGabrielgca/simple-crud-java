/*
 * 
 */
package controler;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import model.bean.User;
import model.dao.UserDAO;

/**
 *
 * @author gabriel
 */
public class Controler {

    public String record(String name, String date, String passwd,
            String occupation, String salary) throws ParseException, ClassNotFoundException, SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {

        User user = new User();

        if (!name.isEmpty()) {
            user.setName(name);
        } else {
            return "Found name!";
        }

        if (!date.isEmpty()) {
            user.setDate(convertDateInp(date));
        } else {
            return "Found date!";
        }

        if (!passwd.isEmpty()) {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = algorithm.digest(passwd.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", 0xFF & b));
            }

            String hash = hexString.toString();

            user.setPasswd(hash);

        } else {
            return "Found password!";
        }

        if (!occupation.isEmpty()) {
            user.setOccupation(occupation);
        } else {
            return "Found occupation!";
        }

        if (!salary.isEmpty() && !salary.equalsIgnoreCase("0")) {
            user.setSalary(new BigDecimal(salary));
        } else {
            return "Found salary!";
        }

        UserDAO userdao = new UserDAO();
        return userdao.record(user);

    }

    public ArrayList<User> readTable() throws SQLException {

        UserDAO userdao = new UserDAO();

        ArrayList<User> users = userdao.read();

        users.forEach((u) -> {
            u.setAge(convertDateInAge(u.getDate()));
        });

        return users;
    }

    public ArrayList<User> readTableOfVariable(String variable) {

        UserDAO userdao = new UserDAO();

        ArrayList<User> users = new ArrayList<>();

        users = userdao.readOfName(variable);

        users.forEach((u) -> {
            u.setAge(convertDateInAge(u.getDate()));
        });

        return users;
    }

    public String changePassword(String id, String curPass, String newPass) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        UserDAO userdao;
        String curPasswdHex;

        if (id.isEmpty()) {
            return "You need passed the ID!";
        }

        if (curPass.isEmpty()) {
            return "You need passed the Password Current!";
        }

        if (newPass.isEmpty()) {
            return "You need passed the Password New!";
        }

        //
        userdao = new UserDAO();
        curPasswdHex = userdao.getPasswd(Integer.parseInt(id));
        if (curPasswdHex == null) {
            return "ID not found!";
        }
        //

        //
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        byte messageDigest[] = algorithm.digest(curPass.getBytes("UTF-8"));

        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }

        String hash = hexString.toString();
        //

        //
        if (!hash.equals(curPasswdHex)) {
            return "Passwords do not match!";
        }
        //

        //
        MessageDigest algorithm2 = MessageDigest.getInstance("SHA-256");
        byte messageDigest2[] = algorithm2.digest(newPass.getBytes("UTF-8"));

        StringBuilder hexString2 = new StringBuilder();
        for (byte b : messageDigest2) {
            hexString2.append(String.format("%02X", 0xFF & b));
        }

        String hash2 = hexString2.toString();
        //

        return userdao.changePasswd(hash2, Integer.parseInt(id));
    }

    public User getInfo(String id) {

        User user = new User();
        UserDAO userdao = new UserDAO();

        user = userdao.getInfoOfUser(Integer.parseInt(id));

        if (user.getId() != -1) {
            user.setAge(convertDateInAge(user.getDate()));
        }

        return user;
    }

    public static LocalDate convertDateInp(String date) {

        int yyyy, MM, dd;

        dd = Integer.parseInt(date.substring(0, 2));
        MM = Integer.parseInt(date.substring(3, 5));
        yyyy = Integer.parseInt(date.substring(6, 10));

        LocalDate new_date = LocalDate.of(yyyy, MM, dd);

        return new_date;
    }
    
    public String deleteUser(String id, String passwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        
        UserDAO userdao;
        User user = new User();
        String curPasswdHex;
        
        if (id.isEmpty()) {
            return "You need passed the ID!";
        }

        if (passwd.isEmpty()) {
            return "You need passed the Password Current!";
        }
        
        //
        userdao = new UserDAO();
        curPasswdHex = userdao.getPasswd(Integer.parseInt(id));
        if (curPasswdHex == null) {
            return "ID not found!";
        }
        //

        //
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        byte messageDigest[] = algorithm.digest(passwd.getBytes("UTF-8"));

        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }

        String hash = hexString.toString();
        //

        //
        if (!hash.equals(curPasswdHex)) {
            return "Passwords do not match!";
        }
        //

        return userdao.deleteUserDb(Integer.parseInt(id));        
    }

    public static int convertDateInAge(LocalDate date) {

        LocalDate today = LocalDate.now();
        int age = (today.getYear() - date.getYear()) - 1;

        if (today.getMonthValue() > date.getMonthValue()) {
            age++;
        } else if (today.getMonthValue() == date.getMonthValue()) {
            if (today.getDayOfMonth() >= date.getDayOfMonth()) {
                age++;
            }
        }

        return age;
    }

}
