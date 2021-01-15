
package main.java.com.team15.ebrs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import main.java.com.team15.ebrs.data.BikeType;
import main.java.com.team15.ebrs.util.DatabaseConnector;
/**
 * Data Access Object class for BikeType. Provides necessary database interaction regarding the specified objects.
 * @author Team 15
 * @see BikeType
 */

public class TypeDAO {
    private ArrayList<BikeType> typelist = new ArrayList<>();
    
    private final String ALL_TYPES = "SELECT * FROM bike_type WHERE active = TRUE";
    private final String TYPE_BY_ID = "SELECT * FROM bike_type WHERE type_id = ";
    private final String INSERT = "INSERT INTO bike_type (type_name, rental_price, active) VALUES (?,?, DEFAULT)";
    private final String UPDATE = "UPDATE bike_type SET type_name = ?, rental_price = ? WHERE type_id = ?";
    private final String DELETE_TYPE = "UPDATE bike_type SET ACTIVE = FALSE WHERE type_name = ? AND rental_price = ?";
    private final String DELETE_BIKES = "UPDATE bike SET active = FALSE WHERE type_id = ?";
    private final String DELETE_POSSIBLE = "DELETE FROM bike_type where type_id = ?";
    private final String BIKE_CHECK = "SELECT * FROM bike WHERE type_id = ";
    private final String DELETE_BY_ID = "UPDATE bike_type SET ACTIVE = FALSE WHERE type_id = ?";
    private final String FIND_TYPE = "SELECT * FROM bike_type WHERE type_name = ? AND rental_price = ?";
    
    //Coloumn labels
    String cTypeId = "type_id";
    String cTypeName = "type_name";
    String cRentalPrice = "rental_price";

