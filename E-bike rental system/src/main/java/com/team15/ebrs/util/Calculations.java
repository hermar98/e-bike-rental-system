package main.java.com.team15.ebrs.util;

import main.java.com.team15.ebrs.dao.*;
import main.java.com.team15.ebrs.data.*;

import java.util.ArrayList;
/**
 * Used to do different calculations for statistics.
 *
 * @author Team 15
 * @see TripDAO
 * @see Trip
 * @see BikeData
 * @see EconomyDAO
 * @see EconomyData
 */

public class Calculations {
    TripDAO trd = new TripDAO();

    ArrayList<Trip> trips = trd.getAllTrips();

    /**
     * Returns the total distance travelled for a specific bike. Uses the bike id as a parameter
     * @param bikeId A int bike id which refers to a bike id in the database
     * @return a sum of the total distance travelled by the specified bike.
     */
    public double getTotalDistanceByID (int bikeId) {
         ArrayList<Trip> tripsForBike = trd.getAllTripsByBikeId(bikeId);
         double sum = 0;
         for(Trip aTrip: tripsForBike) {
             sum+=aTrip.getTripDistance();
         }
         return sum;
    }

    /**
     * Returns a calculation of the total distance for an ArrayList of BikeData.
     *
     * Uses getDistanceBetweenCoords to calculate the total distance.
     *
     * @param bikeDataList This parameter specifies the ArrayList of BikeData.
     * @see BikeData
     * @return a calculation of the total distance for the ArrayList of BikeData.
     */
    //This method uses a list of BikeData to get total distance for the bikeDaa in the list.
    public double getTotalDistance (ArrayList<BikeData> bikeDataList){
        if(bikeDataList.size()<=1){
            return 0;
        }
        double totalDistance = 0;
        for(int i = bikeDataList.size()-1; i>=0; i--){
            if(i!=0) {
                totalDistance+=getDistanceBetweenCoords(bikeDataList.get(i).getLatitude(),bikeDataList.get(i).getLongitude(),bikeDataList.get(i-1).getLatitude(),bikeDataList.get(i-1).getLongitude());
            }
        }
        return totalDistance;
    }

    /**
     *  Returns a calculation of the distance between two coordinates.
     * @param lat1 This parameter specifies the latitude for the first coordinate.
     * @param lng1 This parameter specifies the longitude for the first coordinate.
     * @param lat2 This parameter specifies the latitude for the second coordinate.
     * @param lng2 This parameter specifies the longitude for the second coordinate.
     * @return the distance between the two coordinates specified in the parameters.
     */
    public double getDistanceBetweenCoords(double lat1,double lng1, double lat2,double lng2){

        double R = 6371e3; // metres
        double lar1 = Math.toRadians(lat1);
        double lar2 = Math.toRadians(lat2);
        double dLar = Math.toRadians((lat2 - lat1));
        double dLor = Math.toRadians((lng2 - lng1));

        double a = Math.sin(dLar / 2) * Math.sin(dLar / 2) + Math.cos(lar1) * Math.cos(lar2) * Math.sin(dLor / 2) * Math.sin(dLor / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;

        //System.out.println(lat1 + " - " + lng1 + ", " + lat2 + " - " + lng2);
        //System.out.println("Distance: " + d);

        return d;
    }

    /**
     *  Returns the amount of check ins or check outs from stations depending on the checkIns parameter
     *
     * @param stationId This parameter specifies which station.
     * @param checkIns This parameter says if the method should calculate check ins or check outs. True for check ins and false for check outs
     * @see Trip
     * @return an amount of check ins or check outs depending on the checkIns parameter.
     */
    public double getBikeCheckInsAndOuts (int stationId,boolean checkIns) {
        double amount = 0;
        for(int i = 0; i < trips.size(); i++){
            if(checkIns) {
                if (stationId == trips.get(i).getEndStationId()) {
                    amount++;
                }
            } else {
                if (stationId == trips.get(i).getStartStationId()) {
                    amount++;
                }
            }
        }
        return amount;
    }

    /**
     * Returns profit for a specified month. It uses EconomyDAO to get the different values (bike costs, repair costs and bike income) from the database.
     * @param year This parameter says which year.
     * @param month This parameter says which month.
     * @see EconomyDAO
     * @see EconomyData
     * @return a sum of the profits for the specified month and year.
     */
    public double getProfitForMonth (int year, int month) {
        EconomyDAO ed = new EconomyDAO();
        double sum = 0;
        ArrayList<EconomyData> bikeCosts = ed.getAllBikeCosts();
        ArrayList<EconomyData> repairCosts = ed.getAllRepairCosts();
        ArrayList<EconomyData> bikeIncome = ed.getAllBikeIncome();
        for(EconomyData aBikeCost: bikeCosts){
            if(year == aBikeCost.getYear() && month == aBikeCost.getMonth()){
                sum-=aBikeCost.getValue();
                //System.out.println("BIKE COST SUBTRACTED");
            }
        }
        for(EconomyData aRepairCost: repairCosts){
            if(year == aRepairCost.getYear() && month == aRepairCost.getMonth()){
                sum-=aRepairCost.getValue();
                //System.out.println("REPAIR COST SUBTRACTED");
            }
        }
        for(EconomyData aBikeIncome: bikeIncome){
            if(year == aBikeIncome.getYear() && month == aBikeIncome.getMonth()){
                sum+=aBikeIncome.getValue();
                //System.out.println("BIKE INCOME ADDED");
            }
        }
        return sum;
    }
}
