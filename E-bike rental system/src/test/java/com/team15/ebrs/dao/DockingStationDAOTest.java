 package test.java.com.team15.ebrs.dao;

 import static org.junit.jupiter.api.Assertions.*;

 import java.util.ArrayList;

 import org.junit.jupiter.api.AfterAll;
 import org.junit.jupiter.api.AfterEach;
 import org.junit.jupiter.api.BeforeAll;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;

 import main.java.com.team15.ebrs.dao.DockingStationDAO;
 import main.java.com.team15.ebrs.data.DockingStation;
 import main.java.com.team15.ebrs.util.DatabaseConnector;

 class DockingStationDAOTest {
 	
 	private static DockingStationDAO dockingstationDAO;
 	DockingStation s1 = new DockingStation(1,"Lade ladestasjon",63.445900, 10.441570,20);
 	DockingStation s2 = new DockingStation(2,"Moholt stasjon",63.410468, 10.435377,30);
 	DockingStation s3 = new DockingStation(3,"Strindheim stasjon",63.427548, 10.458589,50);
 	private static DockingStation dockstation11 = new DockingStation("Utest",32.2,33.2,100);
 	private DockingStation dockstation22;
 	
 	@BeforeAll
 	public static void init(){
 		try {
 			DatabaseConnector.useTestDatabase(true);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		dockingstationDAO = new DockingStationDAO();
 	}
 	
 	@AfterAll
 	public static void tearDownClass() {
 		try {
 			DatabaseConnector.resetDatabase();
 		}catch (Exception e) {
 			e.printStackTrace();
 		}
 	}

 	
 	@BeforeEach
 	public void setUp() {
 		dockingstationDAO.addDockingStation(dockstation11);
 		dockstation22 = dockingstationDAO.getStationByName("Utest");
 	}
 	
 	@AfterEach
     public void tearDown() {
 		dockingstationDAO.deleteDockingStation(dockstation22);
     }

 	
 	private void compareStation(DockingStation dock1, DockingStation dock2) {
 		assertEquals(dock1.getStationName(),dock2.getStationName());
 		assertEquals(dock1.getCoordinateLat(),dock2.getCoordinateLat(),0.001);
 		assertEquals(dock1.getCoordinateLng(),dock2.getCoordinateLng(),0.001);
 		assertEquals(dock1.getAntDocks(),dock2.getAntDocks());
 	}

 	@Test
 	void testGetAllDockingStations() {
 		ArrayList<DockingStation> dockingstationlist = dockingstationDAO.getAllDockingStations();
 		ArrayList<DockingStation> dockingstationlist1 = new ArrayList<DockingStation>();
 		dockingstationlist1.add(s1);
 		dockingstationlist1.add(s2);
 		dockingstationlist1.add(s3);
 		for(int i =0;i<3;i++) {
 			 compareStation(dockingstationlist.get(i),dockingstationlist1.get(i));
 		}
 			
 	
 		
 	}

 	@Test
 	void testAddDockingStation() {
 		this.compareStation(dockstation11, dockstation22);
 	}

 	@Test
 	void testDeleteDockingStion() {
 		assertTrue(dockingstationDAO.deleteDockingStation(dockstation22));
 		assertFalse(dockingstationDAO.findStationByName("Utest"));
 	}

 	@Test
 	void testFindStationByID() {
 		assertTrue(dockingstationDAO.findStationByID(dockstation22));
 	}

 	@Test
 	void testFindStationByName() {
 		assertTrue(dockingstationDAO.findStationByName("Utest"));
 	}

 	@Test
 	void testChangeName() {
 		dockingstationDAO.changeName(dockstation22, "nyname");
 		DockingStation dockstation33 = dockingstationDAO.getStationByID(dockstation22.getStationId());
 		assertEquals(dockstation33.getStationName(),"nyname");
 	}

 	@Test
 	void testChangeCoordinate() {
 		double nycoordinatelng = 45.5;
 		double nycoordinatelat = 54.5;
 		dockingstationDAO.changeCoordinate(dockstation22,nycoordinatelat,nycoordinatelng);
 		DockingStation dockstation33 = dockingstationDAO.getStationByID(dockstation22.getStationId());
 		assertEquals(nycoordinatelat,dockstation33.getCoordinateLat(),0.01);
 		assertEquals(nycoordinatelng,dockstation33.getCoordinateLng(),0.01);
 	}

 	@Test
 	void testChangeAntDocks() {
 		int nyAntDocks = 39;
 		dockingstationDAO.changeAntDocks(dockstation22, nyAntDocks);
 		DockingStation dockstation33 = dockingstationDAO.getStationByID(dockstation22.getStationId());
 		assertEquals(nyAntDocks,dockstation33.getAntDocks());
 	}

 	@Test
 	void testGetPowerUsage() {
 		assertEquals(dockingstationDAO.getPowerUsage(s1),490,0.001);
 	}

 	@Test
 	void testGetInformasjonByName() {
 		String information = "<html>Station ID: " +s2.getStationId() +"<br>Station Name: " + s2.getStationName() +"<br>Coordinatelat: "+s2.getCoordinateLat()+
 		"<br>Coordinatelng: " +s2.getCoordinateLng() + "<br>Total docks: " +s2.getAntDocks()+ "<br>Total PowerUsage: "+dockingstationDAO.getPowerUsage(s2)+"<html>";
 		assertEquals(information,dockingstationDAO.getInformationByName("Moholt stasjon"));
 		
 	}

 	@Test
 	void testGetStationByName() {
 		assertEquals(dockstation22.getStationName(),dockstation11.getStationName());
 		assertEquals(dockstation22.getCoordinateLat(),dockstation11.getCoordinateLat(),0.001);
 	}

 	@Test
 	void testGetStationByID() {
 		DockingStation dockstation33 = dockingstationDAO.getStationByID(dockstation22.getStationId());
 		compareStation(dockstation33, dockstation22);	
 	}

 }
