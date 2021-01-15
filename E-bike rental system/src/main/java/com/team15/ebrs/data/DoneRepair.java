package main.java.com.team15.ebrs.data;

import java.time.LocalDate;

/**
 * An object for DoneRepair. This is used to register different DoneRepair objects.
 *
 * @author Team 15
 * @see LocalDate
 */

public class DoneRepair extends Repair {
	
	private double price;
	private LocalDate dateReceived;
	private String repairDesc;

	/**
	 * Constructor for the DoneRepair which is the subclass of Repair with setting repair_id.
	 * @param repairId The unique id of each repair (primary key)
	 * @param price The price for this repair
	 * @param dateSent The date when the repair has been sent
	 * @param dateReceived The date when the repair has been received
	 * @param requestDesc The description of the repair when the repair has been requested
	 * @param repairDesc The description of the repair when the repair is finished
	 */
	public DoneRepair(int repairId, double price, LocalDate dateSent, LocalDate dateReceived, String requestDesc, String repairDesc) {
		super(repairId, dateSent, requestDesc);
		this.price = price;
		this.dateReceived = dateReceived;
		this.repairDesc = repairDesc;
	}
	/**
	 * Constructor for the DoneRepair which is the subclass of Repair without setting repair_id.
	 * @param price The price for this repair
	 * @param dateSent The date when the repair has been sent
	 * @param dateReceived The date when the repair has been received
	 * @param requestDesc The description of the repair when the repair has been requested
	 * @param repairDesc The description of the repair when the repair is finished
	 */
	
	public DoneRepair(double price, LocalDate dateSent, LocalDate dateReceived, String requestDesc, String repairDesc) {
		super(dateSent, requestDesc);
		this.price = price;
		this.dateReceived = dateReceived;
		this.repairDesc = repairDesc;
	}

	/**
	 * Returns the price for this repair
	 * @return double price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Returns the date when the repair has been received
	 * @return LocalDate date(has been received)
	 */
	public LocalDate getDateReceived() {
		return dateReceived;
	}

	/**
	 * Returns the description of the repair when the repair is finished
	 * @return String repair description
	 */
	public String getRepairDesc() {
		return repairDesc;
	}

	/**
	 * Changes the price for this repair
	 * @param price The new price for this repair
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Changes the date when the repair has been received
	 * @param dateReceived The new date when the repair has been received
	 */
	public void setDateReceived(LocalDate dateReceived) {
		this.dateReceived = dateReceived;
	}

	/**
	 * Changes the description when the repair is finished
	 * @param repairDesc The new description when the repair is finished
	 */
	public void setRepairDesc(String repairDesc) {
		this.repairDesc = repairDesc;
	}

}
