package test.java.com.team15.ebrs.dao;

import java.util.ArrayList;
import main.java.com.team15.ebrs.dao.TypeDAO;
import main.java.com.team15.ebrs.data.BikeType;
import main.java.com.team15.ebrs.util.DatabaseConnector;
import main.java.com.team15.ebrs.util.RunSqlScript;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeDAOTest {
    
    private TypeDAO tDAO;
    private BikeType t;
    private BikeType t1;
    private BikeType t2;
    private BikeType t3;
    private BikeType t4;
    
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
            TypeDAO tDAO = new TypeDAO();
            t = new BikeType("Super Bike", 300);
            t1 = new BikeType("Terrain bike", 100);
            t2 = new BikeType("Street bike", 80);
            t3 = new BikeType("Kids bike", 50);
            t4 = new BikeType("Fat bike", 150);
            t1.setTypeId(1);
            t2.setTypeId(2);
            t3.setTypeId(3);
            t4.setTypeId(4);
	}

	@AfterEach
	void tearDown() throws Exception {
            tDAO = null;
            t = null;
            t1 = null;
            t2 = null;
            t3 = null;
            t4 = null;
	}
        
        @Test
        void testGetType(){
            System.out.println("Test get type");
            BikeType type = tDAO.getType(4);
            
            assertEquals(type, t4);
        }
        
        @Test
        void testGetAllTypes(){
            System.out.println("Test get all types");
            ArrayList<BikeType> all = tDAO.getAllTypes();
            
            assertEquals(all.get(0), t1);
            assertEquals(all.get(1), t2);
            assertEquals(all.get(2), t3);
            assertEquals(all.get(3), t4);
        }        

	@Test
	void testAddType() {
            System.out.println("Test addType");
            int expResult = 0;
            int result = tDAO.addType(t);
            
            assertEquals(expResult, result);
	}
        
        @Test
        void testDeleteType(){
            System.out.println("Test delete type");
            BikeType tNew = new BikeType("Bike bike", 270);
            tDAO.addType(tNew);
            int expResult = 0;
            int result = tDAO.deleteType(tNew);
            
            assertEquals(expResult, result);
        }
        
        @Test
        void testFindType(){
            System.out.println("Test find type");
            boolean expResult = true;
            boolean result = tDAO.findType(0);
            
            boolean expResult2 = false;
            boolean result2 = tDAO.findType(50);
            
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
        }
}
