package main.java.com.team15.ebrs.data;


/**
 * An object for EconomyData. This is used to register different EconomyData objects.
 *
 * @author Team 15
 */

public class EconomyData {
    private int year;
    private int month;
    private double value;

    /**
     * Constructor for the EconomyData class. It takes a year, a month and a value
     * @param year The year for the EconomyData
     * @param month The month for the EconomyData
     * @param value The value for the EconomyData
     */
    public EconomyData(int year, int month, double value) {
        this.year = year;
        this.month = month;
        this.value = value;
    }

    /**
     * Returns a value of currency for the EconomyData object.
     * @return a double value
     */
    public double getValue() {
        return value;
    }

    /**
     * Returns the month for the EconomyData object
     * @return an int value for the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the year for the EconomyData object
     * @return an int value for the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns a String for the EconomyData object
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        return "Year: " + year + " Month: " + month + " Value: " + value;
    }
}