    /**
     * Returns ArrayList contatining all biketypes registered in the database, unless they have active BIT set to false.
     * @return ArrayList of BikeType objects.
     */
    public ArrayList<BikeType> getAllTypes(){
            typelist = new ArrayList<>();

            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(ALL_TYPES);
                ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                   BikeType type = new BikeType(rs.getString(cTypeName),rs.getDouble(cRentalPrice));
                   type.setTypeId(rs.getInt(cTypeId));
                   typelist.add(type);
                }

            }catch(Exception e){
                System.out.println("Exception: " + e);
            }
        return typelist;
    }

    /**
     * Returns a BikeType object from the database matching the specified typeId.
     * @param typeId The typeID for the BikeType object you want to retrieve.
     * @return BikeType object from the database.
     */
    public BikeType getType(int typeId){
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(TYPE_BY_ID + typeId);
            ResultSet rs = ps.executeQuery()){
            
            if(rs.next()){
                BikeType type = new BikeType(rs.getString(cTypeName),rs.getDouble(cRentalPrice));
                type.setTypeId(rs.getInt(cTypeId));
                return type;
            }

            }catch(Exception e){
                System.out.println("Exception: " + e);
            }
        return null;
    }

    /**
     * Adds a BikeType to the database. Since typeId is autoincrement this will be ignored if it's already set for the object.
     * @param type The BikeType object that is being added.
     * @return Int stating whether the registration was successful or not. 0 - if added, 1 - if not added, 2 - if already registered.
     */
    public int addType(BikeType type){
        final int ADDED = 0;
        final int NOT_ADDED = 1;
        final int ALREADY_REGISTERED = 2;
        
        if(findType(type)){
            return ALREADY_REGISTERED; //Type already registered
        }else{
            
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(INSERT)){
                
                ps.setString(1, type.getTypeName());
                ps.setDouble(2, type.getRentalPrice());
                ps.executeUpdate();
                return ADDED;
             }catch(Exception e){
                 System.out.println("Exception: " + e);
             }
        }
        return NOT_ADDED;
    }

    /**
     * Edits a BikeType in the database. Edits type name and rental price for the type matching the typeId.
     * @param type The edited BikeType you want added to the database.
     * @return Int stating whether the editing was successful or not. 0 - if edited, 1 - if not edited, 2 - not found.
     */
    public int editType(BikeType type){
        final int EDITED = 0;
        final int NOT_EDITED = 1;
        final int NOT_FOUND = 2;
        if(!findType(type.getTypeId())){ //No matching type_id registered
            return NOT_FOUND;
        }else{   
            
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE)){
                
                ps.setString(1, type.getTypeName());
                ps.setDouble(2, type.getRentalPrice());
                ps.setInt(3, type.getTypeId());
                ps.executeUpdate();
                return EDITED;
            }catch(Exception e){
                System.out.println("Exception: " + e);
            }
        }
        return NOT_EDITED;
    }

    /**
     * Sets active BIT to false on the BikeType in the database mathing the rental price and the type name. Ignores typeId.
     * @param type The BikeType you want deleted.
     * @return Int stating whether the deletion was successful or not. 0 - if "deleted", 1 - if not "deleted", 2 - not found.
     */
    public int deleteType(BikeType type){ //Deletes the type according to name and price OBSOLETE?
        final int DELETED = 0;
        final int NOT_DELETED = 1;
        final int NOT_FOUND = 2;
        
        if(!findType(type)){//Type not in database
            return NOT_FOUND;
        }else{
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE_TYPE)){
                
                ps.setString(1, type.getTypeName());
                ps.setDouble(2, type.getRentalPrice());
                ps.executeUpdate();
                return DELETED;
            }catch(Exception e){
                System.out.println("Exception: " + e);
            }
        }
        return NOT_DELETED;
    }

    /**
     * Sets the active BIT to false in the database for the BikeType matching the specified typeId.
     * @param typeId The typeId for the type you want "deleted"
     * @return Int stating whether the deletion was successful or not. 0 - if "deleted", 1 - if not "deleted", 2 - not found.
     */
    public int deleteType(int typeId){ //Deletes the type according to the typeID
        final int DELETED = 0;
        final int NOT_DELETED = 1;
        final int NOT_FOUND = 2;
        
        if(!findType(typeId)){//TypeId not in database
            return NOT_FOUND;
        }else{
            try(Connection con = DatabaseConnector.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE_BIKES);
                PreparedStatement ps2 = con.prepareStatement(DELETE_BY_ID);
                PreparedStatement ps3 = con.prepareStatement(DELETE_POSSIBLE);
                PreparedStatement ps4 = con.prepareStatement(BIKE_CHECK + typeId);
                ResultSet rs = ps4.executeQuery()){
                con.setAutoCommit(false);
                if(rs.next()){
                    ps.setInt(1, typeId);
                    ps2.setInt(1, typeId);
                    ps.executeUpdate();
                    ps2.executeUpdate();
                }else{
                    ps3.setInt(1, typeId);
                    ps3.executeUpdate();
                }
                con.commit();
                con.setAutoCommit(true);
                return DELETED;
            }catch(Exception e){
                System.out.println("Exception: " + e);
            }
        }
        return NOT_DELETED;
    }

    /**
     * Checks if a BikeType with matching type name and rental price is registered. This method ignores typeId.
     * @param type The desired BikeType to check for.
     * @return Boolean stating whether the type was found.
     */
    public boolean findType(BikeType type){ //Checks if the same name and rental price is registered - true if it's found
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(FIND_TYPE)){
            
            ps.setString(1, type.getTypeName());
            ps.setDouble(2, type.getRentalPrice());
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                return true;
            }
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }
        return false;
    }

    /**
     * Checks if a BikeType with the specified typeId is registered in the database.
     * @param typeId The typeId for the BikeType you want to find.
     * @return Boolean stating whether the typeId was found.
     */
    public boolean findType(int typeId){ //Checks if the typeId is registered - true if it's found
        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(TYPE_BY_ID + typeId);
            ResultSet rs = ps.executeQuery()){
            
            if(rs.isBeforeFirst()){ //If the resultset has data
                return true;
            }
        }catch(Exception e){
            System.out.println("Exception: " + e);
        }    
        return false;
    }
}
