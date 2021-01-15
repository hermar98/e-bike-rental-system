package main.java.com.team15.ebrs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import main.java.com.team15.ebrs.data.*;
import main.java.com.team15.ebrs.util.DatabaseConnector;

/**
 * Data Access Object class for Repair, RequestedRepair and DoneRepair. Provides necessary database interaction.
 * @author Team 15
 * @see Repair
 * @see RequestedRepair
 * @see DoneRepair
 */
public class RepairDAO {
	private final String GET_AUTO_INCREMENT = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'hermanrm' AND TABLE_NAME = 'repair'";
    private final String INSERT = "INSERT INTO repair(date_sent, request_desc,bike_id) VALUES(?, ?, ?)";
    private final String INSERT_DONE = "INSERT INTO done_repair(repair_id, price, date_recieved, repair_desc) VALUES(?,?,?,?)";
    private final String UPDATE = "UPDATE repair SET date_sent=?,request_desc=?,bike_id=? WHERE repair_id=?";
    private final String UPDATE_DONE = "UPDATE done_repair SET price=?, date_recieved=?, repair_desc=? WHERE repair_id=?";
    private final String DELETE = "UPDATE repair SET active=? WHERE repair_id=?";
    private final String FIND_BY_ID = "SELECT * FROM repair WHERE repair_id=?";
    private final String FIND_DONE_BY_ID = "SELECT * FROM done_repair WHERE repair_id=?";
    private final String FIND_ALL_REPAIRS = "SELECT * FROM repair WHERE bike_id=?";
    private final String FIND_ALL_REQUEST_DESC = "SELECT request_desc FROM repair";
    private final String GET_ALL_DONE_REPAIRS_BY_BIKE_ID = "SELECT * FROM repair JOIN done_repair ON repair.repair_id = done_repair.repair_id WHERE active = 1 AND bike_id = ";
    private final String GET_ALL_REQUESTED_REPAIRS_BY_BIKE_ID = "SELECT * FROM repair LEFT JOIN done_repair ON repair.repair_id = done_repair.repair_id WHERE done_repair.repair_id IS NULL AND active = 1 AND repair.bike_id = ";
    private final String DONE_REPAIR_BY_ID = "SELECT * FROM repair JOIN done_repair ON repair.repair_id = done_repair.repair_id WHERE repair.repair_id =";
    private final String REQUESTED_REPAIR_BY_ID = "SELECT * FROM repair LEFT JOIN done_repair ON repair.repair_id = done_repair.repair_id WHERE done_repair.repair_id IS NULL AND repair.repair_id =";

