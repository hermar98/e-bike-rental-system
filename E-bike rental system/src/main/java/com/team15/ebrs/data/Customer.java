
package main.java.com.team15.ebrs.data;


/**
 * An object for Customer. This is used to register different customers
 *
 * @author Team 15
 */
public class Customer {
    private final int customerId;
    private String firstname;
    private String surname;

    /**
     * Constructor for Customer object. In practice this information will be provided by the payment machine.
     * 
     * @param customerId Unique id for the costumer registered in the datapase.
     * @param firstname Firstname for the customer.
     * @param surname Surname for the customer.
     */
    public Customer(int customerId, String firstname, String surname){
        this.customerId = customerId;
        this.firstname = firstname;
        this.surname = surname;
    }

    /**
     * Returns the customerId.
     * @return int customerId.
     */
    public int getCustomerId(){
        return customerId;
    }

    /**
     * Returns the firstname.
     * @return String firstname.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets the firstname for the customer.
     * @param firstname Desired new firstname.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Returns the surname for the customer.
     * @return String surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname for the customer.
     * @param surname Desired new surname.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    /**
     * Custom equals method. Checks for equality in customerId, firstname and surname.
     * @param o Desired object to check for equality with.
     * @return Boolean stating whether the compared objects are equal or not.
     */
    
    @Override
    public boolean equals(Object o){
    	System.out.println("LKJDFLDJFL");
        if(!(o instanceof Customer)){
            return false;
        }

        if (o == this){
            return true;
        }

        Customer oC = (Customer) o;

        return customerId == oC.customerId && firstname.equals(oC.firstname) && surname.equals(oC.surname);
    }
}
