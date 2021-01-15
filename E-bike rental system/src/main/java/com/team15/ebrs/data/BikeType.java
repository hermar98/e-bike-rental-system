
package main.java.com.team15.ebrs.data;

/**
 * An object for BikeType. This is used to register different types of bikes.
 *
 * @author Team 15
 */
public class BikeType {
    private int typeId;
    private final String typeName;
    private double rentalPrice;

    /**
     * This is the constructor for the BikeType object. It takes type name and rental price as parameters.
     * @param typeName the name of the bike type.
     * @param rentalPrice the rental price of the bike type.
     *
     */
    public BikeType(String typeName, double rentalPrice){
        this.typeName = typeName;
        this.rentalPrice = rentalPrice;
    }

    /**
     * Returns an int for the type id for the BikeType object.
     * @return an int for the type id for the BikeType object.
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * Returns a String for the type name for the BikeType object.
     * @return a String for the type name for the BikeType object.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Returns a double for the rental price for the BikeType object.
     * @return a double for the rental price for the BikeType object.
     */
    public double getRentalPrice() {
        return rentalPrice;
    }

    /**
     * Sets the rental price for the BikeType object.
     * @param newPrice the new rental price you want it set to.
     */
    public void setRentalPrice(double newPrice){
        rentalPrice = newPrice;
    }

    /**
     * Sets the type id for the BikeType object.
     * @param newId the type id you want it set to.
     */
    public void setTypeId(int newId){
        typeId = newId;
    }

    /**
     * A toString to write out the type name.
     * @return a String for the type name.
     */
    public String toString(){
        return typeName;
    }

    /**
     * A method that overrides the standard Object equals method.
     *
     * Checks if the type id, type name and rental price ar equal.
     * @param o the reference object you want to check if equals this one.
     * @return true if the reference object equals this one; false if not.
     */
    @Override
    public boolean equals(Object o){
        if(!(o instanceof BikeType)){
            return false;
        }

        if (o == this){
            return true;
        }

        BikeType oT = (BikeType) o;

        return typeId == oT.typeId && typeName.equals(oT.typeName) && rentalPrice == oT.rentalPrice;
    }
}
