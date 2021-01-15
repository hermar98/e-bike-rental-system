package test.java.com.team15.ebrs.dao;

import static org.junit.jupiter.api.Assertions.*;

import main.java.com.team15.ebrs.dao.BikeDAO;
import main.java.com.team15.ebrs.dao.RepairDAO;
import main.java.com.team15.ebrs.data.*;
import main.java.com.team15.ebrs.util.DatabaseConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

class RepairDAOTest {
	private static RepairDAO repairDAO;
	private static BikeDAO bikeDAO;
	Repair r1 = new Repair(1, LocalDate.of(2018, 03, 22),"Handlebar");
	Repair r2 = new Repair(2, LocalDate.of(2018, 03, 20),"Front wheel");
	Repair r3 = new Repair(3, LocalDate.of(2018, 03, 18),"Chain");
	Repair r4 = new Repair(4, LocalDate.of(2018, 03, 16),"Battery");

	DoneRepair dr1 = new DoneRepair(3,200,LocalDate.of(2018, 03, 18),LocalDate.of(2018, 03, 20),"Chain","Repair OK");

	private static Repair repair = new Repair(LocalDate.now(),"Test");


	@BeforeAll
	static void setUpBeforeClass(){
		try {
			DatabaseConnector.useTestDatabase(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		repairDAO = new RepairDAO();
		bikeDAO = new BikeDAO();
	}


	@AfterAll
	static void tearDownAfterClass() throws Exception {
		try {
			DatabaseConnector.resetDatabase();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void compareRepair(Repair r1, Repair r2) {
		assertEquals(r1.getDateSent(),r2.getDateSent());
		assertEquals(r1.getRequestDesc(),r2.getRequestDesc());
	}

	private void compareDoneRepair(DoneRepair r1, DoneRepair r2) {
		assertEquals(r1.getDateSent(),r2.getDateSent());
		assertEquals(r1.getRequestDesc(),r2.getRequestDesc());
		assertEquals(r1.getPrice(),r2.getPrice());
		assertEquals(r1.getRepairDesc(),r2.getRepairDesc());
		assertEquals(r1.getDateReceived(),r2.getDateReceived());
		assertEquals(r1.getRequestDesc(),r2.getRequestDesc());

	}

	@Test
	void testGetRepair() throws Exception {
		compareRepair(r1,repairDAO.getRepair(1));
	}

	@Test
	void testGetAllRepairDesc() throws Exception {
		ArrayList<String> desclist = repairDAO.getAllRepairDesc();
		ArrayList<String> desclist1 = new ArrayList<String>();
		desclist1.add(r1.getRequestDesc());
		desclist1.add(r2.getRequestDesc());
		desclist1.add(r3.getRequestDesc());
		desclist1.add(r4.getRequestDesc());
		for(int i =0;i<4;i++) {
			assertEquals(desclist.get(i),desclist1.get(i));
		}
	}

	@Test
	void testGetAllRepairs()throws Exception {
		Bike bike= bikeDAO.getBike(1000);
		ArrayList<Repair> repairlist= repairDAO.getAllRepairs(bike);
		compareRepair(r1,repairlist.get(0));
	}

	@Test
	void testGetAllDoneRepair() throws Exception{
		Bike bike= bikeDAO.getBike(3000);
		ArrayList<DoneRepair> doneRepairs= repairDAO.getAllDoneRepair(bike);
		compareDoneRepair(dr1,doneRepairs.get(0));
	}

	@Test
	void testGetAllRequestedRepair() throws Exception{
		Bike bike= bikeDAO.getBike(1000);
		ArrayList<Repair> requestedRepairs= repairDAO.getAllRequestedRepair(bike);
		compareRepair(r1,requestedRepairs.get(0));

	}

	@Test
	void testGetDoneRepair() throws Exception{
		compareDoneRepair(dr1,repairDAO.getDoneRepair(3));
	}

	@Test
	void testGetRequestedRepair() throws Exception{
		compareRepair(r1,repairDAO.getRequestedRepair(1));
		assertNull(repairDAO.getRequestedRepair(3));

	}

	@Test
	void testAddRepair() throws Exception {
		Bike bike = bikeDAO.getBike(1000);
		repairDAO.addRepair(repair, bike);
		ArrayList<Repair> repairlist= repairDAO.getAllRepairs(bike);
		Repair repair1= repairlist.get(repairlist.size()-1);
		compareRepair(repair,repair1);
		repairDAO.deleteRepair(repair1);
	}

	@Test
	void testUpdateRepair() throws Exception {
		Bike bike = bikeDAO.getBike(1000);
		repairDAO.addRepair(repair, bike);
		ArrayList<Repair> repairlist= repairDAO.getAllRepairs(bike);
		Repair repair1= repairlist.get(repairlist.size()-1);
		Repair repairU = new Repair(repair1.getRepairId(),LocalDate.of(2018, 04, 01),"TestUpdate");
		repairDAO.updateRepair(repairU,bike);
		ArrayList<String> desclist = repairDAO.getAllRepairDesc();
		assertEquals("TestUpdate",desclist.get(desclist.size()-1));
		repairDAO.deleteRepair(repairU);
	}

	@Test
	void testDeleteRepair() throws Exception {
		Bike bike = bikeDAO.getBike(1000);
		repairDAO.addRepair(repair, bike);
		ArrayList<Repair> repairlist= repairDAO.getAllRepairs(bike);
		Repair repair1= repairlist.get(repairlist.size()-1);
		repairDAO.deleteRepair(repair1);
		ArrayList<Repair> repairlist1= repairDAO.getAllRepairs(bike);
		assertEquals(repairlist.size(),repairlist1.size()+1);
	}


}