package test.java.com.team15.ebrs.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.dao.TripDAO;
import main.java.com.team15.ebrs.data.BikeData;
import main.java.com.team15.ebrs.data.Trip;
import main.java.com.team15.ebrs.util.DatabaseConnector;

class TripDAOTest {
	private static TripDAO tripDAO;
	private static LocalDateTime time1 = LocalDateTime.of(2018, 4, 22,12,0,0);
	private static LocalDateTime time2 = LocalDateTime.of(2018, 4, 22,17,0,0);
	private static LocalDateTime time3 = LocalDateTime.of(2018, 1, 5,12,0,0);
	private static LocalDateTime time4 = LocalDateTime.of(2018, 1, 5,16,20,0);
	private static Trip tripTest = new Trip(1000,1,12345,time3);
	private Trip trip1 = new Trip(1000,1,12345,time1);
	
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		try {
 			DatabaseConnector.useTestDatabase(true);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
		tripDAO = new TripDAO();
		tripTest.setEndStationId(2);
		tripTest.setEndTime(time4);
		tripTest.setTripDistance(2500);
		tripTest.setTripId(1);
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
	
	private void compareTrip(Trip t1,Trip t2) {
		assertEquals(t1.getBikeId(),t2.getBikeId());
		assertEquals(t1.getPersonId(),t2.getPersonId());
		assertEquals(t1.getTripId(),t2.getTripId());
		assertEquals(t1.getStartStationId(),t2.getStartStationId());
		assertEquals(t1.getStartTime(),t2.getStartTime());
		
	}

	@Test
	void testStartTrip() {
		assertTrue(tripDAO.startTrip(trip1));
	}

	@Test
	void testStopTrip() {
		trip1.setEndStationId(1);
		trip1.setEndTime(time2);
		trip1.setTripDistance(500.5);
		trip1.setTripId(tripDAO.getTripId(trip1));
		assertTrue(tripDAO.stopTrip(trip1));
	}

	@Test
	void testGetBikeDataForTrip() {
		BikeDAO bikedao = new BikeDAO();
		Trip trip2 = tripDAO.getTripById(1);
		BikeData bikedat = new BikeData(trip2.getBikeId(),trip2.getStartTime(),63.427548,10.458589,60);
		bikedao.addBikeData(bikedat, trip2.getTripId());
		ArrayList<BikeData> bikedatatest = tripDAO.getBikeDataForTrip(trip2.getTripId());
		assertEquals(bikedatatest.get(0).getBikeId(),bikedat.getBikeId());
		assertEquals(bikedatatest.get(0).getChargingLvl(),bikedat.getChargingLvl(),0.001);
		assertEquals(bikedatatest.get(0).getDateTime(),bikedat.getDateTime());
	}

	@Test
	void testGetTripId() {
		assertEquals(tripDAO.getTripId(tripTest),1);
	}

	@Test
	void testGetTripById() {
		Trip tripbyid = tripDAO.getTripById(1);
		compareTrip(tripbyid,tripTest);
	}

	@Test
	void testGetAllTrips() {
		ArrayList<Trip> triplist = tripDAO.getAllTrips();
		compareTrip(triplist.get(0),tripTest);
		assertEquals(triplist.size(),9);
	}

	@Test
	void testGetAllTripsByBikeId() {
		ArrayList<Trip> triplist = tripDAO.getAllTripsByBikeId(2000);
		assertEquals(triplist.size(),2);
		
	}

}
