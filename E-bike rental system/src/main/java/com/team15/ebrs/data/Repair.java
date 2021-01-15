package main.java.com.team15.ebrs.data;

import java.time.LocalDate;

/**
 * An object for Repair. This is used to register different Repair objects
 *
 * @author Team 15
 * @see LocalDate
 */
public class Repair {

	private int repairId;
	private LocalDate dateSent;
	private String requestDesc;

	/**
	 * Constructor for the Repair object with setting repair_id.
	 * @param repairId The unique id of each repair (primary key)
	 * @param dateSent The date when the repair has been sent
	 * @param requestDesc The description of the repair
	 */
	public Repair(int repairId, LocalDate dateSent, String requestDesc) {
		this.repairId = repairId;
		this.dateSent = dateSent;
		this.requestDesc = requestDesc;
	}

	/**
	 *This constructor of the Repair class is used to create a new repair record in the database without setting repair_id. The value of repair_id will be automatically incremented.
	 *
	 * @param dateSent The date when the repair has been sent
	 * @param requestDesc The description of the repair
	 */
	public Repair(LocalDate dateSent, String requestDesc) {
		this.dateSent = dateSent;
		this.requestDesc = requestDesc;
	}

	/**
	 * Returns the id of the repair
	 * @return int repair id
	 */
	public int getRepairId() {
		return repairId;
	}

	/**
	 * Returns the date when the repair has been sent
	 * @return LocalDate date
	 */
	public LocalDate getDateSent() {
		return dateSent;
	}

	/**
	 * Returns the description of the repair
	 * @return String description
	 */
	public String getRequestDesc() {
		return requestDesc;
	}

	/**
	 * The method changes the value of repair id
	 * @param repairId The new id of the repair
	 */
	public void setRepairId(int repairId)  {
		this.repairId = repairId;
	}

	/**
	 * The method changes the date has been sent
	 * @param dateSent The new date you want to set
	 */
	public void setDateSent(LocalDate dateSent) {
		this.dateSent = dateSent;
	}

	/**
	 * The method changes the description of the repair
	 * @param requestDesc The new description for the repair
	 */
	public void setRequestDesc(String requestDesc) {
		this.requestDesc = requestDesc;
	}

	/**
	 * Returns a string with all the information
	 * @return String information
	 */
	public String toString() {
		return "ID: " + repairId + " - Date: " + dateSent + " - " + requestDesc;
	}
}