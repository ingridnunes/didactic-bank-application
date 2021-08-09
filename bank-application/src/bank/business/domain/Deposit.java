package bank.business.domain;

/**
 * @author Ingrid Nunes
 * 
 */
public class Deposit extends Transaction {

	private long envelope;

	public Deposit(OperationLocation location, CurrentAccount account,
			long envelope, double amount) {
		super(location, account, amount);
		this.envelope = envelope;
	}

	/**
	 * @return the envelope
	 */
	public long getEnvelope() {
		return envelope;
	}

}
