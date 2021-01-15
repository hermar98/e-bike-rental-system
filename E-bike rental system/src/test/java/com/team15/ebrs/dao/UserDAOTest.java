package test.java.com.team15.ebrs.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.com.team15.ebrs.dao.UserDAO;
import main.java.com.team15.ebrs.data.User;
import main.java.com.team15.ebrs.util.DatabaseConnector;

class UserDAOTest {
	private static UserDAO userdao;
	private static String TestEmail="test@test.no";
	private static User user1 = new User("test@test.no","Trond Jacob","Rondis","/2kOn3LNqLjfdxMMn67d1KwH6aodvjVlBTpphyy8MM0=","zyxUEbv3n6Chgi2uZ+rp2E42zwNs91DRb2Xg6gcGn0M=");
	private 	User user2 = new User("testtest@test.no","Trond Jacob","Rondis","/2kOn3LNqLjfdxMMn67d1KwH6aodvjVlBTpphyy8MM0=","zyxUEbv3n6Chgi2uZ+rp2E42zwNs91DRb2Xg6gcGn0M=");

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		try {
 			DatabaseConnector.useTestDatabase(true);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
		
		userdao = new UserDAO();
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

	
	private void compareUser(User user1, User user2) {
		assertEquals(user1.getEmail().trim(),user2.getEmail().trim());
		assertEquals(user1.getFirstname().trim(),user2.getFirstname());
		assertEquals(user1.getSurname().trim(),user2.getSurname().trim());
		assertEquals(user1.getUserPassword(),user2.getUserPassword());
	}
	@Test
	void testGetAllUsers() {
		ArrayList<User> allusers = userdao.getAllUsers();
		assertEquals(allusers.size(),5);
		compareUser(allusers.get(0),user1);
		
	}

	@Test
	void testGetUser() {
		compareUser(userdao.getUser(TestEmail),user1);
	}

	@Test
	void testAddUser() {
		assertFalse(userdao.addUser(user1));
		assertTrue(userdao.addUser(user2));
		compareUser(userdao.getUser(user2.getEmail()),user2);
		userdao.deleteUser(user2);
	}

	@Test
	void testEditUser() {
		userdao.addUser(user2);
		String nypassword = "hohotest";
		user2.setUserPassword(nypassword);
		user2.setUserSalt(nypassword);
		assertTrue(userdao.editUser(user2));
		compareUser(userdao.getUser(user2.getEmail()),user2);
		userdao.deleteUser(user2);
	}

	@Test
	void testDeleteUser() {
		userdao.addUser(user2);
		assertTrue(userdao.deleteUser(user2));
		assertFalse(userdao.findUser(user2));
	}

	@Test
	void testFindUser() {
		assertTrue(userdao.findUser(user1));
		assertFalse(userdao.findUser(user2));
	}

}
