package main.java.com.team15.ebrs.data;

import java.util.ArrayList;
import java.time.LocalDate;

/**
 * An object for Bikes.
 *
 * @author Team 15
 * @see BikeType
 * @see BikeData
 * @see main.java.com.team15.ebrs.dao.BikeDAO
 * @see Repair
 * @see LocalDate
 */
public class Bike {
	
    private int bikeId;
    private LocalDate purchaseDate;
    private double price;
    private String make;
    private BikeType type;
    private ArrayList<Repair> repairList;
    private int stationId;
    private BikeData bikeData;

    /**
     * This is the constructor which constructs the Bike object.
     * @param purchaseDate This is the date the bike was purchased
     * @param price
     * @param make
     * @param type
     * @param stationId
     */
    public Bike(LocalDate purchaseDate, double price, String make, BikeType type, int stationId) {
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.make = make;
        this.type = type;
        this.stationId = stationId;
    }

    /**
     * Returns the bike id for the Bike object.
     * @return the bike id for the Bike object.
     */
    public int getBikeId() {
        return bikeId;
    }

    /**
     * Returns the purchase date for the Bike object.
     * @return the purchase date for the Bike object.
     * @see LocalDate
     */
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Returns the sql purchase date for the Bike object.
     * @return the sql purchase date for the Bike object.
     * @see java.sql.Date
     */
    public java.sql.Date getSqlDate(){
        return java.sql.Date.valueOf(purchaseDate);
    }

    /**
     * Returns the purchase price for the Bike object.
     * @return the purchase price for the Bike object.
     */
    public double getPrice() {
            return price;
    }

    /**
     * Returns the make of the Bike object.
     * @return the make of the Bike object.
     */
    public String getMake() {
            return make;
    }

    /**
     * Returns the BikeType object for the Bike object.
     * @return the BikeType object for the Bike object.
     * @see BikeType
     */
    public BikeType getType(){
        return type;
    }

    /**
     * Returns the type id of the BikeType object of the Bike object.
     * @return the type id of the BikeType object of the Bike object.
     * @see BikeType
     */
    public int getTypeId() {
            return type.getTypeId();
    }

    /**
     * Returns the type name of the BikeType object of the Bike object.
     * @return the type name of the BikeType object of the Bike object.
     * @see BikeType
     */
    public String getTypeName(){
        return type.getTypeName();
    }

    /**
     * Returns the rental price of the BikeType object of the Bike object.
     * @return the rental price of the BikeType object of the Bike object.
     */
    public double getRentalPrice(){
        return type.getRentalPrice();
    }

    /**
     * Returns the current station id of the Bike object.
     * @return the current station id of the Bike object.
     */
    public int getStationId(){
        return stationId;
    }

    /**
     * Returns the BikeData object of the Bike object.
     * @return the BikeData object of the Bike object.
     * @see BikeData
     */
    public BikeData getBikeData(){
        return bikeData;
    }

    /**
     * Returns an ArrayList of Repair objects for the Bike object.
     * @return an ArrayList of Repair objects for the Bike object.
     * @see Repair
     */
    public ArrayList<Repair> getRepairList() {
            return repairList;
    }

    /**
     * Sets the bikeId for the bike.
     * @param bikeId this is the bike id you want it set to
     */
    public void setBikeId(int bikeId){
        this.bikeId = bikeId;
    }

    /**
     * Sets the BikeData object for the Bike object
     * @param bikeData this is the BikeData object you want to set it to.
     * @see BikeData
     */
    public void setBikeData(BikeData bikeData){
        this.bikeData = bikeData;
    }

    /**
     * Sets the purchase date for the Bike object.
     * @param purchaseDate this is the LocalDate object you want it set to.
     * @see LocalDate
     */
    public void setPurchaseDate(LocalDate purchaseDate) {
            this.purchaseDate = purchaseDate;
    }

    /**
     * Sets the purchase price for the Bike object.
     * @param price this is the price you want it set to.
     */
    public void setPrice(double price) {
            this.price = price;
    }

    /**
     * Sets the make of the Bike object.
     * @param make this is the String you want it set to.
     */
    public void setMake(String make) {
            this.make = make;
    }

    /**
     * Sets the BikeType object for the Bike object.
     * @param newType this specifes the BikeType object you want it set to.
     * @see BikeType
     */
    public void setType(BikeType newType){
        type = newType;
    }

    /**
     * Sets the station id for the Bike object.
     * @param newStationId the station id you want it set to.
     */
    public void setStationId(int newStationId){
        stationId = newStationId;
    }

    /**
     * Sets the repair list for the Bike object.
     * @param repairList the ArrayList of Repair objects you want to set the list to.
     * @see Repair
     */

    public void setRepairList(ArrayList<Repair> repairList) {
        this.repairList = repairList;
    }

    /**
     * This is a method which overrides the Object equals method.
     *
     * It is used to check if one Bike object equals another. It checks if the bike id are equal.
     * @param o the object you want to check if equals this one.
     * @return true if this object is the same as the o parameter; false otherwise.
     */
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Bike)){
            return false;
        }

        if (o == this){
            return true;
        }

        Bike oB = (Bike) o;

        return bikeId == oB.bikeId;
    }

    /**
     * A toString method to write out the most important type of the object.
     * @return a String of most necessary information about the object.
     */
    public String toString(){
        return "Bike " + bikeId + " - " + make + " - " + type;
    }
}
