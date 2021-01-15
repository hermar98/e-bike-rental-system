package main.java.com.team15.ebrs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

//import package
import main.java.com.team15.ebrs.data.DockingStation;
import main.java.com.team15.ebrs.util.DatabaseConnector;
import main.java.com.team15.ebrs.data.DockingStationData;


/**
 * Data Access Object class for DockingStation. Provides necessary database interaction regarding the specified objects.
 * @author Team 15
 * @see DockingStation
 *
 */
public class DockingStationDAO {
	
	private ArrayList<DockingStation> DList = new ArrayList<DockingStation>();
	private final String ALL_DockingStaion = "Select * From docking_station where active = true ";
	private final String Add_DockingStation = "Insert ignore into docking_station (station_name, coords_lat, coords_lng,total_docks) values (?,?,?,?)";
	private final String DockingStationByID = "Select * From docking_station Where station_id =";
	private final String DockingStationByName = "Select * From docking_station Where station_name = ";
	private final String Delete_DockingStation = "Delete from docking_station where station_id =";
	private final String Delete_DockingStationWithData = "Update docking_station SET active = false where station_id = ";
	private final String DockingStationWithData = " Select * From station_data where station_id = ";
	private final String ChangeStationName = "Update docking_station SET station_name = ? where station_id = ?";
	private final String ChangeStationCoordinate = "Update docking_station SET coords_lat = ?,coords_lng = ? where station_id = ?";
	private final String ChangeDocks = "Update docking_station Set total_docks = ? where station_id = ?";
	private final String PowerUsage = "SELECT power_usage fROM station_data WHERE station_id= ";
	private final String MAX_DATE = " and date_time=(SELECT MAX(date_time) FROM station_data)";
        private final String ADD_DATA = "INSERT INTO station_data (station_id, date_time, power_usage) VALUES (?,?,?)";
	
	
	/**
	 * Returns an ArrayList containing all of the available docking stations in the database.
	 *
	 * @return ArrayList with DockingStation objects
	 */
	public ArrayList<DockingStation> getAllDockingStations(){	
		
		String cDcID = "station_id";
		String cDcName = "station_name";
		String cCoordinateLat = "coords_lat";
		String cCoordinatelng = "coords_lng";
		String cTotalDocks = "total_docks";
		 try(
		            Connection con = DatabaseConnector.getConnection();
		            PreparedStatement ps = con.prepareStatement(ALL_DockingStaion);
		            ResultSet rs = ps.executeQuery()
		            ){
		            
		            while(rs.next()){
		                int DcID = rs.getInt(cDcID);
		                String DcName = rs.getString(cDcName);
		                double CoordinateLat = rs.getDouble(cCoordinateLat);
		                double Coordinatelng = rs.getDouble(cCoordinatelng);
		                int TotalDocks = rs.getInt(cTotalDocks);
		                

		                DockingStation dockingstation =  new DockingStation(DcID,DcName,CoordinateLat,Coordinatelng,TotalDocks);
		                DList.add(dockingstation);
		            }
		            
		        }catch(Exception e){
		            System.out.println("Exception: " + e);
		        }
		    return DList;
	}
	
	
	/**
	 * Appends a new docking station to the database
	 * 
	 * @param dockingstation new docking station to be appended to the database
	 * @return true if is an available and unique docking station object - if the name of docking station is already existed ,return false
	 */
	public boolean addDockingStation(DockingStation dockingstation) {
	//table docking_station should be unique
		if(findStationByName(dockingstation.getStationName())) {
			return false;
		}else {
			try(
			  Connection con = DatabaseConnector.getConnection();
			  PreparedStatement ps = con.prepareStatement(Add_DockingStation)
			  ){
				ps.setString(1, dockingstation.getStationName());
				ps.setDouble(2, dockingstation.getCoordinateLat());
				ps.setDouble(3, dockingstation.getCoordinateLng());
				ps.setInt(4, dockingstation.getAntDocks());
				ps.executeUpdate();
				return true;
			}catch(Exception e) {
				System.out.println("Exception: " + e);
			}
		}
			return false;
		}
	
	
	/**
	 * Removes the docking station from the database. If the database does not contain the docking station, it is unchanged
	 * If the docking station contains data, the method will change the status of docking station instead of remove it from database.
	 * 
	 * @param dockingstation the docking station to be removed from the database, if present.
	 * @return true if the database contained the specified docking station. False if the docking station is not existed.
	 */
	public boolean deleteDockingStation(DockingStation dockingstation) {
		if(!findStationByID(dockingstation)) {
			return false;
		}else if(!findStationWithData(dockingstation)){
			try(Connection con = DatabaseConnector.getConnection();
				PreparedStatement ps = con.prepareStatement(Delete_DockingStation+dockingstation.getStationId())
			){
				ps.executeUpdate();
				return true;
			}catch(Exception e) {
				System.out.println("Exception: " + e);			
				}
			}else if(findStationWithData(dockingstation)){
			try(
				Connection con = DatabaseConnector.getConnection();
				PreparedStatement ps = con.prepareStatement(Delete_DockingStationWithData+dockingstation.getStationId())
				){
				ps.executeUpdate();
				return true;
				}catch(Exception e) {
					System.out.println("Exception: " + e);				}
		}
		return false;
	}

	
	
	
	/**
	 * Returns true if the docking station contains data
	 *
	 * @param dockingstation the docking station that presence in this list is to be tested
	 * @return true if the the specified docking station contains data
	 */
	public boolean findStationWithData(DockingStation dockingstation) {
		try(
	            Connection con = DatabaseConnector.getConnection();
	            PreparedStatement ps = con.prepareStatement(DockingStationWithData + dockingstation.getStationId());
	            ResultSet rs = ps.executeQuery();
	            ){
	            if(rs.isBeforeFirst()){
	                return true;
	            }
	        }catch(Exception e){
	            System.out.println("Exception: " + e);
	        }    
	        return false;
	}
	 
   
	/**
	 * Returns true if the database contains the specified docking station 
	 * 
	 * @param dockingstation the docking station that presence in this list is to be tested
	 * @return true if the database contains the specified docking station 
	 */
    public boolean findStationByID(DockingStation dockingstation){ 
        try(
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(DockingStationByID + dockingstation.getStationId());
        		//depends on what will the stationid to be 
        	    //PreparedStatement ps = con.prepareStatement(DockingStationByName + dockingstationsname);
            ResultSet rs = ps.executeQuery();
            ){
            if(rs.isBeforeFirst()){
                return true;
            }
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }    
        return false;
    }
    

    
    
