package main.java.com.team15.ebrs.dao;

import main.java.com.team15.ebrs.data.EconomyData;
import main.java.com.team15.ebrs.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
/**
 * Data Access Object class for EconomyData. Provides necessary database interaction regarding the specified objects.
 *
 * @author Team 15
 * @see EconomyData
 * @see DatabaseConnector
 *
 */
public class EconomyDAO {
    private final String SELECT_ALL_BIKE_COSTS = "SELECT * FROM bike_costs";
    private final String SELECT_ALL_REPAIR_COSTS = "SELECT * FROM repair_costs";
    private final String SELECT_ALL_BIKE_INCOME = "SELECT * FROM bike_income";

    /**
     * Returns an ArrayList of EconomyData which it gets from the method getAllEconomy() using the parameter select = 0.
     * @see EconomyData
     * @return an ArrayList of EconomyData from getAllEconomy()
     */
    public ArrayList<EconomyData> getAllBikeCosts () {
        return getAllEconomy(0);
    }

    /**
     * Returns an ArrayList of EconomyData which it gets from the method getAllEconomy() using the parameter select = 1.
     * @see EconomyData
     * @return an ArrayList of EconomyData from getAllEconomy()
     */
    public ArrayList<EconomyData> getAllRepairCosts () {
        return getAllEconomy(1);
    }

    /**
     * Returns an ArrayList of EconomyData which it gets from the method getAllEconomy() using the parameter select = 2.
     * @see EconomyData
     * @return an ArrayList of EconomyData from getAllEconomy()
     */
    public ArrayList<EconomyData> getAllBikeIncome () {
        return getAllEconomy(2);
    }

    /**
     * This is a private method which is used by getAllBikeCosts(), getAllRepairCosts and getAllBikeIncome()
     * to request information from a database, with help from DatabaseConnector.
     *
     * This is most of all a helper method for the three public methods in this class.
     *
     * @param select an integer used to choose what Select-statement to use.
     * @see DatabaseConnector
     * @see EconomyData
     * @return an ArrayList of EconomyData with data from the database
     */
    private ArrayList<EconomyData> getAllEconomy (int select) {
        ArrayList<EconomyData> eList = new ArrayList<>();
        String SELECT = SELECT_ALL_BIKE_INCOME;
        String cValue = "income";
        switch (select) {
            case 0:
                //System.out.println("test1");
                SELECT = SELECT_ALL_BIKE_COSTS;
                cValue = "costs";
                break;
            case 1:
                //System.out.println("test2");
                SELECT = SELECT_ALL_REPAIR_COSTS;
                cValue = "costs";
                break;
            case 2:
                //System.out.println("test3");
                SELECT = SELECT_ALL_BIKE_INCOME;
                cValue = "income";
                break;
        }

        try(Connection con = DatabaseConnector.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                int year = rs.getInt("year");
                int month = rs.getInt("month");
                int value = rs.getInt(cValue);
                EconomyData eco = new EconomyData(year,month,value);
                eList.add(eco);
            }
        }catch(Exception e){
            System.out.println("Exception in getAll: " + e);
        }
        return eList;
    }
}
