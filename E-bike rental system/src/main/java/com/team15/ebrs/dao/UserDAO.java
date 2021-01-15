package main.java.com.team15.ebrs.dao;

import main.java.com.team15.ebrs.data.User;
import main.java.com.team15.ebrs.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Data Access Object class for User. Provides necessary database interaction regarding the specified objects.
 * @author Team 15
 * @see User
 * @see DatabaseConnector
 *
 */
public class UserDAO {
    private static ArrayList<User> userList = new ArrayList<>();
    //SQL Queries
    private final String ALL_USERS = "SELECT * FROM admin_users";
    private final String USER_BY_EMAIL = "SELECT * FROM admin_users WHERE email = '";
    private final String a = "'";

    private final String USER_BY_ID = "SELECT * FROM admin_users WHERE userid = ";

    private final String INSERT = "INSERT INTO admin_users (email, user_password, salt, firstname, surname) VALUES (?,?,?,?,?)";
    private final String DELETE = "DELETE FROM admin_users where email = ?";
    private final String UPDATE = "UPDATE admin_users SET firstname = ?, surname = ?, user_password = ?, salt = ?  WHERE email=?";
    
    //Column labels for admin_users table
    private final String cUserId = "user_id";
    private final String cEmail = "email";
    private final String cFirstname = "firstname";
    private final String cSurname = "surname";
    private final String cPassword = "user_password";
    private final String cSalt = "salt";

    /**
     * Returns an ArrayList of User objects from the database.
     * @return an ArrayList of User objects
     * @see User
     */
    public ArrayList<User> getAllUsers () {
        userList = new ArrayList<>();
        try(
                Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(ALL_USERS);
                ResultSet rs = ps.executeQuery()){
            System.out.println("Connection");
                while (rs.next()) {
                    System.out.println("While");
                    int userId = rs.getInt(cUserId);
                    String email = rs.getString(cEmail); //
                    String firstname = rs.getString(cFirstname);
                    String surname = rs.getString(cSurname);
                    String password = rs.getString(cPassword);
                    String salt = rs.getString(cSalt);
                    User user = new User(email, firstname, surname, password, salt);
                    user.setUserId(userId);
                    userList.add(user);
                }

        } catch (Exception e) {
                System.out.println("Exception: " + e);
        }
        return userList;
    }

    /**
     * Returns a User object with the specified email from the database
     * @param searchEmail
     * @return a User object with the specified email
     * @see User
     */
    public User getUser (String searchEmail) {
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(USER_BY_EMAIL + searchEmail + a);
            ResultSet rs = ps.executeQuery()){
            if(rs.next()){
                int userId = rs.getInt(cUserId);
                String firstname = rs.getString(cFirstname);
                String lastname = rs.getString(cSurname);
                String email = rs.getString(cEmail);
                String userPass = rs.getString(cPassword);
                String salt = rs.getString(cSalt);

                User user = new User(email,firstname,lastname,userPass,salt);
                user.setUserId(userId);
                return user;
            }
        }catch(Exception e){
            System.out.println("Exception in getUser: " + e);
        }
        return null;
    }

    /**
     * Method to add an admin_user to the database, takes a User object as a parameter
     * @param user this parameter is the User object you want to add to the database
     * @return a boolean that says if it managed to add the user to the database (true) or if it failed (false)
     * @see User
     */
    public boolean addUser (User user) {
        if(findUser(user)){
            return false; //user already registered
        }else{
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(INSERT)){

                ps.setString(1, user.getEmail());
                ps.setString(2, user.getUserPassword());
                ps.setString(3, user.getUserSalt());
                ps.setString(4, user.getFirstname());
                ps.setString(5, user.getSurname());

                ps.executeUpdate();
                return true;
            }catch(Exception e){
                System.out.println("Exception in addUser: " + e);
            }
        }
        return false;
    }

    /**
     * Method to edit a admin_user in the database, it updates the user in the database with the same email as
     * the User object in the parameter. So it does not allow for editing an admin_users email adress.
     * @param user this is the User object with the updated variables that will be updated in the database
     * @return a boolean that says if it managed to update the user in the database (true) or if it failed (false)
     * @see User
     */
    public boolean editUser(User user){//Edits the user regarding to userId
        if(!findUser(user)){
            return false;
            //User_Id not registered
        }else{
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE)){
                
                ps.setString(1, user.getFirstname());
                ps.setString(2, user.getSurname());
                ps.setString(3, user.getUserPassword());
                ps.setString(4, user.getUserSalt());
                ps.setString(5, user.getEmail());
                ps.executeUpdate();
                return true;
            }catch(Exception e){
                System.out.println("Exception in editUser: " + e);
            }
        }
        return false;
    }

    /**
     * Method to delete an admin_user in the database, it takes a User object as parameter to specify which user is going to be deleted
     * @param user this is the User object that will be deleted in the database.
     * @return a boolean that says if it managed to delete the user in the database (true) or if it failed (false)
     * @see User
     */
    public boolean deleteUser(User user){
        
        if(!findUser(user)){//User not in database
            return false; //
        }else{
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE)){
                
                ps.setString(1, user.getEmail());
                ps.executeUpdate();
                return true;
            }catch(Exception e){
                System.out.println("Exception in deleteUser: " + e);
            }
        }
        return false;
    }

    /**
     * Method to check if the a user is registered in the database, it takes a User object as parameter
     * @param user
     * @return a boolean that says whether or not the user is already in the database.
     * @see User
     */
    public boolean findUser(User user){ //Checks if the selected user registered in the database - help method
        System.out.println(user.getEmail());
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(USER_BY_EMAIL + user.getEmail() + a);
            ResultSet rs = ps.executeQuery()){

            if(rs.isBeforeFirst()){
                return true;
            }
        }catch(Exception e){
            System.out.println("Exception in findUser(user): " + e);
        }
        return false;
    }
}
