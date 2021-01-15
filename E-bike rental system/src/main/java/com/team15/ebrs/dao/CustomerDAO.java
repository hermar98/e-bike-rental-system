/**
 * Database Access Layer for the bike-rental system. Provides all necessary connection with the database.
 */
package main.java.com.team15.ebrs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import main.java.com.team15.ebrs.data.Customer;
import main.java.com.team15.ebrs.util.DatabaseConnector;
/**
 * Data Access Object for Customer objects. Provides necessary database interaction regarding the specified object. 
 * @author Team 15
 * @see Customer
 * 
 */

public class CustomerDAO {
    private ArrayList<Customer> customerlist;
    
    private final String ALL_CUSTOMERS = "SELECT * FROM customer";
    private final String CUSTOMER_BY_ID = "SELECT * FROM customer WHERE customer_id = ";
    private final String ADD_CUSTOMER = "INSERT INTO customer VALUES (?, ?, ?)";
    private final String EDIT_CUSTOMER = "UPDATE customer SET firstname = ?, surname = ? WHERE customer_id = ?";
    private final String DELETE_CUSTOMER = "UPDATE customer SET firstname = NULL, surname = NULL WHERE customer_id = ?";
    
    private final String cCustomerId = "customer_id";
    private final String cFirstname = "firstname";
    private final String cSurname = "surname";

    /**
     * Returns an ArrayList containing all Customers registered in the database.
     * @return ArrayList with Customer objects.
     */
    public ArrayList<Customer> getAllCustomers(){
        customerlist= new ArrayList<>();
        
        try(
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(ALL_CUSTOMERS);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                Customer cust = getCustomerFromRS(rs);
                customerlist.add(cust);
            }
            
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
    return customerlist;
    }

    /**
     * Returns a Customer object from the datbase matching the specified Id. Null if no customer is found on specified Id.
     * @param customerId The customerId for the wanted Customer.
     * @return Customer object matching specified Id.
     */
    public Customer getCustomer(int customerId){
        try(
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(CUSTOMER_BY_ID + customerId);
            ResultSet rs = ps.executeQuery()){
            
            if(rs.next()){
                return getCustomerFromRS(rs);
            }
        }catch(Exception e){
            System.out.println("Exception in getCustomer " + e);
        }
        return null;
    }

    /**
     * Help method used for various methods in CustomerDAO. Returns Customer object from the specified ResultSet.
     * @param rs ResultSet containing enough info to create a Customer object.
     * @return Customer object according to provided ResultSet.
     */
    private Customer getCustomerFromRS(ResultSet rs) {
        try{
            String firstname = rs.getString(cFirstname);
            if(rs.wasNull()){
                firstname = "";
            }
            
            String surname = rs.getString(cSurname);
            if(rs.wasNull()){
                surname = "";
            }
            
            return new Customer(rs.getInt(cCustomerId), firstname, surname);
        }catch(Exception e ){
            System.out.println("Exception in getCustomerFromRS" + e);
        }
        return null;
    }

    /**
     * Adds a Customer to the database using the provided Customer object.
     * @param customer Customer object used to register a Customer in the database.
     * @return Boolean stating whether the registration was successful.
     */
    public boolean addCustomer(Customer customer){
        try(
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(ADD_CUSTOMER)){
            
            ps.setInt(1, customer.getCustomerId());
            ps.setString(2, customer.getFirstname());
            ps.setString(3, customer.getSurname());
            ps.executeUpdate();
            return true;
        }catch(Exception e){
            System.out.println("Exception in addCustomer " + e);
        }
        return false;
    }

    /**
     * Edits a already registered Customer in the database matching the Id of the provided object.
     * @param customer Customer object containing the desired edited information.
     * @return Boolean stating whether the altered information was registered successfully.
     */
    public boolean editCustomer(Customer customer){
        try(
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(EDIT_CUSTOMER)){
            
            ps.setString(1, customer.getFirstname());
            ps.setString(2, customer.getSurname());
            ps.setInt(3, customer.getCustomerId());
            ps.executeUpdate();
            return true;
        }catch(Exception e){
            System.out.println("Exception in editCustomer " + e);
        }
        return false;
    }

    /**
     * Sets parameter values for firstname and surname to NULL in the database. The customer Id will still be stored, but all personal information will be removed.
     *
     * @param customer The desired customer that you want deleted from the database.
     * @return Boolean stating whether the deletion of personal information was successful.
     */
    public boolean deleteCustomer(Customer customer){
        try(
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(DELETE_CUSTOMER)){
            
            ps.setInt(1, customer.getCustomerId());
            ps.executeUpdate();
            return true;
        }catch(Exception e){
            System.out.println("Exception in getCustomerFromRS " + e);
        }
        return false;
    }
}
