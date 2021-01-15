	package test.java.com.team15.ebrs.dao;
	
	import static org.junit.jupiter.api.Assertions.*;
	
	import java.time.LocalDate;
	import java.time.LocalDateTime;
	import java.time.LocalTime;
	import java.util.ArrayList;
	
	import org.junit.jupiter.api.AfterAll;
	import org.junit.jupiter.api.BeforeAll;
	import org.junit.jupiter.api.Test;
	import org.junit.jupiter.api.BeforeEach;
	
	import main.java.com.team15.ebrs.dao.BikeDAO;
	import main.java.com.team15.ebrs.data.Bike;
	import main.java.com.team15.ebrs.data.BikeData;
	import main.java.com.team15.ebrs.data.BikeType;
	import main.java.com.team15.ebrs.data.DockingStation;
	import main.java.com.team15.ebrs.util.DatabaseConnector;
	
	
	
	class BikeDAOTest {
	
	 private BikeType aType = new BikeType("Fat bike", 150.0);
	 private Bike bike = new Bike(LocalDate.now(), 6000, "Sykkelbyggeriet", aType, 0);
	 private static BikeDAO biketest;
	
	 @BeforeEach
	 public static void initialize() {
	  biketest = new BikeDAO();
	
	 }
	 @BeforeAll
	 public static void init() {
	
	  try {
	
	 DatabaseConnector.useTestDatabase(true);
	
	  } catch (Exception e) {
	   System.out.println("Can't use the database test");
	  }
	 }


	 @AfterAll
	 public static void refresh() {
	  try {
	
	   DatabaseConnector.resetDatabase();
	
	  } catch (Exception e) {
	   e.printStackTrace();
	
	  }
	 }
	
	 @Test
	 public void testGetAllBikes() {
	
	
	  ArrayList < Bike > bikeListCompare = new ArrayList < Bike > ();
	
	  bike.setBikeId(1000);
	  bikeListCompare.add(bike);
	
	  bike.setBikeId(1001);
	  bikeListCompare.add(bike);
	
	  bike.setBikeId(2000);
	  bikeListCompare.add(bike);
	
	
	  bike.setBikeId(2001);
	  bikeListCompare.add(bike);
	
	  bike.setBikeId(3000);
	  bikeListCompare.add(bike);
	
	  bike.setBikeId(4000);
	  bikeListCompare.add(bike);
	
	  bike.setBikeId(4001);
	  bikeListCompare.add(bike);
	
	  ArrayList < Bike > bikeListRes = biketest.getAllBikes();
	  boolean ok = false;
	
	  for (Bike aBike: bikeListRes) {
	
	   for (Bike aBike2: bikeListCompare) {
	    if (aBike.getBikeId() == aBike2.getBikeId()) {
	     ok = true;
	    } else {
	     ok = false;
	     break;
	    }
	   }
	  }
	  assertTrue(ok);
	
	 }
	 
	 @Test
	 public void testGetAllAvailableBikes() {
	  ArrayList < Bike > bikelist = new ArrayList < Bike > ();
	
	  bike.setBikeId(1001);
	  bikelist.add(bike);
	
	  bike.setBikeId(2001);
	  bikelist.add(bike);
	
	  bike.setBikeId(3000);
	  bikelist.add(bike);
	
	
	  bike.setBikeId(4001);
	  bikelist.add(bike);
	
	
	
	  boolean ok = false;
	
	  ArrayList < Bike > bikeListRes = biketest.getAllAvailableBikes();
	  for (Bike aBike: bikeListRes) {
	   for (Bike aBike2: bikelist) {
	    if (aBike2.getBikeId() == aBike.getBikeId()) {
	     ok = true;
	    } else {
	     ok = false;
	     break;
	    }
	   }
	
	 }
	  assertTrue(ok);
	 }
	 

	 @Test
	 public void testGetAllRecentData() {
	  ArrayList < BikeData > bikeDataRes = biketest.getAllRecentData();
	  boolean ok = false;
	  if (bikeDataRes != null) {
	   ok = true;
	
	  }
	  assertTrue(ok);
	
	
	 }
	 
		@Test
		public void getAllBikeData (){
			bike.setBikeId(1000);
			
			ArrayList <BikeData> bikeDataList = biketest.getAllBikeData(bike);
			ArrayList <BikeData> bikeDataList2= new ArrayList <BikeData>();
			BikeData bikeDataTest= new BikeData(1000, LocalDateTime.of(2018, 03, 22, 13, 31, 00), 63.410468, 10.435377,49);
			bikeDataList2.add(bikeDataTest);
			boolean ok=false;
			
			for(BikeData data: bikeDataList) {
				for(BikeData data2: bikeDataList2) {
					if(bikeDataList.get(0).toString().equals(bikeDataTest.toString())) {
						ok=true;
					}else {
						ok=false;
					}
				}
			}
			assertTrue(ok);
			
		}
		
		 @Test
		 public void getAllTravellingData() {
		  ArrayList < BikeData > bikeDataList = biketest.getAllTravellingData();
		  int expectedChargeLvl = 23;
		
		  assertTrue(bikeDataList.get(0).getChargingLvl() == expectedChargeLvl);
		 }
		 
		 @Test
		 public void testEditBike() {
		
		  try {
		
		   biketest = new BikeDAO();
		
		
		   Bike bikeEdit = new Bike(LocalDate.now(), 6980, "SykkelKake", aType, 0);
		   bikeEdit.setBikeId(4000);
		   aType.setTypeId(4);
		
		
		   /* Verify that a bike can be edited*/
		   assertTrue(biketest.editBike(bikeEdit));
		  } catch (Exception e) {
		   System.out.println("Error test EditBike()");
		  }
		 }
		 
		 @Test
		 public void testDeleteBike() {
		  /*Verify that a bike can be deleted */
		  biketest = new BikeDAO();
		  bike.setBikeId(4001);
		  int delete = biketest.deleteBike(bike);
		
		  assertEquals(0, delete);
		
		  /*Verify that a bike that doesn't exist, can't be deleted */
		  bike.setBikeId(32423);
		  delete = biketest.deleteBike(bike);
		  assertEquals(2, delete);
		 }
	
	 @Test
	 public void testAddBike() {
	 biketest = new BikeDAO();
	 aType.setTypeId(4);
	  DockingStation dstest1 = new DockingStation(1, "Lade stasjon", 63.445900, 10.441570, 20);
	 
	
	
	  /* Verify that a bike can be added */
	  assertTrue(biketest.addBike(bike, dstest1));
	 }
	
	 @Test
	
	 public void testfindBike() {
	  try {
	
	 /* Verify that the  bike can be found*/
	   assertTrue(biketest.findBike(4000));
	
	   /* Verify that the  bike can't be found " Doesn't exist in the database*/
	
	   assertFalse(biketest.findBike(92032903));
	
	  } catch (Exception e) {
	   System.out.println("Error at test findBike");
	  }
	 }
	
	 @Test
	 public void testGetBike() {
	  aType = new BikeType("Terrain bike", 100);
	  aType.setTypeId(1);
	  bike = new Bike(LocalDate.of(2018, 3, 22), 3000, "Ferarri", aType, 1);
	  bike.setBikeId(1000);
	
	  Bike bikeRes = biketest.getBike(1000);
	  assertTrue(bikeRes.equals(bike));
	
	 }
	
	 @Test
	 public void testAddBikeData() {
	
	  BikeData bikedata = new BikeData(4000, LocalDateTime.now(), 64.0, 3.0, 23.0);
	  assertTrue(biketest.addBikeData(bikedata, 2));
	 }
		@Test
		public void testGetRecentData (){
		
			bike.setBikeId(2001);	
			
			BikeData bikeDataRes= biketest.getRecentData(bike);
	
			LocalDate currentDate = LocalDate.of(2018, 3, 22);
			LocalTime currentTime = LocalTime.of(13,32); 
			LocalDateTime  fromDateAndTime = LocalDateTime.of(currentDate, currentTime);
			BikeData bikedata = new BikeData(2001, fromDateAndTime, 63.427548, 10.458589, 96.0);
			
			assertTrue(bikedata.toString().equals(bikeDataRes.toString()));
		}
	 	
}