    /**
     * Return a Repair matching the specific repairId. Return null if the bikeId is not found.
     * @param repairId This is the id of the repair in the database.
     * @return The Repair object with the given repairId
     * @throws Exception if database access error or other errors occurs
     */
    public Repair getRepair(int repairId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        Repair repair = null;

        try {
            conn = DatabaseConnector.getConnection();
            stmt = conn.prepareStatement(FIND_BY_ID);
            stmt.setInt(1,repairId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int repair_id= rs.getInt("repair_id");
                LocalDate date = rs.getDate("date_sent").toLocalDate();

                String request_desc= rs.getString("request_desc");
                repair= new RequestedRepair(repair_id,date,request_desc);

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            stmt.close();
            conn.close();
        }

        return repair;
    }

    /**
     * Return descriptions of all the repairs.
     * @return a String Arraylist with the descriptions
     * @throws Exception if database access error or other errors occurs
     */
    public ArrayList<String> getAllRepairDesc() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ArrayList<String> desc = new ArrayList<> ();

        try {
            conn = DatabaseConnector.getConnection();
            stmt = conn.prepareStatement(FIND_ALL_REQUEST_DESC );

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                desc.add(rs.getString("request_desc"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("The requests were denied ");
        } finally {
            stmt.close();
            conn.close();
        }

        return desc;
    }

    /**
     * Return all the repairs of a specific bike, including repairs requested and repairs done.
     * @param bike This is the bike you want to get its entire repair list
     * @return a Repair ArrayList with the repairs
     */
    public ArrayList<Repair> getAllRepairs (Bike bike) {
        ArrayList<DoneRepair> doneRepair = getAllDoneRepair(bike);
        ArrayList<Repair> requestedRepair = getAllRequestedRepair(bike);

        //System.out.println(doneRepair);
        //System.out.println(requestedRepair);

        ArrayList<Repair> allRepair = requestedRepair;
        for(DoneRepair aRepair: doneRepair){
            allRepair.add(aRepair);
        }
        return allRepair;
    }

    /**
     * Return all the repairs done of a specific bike.
     * @param bike This is the bike you want to get its repair done list
     * @return a ArrayList with DoneRepair
     */
    public ArrayList<DoneRepair> getAllDoneRepair (Bike bike) {
        ArrayList<DoneRepair> repairs= new ArrayList<>();
        try(
                Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_ALL_DONE_REPAIRS_BY_BIKE_ID + bike.getBikeId());
                ResultSet rs = ps.executeQuery()){
            //System.out.println(bike.getBikeId());
            //System.out.println("Connection");
            while (rs.next()) {
                int repairId = rs.getInt("repair_id");
                double price = rs.getDouble("price");
                LocalDate dateSent = rs.getDate("date_sent").toLocalDate();
                LocalDate dateRecieved = rs.getDate("date_recieved").toLocalDate();
                String requestDesc = rs.getString("request_desc");
                String repairDesc = rs.getString("repair_desc");
                
                DoneRepair repair = new DoneRepair(repairId, price, dateSent, dateRecieved, requestDesc, repairDesc);
                repairs.add(repair);
            }
        } catch (Exception e) {
            System.out.println("Exception in getAllDoneRepair: " + e);
        }
        return repairs;
    }

    /**
     * Return all the repairs requested of a specific bike.
     * @param bike This is the bike you want to get its repair requested list
     * @return a ArrayList with RequestedRepair
     */
    public ArrayList<Repair> getAllRequestedRepair (Bike bike) {
        ArrayList<Repair> repairs= new ArrayList<>();
        try(
                Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_ALL_REQUESTED_REPAIRS_BY_BIKE_ID + bike.getBikeId());
                ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                int repairId = rs.getInt("repair_id");
                LocalDate dateSent = rs.getDate("date_sent").toLocalDate();
                String requestDesc = rs.getString("request_desc");
                RequestedRepair repair = new RequestedRepair(repairId, dateSent, requestDesc);
                repairs.add(repair);
            }
        } catch (Exception e) {
            System.out.println("Exception in getAllRequestedRepair: " + e);
        }
        return repairs;
    }

    /**
     * Return the repair are done by the id of the repair.
     * @param rID This is the id of the repair in the database.
     * @return The DoneRepair object with the given repairId
     */
    public DoneRepair getDoneRepair (int rID) {
        //All the columns in the joined tables.
        String cRepairID = "repair_id";
        String cPrice = "price";
        String cDateSent = "date_sent";
        String cDateReceived = "date_recieved";
        String cRequestDesc = "request_desc";
        String cRepairDesc = "repair_desc";

        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(DONE_REPAIR_BY_ID + rID);
            ResultSet rs = ps.executeQuery()){

            if(rs.next()){
                int repairID = rs.getInt(cRepairID);
                int price = rs.getInt(cPrice);
                LocalDate dateSent = rs.getDate(cDateSent).toLocalDate();
                LocalDate dateReceived = rs.getDate(cDateReceived).toLocalDate();
                String requestDesc = rs.getString(cRequestDesc);
                String repairDesc = rs.getString(cRepairDesc);

                DoneRepair repair = new DoneRepair(repairID,price,dateSent,dateReceived,requestDesc,repairDesc);
                return repair;
            }
        }catch(Exception e){
            System.out.println("Exception in getDoneRepair: " + e);
        }
        return null;
    }

    /**
     * Return the repair are requested by the id of the repair.
     * @param rID This is the id of the repair in the database.
     * @return The RequestedRepair object with the given repairId
     */
    public RequestedRepair getRequestedRepair (int rID) {
        //All the columns in the joined tables.
        String cRepairID = "repair_id";
        String cDateSent = "date_sent";
        String cRequestDesc = "request_desc";

        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(REQUESTED_REPAIR_BY_ID + rID);
            ResultSet rs = ps.executeQuery()){

            if(rs.next()){
                int repairID = rs.getInt(cRepairID);
                LocalDate dateSent = rs.getDate(cDateSent).toLocalDate();
                String requestDesc = rs.getString(cRequestDesc);

                RequestedRepair repair = new RequestedRepair(repairID,dateSent,requestDesc);
                return repair;
            }
        }catch(Exception e){
            System.out.println("Exception" + e);
        }
        return null;
    }

    /**
     * This method is used to add a record of repair to the database.
     * @param repair the Repair object you want to add
     * @param bike the bike which the repair belongs to
     * @return Return true if the operation is completed successfully. Return false if it fails.
     * @throws Exception if database access error or other errors occurs
     */
    public int addRepair(Repair repair,Bike bike) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;
        
        int currentAutoIncrement = 0;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);
            
            stmt = conn.prepareStatement(GET_AUTO_INCREMENT);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	currentAutoIncrement = rs.getInt("AUTO_INCREMENT");
            }
            
            stmt2 = conn.prepareStatement(INSERT);
            stmt2.setDate(1, Date.valueOf(repair.getDateSent()));
            stmt2.setString(2, repair.getRequestDesc());
            stmt2.setInt(3, bike.getBikeId());
            stmt2.executeUpdate();
            
            if (repair instanceof DoneRepair) {
            	DoneRepair dRepair = (DoneRepair) repair;
            	stmt3 = conn.prepareStatement(INSERT_DONE);
            	stmt3.setInt(1, currentAutoIncrement);
            	stmt3.setDouble(2, dRepair.getPrice());
            	stmt3.setDate(3, Date.valueOf(dRepair.getDateReceived()));
            	stmt3.setString(4, dRepair.getRepairDesc());
                stmt3.executeUpdate();
            }
            
            conn.commit();
            conn.setAutoCommit(true);
            return currentAutoIncrement;

        } catch (SQLException e) {
        	conn.rollback();
            e.printStackTrace();
        } finally {
        	if (rs != null) {
        		rs.close();
        	}
        	if (stmt != null) {
        		stmt.close();
        	}
        	if (stmt2 != null) {
        		stmt2.close();
        	}
        	if (stmt3 != null) {
        		stmt3.close();
        	}
        	if (conn != null) {
        		conn.close();
        	}
        }
        return -1;
    }

    /**
     * This method is used to update a record of repair in the database.
     * @param repair the new Repair object which you want to update
     * @param bike the bike which the repair belongs to
     * @throws Exception if database access error or other errors occurs
     */
    public void updateRepair(Repair repair, Bike bike) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);
            
            if (repair instanceof DoneRepair) {
            	DoneRepair dRepair = (DoneRepair) repair;
            	if (getDoneRepair(repair.getRepairId()) == null) {
            		stmt3 = conn.prepareStatement(INSERT_DONE);
                	stmt3.setInt(1, dRepair.getRepairId());
                	stmt3.setDouble(2, dRepair.getPrice());
                	stmt3.setDate(3, Date.valueOf(dRepair.getDateReceived()));
                	stmt3.setString(4, dRepair.getRepairDesc());
                    stmt3.executeUpdate();
            	} else {
            		stmt2 = conn.prepareStatement(UPDATE_DONE);
	            	stmt2.setDouble(1, dRepair.getPrice());
	                stmt2.setDate(2, Date.valueOf(dRepair.getDateReceived()));
	                stmt2.setString(3, dRepair.getRepairDesc());
	                stmt2.setInt(4, dRepair.getRepairId());
	                stmt2.executeUpdate();
            	}
            }
            
            stmt3 = conn.prepareStatement(UPDATE);
            stmt3.setDate(1, Date.valueOf(repair.getDateSent()));
            stmt3.setString(2, repair.getRequestDesc());
            stmt3.setInt(3, bike.getBikeId());
            stmt3.setInt(4, repair.getRepairId());
            stmt3.executeUpdate();
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
        	conn.rollback();
            e.printStackTrace();
        } finally {
        	if (stmt != null) {
        		stmt.close();
        	}
            if (stmt2 != null) {
            	stmt2.close();
            }
            if (conn != null) {
            	conn.close();
            }
        }

    }

    /**
     * This method is used to delete a record of repair in the database.
     * @param repair the Repair object which you want to delete
     * @throws Exception if database access error or other errors occurs
     */
    public void deleteRepair(Repair repair) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnector.getConnection();
            stmt = conn.prepareStatement(DELETE);
            stmt.setInt(1, 0);
            stmt.setInt(2, repair.getRepairId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if (stmt != null) {
        		stmt.close();
        	}
        	if (conn != null) {
        		conn.close();
        	}
        }

    }
}
