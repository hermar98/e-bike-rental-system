package main.java.com.team15.ebrs.data;

import java.time.LocalDate;
/**
 * An object for RequestedRepair. This is used to register different RequestedRepair objects
 *
 * @author Team 15
 */
public class RequestedRepair extends Repair {
	/**
	 * Constructor for the RequestedRepair which is the subclass of Repair with setting repair_id.
	 * @param repairId The unique id of each repair (primary key)
	 * @param dateSent The date when the repair request has been sent
	 * @param requestDesc The description of the repair
	 */
	public RequestedRepair(int repairId, LocalDate dateSent, String requestDesc) {
		super(repairId, dateSent, requestDesc);
	}
	/**
	 * Constructor for the RequestedRepair which is the subclass of Repair without setting repair_id. The value of repair_id will be automatically incremented.
	 * @param dateSent The date when the repair request has been sent
	 * @param requestDesc The description of the repair
	 */
	public RequestedRepair(LocalDate dateSent, String requestDesc) {
		super(dateSent, requestDesc);
	}
}