    /**
     * Returns true if the database contains the specified docking station.
     * 
     * @param dockingstationsname the name the docking station
     * @return true if the database contains the docking station with this specified name.
     */
    public boolean findStationByName(String dockingstationsname) {
    		try(
    			Connection con = DatabaseConnector.getConnection();
    			PreparedStatement ps = con.prepareStatement(DockingStationByName+"\""+dockingstationsname+"\"");
		    ResultSet rs = ps.executeQuery();
    		){
    			if(rs.isBeforeFirst()) {
    			return true;
    			}
    		}catch(Exception e) {
    			System.out.println("Exception: " + e);
    		}
    		return false;
    }
	
    //Edit
    
	/**
	 * Changes the name of the specified docking station in the database.
	 * 
	 * @param dockingstation the specified docking station which will be changed
	 * @param station_nyname the new name of the station
	 * @return true if the name of the docking station changed as a result of the call
	 */
	public boolean changeName(DockingStation dockingstation,String station_nyname) {
		if(!findStationByID(dockingstation)) {
			return false;
		}else {
			try(Connection con = DatabaseConnector.getConnection();
				PreparedStatement ps = con.prepareStatement(ChangeStationName);	
			){
				ps.setString(1, station_nyname);
				ps.setInt(2, dockingstation.getStationId());
				ps.executeUpdate();
				return true;
			}catch(Exception e) {
				
			}
		}
		return false;
	}
	
	
	/**
	 * Changes the coordinate system of the specified docking station 
	 * 
	 * @param dockingstation the specified docking station
	 * @param nycoordinatelat the new latitude coordinate of the station
	 * @param nycoordinatelng the new long coordinate of the station
	 * @return true if the coordinate system of the docking station changed as a result of the call
	 */
	public boolean changeCoordinate(DockingStation dockingstation,double nycoordinatelat,double nycoordinatelng) {
		if(!findStationByID(dockingstation)) {
			return false;
		}else{
			try(Connection con = DatabaseConnector.getConnection();
				PreparedStatement ps = con.prepareStatement(ChangeStationCoordinate);
			){
				ps.setDouble(1, nycoordinatelat);
				ps.setDouble(2, nycoordinatelng);
				ps.setInt(3, dockingstation.getStationId());
				ps.executeUpdate();
				return true;
			}catch(Exception e) {
				
			}
		}
		return false;
	}
	/**
	 * 
	 * Changes the number of docks on the specified docking station
	 * 
	 * @param dockingstation the specified docking station
	 * @param nydocks the new number of docks
	 * @return true if the number of docks of the docking station changed as a result of the call
	 */
	public boolean changeAntDocks(DockingStation dockingstation, int nydocks) {
		if(!findStationByID(dockingstation)) {
			return false;
		}else {
			try(Connection con = DatabaseConnector.getConnection();
					PreparedStatement ps = con.prepareStatement(ChangeDocks);
				){
					ps.setInt(1, nydocks);
					ps.setInt(2, dockingstation.getStationId());
					ps.executeUpdate();
					return true;
				}catch(Exception e) {
					System.out.println(e);
				}
			}
		return false;
	}
	
	
	
	/**
	 * 
	 * Returns the power usage of the specified station 
	 * 
	 * @param dockingstation - the specified docking station
	 * @return the power usage of the specified station
	 */
	public double getPowerUsage(DockingStation dockingstation) {
		String cPower = "power_usage";
		if(!findStationByID(dockingstation)) {
			return -1;
		}else {
			int powerUsage = 0;
			try(Connection con = DatabaseConnector.getConnection();
				PreparedStatement ps = con.prepareStatement(PowerUsage + dockingstation.getStationId()+MAX_DATE);
				ResultSet rs = ps.executeQuery();
				){
			   
				while(rs.next()) {
					powerUsage = rs.getInt(cPower);
				}
				
			}catch(Exception e) {
				System.out.println("Exception: " + e);
			}
		 return powerUsage;
		}
	}	
	
