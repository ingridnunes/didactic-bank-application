package bank.business.domain;

import java.util.Date;

/**
 * @author Ingrid Nunes
 * 
 */
public class Client extends User {

	private CurrentAccount account;
	private int cpf;

	public Client(String firstName, String lastName, int cpf, String password,
			Date birthday) {
		super(firstName, lastName, password, birthday);
		this.cpf = cpf;
		this.account = null;
	}

	/**
	 * @return the account
	 */
	public CurrentAccount getAccount() {
		return account;
	}

	/**
	 * @return the cpf
	 */
	public int getCpf() {
		return cpf;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(CurrentAccount account) {
		this.account = account;
	}

}
