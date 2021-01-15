package test.java.com.team15.ebrs.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.com.team15.ebrs.util.DatabaseConnector;
import main.java.com.team15.ebrs.dao.EconomyDAO;


class EconomyDAOTest {
	private static EconomyDAO economydao;
	
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		try {
 			DatabaseConnector.useTestDatabase(true);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
		economydao = new EconomyDAO();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		try {
 			DatabaseConnector.resetDatabase();
 		}catch (Exception e) {
 			e.printStackTrace();
 		}
	}

	@BeforeEach
	void setUp() throws Exception {
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetAllBikeCosts() {
		assertEquals(economydao.getAllBikeCosts().get(0).getValue(),18500,0.0001);
	}

	@Test
	void testGetAllRepairCosts() {
		assertEquals(economydao.getAllRepairCosts().get(0).getValue(),600,0.001);
	}

	@Test
	void testGetAllBikeIncome() {
		assertEquals(economydao.getAllBikeIncome().get(0).getValue(),833,0.5);
	}

}