	/**
	 * Returns the information of the specified station 
	 * 
	 * @param dockingstationsname - the name of the specified docking station 
	 * @return a string representation is used in JLabel of all the information about the specified station
	 */
	public String getInformationByName(String dockingstationsname) {
		String cDcID = "station_id";
		String cDcName = "station_name";
		String cCoordinateLat = "coords_lat";
		String cCoordinatelng = "coords_lng";
		String cTotalDocks = "total_docks";
		String informasjonwindow = "";
		 try(
		            Connection con = DatabaseConnector.getConnection();
		            PreparedStatement ps = con.prepareStatement(DockingStationByName+"\""+dockingstationsname+"\"");
		            ResultSet rs = ps.executeQuery();
		            ){
		            
		            while(rs.next()){
		                int DcID = rs.getInt(cDcID);
		                String DcName = rs.getString(cDcName);
		                double CoordinateLat = rs.getDouble(cCoordinateLat);
		                double Coordinatelng = rs.getDouble(cCoordinatelng);
		                int TotalDocks = rs.getInt(cTotalDocks);
		                
		                informasjonwindow ="<html>Station ID: " +DcID +"<br>Station Name: " + DcName +"<br>Coordinatelat: "+CoordinateLat+
		                		"<br>Coordinatelng: " +Coordinatelng + "<br>Total docks: " +TotalDocks+ "<br>Total PowerUsage: "+getPowerUsage(getStationByName(dockingstationsname))+"<html>";
		            }
		            
		        }catch(Exception e){
		            System.out.println("Exception: " + e);
		        }
		    return informasjonwindow;
	}
	
	
	
	/**
	 * Returns a docking station that exited in database by the name. 
	 *
	 * @param dockingstationsname - the name of docking station 
	 * @return a docking station as a result of the call
	 */
	public DockingStation getStationByName(String dockingstationsname) {
		String cDcID = "station_id";
		String cDcName = "station_name";
		String cCoordinateLat = "coords_lat";
		String cCoordinatelng = "coords_lng";
		String cTotalDocks = "total_docks";
		DockingStation dockingstation = null;
		 try(
		            Connection con = DatabaseConnector.getConnection();
		            PreparedStatement ps = con.prepareStatement(DockingStationByName+"\""+dockingstationsname+"\"");
		            ResultSet rs = ps.executeQuery();
		            ){
		            
		            while(rs.next()){
		                int DcID = rs.getInt(cDcID);
		                String DcName = rs.getString(cDcName);
		                double CoordinateLat = rs.getDouble(cCoordinateLat);
		                double Coordinatelng = rs.getDouble(cCoordinatelng);
		                int TotalDocks = rs.getInt(cTotalDocks);
		                
		                dockingstation =  new DockingStation(DcID,DcName,CoordinateLat,Coordinatelng,TotalDocks);
		            }
		            
		        }catch(Exception e){
		            System.out.println("Exception: " + e);
		        }
		    return dockingstation;
		
	}
	
	
	/**
	 * Returns a docking station that exited in database by the Station_ID. 
	 *
	 * @param dockingstationsid - the id of docking station 
	 * @return a docking station as a result of the call
	 */
	public DockingStation getStationByID(int dockingstationsid) {
		String cDcID = "station_id";
		String cDcName = "station_name";
		String cCoordinateLat = "coords_lat";
		String cCoordinatelng = "coords_lng";
		String cTotalDocks = "total_docks";
		DockingStation dockingstation = null;
		 try(
		            Connection con = DatabaseConnector.getConnection();
		            PreparedStatement ps = con.prepareStatement(DockingStationByID+dockingstationsid);
		            ResultSet rs = ps.executeQuery();
		            ){
		            
		            while(rs.next()){
		                int DcID = rs.getInt(cDcID);
		                String DcName = rs.getString(cDcName);
		                double CoordinateLat = rs.getDouble(cCoordinateLat);
		                double Coordinatelng = rs.getDouble(cCoordinatelng);
		                int TotalDocks = rs.getInt(cTotalDocks);
		                
		                dockingstation =  new DockingStation(DcID,DcName,CoordinateLat,Coordinatelng,TotalDocks);
		            }
		            
		        }catch(Exception e){
		            System.out.println("Exception: " + e);
		        }
		    return dockingstation;
		
	}
        
        /**
         * Inserts dockingStationData into the database.
         * @param dsd Object of DockingStationData you want inserted.
         * @return Boolean stating whether the registration was successful.
         */
        
        public boolean addDockingStationData(DockingStationData dsd){
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(ADD_DATA)){

                ps.setInt(1, dsd.getStationId());
                ps.setTimestamp(2, dsd.getSQLDateTime());
                ps.setDouble(3, dsd.getPowerUsage());
                ps.executeUpdate();
                return true;

            }catch(Exception e) {
            System.out.println("Exception at addBikeData " + e);
            }
            return false;
        }
}
