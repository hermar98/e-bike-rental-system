package test.java.com.team15.ebrs.dao;

import java.util.ArrayList;
import main.java.com.team15.ebrs.dao.CustomerDAO;
import main.java.com.team15.ebrs.data.Customer;
import main.java.com.team15.ebrs.util.DatabaseConnector;
import main.java.com.team15.ebrs.util.RunSqlScript;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerDAOTest{
    private CustomerDAO cDAO;
    private Customer c;
    private Customer c1;
    private Customer c2;
    private Customer c3;
    
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
            try{
                DatabaseConnector.useTestDatabase(true);
                String sqlFilePath = "/main/resources/com/team15/ebrs/CreateTablesTest.sql";
                RunSqlScript.run(sqlFilePath);
            }catch(Exception e){
                System.out.println("Error in useTestDatabase " + e);
            }
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
            String sqlFilePath = "/main/resources/com/team15/ebrs/CreateTablesTest.sql";
            RunSqlScript.run(sqlFilePath);
            DatabaseConnector.useTestDatabase(false);
	}

	@BeforeEach
	void setUp() throws Exception {
            cDAO = new CustomerDAO();
            c = new Customer(666, "Lars", "Larsen");
            c1 = new Customer(12345, "Herman", "Martinsen");
            c2 = new Customer(23456, "Trond", "Rondestvedt");
            c3 = new Customer(34567, "Jorgen", "Aasvestad");
	}

	@AfterEach
	void tearDown() throws Exception {
            cDAO = null;
            c = null;
            c1 = null;
            c2 = null;
            c3 = null;
	}
        
        @Test
        void testGetCustomer(){
            System.out.println("Test get customer");
            Customer test = cDAO.getCustomer(12345);
            assertEquals(test, c1);
        }
        
        @Test
        void testGetAllCustomers(){
            System.out.println("Test get all customers");
            ArrayList<Customer> allcust = cDAO.getAllCustomers();
            for (Customer c : allcust) {
            	System.out.println(c.getCustomerId() + " " + c.getFirstname() + " " + c.getSurname());
            }
            assertEquals(allcust.get(1), c1);
            assertEquals(allcust.get(2), c2);
            assertEquals(allcust.get(3), c3);
        }

	@Test
	void testAddCustomer() {
            System.out.println("Test add customer");
            boolean result = cDAO.addCustomer(c);
            boolean expResult = true;
            Customer cust = cDAO.getCustomer(666);
            assertEquals(expResult, result);
            assertEquals(c, cust);
	}
        
        @Test
        void testDeleteCustomer(){
            System.out.println("Test delete customer");
            boolean result = cDAO.deleteCustomer(c);
            boolean expResult = true;
            Customer cust = cDAO.getCustomer(666);
            String expResult2 = "";
            String result2 = cust.getFirstname() + cust.getSurname();
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
        }
        
        @Test
        void testEditCustomer(){
            System.out.println("Test edit customer");
            Customer c4 = new Customer (12345, "Herr", "Mansen");
            boolean result = cDAO.editCustomer(c4);
            boolean expResult = true;
            
            Customer cust = cDAO.getCustomer(12345);
            String expResult2 = "HerrMansen";
            String result2 = cust.getFirstname() + cust.getSurname();
            
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
            
            cDAO.editCustomer(c1);
        }
}
