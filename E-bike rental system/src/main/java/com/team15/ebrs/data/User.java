package main.java.com.team15.ebrs.data;


/**
 * An object for User. This is used to register different User objects
 *
 * @author Team 15
 */

public class User {
    private String email;
    private String firstname;
    private String surname;
    private String userPassword;
    private String userSalt;
    private int id;

    /**
     *This is the constructor which constructs the User object.
     * @param email     the email of the user
     * @param firstname	first name of the user
     * @param surname   surname of the user
     * @param userPassword	the hashed password for the user
     * @param userSalt      a  salt for password
     */
    public User(String email, String firstname, String surname, String userPassword, String userSalt) {
        this.email = email;
        this.firstname = firstname;
        this.surname = surname;
        this.userPassword = userPassword;
        this.userSalt = userSalt;
    }

    /**
     * Replaces the old password with a new password for user
     * @param userPassword the new password
     */
    public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

    /**
     * Replaces the old one with new salt for password
     * @param userSalt
     */
	public void setUserSalt(String userSalt) {
		this.userSalt = userSalt;
	}

    /**
     * Returns the first name of the user
     * @return the first name of the user
     */
	public String getFirstname() {
        return firstname;
    }

    /**
     *Returns the surname of the user 
     * @return the surname of the user
     */
    public String getSurname(){
        return surname;
    }

    /**
     *Returns the email of the user 
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     *Returns the hashed password of the user
     * @return the hashed password
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Returns a salt for password of the user
     * @return a salt for password
     */
    public String getUserSalt() {
        return userSalt;
    }

    /**
     *Returns the id of the user
     * @return  the id of the user
     */
    public int getUserId(){
        return id;
    }

    /**
     *Replaces the old id of the user with a new id
     * @param id  the new id
     */
    public void setUserId(int id){
        this.id = id;
    }

    /**
     * Returns a string representation of a user 
     * @return a string representation of a user.
     */
    public String toString() {
    	return email;
    }
}
